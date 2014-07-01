package lotr.common.command;

import java.util.List;

import lotr.common.LOTRLevelData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

public class LOTRCommandFastTravelCooldown extends CommandBase
{
	@Override
    public String getCommandName()
    {
        return "fastTravelCooldown";
    }

	@Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

	@Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.lotr.fastTravelCooldown.usage";
    }

	@Override
    public void processCommand(ICommandSender sender, String[] input)
    {
        if (input.length > 0)
        {
			int cooldown = parseIntBounded(sender, input[0], 0, 1728000);
			LOTRLevelData.setFastTravelCooldown(cooldown);
			func_152373_a(sender, this, "commands.lotr.fastTravelCooldown.set", new Object[] {cooldown, LOTRLevelData.getHMSTime(cooldown)});
			return;
		}
		
		throw new WrongUsageException(getCommandUsage(sender), new Object[0]);
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] input)
    {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] input, int i)
    {
        return false;
    }
}