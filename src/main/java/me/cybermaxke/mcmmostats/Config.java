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

import java.io.File;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Config {
	private static File file;
	private static YamlConfiguration c;

	public Config(Plugin plugin) {
		file = new File(plugin.getDataFolder(), "Config.yml");

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e) {}

			c = new YamlConfiguration();
			save();
		} else {
			c = YamlConfiguration.loadConfiguration(file);
		}
	}

	public static ConfigurationSection getPlayerConfig() {
		if (!c.contains("Players")) {
			c.createSection("Players");
		}
		return c.getConfigurationSection("Players");
	}

	public static boolean isGuiShown(Player player) {
		ConfigurationSection s = getPlayerConfig();
		return s.contains(player.getName()) ? s.getBoolean(player.getName()) : true;
	}

	public static void setGuiShown(Player player, boolean show) {
		ConfigurationSection s = getPlayerConfig();
		s.set(player.getName(), show);
		save();
	}

	public static void save() {
		try {
			c.save(file);
		} catch (Exception e) {}
	}
}