package de.gecko.medicats.alphaid;

import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.IcdService;

public class AlphaIdNodeRoot extends AlphaIdNode
{
	private final String version;
	private final String previousVersion;
	private final String icdVersion;

	private IcdNodeWalker icdNodeWalker;
	private boolean icdVersionNotSupported;

	private AlphaIdNodeWalker previousAlphaIdNodeWalker;
	private boolean previousAlphaIdNotSupported;

	public AlphaIdNodeRoot(String version, String previousVersion, String icdVersion)
	{
		super(null, "ROOT", true, null, null, null, null, "ROOT");

		this.version = version;
		this.previousVersion = previousVersion;
		this.icdVersion = icdVersion;
	}

	@Override
	public String getVersion()
	{
		return version;
	}

	@Override
	public String getPreviousVersion()
	{
		return previousVersion;
	}

	@Override
	public String getIcdVersion()
	{
		return icdVersion;
	}

	@Override
	protected AlphaIdNodeWalker getPreviousAlphaIdNodeWalker()
	{
		if (previousAlphaIdNodeWalker == null && !previousAlphaIdNotSupported)
		{
			AlphaIdService service = AlphaIdService.getService();
			if (service.supportsVersion(getPreviousVersion()))
			{
				previousAlphaIdNodeWalker = service.getNodeFactory(getPreviousVersion()).createNodeWalker();
			}
			else
				previousAlphaIdNotSupported = true;
		}

		return previousAlphaIdNodeWalker;
	}

	@Override
	protected IcdNodeWalker getIcdNodeWalker()
	{
		if (icdNodeWalker == null && !icdVersionNotSupported)
		{
			IcdService service = IcdService.getService();
			if (service.supportsVersion(getIcdVersion()))
			{
				icdNodeWalker = service.getNodeFactory(getIcdVersion()).createNodeWalker();
			}
			else
				icdVersionNotSupported = true;
		}

		return icdNodeWalker;
	}

	@Override
	public String getPath()
	{
		return getVersion();
	}

	@Override
	public String toString()
	{
		return getAlphaId() + " - " + getVersion();
	}
}
