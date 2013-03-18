/**
 * 
 * This software is part of the mcMMOStatsGui
 * 
 * mcMMOStatsGui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * any later version.
 *  
 * mcMMOStatsGui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mcMMOStatsGui. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package me.cybermaxke.mcmmostats;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class GuiCommand implements CommandExecutor {

	public GuiCommand(JavaPlugin plugin) {
		plugin.getCommand("statsgui").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("You have to be a player to use this command.");
			return true;
		}

		if (args.length != 1) {
			sender.sendMessage("Please use '/statsgui [hide|show]'");
			return true;
		}

		Player p = (Player) sender;
		if (args[0].equalsIgnoreCase("hide")) {
			McMMOStats.getGui(p).hide();
			sender.sendMessage("Stats gui hidden.");
		} else if (args[0].equalsIgnoreCase("show")) {
			McMMOStats.getGui(p).show();
			sender.sendMessage("Stats gui shown.");
		} else {
			sender.sendMessage("Please use '/statsgui [hide|show]'");
		}

		return true;
	}
}