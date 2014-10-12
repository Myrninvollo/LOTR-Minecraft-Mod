package lotr.client.gui.config;
 
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.client.IModGuiFactory;
 
public class LOTRGuiFactory implements IModGuiFactory
{
	@Override
    public void initialize(Minecraft mc) {}
 
    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass()
    {
        return LOTRGuiConfig.class;
    }
 
    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
    {
        return null;
    }
 
    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element)
    {
        return null;
    }
}