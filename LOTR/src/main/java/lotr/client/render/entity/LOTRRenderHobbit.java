package lotr.client.render.entity;

import java.util.List;

import lotr.client.model.LOTRModelHobbit;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityDwarf;
import lotr.common.entity.npc.LOTREntityHobbit;
import lotr.common.entity.npc.LOTREntityHobbitBartender;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderHobbit extends LOTRRenderBiped
{
	private static List hobbitSkinsMale;
	private static List hobbitSkinsFemale;
	private static ResourceLocation apron = new ResourceLocation("lotr:mob/hobbit/bartender_apron.png");
	private static ResourceLocation ringTexture = new ResourceLocation("lotr:mob/hobbit/ring.png");
	
	private ModelBiped standardRenderPassModel = new LOTRModelHobbit(0.5F);
	
	public LOTRRenderHobbit()
	{
		super(new LOTRModelHobbit(), 0.5F);
		setRenderPassModel(standardRenderPassModel);
		
		hobbitSkinsMale = LOTRRandomSkins.loadSkinsList("lotr:mob/hobbit/hobbit_male");
		hobbitSkinsFemale = LOTRRandomSkins.loadSkinsList("lotr:mob/hobbit/hobbit_female");
	}
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        LOTREntityHobbit hobbit = (LOTREntityHobbit)entity;
		if (hobbit.familyInfo.isNPCMale())
		{
			return LOTRRandomSkins.getRandomSkin(hobbitSkinsMale, hobbit);
		}
		else
		{
			return LOTRRandomSkins.getRandomSkin(hobbitSkinsFemale, hobbit);
		}
    }
	
	@Override
    protected int shouldRenderPass(EntityLivingBase entity, int i, float f)
    {
		LOTREntityHobbit hobbit = (LOTREntityHobbit)entity;
		if (i == 1 && entity instanceof LOTREntityHobbitBartender)
		{
			bindTexture(apron);
			setRenderPassModel(standardRenderPassModel);
			((ModelBiped)renderPassModel).bipedRightArm.showModel = true;
			return 1;
		}
		else if (i == 1 && hobbit.getClass() == hobbit.familyInfo.marriageEntityClass && hobbit.getEquipmentInSlot(4) != null && hobbit.getEquipmentInSlot(4).getItem() == hobbit.familyInfo.marriageRing)
		{
			bindTexture(ringTexture);
			setRenderPassModel(standardRenderPassModel);
			((ModelBiped)renderPassModel).bipedRightArm.showModel = false;
			return 1;
		}
		else
		{
			return super.shouldRenderPass(entity, i, f);
		}
    }
	
	@Override
	protected void preRenderCallback(EntityLivingBase entity, float f)
	{
		if (LOTRMod.isAprilFools())
		{
			GL11.glScalef(2F, 2F, 2F);
		}
		else
		{
			GL11.glScalef(0.75F, 0.75F, 0.75F);
		}
	}
	
	@Override
    public float getHeldItemYTranslation()
    {
        return 0.075F;
    }
}
