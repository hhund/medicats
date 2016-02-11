package de.gecko.medicats;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class CodeService<F extends VersionedNodeFactory<?, ?>>
{
	private final ServiceLoader<F> loader;

	protected CodeService(ServiceLoader<F> loader)
	{
		this.loader = loader;
	}

	public boolean supportsVersion(String version)
	{
		return StreamSupport.stream(loader.spliterator(), false).anyMatch(f -> f.supportsVersion(version));
	}

	/**
	 * @param version
	 * @return
	 * @throws NoSuchElementException
	 * @see #supportsVersion(String)
	 */
	public F getNodeFactory(String version)
	{
		return StreamSupport.stream(loader.spliterator(), false).filter(f -> f.supportsVersion(version)).findFirst()
				.get();
	}

	/**
	 * @return registered node factories in random order
	 */
	public List<F> getNodeFactories()
	{
		return Collections.unmodifiableList(StreamSupport.stream(loader.spliterator(), false)
				.sorted((f1, f2) -> f1.getVersion().compareTo(f2.getVersion())).collect(Collectors.toList()));
	}
}
