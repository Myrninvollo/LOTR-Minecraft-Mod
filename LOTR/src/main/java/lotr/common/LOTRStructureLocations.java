package lotr.common;

import lotr.common.world.genlayer.LOTRGenLayerWorld;

public enum LOTRStructureLocations
{
	UTUMNO_ENTRANCE(1139, 394),
	
	MORDOR_CHERRY_TREE(1630, 1170);
	
	public int xCoord;
	public int zCoord;
	
	private LOTRStructureLocations(int x, int z)
	{
		xCoord = Math.round(((float)(x - LOTRGenLayerWorld.originX) + 0.5F) * LOTRGenLayerWorld.scale);
		zCoord = Math.round(((float)(z - LOTRGenLayerWorld.originZ) + 0.5F) * LOTRGenLayerWorld.scale);
	}
	
	public boolean isAt(int x, int z)
	{
		return xCoord == x && zCoord == z;
	}
}
