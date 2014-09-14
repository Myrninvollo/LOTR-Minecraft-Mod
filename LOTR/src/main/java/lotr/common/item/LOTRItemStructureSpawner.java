package lotr.common.item;

import java.util.Iterator;
import java.util.List;

import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRLevelData;
import lotr.common.world.structure.LOTRStructures;
import lotr.common.world.structure.LOTRWorldGenStructureBase;
import lotr.common.world.structure2.LOTRWorldGenStructureBase2;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRItemStructureSpawner extends Item
{
	@SideOnly(Side.CLIENT)
	private IIcon baseIcon;
	@SideOnly(Side.CLIENT)
	private IIcon overlayIcon;
	public static int lastStructureSpawnTick = 0;
	
    public LOTRItemStructureSpawner()
    {
        super();
        setHasSubtypes(true);
        setCreativeTab(LOTRCreativeTabs.tabSpawn);
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemstack)
    {
        String s = ("" + StatCollector.translateToLocal(getUnlocalizedName() + ".name")).trim();
        String structureName = LOTRStructures.getNameFromID(itemstack.getItemDamage());

        if (structureName != null)
        {
            s = s + " " + StatCollector.translateToLocal("lotr.structure." + structureName + ".name");
        }

        return s;
    }

	@Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemstack, int pass)
    {
		LOTRStructures.StructureInfo info = LOTRStructures.structureSpawners.get(itemstack.getItemDamage());
        return info != null ? (pass == 0 ? info.primaryColor : info.secondaryColor) : 16777215;
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float f, float f1, float f2)
    {
        if (world.isRemote)
        {
            return true;
        }
		else
        {
			if (LOTRLevelData.structuresBanned == 1)
			{
				entityplayer.addChatMessage(new ChatComponentTranslation("chat.lotr.spawnStructure.disabled"));
				return false;
			}
			if (LOTRLevelData.isPlayerBannedForStructures(entityplayer))
			{
				entityplayer.addChatMessage(new ChatComponentTranslation("chat.lotr.spawnStructure.banned"));
				return false;
			}
			if (lastStructureSpawnTick > 0)
			{
				entityplayer.addChatMessage(new ChatComponentTranslation("chat.lotr.spawnStructure.wait", new Object[] {(double)lastStructureSpawnTick / 20D}));
				return false;
			}
			
            i += Facing.offsetsXForSide[side];
            j += Facing.offsetsYForSide[side];
            k += Facing.offsetsZForSide[side];

            if (spawnStructure(entityplayer, world, itemstack.getItemDamage(), i, j, k) && !entityplayer.capabilities.isCreativeMode)
            {
				itemstack.stackSize--;
            }
			
            return true;
        }
    }

    private boolean spawnStructure(EntityPlayer entityplayer, World world, int id, int i, int j, int k)
    {
        if (!LOTRStructures.structureSpawners.containsKey(Integer.valueOf(id)))
        {
            return false;
        }
        else
        {
        	WorldGenerator generator = LOTRStructures.getStructureFromID(id);
			if (generator != null)
			{
				boolean generated = false;
				
				if (generator instanceof LOTRWorldGenStructureBase2)
				{
					LOTRWorldGenStructureBase2 structureGenerator = (LOTRWorldGenStructureBase2)generator;
					structureGenerator.restrictions = false;
					structureGenerator.usingPlayer = entityplayer;
					generated = structureGenerator.generateWithSetRotation(world, world.rand, i, j, k, structureGenerator.usingPlayerRotation());
				}
				else if (generator instanceof LOTRWorldGenStructureBase)
				{
					((LOTRWorldGenStructureBase)generator).restrictions = false;
					((LOTRWorldGenStructureBase)generator).usingPlayer = entityplayer;
					generated = generator.generate(world, world.rand, i, j, k);
				}

				if (generated)
				{
					lastStructureSpawnTick = 20;
					world.playSoundAtEntity(entityplayer, "lotr:item.structureSpawner", 1F, 1F);
				}
				return generated;
			}
            return false;
        }
    }

	@Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int i, int j)
    {
        return j > 0 ? overlayIcon : baseIcon;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconregister)
    {
        baseIcon = iconregister.registerIcon(getIconString() + "_base");
		overlayIcon = iconregister.registerIcon(getIconString() + "_overlay");
    }

	@Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        Iterator it = LOTRStructures.structureSpawners.values().iterator();

        while (it.hasNext())
        {
        	LOTRStructures.StructureInfo info = (LOTRStructures.StructureInfo)it.next();
            list.add(new ItemStack(item, 1, info.spawnedID));
        }
    }
}
