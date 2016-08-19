package de.gecko.medicats.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.gecko.medicats.alphaid.AlphaIdService;
import de.gecko.medicats.icd10.IcdService;
import de.gecko.medicats.ops.OpsService;

@Configuration
public class ServicesConfig
{
	@Bean
	public AlphaIdService alphaIdService()
	{
		return AlphaIdService.getService();
	}

	@Bean
	public IcdService icdService()
	{
		return IcdService.getService();
	}

	@Bean
	public OpsService opsService()
	{
		return OpsService.getService();
	}
}
