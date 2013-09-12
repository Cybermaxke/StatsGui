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

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsGuiCommand implements CommandExecutor {
	private final StatsPlugin plugin;

	public StatsGuiCommand(StatsPlugin plugin) {
		this.plugin = plugin;
		this.plugin.getCommand("statsgui").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("You have to be a player to perform this command.");
			return true;
		}

		Player p = (Player) sender;
		if (!StatsPermissions.hasUseGuiPermission(p)) {
			p.sendMessage(ChatColor.RED + "I'm sorry, but you don't have permission to perform this command. ");
			return true;
		}

		if (args.length != 1) {
			sender.sendMessage("Please use '/statsgui [hide|show]'");
			return true;
		}

		if (args[0].equalsIgnoreCase("hide")) {
			this.plugin.getGui(p).hide();
			sender.sendMessage("Stats gui hidden.");
		} else if (args[0].equalsIgnoreCase("show")) {
			this.plugin.getGui(p).show();
			sender.sendMessage("Stats gui shown.");
		} else {
			sender.sendMessage("Please use '/statsgui [hide|show]'");
		}

		return true;
	}
}