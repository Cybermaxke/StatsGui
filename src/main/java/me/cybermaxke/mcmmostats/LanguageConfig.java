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

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.gmail.nossr50.datatypes.skills.SkillType;

public class LanguageConfig {
	private static File file;
	private static YamlConfiguration c;

	private static Map<SkillType, String> skills = new HashMap<SkillType, String>();
	private static Map<String, String> names = new HashMap<String, String>();

	public LanguageConfig(Plugin plugin) {
		try {
			if (!plugin.getDataFolder().exists()) {
				plugin.getDataFolder().mkdirs();
			}

			file = new File(plugin.getDataFolder(), "Language.yml");
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (Exception e) {}

		add(SkillType.ACROBATICS, "Acrobatics");
		add(SkillType.ARCHERY, "Archery");
		add(SkillType.AXES, "Axes");
		add(SkillType.EXCAVATION, "Excavation");
		add(SkillType.FISHING, "Fishing");
		add(SkillType.HERBALISM, "Herbalism");
		add(SkillType.MINING, "Mining");
		add(SkillType.REPAIR, "Repairing");
		add(SkillType.SWORDS, "Swords");
		add(SkillType.TAMING, "Taming");
		add(SkillType.UNARMED, "Unarmed");
		add(SkillType.WOODCUTTING, "Woodcutting");
		add(SkillType.SMELTING, "Smelting");
		add("LEVEL", "Level");
		add("REQUIRED_XP", "Required XP");
		add("EARNED_XP", "Earned XP");
		add("POWER_LEVEL", "Power Level");
		add("SKILL_STATS", "Skill Stats");

		load();
	}

	public static void load() {
		c = YamlConfiguration.loadConfiguration(file);

		for (Entry<String, String> en : names.entrySet()) {
			String k = en.getKey();
			if (!c.contains(k)) {
				c.set(k, Config.tralslateColorCodeToNew(en.getValue()));
			} else {
				names.put(k, Config.translateAlternateColorCodes(c.getString(k)));
			}
		}

		for (Entry<SkillType, String> t : skills.entrySet()) {
			String k = "Skills." + t.getKey().toString();
			if (!c.contains(k)) {
				c.set(k, Config.tralslateColorCodeToNew(t.getValue()));
			} else {
				String s = c.getString(k);
				if (s.length() > 16) {
					s = s.substring(0, 16);
				}
				skills.put(t.getKey(), Config.translateAlternateColorCodes(s));
			}
		}

		try {
			c.save(file);
		} catch (Exception e) {}
	}

	public static void add(String path, String def) {
		names.put(path, def);
	}

	public static void add(SkillType type, String def) {
		skills.put(type, def);
	}

	public static String get(SkillType type) {
		return skills.containsKey(type)? skills.get(type) : null;
	}

	public static String get(String string) {
		return names.containsKey(string) ? names.get(string) : null;
	}
}