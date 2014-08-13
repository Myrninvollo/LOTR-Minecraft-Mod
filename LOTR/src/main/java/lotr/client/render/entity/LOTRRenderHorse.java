package lotr.client.render.entity;

import java.util.*;

import lotr.common.LOTRMod;
import lotr.common.entity.animal.LOTREntityHorse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.renderer.entity.RenderHorse;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.LayeredTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderHorse extends RenderHorse
{
    public LOTRRenderHorse()
    {
        super(new ModelHorse(), 0.75F);
    }
	
	@Override
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
	{
		LOTREntityHorse horse = (LOTREntityHorse)entity;
		boolean fools = LOTRMod.isAprilFools();
		int horseType = horse.getHorseType();
		if (fools)
		{
			horse.setHorseType(1);
		}
		
		super.doRender(entity, d, d1, d2, f, f1);
		
		if (fools)
		{
			horse.setHorseType(horseType);
		}
	}
	
	@Override
	public ResourceLocation getEntityTexture(Entity entity)
	{
		LOTREntityHorse horse = (LOTREntityHorse)entity;
		ResourceLocation horseSkin = super.getEntityTexture(entity);
		return getLayeredMountTexture(horse, horseSkin);
	}
	
	private static Map layeredMountTextures = new HashMap();
	
	public static ResourceLocation getLayeredMountTexture(LOTREntityHorse mount, ResourceLocation mountSkin)
	{
		String skinPath = mountSkin.toString();
        String armorPath = mount.getMountArmorTexture();

        if (armorPath == null)
        {
        	return mountSkin;
        }
        else
        {
        	Minecraft mc = Minecraft.getMinecraft();
        	
        	String path = "lotr_" + skinPath + "_" + armorPath;
	        ResourceLocation texture = (ResourceLocation)layeredMountTextures.get(path);
	        if (texture == null)
	        {
	        	texture = new ResourceLocation(path);
	        	
	        	List<String> layers = new ArrayList();
	        	ITextureObject skinTexture = mc.getTextureManager().getTexture(mountSkin);
	        	if (skinTexture instanceof LayeredTexture)
	        	{
	        		LayeredTexture skinTextureLayered = (LayeredTexture)skinTexture;
	        		layers.addAll(skinTextureLayered.layeredTextureNames);
	        	}
	        	else
	        	{
	        		layers.add(skinPath);
	        	}
	        	layers.add(armorPath);
	        			
	        	mc.getTextureManager().loadTexture(texture, new LayeredTexture(layers.toArray(new String[0])));
	            layeredMountTextures.put(path, texture);
	        }
	        
	        return texture;
        }
	}
}
