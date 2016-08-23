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

	private final StandardAnalyzer analyzer;
	private final IndexSearcher searcher;

	private final Query icdVersion = new TermQuery(new Term("version", "icd10gm2016"));
	private final Query opsVersion = new TermQuery(new Term("version", "ops2016"));
	private final Query alphaIdVersion = new TermQuery(new Term("version", "alphaid2016"));

	public SearchWebservice(String baseUrl, IcdService icdService, OpsService opsService, AlphaIdService alphaIdService)
			throws IOException
	{
		this.baseUrl = baseUrl;
		this.icdService = icdService;
		this.opsService = opsService;
		this.alphaIdService = alphaIdService;

		analyzer = new StandardAnalyzer();
		Directory index = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(index, config);

		logger.debug("adding icd entries to index ...");
		icdService.getNodeFactories().stream().sorted(Comparator.comparing(IcdNodeFactory::getSortIndex)).forEach(f ->
		{
			logger.debug("... {}", f.getName());
			f.createNodeWalker().preOrderStream()
					.forEach(createLableIndex("icd-10-gm", f.getOid(), f.getSortIndex(), writer));
		});
		logger.debug("adding ops entries to index ...");
		opsService.getNodeFactories().stream().sorted(Comparator.comparing(OpsNodeFactory::getSortIndex)).forEach(f ->
		{
			logger.debug("... {}", f.getName());
			f.createNodeWalker().preOrderStream()
					.forEach(createLableIndex("ops", f.getOid(), f.getSortIndex(), writer));
		});
		logger.debug("adding alpha-id entries to index ...");
		alphaIdService.getNodeFactories().stream().sorted(Comparator.comparing(AlphaIdNodeFactory::getSortIndex))
				.forEach(f ->
				{
					logger.debug("... {}", f.getName());
					f.createNodeWalker().preOrderStream()
							.forEach(createLableIndex("alpha-id", f.getOid(), f.getSortIndex(), writer));
				});

		writer.close();
		logger.debug("index created");

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
			QueryParser parser = new QueryParser("label", analyzer);
			parser.setAllowLeadingWildcard(true);
			Query q = parser.parse(qParam);

			BooleanQuery bQ = new BooleanQuery.Builder().add(q, Occur.MUST).add(icdVersion, Occur.SHOULD)
					.add(opsVersion, Occur.SHOULD).add(alphaIdVersion, Occur.SHOULD).build();

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

			return Response.ok(new SearchResultNodeListDto(results)).build();
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

		Link l = Link.fromUri(
				baseUrl + "/" + AlphaIdDictionaryWebservice.PATH + "/" + String.valueOf(f.getSortIndex()) + n.getUri())
				.rel("via").type("dictionary").title(n.getCode()).build();
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
