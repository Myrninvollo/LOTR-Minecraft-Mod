package lotr.client.render;

import java.util.List;
import java.util.Random;

import lotr.client.render.entity.LOTRRandomSkins;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.client.IRenderHandler;

import org.lwjgl.opengl.GL11;

public class LOTRSkyRenderer extends IRenderHandler
{
    private static final ResourceLocation moonTexture = new ResourceLocation("textures/environment/moon_phases.png");
    private static final ResourceLocation sunTexture = new ResourceLocation("textures/environment/sun.png");
	
	private List skyTextures;
	private ResourceLocation currentSkyTexture;
	private Random skyRand = new Random();
	
    private int glSkyList;
    private int glSkyList2;
	
	public LOTRSkyRenderer()
	{
		skyTextures = LOTRRandomSkins.loadSkinsList("lotr:sky");
		
		Tessellator tessellator = Tessellator.instance;
		
		glSkyList = GLAllocation.generateDisplayLists(3);
        GL11.glNewList(glSkyList, GL11.GL_COMPILE);
        byte b2 = 64;
        int i = 256 / b2 + 2;
        float f = 16F;
        int j;
        int k;

        for (j = -b2 * i; j <= b2 * i; j += b2)
        {
            for (k = -b2 * i; k <= b2 * i; k += b2)
            {
                tessellator.startDrawingQuads();
                tessellator.addVertex((double)(j + 0), (double)f, (double)(k + 0));
                tessellator.addVertex((double)(j + b2), (double)f, (double)(k + 0));
                tessellator.addVertex((double)(j + b2), (double)f, (double)(k + b2));
                tessellator.addVertex((double)(j + 0), (double)f, (double)(k + b2));
                tessellator.draw();
            }
        }

        GL11.glEndList();
		
        glSkyList2 = glSkyList + 1;
        GL11.glNewList(glSkyList2, GL11.GL_COMPILE);
        f = -16F;
        tessellator.startDrawingQuads();

        for (j = -b2 * i; j <= b2 * i; j += b2)
        {
            for (k = -b2 * i; k <= b2 * i; k += b2)
            {
                tessellator.addVertex((double)(j + b2), (double)f, (double)(k + 0));
                tessellator.addVertex((double)(j + 0), (double)f, (double)(k + 0));
                tessellator.addVertex((double)(j + 0), (double)f, (double)(k + b2));
                tessellator.addVertex((double)(j + b2), (double)f, (double)(k + b2));
            }
        }

        tessellator.draw();
        GL11.glEndList();
	}
	
	private ResourceLocation getRandomSkyTexture(World world)
	{
		return (ResourceLocation)skyTextures.get(skyRand.nextInt(skyTextures.size()));
	}
	
	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc)
	{
		world.theProfiler.startSection("lotrSky");
		
		boolean renderSkyFeatures = true;
		
		int i = MathHelper.floor_double(mc.renderViewEntity.posX);
		int k = MathHelper.floor_double(mc.renderViewEntity.posZ);
		BiomeGenBase biome = world.getBiomeGenForCoords(i, k);
		if (biome instanceof LOTRBiome)
		{
			renderSkyFeatures = ((LOTRBiome)biome).hasSky();
		}
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		Vec3 skyColor = world.getSkyColor(mc.renderViewEntity, partialTicks);
		float skyR = (float)skyColor.xCoord;
		float skyG = (float)skyColor.yCoord;
		float skyB = (float)skyColor.zCoord;

		if (mc.gameSettings.anaglyph)
		{
			float newSkyR = (skyR * 30F + skyG * 59F + skyB * 11F) / 100F;
			float newSkyG = (skyR * 30F + skyG * 70F) / 100F;
			float newSkyB = (skyR * 30F + skyB * 70F) / 100F;
			skyR = newSkyR;
			skyG = newSkyG;
			skyB = newSkyB;
		}

		GL11.glColor3f(skyR, skyG, skyB);
		Tessellator tessellator = Tessellator.instance;
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_FOG);
		GL11.glColor3f(skyR, skyG, skyB);
		GL11.glCallList(glSkyList);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderHelper.disableStandardItemLighting();
		float[] afloat = world.provider.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks);
		float f7;
		float f8;
		float f9;
		float f10;

		if (afloat != null)
		{
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glPushMatrix();
			GL11.glRotatef(90F, 1F, 0F, 0F);
			GL11.glRotatef(MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) < 0F ? 180F : 0F, 0F, 0F, 1F);
			GL11.glRotatef(90F, 0F, 0F, 1F);
			float f4 = afloat[0];
			f7 = afloat[1];
			f8 = afloat[2];
			float f11;

			if (mc.gameSettings.anaglyph)
			{
				f9 = (f4 * 30F + f7 * 59F + f8 * 11F) / 100F;
				f10 = (f4 * 30F + f7 * 70F) / 100F;
				f11 = (f4 * 30F + f8 * 70F) / 100F;
				f4 = f9;
				f7 = f10;
				f8 = f11;
			}

			tessellator.startDrawing(6);
			tessellator.setColorRGBA_F(f4, f7, f8, afloat[3]);
			tessellator.addVertex(0D, 100D, 0D);
			byte b0 = 16;
			tessellator.setColorRGBA_F(afloat[0], afloat[1], afloat[2], 0F);

			for (int j = 0; j <= b0; ++j)
			{
				f11 = (float)j * (float)Math.PI * 2F / (float)b0;
				float f12 = MathHelper.sin(f11);
				float f13 = MathHelper.cos(f11);
				tessellator.addVertex((double)(f12 * 120F), (double)(f13 * 120F), (double)(-f13 * 40F * afloat[3]));
			}

			tessellator.draw();
			GL11.glPopMatrix();
			GL11.glShadeModel(GL11.GL_FLAT);
		}
		
		GL11.glPushMatrix();
		if (renderSkyFeatures)
		{
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			float rainBrightness = 1F - world.getRainStrength(partialTicks);
			f7 = 0F;
			f8 = 0F;
			f9 = 0F;
			GL11.glColor4f(1F, 1F, 1F, rainBrightness);
			GL11.glTranslatef(f7, f8, f9);
			GL11.glRotatef(-90F, 0F, 1F, 0F);
			
			float starBrightness = world.getStarBrightness(partialTicks) * rainBrightness;
			if (starBrightness > 0F)
			{
				if (currentSkyTexture == null)
				{
					currentSkyTexture = getRandomSkyTexture(world);
				}
				mc.renderEngine.bindTexture(currentSkyTexture);
				
				GL11.glPushMatrix();
				GL11.glRotatef(world.getCelestialAngle(partialTicks) * 360F, 1F, 0F, 0F);
				GL11.glRotatef(-90F, 1F, 0F, 0F);
				GL11.glColor4f(1F, 1F, 1F, starBrightness);

				GL11.glRotatef(90F, 1F, 0F, 0F);
				GL11.glRotatef(-90F, 0F, 0F, 1F);
				renderSide(tessellator, 4);

				GL11.glPushMatrix();
				GL11.glRotatef(90F, 1F, 0F, 0F);
				renderSide(tessellator, 1);
				GL11.glPopMatrix();

				GL11.glPushMatrix();
				GL11.glRotatef(-90F, 1F, 0F, 0F);
				renderSide(tessellator, 0);
				GL11.glPopMatrix();

				GL11.glRotatef(90F, 0F, 0F, 1F);
				renderSide(tessellator, 5);

				GL11.glRotatef(90F, 0F, 0F, 1F);
				renderSide(tessellator, 2);

				GL11.glRotatef(90F, 0F, 0F, 1F);
				renderSide(tessellator, 3);

				GL11.glPopMatrix();
			}
			else
			{
				if (currentSkyTexture != null)
				{
					currentSkyTexture = null;
				}
			}
			
			GL11.glRotatef(world.getCelestialAngle(partialTicks) * 360F, 1F, 0F, 0F);
			GL11.glColor4f(1F, 1F, 1F, rainBrightness);
			
			f10 = 30F;
			mc.renderEngine.bindTexture(sunTexture);
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV((double)(-f10), 100D, (double)(-f10), 0D, 0D);
			tessellator.addVertexWithUV((double)f10, 100D, (double)(-f10), 1D, 0D);
			tessellator.addVertexWithUV((double)f10, 100D, (double)f10, 1D, 1D);
			tessellator.addVertexWithUV((double)(-f10), 100D, (double)f10, 0D, 1D);
			tessellator.draw();
			
			f10 = 20F;
			mc.renderEngine.bindTexture(moonTexture);
			int moonPhase = world.getMoonPhase();
			int l = moonPhase % 4;
			int i1 = moonPhase / 4 % 2;
			float f14 = (float)(l + 0) / 4F;
			float f15 = (float)(i1 + 0) / 2F;
			float f16 = (float)(l + 1) / 4F;
			float f17 = (float)(i1 + 1) / 2F;
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV((double)(-f10), -100D, (double)f10, (double)f16, (double)f17);
			tessellator.addVertexWithUV((double)f10, -100D, (double)f10, (double)f14, (double)f17);
			tessellator.addVertexWithUV((double)f10, -100D, (double)(-f10), (double)f14, (double)f15);
			tessellator.addVertexWithUV((double)(-f10), -100D, (double)(-f10), (double)f16, (double)f15);
			tessellator.draw();
			
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}

		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_FOG);
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(0F, 0F, 0F);
		double d0 = mc.thePlayer.getPosition(partialTicks).yCoord - world.getHorizon();

		if (d0 < 0D)
		{
			GL11.glPushMatrix();
			GL11.glTranslatef(0F, 12F, 0F);
			GL11.glCallList(glSkyList2);
			GL11.glPopMatrix();
			f8 = 1F;
			f9 = -((float)(d0 + 65D));
			f10 = -f8;
			tessellator.startDrawingQuads();
			tessellator.setColorRGBA_I(0, 255);
			tessellator.addVertex((double)(-f8), (double)f9, (double)f8);
			tessellator.addVertex((double)f8, (double)f9, (double)f8);
			tessellator.addVertex((double)f8, (double)f10, (double)f8);
			tessellator.addVertex((double)(-f8), (double)f10, (double)f8);
			tessellator.addVertex((double)(-f8), (double)f10, (double)(-f8));
			tessellator.addVertex((double)f8, (double)f10, (double)(-f8));
			tessellator.addVertex((double)f8, (double)f9, (double)(-f8));
			tessellator.addVertex((double)(-f8), (double)f9, (double)(-f8));
			tessellator.addVertex((double)f8, (double)f10, (double)(-f8));
			tessellator.addVertex((double)f8, (double)f10, (double)f8);
			tessellator.addVertex((double)f8, (double)f9, (double)f8);
			tessellator.addVertex((double)f8, (double)f9, (double)(-f8));
			tessellator.addVertex((double)(-f8), (double)f9, (double)(-f8));
			tessellator.addVertex((double)(-f8), (double)f9, (double)f8);
			tessellator.addVertex((double)(-f8), (double)f10, (double)f8);
			tessellator.addVertex((double)(-f8), (double)f10, (double)(-f8));
			tessellator.addVertex((double)(-f8), (double)f10, (double)(-f8));
			tessellator.addVertex((double)(-f8), (double)f10, (double)f8);
			tessellator.addVertex((double)f8, (double)f10, (double)f8);
			tessellator.addVertex((double)f8, (double)f10, (double)(-f8));
			tessellator.draw();
		}

		if (world.provider.isSkyColored())
		{
			GL11.glColor3f(skyR * 0.2F + 0.04F, skyG * 0.2F + 0.04F, skyB * 0.6F + 0.1F);
		}
		else
		{
			GL11.glColor3f(skyR, skyG, skyB);
		}

		GL11.glPushMatrix();
		GL11.glTranslatef(0F, -((float)(d0 - 16D)), 0F);
		GL11.glCallList(glSkyList2);
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDepthMask(true);
		
		world.theProfiler.endSection();
	}
	
	private void renderSide(Tessellator tessellator, int side)
	{
		double u = side % 3 / 3D;
		double v = side / 3 / 2D;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(-100D, -100D, -100D, u, v);
		tessellator.addVertexWithUV(-100D, -100D, 100D, u, v + 0.5D);
		tessellator.addVertexWithUV(100D, -100D, 100D, u + 0.3333333333333333D, v + 0.5D);
		tessellator.addVertexWithUV(100D, -100D, -100D, u + 0.3333333333333333D, v);
		tessellator.draw();
	}
}
