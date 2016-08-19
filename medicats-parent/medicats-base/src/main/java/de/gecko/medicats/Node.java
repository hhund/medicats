package de.gecko.medicats;

import java.util.stream.Stream;

public interface Node<N extends Node<N>>
{
	Stream<N> children();

	default Stream<Node<N>> childrenRecursive()
	{
		return Stream.concat(Stream.of(this), children().flatMap(Node::childrenRecursive));
	}

	N getParent();

	String getCode();

	String getLabel();

	String getPath();

	default String getUri()
	{
		if (getParent() == null)
			return "/";
		else if ("/".equals(getParent().getUri()))
			return "/" + getCode();
		else
			return getParent().getUri() + "/" + getCode();
	}
}
