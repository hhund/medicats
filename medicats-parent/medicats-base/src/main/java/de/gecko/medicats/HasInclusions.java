package de.gecko.medicats;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public interface HasInclusions<N extends Node<N>>
{
	Stream<N> getInclusions(Function<String, List<N>> byCode);

	List<N> getInclusionsList(Function<String, List<N>> byCode);
}
