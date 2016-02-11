package de.gecko.medicats.ops;

import java.util.ServiceLoader;

import de.gecko.medicats.CodeService;

public class OpsService extends CodeService<OpsNodeFactory>
{
	private static OpsService service;

	private OpsService()
	{
		super(ServiceLoader.load(OpsNodeFactory.class));
	}

	public static synchronized OpsService getService()
	{
		if (service == null)
			service = new OpsService();

		return service;
	}
}
