package lotr.client.render.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

public class LOTRRenderSkullStaff implements IItemRenderer
{
	private static ModelBase staffModel = new ModelBase()
	{
		private ModelRenderer staff;
		{
			staff = new ModelRenderer(this, 0, 0);
			staff.addBox(-0.5F, 8F, -6F, 1, 1, 28, 0F);
			staff.addBox(-2.5F, 6F, -11F, 5, 5, 5, 0F);
			staff.rotateAngleY = (float)Math.toRadians(90D);
			staff.rotateAngleZ = (float)Math.toRadians(-20D);
		}
		
		@Override
		public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
		{
			staff.render(f5);
		}
	};
	
	private static ResourceLocation staffTexture = new ResourceLocation("lotr:item/skullStaff.png");
	
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
		Minecraft.getMinecraft().getTextureManager().bindTexture(staffTexture);
		if (type == ItemRenderType.EQUIPPED_FIRST_PERSON)
		{
			GL11.glRotatef(-70F, 0F, 0F, 1F);
			GL11.glTranslatef(-0.5F, 0F, -0.5F);
		}
		staffModel.render(null, 0F, 0F, 0F, 0F, 0F, 0.0625F);
	}
}
