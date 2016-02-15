package de.gecko.medicats;

import java.util.Optional;
import java.util.stream.Stream;

public interface VersionedNode<N extends VersionedNode<N>> extends Node<N>
{
	String getVersion();

	String getPreviousVersion();

	Optional<String> getPreviousCode();

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
}
