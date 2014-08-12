package lotr.client.render.entity;

import java.util.List;

import lotr.client.model.LOTRModelDwarf;
import lotr.client.model.LOTRModelHobbit;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityBlueDwarf;
import lotr.common.entity.npc.LOTREntityDwarf;
import lotr.common.entity.npc.LOTREntityHobbit;
import lotr.common.entity.npc.LOTREntityHobbitBartender;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderDwarf extends LOTRRenderBiped
{
	private static List dwarfSkinsMale;
	private static List dwarfSkinsFemale;
	
	private static List blueDwarfSkinsMale;
	private static List blueDwarfSkinsFemale;
	
	private static ResourceLocation ringTexture = new ResourceLocation("lotr:mob/dwarf/ring.png");
	private ModelBiped standardRenderPassModel = new LOTRModelDwarf(0.5F, 64, 64);
	
	public LOTRRenderDwarf()
	{
		super(new LOTRModelDwarf(), 0.5F);
		setRenderPassModel(standardRenderPassModel);
		
		dwarfSkinsMale = LOTRRandomSkins.loadSkinsList("lotr:mob/dwarf/dwarf_male");
		dwarfSkinsFemale = LOTRRandomSkins.loadSkinsList("lotr:mob/dwarf/dwarf_female");
		
		blueDwarfSkinsMale = LOTRRandomSkins.loadSkinsList("lotr:mob/dwarf/blueMountains_male");
		blueDwarfSkinsFemale = LOTRRandomSkins.loadSkinsList("lotr:mob/dwarf/blueMountains_female");
	}

	@Override
	protected void func_82421_b()
    {
        field_82423_g = new LOTRModelDwarf(1.0F);
        field_82425_h = new LOTRModelDwarf(0.5F);
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        LOTREntityDwarf dwarf = (LOTREntityDwarf)entity;
        
        if (dwarf instanceof LOTREntityBlueDwarf)
        {
			if (dwarf.familyInfo.isNPCMale())
			{
				return LOTRRandomSkins.getRandomSkin(blueDwarfSkinsMale, dwarf);
			}
			else
			{
				return LOTRRandomSkins.getRandomSkin(blueDwarfSkinsFemale, dwarf);
			}
        }
        else
        {
        	if (dwarf.familyInfo.isNPCMale())
			{
				return LOTRRandomSkins.getRandomSkin(dwarfSkinsMale, dwarf);
			}
			else
			{
				return LOTRRandomSkins.getRandomSkin(dwarfSkinsFemale, dwarf);
			}
        }
    }
	
	@Override
    protected int shouldRenderPass(EntityLivingBase entity, int pass, float f)
    {
		LOTREntityDwarf dwarf = (LOTREntityDwarf)entity;
		if (pass == 1 && dwarf.getClass() == dwarf.familyInfo.marriageEntityClass && dwarf.getEquipmentInSlot(4) != null && dwarf.getEquipmentInSlot(4).getItem() == dwarf.familyInfo.marriageRing)
		{
			bindTexture(ringTexture);
			setRenderPassModel(standardRenderPassModel);
			((ModelBiped)renderPassModel).bipedRightArm.showModel = false;
			return 1;
		}
		else
		{
			return super.shouldRenderPass(entity, pass, f);
		}
    }
	
	@Override
	protected void preRenderCallback(EntityLivingBase entity, float f)
	{
		GL11.glScalef(0.8125F, 0.8125F, 0.8125F);
		
		if (LOTRMod.isAprilFools())
		{
			GL11.glRotatef(180F, 0F, 0F, 1F);
		}
	}
	
	@Override
    public float getHeldItemYTranslation()
    {
        return 0.125F;
    }
}
