package de.gecko.medicats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PreviousCodeMappings
{
	private final Map<String, PreviousCodeMapping> mappingsByNewCode = new HashMap<>();
	private final List<PreviousCodeMapping> mappings = new ArrayList<>();

	public PreviousCodeMappings(List<PreviousCodeMapping> mappings)
	{
		if (mappings != null)
		{
			this.mappings.addAll(mappings);
			mappings.forEach(p -> mappingsByNewCode.put(p.getCurrentCode(), p));
		}
	}

	public Map<String, PreviousCodeMapping> getMappingsByNewCode()
	{
		return Collections.unmodifiableMap(mappingsByNewCode);
	}

	public List<PreviousCodeMapping> getPreviousCodes()
	{
		return Collections.unmodifiableList(mappings);
	}

	public Optional<PreviousCodeMapping> get(String code)
	{
		return Optional.ofNullable(mappingsByNewCode.get(code));
	}
}