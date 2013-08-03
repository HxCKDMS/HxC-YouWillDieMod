package assets.fyresmodjam;

import java.util.List;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandCurrentBlessing implements ICommand {

	public int compareTo(Object arg0) {return 0;}

	public String getCommandName() {return "currentBlessing";}

	public String getCommandUsage(ICommandSender icommandsender) {return "commands.currentBlessing.usage";}

	public List getCommandAliases() {return null;}

	public void processCommand(ICommandSender icommandsender, String[] astring) {
		if(icommandsender instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) icommandsender;
			String blessing = entityplayer.getEntityData().hasKey("Blessing") ? entityplayer.getEntityData().getString("Blessing") : null;
			PacketDispatcher.sendPacketToPlayer(PacketHandler.newPacket(PacketHandler.SEND_MESSAGE, new Object[] {blessing != null ? "\u00A7eCurrent Blessing: \u00A7oBlessing of the " + blessing : "You don't currently have a blessing."}), (Player) entityplayer);
		}
	}

	public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {return true;}

	public List addTabCompletionOptions(ICommandSender icommandsender, String[] astring) {return null;}

	public boolean isUsernameIndex(String[] astring, int i) {return false;}
	
	public int getRequiredPermissionLevel() {return 0;}
	
}