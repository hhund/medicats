package de.gecko.medicats;

import java.io.PrintStream;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface NodeWalker<N extends Node<N>>
{
	N getRootNode();

	default Optional<N> getNodeByCode(Optional<PreviousCodeMapping> code)
	{
		return code.map(c -> getNodesByCode().get(c.getPreviousCode()));
	}
	
	default Optional<N> getNodeByCodeString(Optional<String> code)
	{
		return code.map(c -> getNodesByCode().get(c));
	}

	/**
	 * @param code
	 * @return <code>null</code> if the given <b>code</b> is <code>null</code>
	 *         or no node is found for the given value
	 */
	default N getNodeByCode(String code)
	{
		return code == null ? null : getNodesByCode().get(code);
	}

	Map<String, N> getNodesByCode();

	default void printTree(PrintStream out)
	{
		printSubTree(getRootNode(), out);
	}

	default void printSubTree(N start, PrintStream out)
	{
		preOrderStream(start).forEach(printer(out));
	}

	Consumer<N> printer(PrintStream out);

	default Stream<N> preOrderStream()
	{
		return preOrderStream(getRootNode());
	}

	default Stream<N> preOrderStream(N start)
	{
		return Stream.concat(Stream.of(start), start.children().flatMap(this::preOrderStream));
	}

	default Stream<N> postOrderStream()
	{
		return postOrderStream(getRootNode());
	}

	default Stream<N> postOrderStream(N start)
	{
		return Stream.concat(start.children().flatMap(this::postOrderStream), Stream.of(start));
	}
}