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
	private static Map<SkillType, String> skills = new HashMap<SkillType, String>();
	private static Map<String, String> names = new HashMap<String, String>();

	public LanguageConfig(Plugin plugin) {
		file = new File(plugin.getDataFolder(), "Language.yml");
		YamlConfiguration c = null;

		skills.put(SkillType.ACROBATICS, "Acrobatics");
		skills.put(SkillType.ARCHERY, "Archery");
		skills.put(SkillType.AXES, "Axes");
		skills.put(SkillType.EXCAVATION, "Excavation");
		skills.put(SkillType.FISHING, "Fishing");
		skills.put(SkillType.HERBALISM, "Herbalism");
		skills.put(SkillType.MINING, "Mining");
		skills.put(SkillType.REPAIR, "Repairing");
		skills.put(SkillType.SWORDS, "Swords");
		skills.put(SkillType.TAMING, "Taming");
		skills.put(SkillType.UNARMED, "Unarmed");
		skills.put(SkillType.WOODCUTTING, "Woodcutting");
		skills.put(SkillType.SMELTING, "Smelting");
		names.put("LEVEL", "Level");
		names.put("REQUIRED_XP", "Required XP");
		names.put("EARNED_XP", "Earned XP");
		names.put("POWER_LEVEL", "Power Level");
		names.put("SKILL_STATS", "Skill Stats");

		if (!file.exists()) {
			c = new YamlConfiguration();
			for (Entry<SkillType, String> en : skills.entrySet()) {
				c.set("Skills." + en.getKey().toString(), en.getValue());
			}

			for (Entry<String, String> en : names.entrySet()) {
				c.set(en.getKey(), en.getValue());
			}

			try {
				file.createNewFile();
				c.save(file);
			} catch (Exception e) {}
		} else {
			c = YamlConfiguration.loadConfiguration(file);

			for (SkillType t : skills.keySet()) {
				if (c.contains("Skills." + t.toString())) {
					String s = c.getString("Skills." + t.toString());
					if (s.length() > 16) {
						s = s.substring(0, 16);
					}
					skills.put(t, s);
				}
			}

			for (String s : names.keySet()) {
				if (c.contains(s)) {
					String s1 = c.getString(s);
					if (s1.length() > 16) {
						s1 = s.substring(0, 16);
					}
					names.put(s, s1);
				}
			}
		}
	}

	public static String getName(SkillType type) {
		return skills.containsKey(type)? skills.get(type) : null;
	}

	public static String getName(String string) {
		return names.containsKey(string) ? names.get(string) : null;
	}
}