package lotr.client.render.tileentity;

import java.util.HashMap;
import java.util.Iterator;

import lotr.common.entity.LOTREntities;
import lotr.common.tileentity.LOTRTileEntityMobSpawner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class LOTRTileEntityMobSpawnerRenderer extends TileEntitySpecialRenderer
{
	private int tempID;
	private HashMap initialisedItemEntities = new HashMap();
	public static double itemYaw;
	public static double prevItemYaw;
	
	public static void onRenderTick()
	{
		prevItemYaw = itemYaw;
		itemYaw = (itemYaw + 0.75D) % 360D;
	}
	
	@Override
	public void func_147496_a(World world)
	{
		loadEntities(world);
	}
	
	private void loadEntities(World world)
	{
		unloadEntities();
		if (world != null)
		{
			Iterator it = LOTREntities.creatures.values().iterator();
			while (it.hasNext())
			{
				LOTREntities.SpawnEggInfo info = (LOTREntities.SpawnEggInfo)it.next();
				Entity entity = LOTREntities.createEntityByID(info.spawnedID, world);
				if (entity instanceof EntityLiving)
				{
					((EntityLiving)entity).onSpawnWithEgg(null);
				}
				initialisedItemEntities.put(info.spawnedID, entity);
			}
		}
	}
	
	private void unloadEntities()
	{
		initialisedItemEntities.clear();
	}
	
	@Override
    public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
    {
		World world = Minecraft.getMinecraft().theWorld;
		LOTRTileEntityMobSpawner mobSpawner = (LOTRTileEntityMobSpawner)tileentity;
		
		if (mobSpawner != null && !mobSpawner.isActive())
		{
			return;
		}
		
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d + 0.5F, (float)d1, (float)d2 + 0.5F);
        Entity entity = null;
		double yaw = 0D;
		double prevYaw = 0D;
		if (mobSpawner != null)
		{
			entity = mobSpawner.getMobEntity(world);
			yaw = mobSpawner.yaw;
			prevYaw = mobSpawner.prevYaw;
		}
		else
		{
			entity = (Entity)initialisedItemEntities.get(tempID);
			yaw = itemYaw;
			prevYaw = prevItemYaw;
		}

        if (entity != null)
        {
            float f1 = 0.4375F;
            GL11.glTranslatef(0.0F, 0.4F, 0.0F);
            GL11.glRotatef((float)(prevYaw + (yaw - prevYaw) * (double)f) * 10.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0.0F, -0.4F, 0.0F);
            GL11.glScalef(f1, f1, f1);
            entity.setLocationAndAngles(d, d1, d2, 0.0F, 0.0F);
            RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, f);
        }

        GL11.glPopMatrix();
    }
	
	public void renderInvMobSpawner(int i)
	{
		if (Minecraft.getMinecraft().currentScreen != null && Minecraft.getMinecraft().currentScreen instanceof GuiIngameMenu)
		{
			return;
		}
		
		GL11.glPushMatrix();
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		tempID = i;
		GL11.glColor4f(1F, 1F, 1F, 1F);
		renderTileEntityAt(null, 0D, 0D, 0D, 0F);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		tempID = 0;
		GL11.glPopMatrix();
		
		GL11.glLoadIdentity();

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
        GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
}
