package de.gecko.medicats;

import java.util.stream.Stream;

public interface Node<N extends Node<N>>
{
	Stream<N> children();
	
	String getCode();
	
	String getLabel();
}
