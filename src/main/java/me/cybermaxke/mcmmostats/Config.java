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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Config {
	private static Map<String, Object> defaults = new HashMap<String, Object>();
	private static File file;
	private static YamlConfiguration c;

	public static Boolean SHOW_BY_DEFAULT = true;
	//public static Character COLOR_CHAR = '§';
	public static Character COLOR_CHAR = ChatColor.COLOR_CHAR;

	public Config(Plugin plugin) {
		try {
			if (!plugin.getDataFolder().exists()) {
				plugin.getDataFolder().mkdirs();
			}

			file = new File(plugin.getDataFolder(), "Config.yml");
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (Exception e) {}

		add("ColorCharacter", COLOR_CHAR);
		add("ShowByDefault", SHOW_BY_DEFAULT);
		load();

		String cchar = get("ColorCharacter") + "";
		//COLOR_CHAR = cchar.contains("§") ? null : cchar.charAt(0);
		COLOR_CHAR = cchar.contains(ChatColor.COLOR_CHAR + "") ? null : cchar.charAt(0);
		SHOW_BY_DEFAULT = get("ShowByDefault", Boolean.class);
	}

	public static void load() {
		c = YamlConfiguration.loadConfiguration(file);

		for (Entry<String, Object> en : defaults.entrySet()) {
			String k = en.getKey();
			if (!c.contains(k)) {
				c.set(k, en.getValue());
			} else {
				defaults.put(k, c.get(k));
			}
		}

		try {
			c.save(file);
		} catch (Exception e) {}
	}

	public static ConfigurationSection getPlayerConfig() {
		if (!c.contains("Players")) {
			c.createSection("Players");
		}
		return c.getConfigurationSection("Players");
	}

	public static boolean isGuiShown(Player player) {
		ConfigurationSection s = getPlayerConfig();
		return s.contains(player.getName()) ? s.getBoolean(player.getName()) : SHOW_BY_DEFAULT;
	}

	public static void setGuiShown(Player player, boolean show) {
		ConfigurationSection s = getPlayerConfig();
		s.set(player.getName(), show);
		save();
	}

	public static <T> T get(String path, Class<T> clazz) {
		return c.contains(path) ? clazz.cast(c.get(path)) : null;
	}

	public static Object get(String path) {
		return c.contains(path) ? c.get(path) : null;
	}

	public static <T> void add(String path, T def) {
		defaults.put(path, def);
	}

	public static String translateAlternateColorCodes(String string) {
		return COLOR_CHAR == null ? string : ChatColor.translateAlternateColorCodes(COLOR_CHAR, string);
	}

	public static String tralslateColorCodeToNew(String string) {
		return COLOR_CHAR == null ? string : string.replace(ChatColor.COLOR_CHAR, COLOR_CHAR);
	}

	public static void save() {
		try {
			c.save(file);
		} catch (Exception e) {}
	}
}