package lotr.common.command;

import java.util.List;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFaction;
import lotr.common.LOTRLevelData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class LOTRCommandAlignment extends CommandBase
{
	@Override
    public String getCommandName()
    {
        return "alignment";
    }

	@Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

	@Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.lotr.alignment.usage";
    }

	@Override
    public void processCommand(ICommandSender sender, String[] input)
    {
        if (input.length > 1)
        {
        	LOTRFaction faction = LOTRFaction.forName(input[1]);
        	if (faction == null)
        	{
        		throw new WrongUsageException("commands.lotr.alignment.noFaction", new Object[] {input[1]});
        	}
        	
			if (input[0].equals("set"))
			{
				int alignment = parseIntBounded(sender, input[2], -LOTRAlignmentValues.MAX_ALIGNMENT, LOTRAlignmentValues.MAX_ALIGNMENT);
				EntityPlayerMP entityplayer;

				if (input.length > 3)
				{
					entityplayer = getPlayer(sender, input[3]);
				}
				else
				{
					entityplayer = getCommandSenderAsPlayer(sender);
					if (entityplayer == null)
					{
						throw new PlayerNotFoundException();
					}
				}

				LOTRLevelData.addAlignmentFromCommand(entityplayer, faction, alignment - LOTRLevelData.getAlignment(entityplayer, faction));
				notifyAdmins(sender, "commands.lotr.alignment.set", new Object[] {sender.getCommandSenderName(), faction.name(), alignment});
				return;
			}
			
			if (input[0].equals("add"))
			{
				int alignment = parseInt(sender, input[2]);
				EntityPlayerMP entityplayer;

				if (input.length > 3)
				{
					entityplayer = getPlayer(sender, input[3]);
				}
				else
				{
					entityplayer = getCommandSenderAsPlayer(sender);
					if (entityplayer == null)
					{
						throw new PlayerNotFoundException();
					}
				}
				
				int newAlignment = LOTRLevelData.getAlignment(entityplayer, faction) + alignment;
				if (newAlignment < -LOTRAlignmentValues.MAX_ALIGNMENT)
				{
					throw new WrongUsageException("commands.lotr.alignment.tooLow", new Object[] {-LOTRAlignmentValues.MAX_ALIGNMENT});
				}
				else if (newAlignment > LOTRAlignmentValues.MAX_ALIGNMENT)
				{
					throw new WrongUsageException("commands.lotr.alignment.tooHigh", new Object[] {LOTRAlignmentValues.MAX_ALIGNMENT});
				}

				LOTRLevelData.addAlignmentFromCommand(entityplayer, faction, alignment);
				notifyAdmins(sender, "commands.lotr.alignment.add", new Object[] {alignment, sender.getCommandSenderName(), faction.name()});
				return;
			}
        }
		
		throw new WrongUsageException(getCommandUsage(sender), new Object[0]);
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] input)
    {
		if (input.length == 1)
		{
			return getListOfStringsMatchingLastWord(input, new String[] {"set", "add"});
		}
		if (input.length == 2)
		{
			List list = LOTRFaction.getFactionNameList();
			return getListOfStringsMatchingLastWord(input, (String[])list.toArray(new String[list.size()]));
		}
		if (input.length == 4)
		{
			return getListOfStringsMatchingLastWord(input, MinecraftServer.getServer().getAllUsernames());
		}
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] input, int i)
    {
        return i == 3;
    }
}