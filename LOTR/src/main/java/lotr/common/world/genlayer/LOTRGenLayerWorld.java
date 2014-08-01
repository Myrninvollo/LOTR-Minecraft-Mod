package lotr.common.world.genlayer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;

import lotr.common.LOTRMod;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerFuzzyZoom;
import net.minecraft.world.gen.layer.GenLayerRiverInit;
import net.minecraft.world.gen.layer.GenLayerSmooth;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;
import net.minecraft.world.gen.layer.IntCache;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ModContainer;

public class LOTRGenLayerWorld extends GenLayer
{
	public static Map colorsToBiomeIDs = new HashMap();
	public static int[] biomeImageData;
	public static int originX = 810;
	public static int originZ = 730;
	public static int scale = 128;
	public static int imageWidth;
	public static int imageHeight;
	
	public static GenLayer[] createWorld(long seed, WorldType worldType)
	{
        byte scale = 3;
		
		GenLayer layer = new LOTRGenLayerWorld();
		layer = new GenLayerFuzzyZoom(2000L, layer);
		layer = new GenLayerZoom(2001L, layer);
		layer = new GenLayerZoom(2002L, layer);
		
		GenLayer rivers = GenLayerZoom.magnify(1000L, layer, 1);
		rivers = new GenLayerRiverInit(100L, rivers);
		rivers = GenLayerZoom.magnify(1000L, rivers, 10);
		rivers = new LOTRGenLayerRiver(1L, rivers);
		rivers = new GenLayerSmooth(1000L, rivers);
		
		GenLayer biomeVariants = new LOTRGenLayerBiomeVariantsInit(3000L);
		biomeVariants = GenLayerZoom.magnify(3000L, biomeVariants, 2);
		
		GenLayer biomes = new LOTRGenLayerWorld();
		biomes = new LOTRGenLayerBeach(1000L, biomes);
		biomes = new LOTRGenLayerBiomeVariants(1000L, biomes, biomeVariants);
		biomes = new LOTRGenLayerBiomeVariantsSmall(500L, biomes);
		
		biomes = GenLayerZoom.magnify(1000L, biomes, 2);
		biomes = new LOTRGenLayerBiomeFeatures(1000L, biomes);

        for (int i = 0; i < scale; i++)
        {
        	biomes = new GenLayerZoom(1000L + (long)i, biomes);
        }
		
        biomes = new GenLayerSmooth(1000L, biomes);
		layer = new LOTRGenLayerRiverVariants(100L, biomes, rivers);
		GenLayer layer1 = new GenLayerVoronoiZoom(10L, layer);

        layer.initWorldGenSeed(seed + 1000L);
        layer1.initWorldGenSeed(seed + 1000L);
		
        return new GenLayer[] {layer, layer1, layer};
	}
	
    public LOTRGenLayerWorld()
    {
        super(0L);
		
		if (biomeImageData == null)
		{
			try
			{
				BufferedImage biomeImage = null;
				String imageName = "assets/lotr/map/map.png";
						
				ModContainer mc = FMLCommonHandler.instance().findContainerFor(LOTRMod.instance);
				if (mc.getSource().isFile())
				{
					ZipFile zip = new ZipFile(mc.getSource());
					Enumeration entries = zip.entries();
					while (entries.hasMoreElements())
					{
						ZipEntry entry = (ZipEntry)entries.nextElement();
						if (entry.getName().equals(imageName))
						{
							biomeImage = ImageIO.read(zip.getInputStream(entry));
						}
					}
					zip.close();
				}
				else
				{
					File file = new File(LOTRMod.class.getResource("/" + imageName).toURI());
					biomeImage = ImageIO.read(new FileInputStream(file));
				}
				
				if (biomeImage == null)
				{
					throw new RuntimeException("Could not load LOTR biome map image");
				}
				
				imageWidth = biomeImage.getWidth();
				imageHeight = biomeImage.getHeight();
				int[] colors = biomeImage.getRGB(0, 0, imageWidth, imageHeight, null, 0, imageWidth);
				biomeImageData = new int[imageWidth * imageHeight];

				for (int i = 0; i < colors.length; i++)
				{
					int color = colors[i] & 0x00FFFFFF;
					Object obj = colorsToBiomeIDs.get(Integer.valueOf(color));
					if (obj != null)
					{
						biomeImageData[i] = (Integer)obj;
					}
					else
					{
						biomeImageData[i] = LOTRBiome.ocean.biomeID;
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
    }
	
	@Override
    public int[] getInts(int i, int k, int xSize, int zSize)
    {
        int[] intArray = IntCache.getIntCache(xSize * zSize);
        for (int l = 0; l < zSize; l++)
        {
            for (int l1 = 0; l1 < xSize; l1++)
            {
                int i1 = i + l1 + originX;
                int k1 = k + l + originZ;
				if (i1 < 0 || i1 >= imageWidth || k1 < 0 || k1 >= imageHeight)
				{
					intArray[l1 + l * xSize] = LOTRBiome.ocean.biomeID;
				}
				else
				{
					intArray[l1 + l * xSize] = biomeImageData[i1 + k1 * imageWidth];
				}
            }
        }
        return intArray;
    }
}
