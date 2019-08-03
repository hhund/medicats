package de.gecko.medicats.ops;

import de.gecko.medicats.FileSource;
import de.gecko.medicats.PreviousCodeMappings;

public abstract class AbstractOpsNodeFactory implements OpsNodeFactory
{
	private PreviousCodeMappings mappings;

	protected abstract FileSource getSystFile();

	protected abstract FileSource getTransitionFile();

	/**
	 * @return 0-based index
	 */
	protected int getPreviousCodesColumn()
	{
		return 0;
	}

	/**
	 * @return 0-based index
	 */
	protected int getCurrentCodesColumn()
	{
		return 2;
	}

	/**
	 * @return 0-based index
	 */
	protected int getCurrentCodesBackwardsCompatibleColumn()
	{
		return 6;
	}

	/**
	 * @return 0-based index
	 */
	protected int getPreviousCodesForwardsCompatibleColumn()
	{
		return 5;
	}

	protected PreviousCodeMappings getPreviousCodes()
	{
		if (mappings == null)
		{
			FileSource transitionFile = getTransitionFile();
			if (transitionFile == null)
				mappings = new PreviousCodeMappings(null);
			else
				mappings = PreviousOpsCodesReader.read(transitionFile.getInputStream(), getPreviousCodesColumn(),
						getCurrentCodesColumn(), getPreviousCodesForwardsCompatibleColumn(),
						getCurrentCodesBackwardsCompatibleColumn());
		}

		return mappings;
	}

	@Override
	public boolean supportsVersion(String version)
	{
		return getVersion().equals(version);
	}
}
