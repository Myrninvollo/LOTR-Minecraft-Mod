package lotr.common.command;

import java.util.ArrayList;
import java.util.List;

import lotr.common.LOTRLevelData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;

public class LOTRCommandAllowStructures extends CommandBase
{
	@Override
    public String getCommandName()
    {
        return "allowStructures";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 3;
    }

	@Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.lotr.allowStructures.usage";
    }

	@Override
    public void processCommand(ICommandSender sender, String[] input)
    {
		if (input.length == 0)
		{
			if (LOTRLevelData.structuresBanned == 0)
			{
				throw new WrongUsageException("commands.lotr.allowStructures.alreadyAllowed");
			}
			LOTRLevelData.structuresBanned = 0;
			LOTRLevelData.needsSave = true;
			notifyAdmins(sender, "commands.lotr.allowStructures.allow", new Object[0]);
		}
        else
        {
			LOTRLevelData.bannedStructurePlayers.remove(input[0]);
			LOTRLevelData.needsSave = true;
			notifyAdmins(sender, "commands.lotr.allowStructures.allowPlayer", new Object[] {input[0]});
			EntityPlayerMP entityplayer = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(input[0]);
			if (entityplayer != null)
			{
				entityplayer.addChatMessage(new ChatComponentTranslation("chat.lotr.allowStructures"));
			}
		}
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] input)
    {
		if (input.length == 1)
		{
			List list = new ArrayList();
			list.addAll(LOTRLevelData.bannedStructurePlayers);
			return list;
		}
        return null;
    }
}
