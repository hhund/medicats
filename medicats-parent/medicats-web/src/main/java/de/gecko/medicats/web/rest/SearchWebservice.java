package de.gecko.medicats.web.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.gecko.medicats.VersionedNode;
import de.gecko.medicats.alphaid.AlphaIdNode;
import de.gecko.medicats.alphaid.AlphaIdNodeFactory;
import de.gecko.medicats.alphaid.AlphaIdService;
import de.gecko.medicats.icd10.IcdNode;
import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdService;
import de.gecko.medicats.ops.OpsNode;
import de.gecko.medicats.ops.OpsNodeFactory;
import de.gecko.medicats.ops.OpsService;
import de.gecko.medicats.web.transfer.SearchResultNodeDto;
import de.gecko.medicats.web.transfer.SearchResultNodeListDto;

@Path(SearchWebservice.PATH)
public class SearchWebservice
{
	public static final String PATH = "search";

	private static final Logger logger = LoggerFactory.getLogger(SearchWebservice.class);

	private final String baseUrl;

	private final IcdService icdService;
	private final OpsService opsService;
	private final AlphaIdService alphaIdService;
	private final XsltTransformer transformer;

	private final StandardAnalyzer analyzer;
	private final IndexSearcher searcher;

	private final Query preferedIcdVersion;
	private final Query preferedOpsVersion;
	private final Query preferedAlphaIdVersion;

	public SearchWebservice(String baseUrl, IcdService icdService, OpsService opsService, AlphaIdService alphaIdService,
			XsltTransformer transformer) throws IOException
	{
		this.baseUrl = baseUrl;
		this.icdService = icdService;
		this.opsService = opsService;
		this.alphaIdService = alphaIdService;
		this.transformer = transformer;

		analyzer = new StandardAnalyzer();
		Directory index = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(index, config);

		logger.info("adding icd entries to search index ...");
		icdService.getNodeFactories().stream().sorted(Comparator.comparing(IcdNodeFactory::getSortIndex)).forEach(f ->
		{
			logger.info("... {}", f.getName());
			f.createNodeWalker().preOrderStream()
					.forEach(createLableIndex("icd-10-gm", f.getOid(), f.getSortIndex(), writer));
		});
		logger.info("adding ops entries to search index ...");
		opsService.getNodeFactories().stream().sorted(Comparator.comparing(OpsNodeFactory::getSortIndex)).forEach(f ->
		{
			logger.info("... {}", f.getName());
			f.createNodeWalker().preOrderStream()
					.forEach(createLableIndex("ops", f.getOid(), f.getSortIndex(), writer));
		});
		logger.info("adding alpha-id entries to search index ...");
		alphaIdService.getNodeFactories().stream().sorted(Comparator.comparing(AlphaIdNodeFactory::getSortIndex))
				.forEach(f ->
				{
					logger.info("... {}", f.getName());
					f.createNodeWalker().preOrderStream()
							.forEach(createLableIndex("alpha-id", f.getOid(), f.getSortIndex(), writer));
				});

		writer.close();
		logger.info("lucene search index created");

		String preferredIcdVersionString = icdService.getNodeFactories().stream()
				.sorted(Comparator.comparing(IcdNodeFactory::getSortIndex).reversed()).findFirst()
				.map(IcdNodeFactory::getVersion).orElse("");
		String preferredOpsVersionString = opsService.getNodeFactories().stream()
				.sorted(Comparator.comparing(OpsNodeFactory::getSortIndex).reversed()).findFirst()
				.map(OpsNodeFactory::getVersion).orElse("");
		String preferredAlphaIdVersionString = alphaIdService.getNodeFactories().stream()
				.sorted(Comparator.comparing(AlphaIdNodeFactory::getSortIndex).reversed()).findFirst()
				.map(AlphaIdNodeFactory::getVersion).orElse("");

		logger.info("Prefered ICD Version: {}", preferredIcdVersionString);
		logger.info("Prefered OPS Version: {}", preferredOpsVersionString);
		logger.info("Prefered AlphaId Version: {}", preferredAlphaIdVersionString);

		preferedIcdVersion = new TermQuery(new Term("version", preferredIcdVersionString));
		preferedOpsVersion = new TermQuery(new Term("version", preferredOpsVersionString));
		preferedAlphaIdVersion = new TermQuery(new Term("version", preferredAlphaIdVersionString));

		searcher = new IndexSearcher(DirectoryReader.open(index));
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response search(@QueryParam("q") String qParam)
	{
		if (qParam == null || qParam.isEmpty())
			return Response.status(Status.BAD_REQUEST).build();

		try
		{
			SearchResultNodeListDto dto = searchImpl(qParam);

			return Response.ok(dto).build();
		}
		catch (ParseException e)
		{
			throw new WebApplicationException(Status.BAD_REQUEST);
		}
		catch (IOException e)
		{
			throw new WebApplicationException();
		}
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response searchHtml(@QueryParam("q") String qParam)
	{
		try
		{
			SearchResultNodeListDto dto = qParam == null || qParam.isEmpty()
					? new SearchResultNodeListDto(Collections.emptyList()) : searchImpl(qParam);

			return Response.ok(transformer.transform(dto, "SearchResultNodeListDto.xslt")).build();
		}
		catch (ParseException e)
		{
			throw new WebApplicationException(Status.BAD_REQUEST);
		}
		catch (IOException e)
		{
			throw new WebApplicationException();
		}
	}

	private SearchResultNodeListDto searchImpl(String qParam) throws ParseException, IOException
	{
		QueryParser parser = new QueryParser("label", analyzer);
		parser.setAllowLeadingWildcard(true);
		Query q = parser.parse(qParam);

		BooleanQuery bQ = new BooleanQuery.Builder().add(q, Occur.MUST).add(preferedIcdVersion, Occur.SHOULD)
				.add(preferedOpsVersion, Occur.SHOULD).add(preferedAlphaIdVersion, Occur.SHOULD).build();

		TopDocs docs = searcher.search(bQ, 100);
		ScoreDoc[] hits = docs.scoreDocs;

		List<SearchResultNodeDto> results = new ArrayList<>(hits.length);

		logger.debug("Found " + docs.totalHits + (hits.length == 1 ? " hit" : " hits"));
		for (int i = 0; i < hits.length; ++i)
		{
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			String type = d.get("type");
			String version = d.get("version");
			String code = d.get("code");

			if ("icd-10-gm".equals(type))
				toIcdResult(version, code, results);
			else if ("ops".equals(type))
				toOpsResult(version, code, results);
			else if ("alpha-id".equals(type))
				toAlphaIdResult(version, code, results);
		}

		results = results.stream().filter(distinctByKey(r -> r.getOid() + r.getCode() + r.getLabel()))
				.collect(Collectors.toList());

		SearchResultNodeListDto dto = new SearchResultNodeListDto(results);
		return dto;
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor)
	{
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	private void toIcdResult(String version, String code, Collection<? super SearchResultNodeDto> results)
	{
		logger.debug("ICD result {}, {}", version, code);

		IcdNodeFactory f = icdService.getNodeFactory(version);
		IcdNode n = f.createNodeWalker().getNodeByCode(code);

		if (n == null)
		{
			logger.warn("ICD {}, {} not found", version, code);
			return;
		}

		Link l = Link.fromUri(
				baseUrl + "/" + IcdDictionaryWebservice.PATH + "/" + String.valueOf(f.getSortIndex()) + n.getUri())
				.rel("via").type("dictionary").title(n.getCode()).build();
		SearchResultNodeDto r = new SearchResultNodeDto(Collections.singleton(l), f.getOid(), f.getName(),
				String.valueOf(f.getSortIndex()), n.getLabel(), n.getCode());

		results.add(r);
	}

	private void toOpsResult(String version, String code, Collection<? super SearchResultNodeDto> results)
	{
		logger.debug("OPS result {}, {}", version, code);

		OpsNodeFactory f = opsService.getNodeFactory(version);
		OpsNode n = f.createNodeWalker().getNodeByCode(code);

		if (n == null)
		{
			logger.warn("OPS {}, {} not found", version, code);
			return;
		}

		Link l = Link.fromUri(
				baseUrl + "/" + OpsDictionaryWebservice.PATH + "/" + String.valueOf(f.getSortIndex()) + n.getUri())
				.rel("via").type("dictionary").title(n.getCode()).build();
		SearchResultNodeDto r = new SearchResultNodeDto(Collections.singleton(l), f.getOid(), f.getName(),
				String.valueOf(f.getSortIndex()), n.getLabel(), n.getCode());

		results.add(r);
	}

	private void toAlphaIdResult(String version, String code, Collection<? super SearchResultNodeDto> results)
	{
		logger.debug("Alpha-ID result {}, {}", version, code);

		AlphaIdNodeFactory f = alphaIdService.getNodeFactory(version);
		AlphaIdNode n = f.createNodeWalker().getNodeByCode(code);

		if (n == null)
		{
			logger.warn("Alpha-ID {}, {} not found", version, code);
			return;
		}

		Link l = Link
				.fromUri(baseUrl + "/" + AlphaIdDictionaryWebservice.PATH + "/" + String.valueOf(f.getSortIndex())
						+ n.getUri())
				.rel("via").type("dictionary").title(n.getCode() + " (" + n.getLabel() + ")").build();
		SearchResultNodeDto r = new SearchResultNodeDto(Collections.singleton(l), f.getOid(), f.getName(),
				String.valueOf(f.getSortIndex()), n.getLabel(), n.getCode());

		results.add(r);

		if (n.getPrimaryIcdCode() != null)
			toIcdResult(n.getIcdVersion(), n.getPrimaryIcdCode(), results);
		if (n.getAsterixIcdCode() != null)
			toIcdResult(n.getIcdVersion(), n.getAsterixIcdCode(), results);
		if (n.getAdditionalIcdCode() != null)
			toIcdResult(n.getIcdVersion(), n.getAdditionalIcdCode(), results);
	}

	private static Consumer<? super VersionedNode<?>> createLableIndex(String type, String oid, int sortIndex,
			IndexWriter writer)
	{
		return n ->
		{
			Document d = new Document();
			d.add(new StringField("type", type, Field.Store.YES));
			d.add(new StringField("version", n.getVersion(), Field.Store.YES));
			d.add(new StringField("code", n.getCode(), Field.Store.YES));
			d.add(new TextField("label", n.getLabel(), Field.Store.NO));
			try
			{
				writer.addDocument(d);
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		};
	}
}
