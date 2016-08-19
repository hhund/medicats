package de.gecko.medicats;

public interface NodeFactory<N extends Node<N>, W extends NodeWalker<N>>
{
	/**
	 * @return not <code>null</code>
	 */
	String getName();

	/**
	 * @return not <code>null</code>, might be empty
	 */
	String getOid();

	/**
	 * reads nodes if necessary
	 * 
	 * @return root node
	 */
	N getRootNode();

	W createNodeWalker();
}