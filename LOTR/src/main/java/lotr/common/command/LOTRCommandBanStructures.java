package lotr.common.command;

import java.util.List;

import lotr.common.LOTRLevelData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;

public class LOTRCommandBanStructures extends CommandBase
{
	@Override
    public String getCommandName()
    {
        return "banStructures";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 3;
    }

	@Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "";
    }

	@Override
    public void processCommand(ICommandSender sender, String[] input)
    {
		if (input.length == 0)
		{
			if (LOTRLevelData.structuresBanned == 1)
			{
				throw new WrongUsageException("commands.lotr.banStructures.alreadyBanned");
			}
			LOTRLevelData.structuresBanned = 1;
			LOTRLevelData.needsSave = true;
			func_152373_a(sender, this, "commands.lotr.banStructures.ban", new Object[0]);
		}
        else
        {
			LOTRLevelData.bannedStructurePlayers.add(input[0]);
			LOTRLevelData.needsSave = true;
			func_152373_a(sender, this, "commands.lotr.banStructures.banPlayer", new Object[] {input[0]});
			EntityPlayerMP entityplayer = getPlayer(sender, input[0]);
			if (entityplayer != null)
			{
				entityplayer.addChatMessage(new ChatComponentTranslation("chat.lotr.banStructures"));
			}
		}
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] input)
    {
		if (input.length == 1)
		{
			return getListOfStringsMatchingLastWord(input, MinecraftServer.getServer().getAllUsernames());
		}
        return null;
    }
}
