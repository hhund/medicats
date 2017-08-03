package de.gecko.medicats.web.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.gecko.medicats.alphaid.AlphaIdService;
import de.gecko.medicats.icd10.IcdService;
import de.gecko.medicats.ops.OpsService;
import de.gecko.medicats.web.rest.AlphaIdDictionaryWebservice;
import de.gecko.medicats.web.rest.IcdDictionaryWebservice;
import de.gecko.medicats.web.rest.MedicatsWebservice;
import de.gecko.medicats.web.rest.OidDictionaryWebservice;
import de.gecko.medicats.web.rest.DictionaryWebservice;
import de.gecko.medicats.web.rest.OpsDictionaryWebservice;
import de.gecko.medicats.web.rest.SearchWebservice;
import de.gecko.medicats.web.rest.XsltTransformer;

@Configuration
public class WebserviceConfig
{
	@Value("${de.gecko.medicats.baseUrl}")
	private String baseUrl;

	@Autowired
	private AlphaIdService alphaIdService;

	@Autowired
	private IcdService icdService;

	@Autowired
	private OpsService opsService;

	@Bean
	public XsltTransformer xsltTransformer()
	{
		return new XsltTransformer();
	}

	@Bean
	public DictionaryWebservice dictionaryWebservice()
	{
		return new DictionaryWebservice(baseUrl, xsltTransformer());
	}

	@Bean
	public IcdDictionaryWebservice icdMedicatsWebservice()
	{
		return new IcdDictionaryWebservice(icdService, baseUrl, xsltTransformer());
	}

	@Bean
	public OpsDictionaryWebservice opsMedicatsWebservice()
	{
		return new OpsDictionaryWebservice(opsService, baseUrl, xsltTransformer());
	}

	@Bean
	public AlphaIdDictionaryWebservice alphaIdMedicatsWebservice()
	{
		return new AlphaIdDictionaryWebservice(alphaIdService, icdService, baseUrl, xsltTransformer());
	}

	@Bean
	public OidDictionaryWebservice oidMedicatsWebservice()
	{
		return new OidDictionaryWebservice(baseUrl, icdService, opsService, alphaIdService, xsltTransformer());
	}

	@Bean
	public SearchWebservice searchWebservice() throws IOException
	{
		return new SearchWebservice(baseUrl, icdService, opsService, alphaIdService, xsltTransformer());
	}

	@Bean
	public MedicatsWebservice medicatsWebservice()
	{
		return new MedicatsWebservice(baseUrl, xsltTransformer());
	}
}
