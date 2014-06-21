package lotr.client.render.entity;

import java.util.List;

import lotr.client.model.LOTRModelElf;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityElf;
import lotr.common.entity.npc.LOTREntityHighElf;
import lotr.common.entity.npc.LOTREntityWoodElf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderElf extends LOTRRenderBiped
{
	private static List galadhrimSkinsMale;
	private static List galadhrimSkinsFemale;
	
	private static List woodElfSkinsMale;
	private static List woodElfSkinsFemale;
	
	private static List highElfSkinsMale;
	private static List highElfSkinsFemale;
	
	public LOTRRenderElf()
	{
		super(new LOTRModelElf(), 0.5F);
		
		galadhrimSkinsMale = LOTRRandomSkins.loadSkinsList("lotr:mob/elf/galadhrim_male");
		galadhrimSkinsFemale = LOTRRandomSkins.loadSkinsList("lotr:mob/elf/galadhrim_female");
		
		woodElfSkinsMale = LOTRRandomSkins.loadSkinsList("lotr:mob/elf/woodElf_male");
		woodElfSkinsFemale = LOTRRandomSkins.loadSkinsList("lotr:mob/elf/woodElf_female");
		
		highElfSkinsMale = LOTRRandomSkins.loadSkinsList("lotr:mob/elf/highElf_male");
		highElfSkinsFemale = LOTRRandomSkins.loadSkinsList("lotr:mob/elf/highElf_female");
	}
	
	@Override
	protected void func_82421_b()
    {
        field_82423_g = new LOTRModelElf(1.0F);
        field_82425_h = new LOTRModelElf(0.5F);
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        LOTREntityElf elf = (LOTREntityElf)entity;
		boolean male = elf.familyInfo.isNPCMale();
		if (elf instanceof LOTREntityHighElf)
		{
			if (male)
			{
				return LOTRRandomSkins.getRandomSkin(highElfSkinsMale, entity);
			}
			else
			{
				return LOTRRandomSkins.getRandomSkin(highElfSkinsFemale, entity);
			}
		}
		else if (elf instanceof LOTREntityWoodElf)
		{
			if (male)
			{
				return LOTRRandomSkins.getRandomSkin(woodElfSkinsMale, entity);
			}
			else
			{
				return LOTRRandomSkins.getRandomSkin(woodElfSkinsFemale, entity);
			}
		}
		else
		{
			if (male)
			{
				return LOTRRandomSkins.getRandomSkin(galadhrimSkinsMale, entity);
			}
			else
			{
				return LOTRRandomSkins.getRandomSkin(galadhrimSkinsFemale, entity);
			}
		}
    }
	
	@Override
	protected void preRenderCallback(EntityLivingBase entity, float f)
	{
		if (LOTRMod.isAprilFools())
		{
			GL11.glScalef(0.25F, 0.25F, 0.25F);
		}
	}
}