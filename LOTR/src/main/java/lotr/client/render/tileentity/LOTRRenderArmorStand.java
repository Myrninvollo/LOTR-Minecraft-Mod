package lotr.client.render.tileentity;

import lotr.client.LOTRClientProxy;
import lotr.client.model.LOTRModelArmorStand;
import lotr.common.tileentity.LOTRTileEntityArmorStand;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class LOTRRenderArmorStand extends TileEntitySpecialRenderer
{
	private static ResourceLocation standTexture = new ResourceLocation("lotr:item/armorStand.png");
	private static ModelBase standModel = new LOTRModelArmorStand();
	private static ModelBiped modelBiped1 = new ModelBiped(1F);
	private static ModelBiped modelBiped2 = new ModelBiped(0.5F);
	private static float BIPED_ARM_ROTATION = -7.07353F;
	private static float BIPED_TICKS_EXISTED = (float)Math.PI / 0.067F + 0.0001F;
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
	{
		LOTRTileEntityArmorStand armorStand = (LOTRTileEntityArmorStand)tileentity;
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glTranslatef((float)d + 0.5F, (float)d1 + 1.5F, (float)d2 + 0.5F);
		switch (armorStand.getBlockMetadata() & 3)
		{
			case 0:
				GL11.glRotatef(0F, 0F, 1F, 0F);
				break;
			case 1:
				GL11.glRotatef(270F, 0F, 1F, 0F);
				break;
			case 2:
				GL11.glRotatef(180F, 0F, 1F, 0F);
				break;
			case 3:
				GL11.glRotatef(90F, 0F, 1F, 0F);
				break;
		}
		GL11.glScalef(-1F, -1F, 1F);
		float scale = 0.0625F;
		bindTexture(standTexture);
		standModel.render(null, 0F, 0F, 0F, 0F, 0F, scale);
		
		GL11.glTranslatef(0F, -0.1875F, 0F);
		for (int i = 0; i < 4; i++)
		{
			ItemStack itemstack = armorStand.getStackInSlot(i);
			if (itemstack != null && itemstack.getItem() instanceof ItemArmor)
			{
	            ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
                bindTexture(RenderBiped.getArmorResource(null, itemstack, i, null));
                ModelBiped modelbiped = i == 2 ? modelBiped2 : modelBiped1;
                modelbiped.bipedHead.showModel = i == 0;
                modelbiped.bipedHeadwear.showModel = i == 0;
                modelbiped.bipedBody.showModel = i == 1 || i == 2;
                modelbiped.bipedRightArm.showModel = i == 1;
                modelbiped.bipedLeftArm.showModel = i == 1;
                modelbiped.bipedRightLeg.showModel = i == 2 || i == 3;
                modelbiped.bipedLeftLeg.showModel = i == 2 || i == 3;
                modelbiped = ForgeHooksClient.getArmorModel(null, itemstack, i, modelbiped);
				modelbiped.isChild = false;
				
				float f1 = 1F;
				boolean flag = false;
				
                int j = itemarmor.getColor(itemstack);
                if (j != -1)
                {
                    float f2 = (float)(j >> 16 & 255) / 255F;
                    float f3 = (float)(j >> 8 & 255) / 255F;
                    float f4 = (float)(j & 255) / 255F;
                    GL11.glColor3f(f1 * f2, f1 * f3, f1 * f4);
					flag = true;
                }
				else
				{
					GL11.glColor3f(f1, f1, f1);
				}
				
				modelbiped.render(null, BIPED_ARM_ROTATION, 0F, BIPED_TICKS_EXISTED, 0F, 0F, scale);
				
				if (flag)
				{
					bindTexture(RenderBiped.getArmorResource(null, itemstack, i, "overlay"));
					f1 = 1F;
					GL11.glColor3f(f1, f1, f1);
					modelbiped.render(null, BIPED_ARM_ROTATION, 0F, BIPED_TICKS_EXISTED, 0F, 0F, scale);
				}
				
				if (itemstack.isItemEnchanted())
				{
					float f2 = (float)armorStand.ticksExisted + f;
					bindTexture(LOTRClientProxy.enchantmentTexture);
					GL11.glEnable(GL11.GL_BLEND);
					float f3 = 0.5F;
					GL11.glColor4f(f3, f3, f3, 1F);
					GL11.glDepthFunc(GL11.GL_EQUAL);
					GL11.glDepthMask(false);

					for (int k = 0; k < 2; k++)
					{
						GL11.glDisable(GL11.GL_LIGHTING);
						float f4 = 0.76F;
						GL11.glColor4f(0.5F * f4, 0.25F * f4, 0.8F * f4, 1F);
						GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
						GL11.glMatrixMode(GL11.GL_TEXTURE);
						GL11.glLoadIdentity();
						float f5 = 0.33333334F;
						GL11.glScalef(f5, f5, f5);
						GL11.glRotatef(30F - (float)k * 60F, 0F, 0F, 1F);
						float f6 = f2 * (0.001F + (float)k * 0.003F) * 20F;
						GL11.glTranslatef(0F, f6, 0F);
						GL11.glMatrixMode(GL11.GL_MODELVIEW);
						modelbiped.render(null, BIPED_ARM_ROTATION, 0F, BIPED_TICKS_EXISTED, 0F, 0F, scale);
					}

					GL11.glColor4f(1F, 1F, 1F, 1F);
					GL11.glMatrixMode(GL11.GL_TEXTURE);
					GL11.glDepthMask(true);
					GL11.glLoadIdentity();
					GL11.glMatrixMode(GL11.GL_MODELVIEW);
					GL11.glEnable(GL11.GL_LIGHTING);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glDepthFunc(GL11.GL_LEQUAL);
				}
			}
		}
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}
}
