package de.gecko.medicats;

import java.util.Optional;
import java.util.stream.Stream;

public interface VersionedNode<N extends VersionedNode<N>> extends Node<N>
{
	String getVersion();

	String getPreviousVersion();

	Optional<PreviousCodeMapping> getPreviousMapping();

	Optional<N> getPrevious();

	default Stream<N> getPreviousNodes()
	{
		if (!getPrevious().isPresent())
			return Stream.empty();
		else
		{
			N previous = getPrevious().get();
			return Stream.concat(Stream.of(previous), previous.getPreviousNodes());
		}
	}

	default boolean hasPreviousWithDifferentCode()
	{
		return getPrevious().map(p -> p.hasPreviousWithDifferentCode() || !getCode().equals(p.getCode())).orElse(false);
	}

	default int getPreviousWithDifferentCodeCount()
	{
		return getPrevious().map(p -> p.getPreviousWithDifferentCodeCount() + (!getCode().equals(p.getCode()) ? 1 : 0))
				.orElse(0);
	}
}
