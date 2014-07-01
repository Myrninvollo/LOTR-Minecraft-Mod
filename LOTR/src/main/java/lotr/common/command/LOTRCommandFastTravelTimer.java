package lotr.common.command;

import java.util.List;

import lotr.common.LOTRLevelData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class LOTRCommandFastTravelTimer extends CommandBase
{
	@Override
    public String getCommandName()
    {
        return "fastTravelTimer";
    }

	@Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

	@Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.lotr.fastTravelTimer.usage";
    }

	@Override
    public void processCommand(ICommandSender sender, String[] input)
    {
        if (input.length > 0)
        {
			int timer = parseIntBounded(sender, input[0], 0, 1728000);
			EntityPlayerMP entityplayer;

			if (input.length > 1)
			{
				entityplayer = getPlayer(sender, input[1]);
			}
			else
			{
				entityplayer = getCommandSenderAsPlayer(sender);
				if (entityplayer == null)
				{
					throw new PlayerNotFoundException();
				}
			}

			LOTRLevelData.setFastTravelTimer(entityplayer, timer);
			func_152373_a(sender, this, "commands.lotr.fastTravelTimer.set", new Object[] {entityplayer.getCommandSenderName(), timer, LOTRLevelData.getHMSTime(timer)});
			return;
		}
		
		throw new WrongUsageException(getCommandUsage(sender), new Object[0]);
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] input)
    {
		if (input.length == 2)
		{
			return getListOfStringsMatchingLastWord(input, MinecraftServer.getServer().getAllUsernames());
		}
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] input, int i)
    {
        return i == 1;
    }
}