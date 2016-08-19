package de.gecko.medicats.alphaid;

import java.util.ServiceLoader;

import de.gecko.medicats.CodeService;

public class AlphaIdService extends CodeService<AlphaIdNode, AlphaIdNodeWalker, AlphaIdNodeFactory>
{
	private static AlphaIdService service;

	private AlphaIdService()
	{
		super(ServiceLoader.load(AlphaIdNodeFactory.class));
	}

	public static synchronized AlphaIdService getService()
	{
		if (service == null)
			service = new AlphaIdService();

		return service;
	}
}
