package de.gecko.medicats;

public class PreviousCodeMapping
{
	public static PreviousCodeMapping unchanged(String code)
	{
		return new PreviousCodeMapping(code, code, Compatible.YES, Compatible.YES);
	}

	public static enum Compatible
	{
		YES, NO, UNKNOWN
	}

	private final String previousCode;
	private final String currentCode;
	private final Compatible previousCodeForwardsCompatible;
	private final Compatible currentCodeBackwardsCompatible;

	public PreviousCodeMapping(String previousCode, String currentCode, Compatible previousCodeForwardsCompatible,
			Compatible currentCodeBackwardsCompatible)
	{
		this.previousCode = previousCode;
		this.currentCode = currentCode;
		this.previousCodeForwardsCompatible = previousCodeForwardsCompatible;
		this.currentCodeBackwardsCompatible = currentCodeBackwardsCompatible;
	}

	public String getPreviousCode()
	{
		return previousCode;
	}

	public String getCurrentCode()
	{
		return currentCode;
	}

	public Compatible isPreviousCodeForwardsCompatible()
	{
		return previousCodeForwardsCompatible;
	}

	public Compatible isCurrentCodeBackwardsCompatible()
	{
		return currentCodeBackwardsCompatible;
	}
}