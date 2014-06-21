package lotr.common.command;

import lotr.common.entity.LOTREntities;
import net.minecraft.command.server.CommandSummon;

public class LOTRCommandSummon extends CommandSummon
{
	@Override
    public String getCommandName()
    {
        return "lotr_summon";
    }
    
	@Override
    protected String[] func_147182_d()
    {
        return (String[])LOTREntities.getSummonNames().toArray(new String[0]);
    }
}
