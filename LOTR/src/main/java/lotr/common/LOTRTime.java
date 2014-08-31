package lotr.common;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class LOTRTime
{
	public static void saveDates(NBTTagCompound levelData)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("ShireDate", ShireReckoning.currentDay);
		levelData.setTag("Dates", nbt);
	}
	
	public static void loadDates(NBTTagCompound levelData)
	{
		if (levelData.hasKey("Dates"))
		{
			NBTTagCompound nbt = levelData.getCompoundTag("Dates");
			ShireReckoning.currentDay = nbt.getInteger("ShireDate");
		}
		else
		{
			ShireReckoning.currentDay = 0;
		}
	}
	
	private static int ticksInDay = 20 * 60 * 20;
	private static long prevWorldTime = -1L;
	
	public static void update(World world)
	{
		long worldTime = world.getWorldTime();
		
		if (prevWorldTime == -1L)
		{
			prevWorldTime = worldTime;
		}
		
		long prevDay = prevWorldTime / ticksInDay;
		long day = worldTime / ticksInDay;
		
		if (day != prevDay)
		{
			ShireReckoning.currentDay++;
			LOTRLevelData.markDirty();
			
			System.out.println("Updating LOTR day");
			for (Object obj : MinecraftServer.getServer().getConfigurationManager().playerEntityList)
			{
				EntityPlayerMP entityplayer = (EntityPlayerMP)obj;
				sendUpdatePacket(entityplayer, true);
			}
		}
		
		prevWorldTime = worldTime;
	}
	
	public static void sendUpdatePacket(EntityPlayerMP entityplayer, boolean update)
	{
		try
		{
			ByteBuf data = Unpooled.buffer();
			
			data.writeBoolean(update);
			
			NBTTagCompound nbt = new NBTTagCompound();
			saveDates(nbt);
			new PacketBuffer(data).writeNBTTagCompoundToBuffer(nbt);

			S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.time", data);
			((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(packet);
		}
		catch (IOException e)
		{
			System.out.println("Failed to send LOTR time info to player " + entityplayer.getCommandSenderName());
			e.printStackTrace();
		}
	}
	
	public static class ShireReckoning
	{
		public static enum Day
		{
			STERDAY("sterday"),
			SUNDAY("sunday"),
			MONDAY("monday"),
			TREWSDAY("trewsday"),
			HEVENSDAY("hevensday"),
			MERSDAY("mersday"),
			HIGHDAY("highday");
			
			private String name;
			
			private Day(String s)
			{
				name = s;
			}
			
			public String getDayName()
			{
				return StatCollector.translateToLocal("lotr.date.shire.day." + name);
			}
		}
		
		public static enum Month
		{
			YULE_2("yule2", 1),
			AFTERYULE("afteryule", 30),
			SOLMATH("solmath", 30),
			RETHE("rethe", 30),
			ASTRON("astron", 30),
			THRIMIDGE("thrimidge", 30),
			FORELITHE("forelithe", 30),
			LITHE_1("lithe1", 1),
			MIDYEARSDAY("midyearsday", 1, false, false),
			OVERLITHE("overlithe", 1, false, true),
			LITHE_2("lithe2", 1),
			AFTERLITHE("afterlithe", 30),
			WEDMATH("wedmath", 30),
			HALIMATH("halimath", 30),
			WINTERFILTH("winterfilth", 30),
			BLOTMATH("blotmath", 30),
			FOREYULE("foreyule", 30),
			YULE_1("yule1", 1);
			
			private String name;
			public int days;
			public boolean hasWeekdayName;
			public boolean isLeapYear;
			
			private Month(String s, int i)
			{
				this(s, i, true, false);
			}
			
			private Month(String s, int i, boolean flag, boolean flag1)
			{
				name = s;
				days = i;
				hasWeekdayName = flag;
				isLeapYear = flag1;
			}
			
			public String getMonthName()
			{
				return StatCollector.translateToLocal("lotr.date.shire.month." + name);
			}
			
			public boolean isSingleDay()
			{
				return days == 1;
			}
		}
		
		public static class Date
		{
			public final int year;
			public final Month month;
			public final int monthDate;
			private Day day;
			
			public Date(int y, Month m, int d)
			{
				year = y;
				month = m;
				monthDate = d;
			}
			
			public String getDateName(boolean longName)
			{
				StringBuilder builder = new StringBuilder();
				if (month.hasWeekdayName)
				{
					builder.append(getDay().getDayName());
				}
				builder.append(" ");
				if (!month.isSingleDay())
				{
					builder.append(monthDate);
				}
				builder.append(" ");
				builder.append(month.getMonthName());
				builder.append(", ");
				if (longName)
				{
					builder.append(StatCollector.translateToLocal("lotr.date.shire.long"));
				}
				else
				{
					builder.append(StatCollector.translateToLocal("lotr.date.shire"));
				}
				builder.append(" ");
				builder.append(year);
				return builder.toString();
			}
			
			public Day getDay()
			{
				if (!month.hasWeekdayName)
				{
					return null;
				}
				else
				{
					if (day == null)
					{
						int yearDay = 0;
						
						int monthID = month.ordinal();
						for (int i = 0; i < monthID; i++)
						{
							Month m = Month.values()[i];
							if (m.hasWeekdayName)
							{
								yearDay += m.days;
							}
						}
						
						yearDay += monthDate;
						
						int dayID = (yearDay - 1) % Day.values().length;
						day = Day.values()[dayID];
					}
					return day;
				}
			}
			
			public Date copy()
			{
				return new Date(year, month, monthDate);
			}
			
			public Date increment()
			{
				int newYear = year;
				Month newMonth = month;
				int newDate = monthDate;
				
				newDate++;
				if (newDate > newMonth.days)
				{
					newDate = 1;
					
					int monthID = newMonth.ordinal();
					monthID++;
					
					if (monthID >= Month.values().length)
					{
						monthID = 0;
						newYear++;
					}
					newMonth = Month.values()[monthID];
					
					if (newMonth.isLeapYear && !isLeapYear(newYear))
					{
						monthID++;
						newMonth = Month.values()[monthID];
					}
				}
				
				return new Date(newYear, newMonth, newDate);
			}
		}
		
		public static boolean isLeapYear(int year)
		{
			return year % 4 == 0 && year % 100 != 0;
		}

		public static Date getShireDate()
		{
			Date date = startDate.copy();
			long day = currentDay;
			for (int i = 0; i < day; i++)
			{
				date = date.increment();
			}
			return date;
		}
		
		public static Date startDate = new Date(1401, Month.HALIMATH, 22);
		public static int currentDay = 0;
	}
}
