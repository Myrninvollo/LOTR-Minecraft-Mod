package lotr.client.render.entity;

import java.util.HashMap;

import lotr.client.model.LOTRModelEnt;
import lotr.common.entity.npc.LOTREntityEnt;
import lotr.common.entity.npc.LOTREntityTree;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderEnt extends RenderLiving
{
	private static HashMap entTextures = new HashMap();
	
    public LOTRRenderEnt()
    {
        super(new LOTRModelEnt(), 0.5F);
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        int treeType = ((LOTREntityEnt)entity).getTreeType();
		if (treeType < 0 || treeType >= LOTREntityTree.TYPES.length)
		{
			treeType = 0;
		}
		String s = "lotr:mob/ent/" + LOTREntityTree.TYPES[treeType] + ".png";
		ResourceLocation r = (ResourceLocation)entTextures.get(Integer.valueOf(treeType));
		if (r == null)
		{
			r = new ResourceLocation(s);
			entTextures.put(Integer.valueOf(treeType), r);
		}
		return r;
    }
}
