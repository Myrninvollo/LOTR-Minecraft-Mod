package lotr.client.render.item;

import lotr.client.model.LOTRModelBanner;
import lotr.client.render.entity.LOTRRenderBanner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

public class LOTRRenderBannerItem implements IItemRenderer
{
	private static LOTRModelBanner model = new LOTRModelBanner();
	
	@Override
    public boolean handleRenderType(ItemStack itemstack, ItemRenderType type)
	{
		return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON;
	}
    
	@Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack itemstack, ItemRendererHelper helper)
	{
		return false;
	}
    
	@Override
    public void renderItem(ItemRenderType type, ItemStack itemstack, Object... data)
	{
		GL11.glDisable(GL11.GL_CULL_FACE);
		
		Entity entity = (Entity)data[1];

		boolean isFirstPerson = entity == Minecraft.getMinecraft().thePlayer && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0;
		boolean renderStand = false;

		TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
		
		if (isFirstPerson)
		{
			GL11.glTranslatef(1F, 1F, 0F);
		}
		else
		{
			GL11.glTranslatef(-1.5F, 0.85F, -0.1F);
			GL11.glRotatef(75F, 0F, 0F, 1F);
		}
		
		GL11.glScalef(1F, -1F, 1F);
		textureManager.bindTexture(LOTRRenderBanner.getStandTexture(itemstack.getItemDamage()));
		if (renderStand)
		{
			model.renderStand(0.0625F);
		}
		model.renderPost(0.0625F);
		model.renderLowerPost(0.0625F);
		textureManager.bindTexture(LOTRRenderBanner.getBannerTexture(itemstack.getItemDamage()));
		model.renderBanner(0.0625F);
	}
}
