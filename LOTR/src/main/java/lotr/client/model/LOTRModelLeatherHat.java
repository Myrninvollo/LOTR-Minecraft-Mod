package lotr.client.model;

import lotr.common.entity.npc.LOTREntityDwarf;
import lotr.common.entity.npc.LOTREntityElf;
import lotr.common.entity.npc.LOTREntityHobbit;
import lotr.common.entity.npc.LOTREntityOrc;
import lotr.common.item.LOTRItemLeatherHat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

public class LOTRModelLeatherHat extends LOTRModelBiped
{
	private static ItemStack feather = new ItemStack(Items.feather);
	private ModelRenderer hat;
	private ItemStack hatItem;
	private ModelBiped internalModelBiped;
	private ModelBiped HUMAN;
	private ModelBiped DWARF;
	private ModelBiped ELF;
	private ModelBiped ORC;
	private ModelBiped HOBBIT;
	
	public LOTRModelLeatherHat()
	{
		this(0F);
	}
	
	public LOTRModelLeatherHat(float f)
	{
		super(f);
		
		hat = new ModelRenderer(this, 0, 0);
		hat.addBox(-6F, -9F, -6F, 12, 2, 12, f);
		hat.setRotationPoint(0F, 0F, 0F);
		hat.setTextureOffset(0, 14).addBox(-4F, -13F, -4F, 8, 4, 8, f);
		
		HUMAN = new LOTRModelBiped(f);
		DWARF = new LOTRModelDwarf(f);
		ELF = new LOTRModelElf(f);
		ORC = new LOTRModelOrc(f);
		HOBBIT = new LOTRModelHobbit(f);
	}
	
	private void setInternalModelBipedFromEntity(Entity entity)
	{
		if (entity instanceof LOTREntityDwarf)
		{
			internalModelBiped = DWARF;
		}
		else if (entity instanceof LOTREntityHobbit)
		{
			internalModelBiped = HOBBIT;
		}
		else if (entity instanceof LOTREntityElf)
		{
			internalModelBiped = ELF;
		}
		else if (entity instanceof LOTREntityOrc)
		{
			internalModelBiped = ORC;
		}
		else
		{
			internalModelBiped = HUMAN;
		}
	}
	
	public void setHatItem(ItemStack itemstack)
	{
		hatItem = itemstack;
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		GL11.glPushMatrix();
		
		setInternalModelBipedFromEntity(entity);
		
		internalModelBiped.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		hat.rotationPointX = internalModelBiped.bipedHead.rotationPointX;
		hat.rotationPointY = internalModelBiped.bipedHead.rotationPointY;
		hat.rotationPointZ = internalModelBiped.bipedHead.rotationPointZ;
		hat.rotateAngleX = internalModelBiped.bipedHead.rotateAngleX;
		hat.rotateAngleY = internalModelBiped.bipedHead.rotateAngleY;
		hat.rotateAngleZ = internalModelBiped.bipedHead.rotateAngleZ;
		
		int hatColor = LOTRItemLeatherHat.getHatColor(hatItem);
		float r = (float)(hatColor >> 16 & 255) / 255F;
		float g = (float)(hatColor >> 8 & 255) / 255F;
		float b = (float)(hatColor & 255) / 255F;
		GL11.glColor3f(r, g, b);
		
		hat.render(f5);
		
		if (LOTRItemLeatherHat.hasFeather(hatItem))
		{
			hat.postRender(0.0625F);
			GL11.glScalef(0.375F, 0.375F, 0.375F);
			GL11.glRotatef(130F, 1F, 0F, 0F);
			GL11.glRotatef(30F, 0F, 1F, 0F);
			GL11.glTranslatef(0.25F, 1.5F, 0.75F);
			GL11.glRotatef(-45F, 0F, 0F, 1F);
			int featherColor = LOTRItemLeatherHat.getFeatherColor(hatItem);
			r = (float)(featherColor >> 16 & 255) / 255F;
			g = (float)(featherColor >> 8 & 255) / 255F;
			b = (float)(featherColor & 255) / 255F;
			GL11.glColor3f(r, g, b);
			
			if (entity instanceof EntityLivingBase)
			{
				RenderManager.instance.itemRenderer.renderItem((EntityLivingBase)entity, feather, 0);
			}
			else
			{
				RenderManager.instance.itemRenderer.renderItem(Minecraft.getMinecraft().thePlayer, feather, 0);
			}
		}
		
		GL11.glPopMatrix();
	}
}
