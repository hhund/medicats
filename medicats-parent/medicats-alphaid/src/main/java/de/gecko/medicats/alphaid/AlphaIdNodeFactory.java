package de.gecko.medicats.alphaid;

import de.gecko.medicats.VersionedNodeFactory;

public interface AlphaIdNodeFactory extends VersionedNodeFactory<AlphaIdNode, AlphaIdNodeWalker>
{
	String getIcdVersion();
}