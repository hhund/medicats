package de.gecko.medicats.icd10;

import de.gecko.medicats.PreviousCodeMappings;

public abstract class AbstractIcdNodeFactory implements IcdNodeFactory
{
	private PreviousCodeMappings mappings;

	protected abstract FileSource getSystFile();

	protected abstract FileSource getTransitionFile();

	protected PreviousCodeMappings getPreviousCodes()
	{
		if (mappings == null)
		{
			FileSource transitionFile = getTransitionFile();
			if (transitionFile == null)
				mappings = new PreviousCodeMappings(null);
			else
				mappings = PreviousIcdCodesReader.read(transitionFile.getInputStream());
		}

		return mappings;
	}

	@Override
	public boolean supportsVersion(String version)
	{
		return getVersion().equals(version);
	}
}
