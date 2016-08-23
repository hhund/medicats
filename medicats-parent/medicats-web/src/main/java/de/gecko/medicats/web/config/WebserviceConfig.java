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
	public DictionaryWebservice dictionaryWebservice()
	{
		return new DictionaryWebservice(baseUrl);
	}

	@Bean
	public IcdDictionaryWebservice icdMedicatsWebservice()
	{
		return new IcdDictionaryWebservice(icdService, baseUrl);
	}

	@Bean
	public OpsDictionaryWebservice opsMedicatsWebservice()
	{
		return new OpsDictionaryWebservice(opsService, baseUrl);
	}

	@Bean
	public AlphaIdDictionaryWebservice alphaIdMedicatsWebservice()
	{
		return new AlphaIdDictionaryWebservice(alphaIdService, icdService, baseUrl);
	}

	@Bean
	public OidDictionaryWebservice oidMedicatsWebservice()
	{
		return new OidDictionaryWebservice(baseUrl, icdService, opsService, alphaIdService);
	}

	@Bean
	public SearchWebservice searchWebservice() throws IOException
	{
		return new SearchWebservice(baseUrl, icdService, opsService, alphaIdService);
	}

	@Bean
	public MedicatsWebservice medicatsWebservice()
	{
		return new MedicatsWebservice(baseUrl);
	}
}
