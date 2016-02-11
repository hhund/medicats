package de.gecko.medicats;

public interface NodeFactory<N extends Node<N>, W extends NodeWalker<N>>
{
	/**
	 * reads nodes if necessary
	 * 
	 * @return root node
	 */
	N getRootNode();

	W createNodeWalker();
}