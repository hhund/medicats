package de.gecko.medicats.claml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface WithMixedValues
{
	/**
	 * @return unmodifiable list
	 */
	List<Object> getValues();

	/**
	 * @return unmodifiable list, without empty {@link String} values
	 * @see #getValues()
	 */
	default List<Object> getValuesWithoutEmptyStringsTrimed()
	{
		return Collections.unmodifiableList(
				getValues().stream().filter(o -> !(o instanceof String) || !((String) o).trim().isEmpty())
						.map(o -> (o instanceof String ? ((String) o).trim() : o)).collect(Collectors.toList()));
	}

	default List<Reference> getReferencesRecursive()
	{
		List<Reference> references = new ArrayList<>();

		for (Object o : getValues())
			if (o == null)
				throw new NullPointerException();
			else if (o instanceof Reference)
				references.add((Reference) o);
			else if (o instanceof WithMixedValues)
				references.addAll(((WithMixedValues) o).getReferencesRecursive());

		return references;
	}
}
