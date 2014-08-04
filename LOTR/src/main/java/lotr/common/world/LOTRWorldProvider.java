package lotr.common.world;

import lotr.client.render.LOTRSkyRenderer;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.world.biome.LOTRBiome;
import lotr.common.world.biome.LOTRBiomeGenFangorn;
import lotr.common.world.biome.LOTRBiomeGenMirkwood;
import lotr.common.world.biome.LOTRBiomeGenOcean;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.common.ForgeModContainer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRWorldProvider extends WorldProvider 
{
	@SideOnly(Side.CLIENT)
	private IRenderHandler lotrSkyRenderer;
	
	@Override
    public void registerWorldChunkManager()
    {
        worldChunkMgr = new LOTRWorldChunkManager(worldObj);
        dimensionId = LOTRMod.idDimension;
    }
    
	@Override
    public IChunkProvider createChunkGenerator()
    {
        return new LOTRChunkProvider(worldObj, worldObj.getSeed());
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public IRenderHandler getSkyRenderer()
    {
		if (LOTRMod.enableLOTRSky)
		{
			if (lotrSkyRenderer == null)
			{
				lotrSkyRenderer = new LOTRSkyRenderer();
			}
			return lotrSkyRenderer;
		}
		else
		{
			return super.getSkyRenderer();
		}
    }

	@Override
    public boolean canRespawnHere()
    {
        return true;
    }
	
	@Override
    public String getWelcomeMessage()
	{
		return "Entering Middle-earth";
	}
	
	@Override
    public String getDepartMessage()
	{
		return "Leaving Middle-earth";
	}
	
	@Override
    public String getSaveFolder()
	{
		return "MiddleEarth";
	}
	
	@Override
    public String getDimensionName()
    {
        return "MiddleEarth";
    }
	
	@Override
    public ChunkCoordinates getSpawnPoint()
    {
        return new ChunkCoordinates(LOTRLevelData.middleEarthPortalX, LOTRLevelData.middleEarthPortalY, LOTRLevelData.middleEarthPortalZ);
    }

	@Override
    public void setSpawnPoint(int i, int j, int k)
    {
		if (!(i == 8 && j == 64 && k == 8) && !worldObj.isRemote)
		{
			LOTRLevelData.markMiddleEarthPortalLocation(i, j, k);
		}
    }
	
	@Override
    public BiomeGenBase getBiomeGenForCoords(int i, int k)
    {
        if (worldObj.blockExists(i, 0, k))
        {
            Chunk chunk = worldObj.getChunkFromBlockCoords(i, k);
            if (chunk != null)
            {
				int chunkX = i & 15;
				int chunkZ = k & 15;
				int biomeID = chunk.getBiomeArray()[chunkZ << 4 | chunkX] & 255;

				if (biomeID == 255)
				{
					BiomeGenBase biomegenbase = worldChunkMgr.getBiomeGenAt((chunk.xPosition << 4) + chunkX, (chunk.zPosition << 4) + chunkZ);
					biomeID = biomegenbase.biomeID;
					chunk.getBiomeArray()[chunkZ << 4 | chunkX] = (byte)(biomeID & 255);
				}
				
				return LOTRBiome.lotrBiomeList[biomeID] == null ? LOTRBiome.shire : LOTRBiome.lotrBiomeList[biomeID];
            }
        }

        return worldChunkMgr.getBiomeGenAt(i, k);
    }
	
	@Override
    public boolean canBlockFreeze(int i, int j, int k, boolean byWater)
    {
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
		if (biome instanceof LOTRBiomeGenOcean)
		{
			float temp = LOTRBiome.ocean.temperature;
			LOTRBiome.ocean.temperature = 0F;
			boolean flag = worldObj.canBlockFreezeBody(i, j, k, byWater);
			LOTRBiome.ocean.temperature = temp;
			return flag && LOTRBiomeGenOcean.isFrozen(i, k);
		}
		else if (biome instanceof LOTRBiome)
		{
			return worldObj.canBlockFreezeBody(i, j, k, byWater);
		}
		else
		{
			return worldObj.canBlockFreezeBody(i, j, k, byWater);
		}
    }

	@Override
    public boolean canSnowAt(int i, int j, int k, boolean checkLight)
    {
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
		if (biome instanceof LOTRBiomeGenOcean)
		{
			float temp = LOTRBiome.ocean.temperature;
			LOTRBiome.ocean.temperature = 0F;
			boolean flag = worldObj.canSnowAtBody(i, j, k, checkLight);
			LOTRBiome.ocean.temperature = temp;
			return flag && LOTRBiomeGenOcean.isFrozen(i, k);
		}
		else if (biome instanceof LOTRBiome)
		{
			return j >= ((LOTRBiome)biome).getSnowHeight() && worldObj.canSnowAtBody(i, j, k, checkLight);
		}
		else
		{
			return worldObj.canSnowAtBody(i, j, k, checkLight);
		}
    }
	
	@Override
	public boolean shouldMapSpin(String entity, double x, double y, double z)
    {
        return false;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public float getCloudHeight()
    {
        return 192F;
    }
	
    private double cloudsR;
    private double cloudsG;
    private double cloudsB;
	
	@Override
    @SideOnly(Side.CLIENT)
    public Vec3 drawClouds(float f)
    {
		Minecraft mc = Minecraft.getMinecraft();
		int i = (int)(mc.renderViewEntity.posX);
		int k = (int)(mc.renderViewEntity.posZ);
		
		Vec3 clouds = super.drawClouds(f);
		cloudsR = cloudsG = cloudsB = 0D;
		
        GameSettings settings = mc.gameSettings;
        int[] ranges = ForgeModContainer.blendRanges;
        int distance = 0;
        if (settings.fancyGraphics && settings.renderDistanceChunks >= 0 && settings.renderDistanceChunks < ranges.length)
        {
            distance = ranges[settings.renderDistanceChunks];
        }
		
        int l = 0;
        for (int i1 = -distance; i1 <= distance; i1++)
        {
            for (int k1 = -distance; k1 <= distance; k1++)
            {
				Vec3 tempClouds = Vec3.createVectorHelper(clouds.xCoord, clouds.yCoord, clouds.zCoord);
                BiomeGenBase biome = worldObj.getBiomeGenForCoords(i + i1, k + k1);
				if (biome instanceof LOTRBiome)
				{
					((LOTRBiome)biome).getCloudColor(tempClouds);
				}
                cloudsR += tempClouds.xCoord;
                cloudsG += tempClouds.yCoord;
                cloudsB += tempClouds.zCoord;
                l++;
            }
        }
		
		cloudsR /= (double)l;
		cloudsG /= (double)l;
		cloudsB /= (double)l;
        return Vec3.createVectorHelper(cloudsR, cloudsG, cloudsB);
    }
	
    private double fogR;
    private double fogG;
    private double fogB;
	
	@Override
    @SideOnly(Side.CLIENT)
    public Vec3 getFogColor(float f, float f1)
    {
		Minecraft mc = Minecraft.getMinecraft();
		int i = (int)(mc.renderViewEntity.posX);
		int k = (int)(mc.renderViewEntity.posZ);
		
		Vec3 fog = super.getFogColor(f, f1);
		fogR = fogG = fogB = 0D;
		
        GameSettings settings = mc.gameSettings;
        int[] ranges = ForgeModContainer.blendRanges;
        int distance = 0;
        if (settings.fancyGraphics && settings.renderDistanceChunks >= 0 && settings.renderDistanceChunks < ranges.length)
        {
            distance = ranges[settings.renderDistanceChunks];
        }
		
        int l = 0;
        for (int i1 = -distance; i1 <= distance; i1++)
        {
            for (int k1 = -distance; k1 <= distance; k1++)
            {
				Vec3 tempFog = Vec3.createVectorHelper(fog.xCoord, fog.yCoord, fog.zCoord);
                BiomeGenBase biome = worldObj.getBiomeGenForCoords(i + i1, k + k1);
				if (biome instanceof LOTRBiome)
				{
					((LOTRBiome)biome).getFogColor(tempFog);
				}
                fogR += tempFog.xCoord;
                fogG += tempFog.yCoord;
                fogB += tempFog.zCoord;
                l++;
            }
        }
		
		fogR /= (double)l;
		fogG /= (double)l;
		fogB /= (double)l;
        return Vec3.createVectorHelper(fogR, fogG, fogB);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean doesXZShowFog(int i, int k)
    {
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
		
		if (biome instanceof LOTRBiome)
		{
			return ((LOTRBiome)biome).hasFog();
		}
		
		return super.doesXZShowFog(i, k);
    }
}
