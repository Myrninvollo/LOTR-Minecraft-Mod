package lotr.client.gui.config;

import lotr.common.LOTRMod;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.config.GuiConfig;

public class LOTRGuiConfig extends GuiConfig
{
	public LOTRGuiConfig(GuiScreen parent)
	{
	    super(parent, new ConfigElement(LOTRMod.modConfig.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), LOTRMod.getModID(), false, false, GuiConfig.getAbridgedConfigPath(LOTRMod.modConfig.toString()));
	}
}
