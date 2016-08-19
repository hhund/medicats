package de.gecko.medicats.icd10;

import java.util.ServiceLoader;

import de.gecko.medicats.CodeService;

public class IcdService extends CodeService<IcdNode, IcdNodeWalker, IcdNodeFactory>
{
	private static IcdService service;

	private IcdService()
	{
		super(ServiceLoader.load(IcdNodeFactory.class));
	}

	public static synchronized IcdService getService()
	{
		if (service == null)
			service = new IcdService();

		return service;
	}
}
