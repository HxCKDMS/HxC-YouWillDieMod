package YouWillDie.handlers;

import YouWillDie.Config;
import YouWillDie.misc.EntityStatHelper;
import YouWillDie.network.PacketHandler;
import YouWillDie.worldgen.WorldData;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.WorldServer;

import java.util.ArrayList;

public class CommonTickHandler {
	public static WorldData worldData = null;

	public static ArrayList<Entity> addLater = new ArrayList<Entity>();

	@SubscribeEvent
	public void worldTick(WorldTickEvent event) {
		if(event.phase == TickEvent.Phase.START) {
			if(event.world != null && event.world.provider.dimensionId == 0) {
				worldData = WorldData.forWorld(event.world);
				worldData.markDirty();
			}
		}
	}

	@SubscribeEvent
	public void serverTick(ServerTickEvent event) {
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

		for(int i = 0; i < server.worldServers.length; i++) {
			WorldServer s = FMLCommonHandler.instance().getMinecraftServerInstance().worldServers[i];

			if(s == null) {continue;}

			for(Object o : s.playerEntities) {
				if(o == null || !(o instanceof EntityPlayer)) {continue;}

				EntityPlayer player = (EntityPlayer) o;

				String blessing = player.getEntityData().hasKey("Blessing") ? player.getEntityData().getString("Blessing") : null;
				int coolDown = 0, counter = 0, timer = 0;
				boolean blessingActive = EntityStatHelper.hasStat(player, "BlessingActive") && Boolean.parseBoolean(EntityStatHelper.getStat(player, "BlessingActive"));

				if(EntityStatHelper.hasStat(player, "BlessingCooldown")) {
					coolDown = Integer.parseInt(EntityStatHelper.getStat(player, "BlessingCooldown"));
					if(coolDown > 0) {coolDown--;}
				}

				if(EntityStatHelper.hasStat(player, "BlessingTimer")) {
					timer = Integer.parseInt(EntityStatHelper.getStat(player, "BlessingTimer"));
					if(blessingActive) {timer++;} else {timer = 0;}
				}

				if(EntityStatHelper.hasStat(player, "BlessingCounter")) {
					counter = Integer.parseInt(EntityStatHelper.getStat(player, "BlessingCounter"));

					if(blessing != null) {
						if(blessingActive && blessing.equals("Berserker") && timer % 40 == 0) {
							counter = Math.max(0, counter - 1);

							if(counter == 0) {
								PacketHandler.SEND_MESSAGE.sendToPlayer(player, "\u00A7cYou calm down.");
								coolDown = 1200; timer = 0; blessingActive = false;
							}
						}
					}
				}

				if(blessing != null) {
					if(player.isSneaking() && blessing.equals("Ninja")) {
						PotionEffect e = player.getActivePotionEffect(Potion.invisibility);
						if(e == null || player.getActivePotionEffect(Potion.invisibility).getDuration() < 10) {player.addPotionEffect(new PotionEffect(Potion.invisibility.id, 10, 1, false));}
					} else if(blessing.equals("Diver")) {
						player.setAir(0);
					} else if(blessing.equals("Inferno") && player.isWet() && player.ticksExisted % 10 == 0) {
						player.attackEntityFrom(DamageSource.drown, 1.0F);
					}
				}

				if(EntityStatHelper.hasStat(player, "BlessingCounter") && Integer.parseInt(EntityStatHelper.getStat(player, "BlessingCounter")) != counter) {
					PacketHandler.UPDATE_STAT.sendToPlayer(player, "BlessingCounter", "" + counter);}
				
				EntityStatHelper.giveStat(player, "BlessingActive", blessingActive);
				EntityStatHelper.giveStat(player, "BlessingCooldown", coolDown);
				EntityStatHelper.giveStat(player, "BlessingCounter", counter);
				EntityStatHelper.giveStat(player, "BlessingTimer", timer);

			}

			for(Object o : s.loadedEntityList) {
				if(o == null || !(o instanceof Entity)) {continue;}

				if(o instanceof EntityItem) {
					EntityItem item = (EntityItem) o;

					if(((EntityItem) o).isBurning()) {
						ItemStack stack = item.getDataWatcher().getWatchableObjectItemStack(10);

						if(worldData.currentTask.equals("Burn") && (stack.getItem() == WorldData.validItems[worldData.currentTaskID].getItem() && stack.getItemDamage() == WorldData.validItems[worldData.currentTaskID].getItemDamage())) {
							worldData.progress += stack.stackSize;
							((EntityItem) o).isDead = true;

							String name1 = CommonTickHandler.worldData.currentTask.equals("Kill") ? WorldData.validMobNames[CommonTickHandler.worldData.currentTaskID] : WorldData.validItems[CommonTickHandler.worldData.currentTaskID].getDisplayName();

							if(name1.contains("Block")) {if(name1.contains("Block")) {name1 = name1.replace("Block", "Blocks").replace("block", "blocks");}}
							else {name1 += "s";}

							PacketHandler.SEND_MESSAGE.sendToAllPlayers("\u00A7fCurrent Goal Progress: " + worldData.progress + "/" + worldData.currentTaskAmount + " " + name1 + " "+ worldData.currentTask + "ed.");

							if(worldData.progress >= worldData.currentTaskAmount) {
								worldData.progress = 0;
								worldData.tasksCompleted++;

								PacketHandler.LEVEL_UP.sendToAllPlayers(worldData.rewardLevels);

								worldData.giveNewTask();


								String name = worldData.currentTask.equals("Kill") ? WorldData.validMobNames[worldData.currentTaskID] : WorldData.validItems[worldData.currentTaskID].getDisplayName();

								if(worldData.currentTaskAmount > 1) {
									if(name.contains("Block")) {if(name.contains("Block")) {name = name.replace("Block", "Blocks").replace("block", "blocks");}}
									else {name += "s";}
								}

								PacketHandler.SEND_MESSAGE.sendToAllPlayers("\u00A7eA world goal has been completed!" + (!worldData.getDisadvantage().equals("None") ? " World disadvantage has been lifted!": ""));

								PacketHandler.SEND_MESSAGE.sendToAllPlayers("\u00A7eA new world goal has been set: " + (worldData.currentTask + " " + worldData.currentTaskAmount + " " + name + ". (" + worldData.progress + " " + worldData.currentTask + "ed)"));

								worldData.currentDisadvantage = "None";
							}

							PacketHandler.UPDATE_WORLD_DATA.sendToAllPlayers(worldData.potionValues, worldData.potionDurations, worldData.getDisadvantage(), worldData.currentTask, worldData.currentTaskID, worldData.currentTaskAmount, worldData.progress, worldData.tasksCompleted, worldData.enderDragonKilled, Config.spawnTraps, worldData.rewardLevels, worldData.mushroomColors);

							worldData.setDirty(true);
						}
					}
				}
			}
		}

		for(Entity e : addLater) {
			WorldServer world = null;
			for(WorldServer s : MinecraftServer.getServer().worldServers) {if(s.provider.dimensionId == e.dimension) {world = s; break;}}
			if(world != null) {world.spawnEntityInWorld(e);}
		}
		addLater.clear();

		if(worldData != null) {
			if(worldData.getDisadvantage().equals("Neverending Rain")) {
				if(!MinecraftServer.getServer().worldServers[0].getWorldInfo().isRaining()) {MinecraftServer.getServer().worldServers[0].getWorldInfo().setRaining(true);}
				if(!MinecraftServer.getServer().worldServers[0].getWorldInfo().isThundering()) {MinecraftServer.getServer().worldServers[0].getWorldInfo().setThundering(true);}
			} else if(worldData.getDisadvantage().equals("Neverending Night")) {
				MinecraftServer.getServer().worldServers[0].getWorldInfo().setWorldTime(18000);
			}
		}
	}
}