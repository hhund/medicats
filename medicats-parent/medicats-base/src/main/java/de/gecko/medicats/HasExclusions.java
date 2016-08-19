package de.gecko.medicats;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public interface HasExclusions<N extends Node<N>>
{
	Stream<N> getExclusions(Function<String, List<N>> byCode);

	List<N> getExclusionList(Function<String, List<N>> byCode);
}
