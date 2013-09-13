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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import me.cybermaxke.mcmmostats.util.Config;
import me.cybermaxke.mcmmostats.util.ConfigLanguage;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.events.experience.McMMOPlayerLevelUpEvent;
import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;

public class StatsPlugin extends JavaPlugin implements Listener {
	private static StatsPlugin instance;

	private Map<String, StatsGui> statsGui = new HashMap<String, StatsGui>();
	private ConfigLanguage languageConfig;
	private Config config;

	private boolean showByDefault = true;

	@Override
	public void onEnable() {
		instance = this;

		if (!this.getDataFolder().exists()) {
			this.getDataFolder().mkdirs();
		}

		try {
			this.config = new Config(this);
			this.config.add("ColorCharacter", ChatColor.COLOR_CHAR + "");
			this.config.add("ShowByDefault", true);
			this.config.load();

			this.showByDefault = this.config.get("ShowByDefault", Boolean.class);

			this.languageConfig = new ConfigLanguage(this);
			this.languageConfig.add(SkillType.ACROBATICS.toString(), "Acrobatics");
			this.languageConfig.add(SkillType.ARCHERY.toString(), "Archery");
			this.languageConfig.add(SkillType.AXES.toString(), "Axes");
			this.languageConfig.add(SkillType.EXCAVATION.toString(), "Excavation");
			this.languageConfig.add(SkillType.FISHING.toString(), "Fishing");
			this.languageConfig.add(SkillType.HERBALISM.toString(), "Herbalism");
			this.languageConfig.add(SkillType.MINING.toString(), "Mining");
			this.languageConfig.add(SkillType.REPAIR.toString(), "Repairing");
			this.languageConfig.add(SkillType.SWORDS.toString(), "Swords");
			this.languageConfig.add(SkillType.TAMING.toString(), "Taming");
			this.languageConfig.add(SkillType.UNARMED.toString(), "Unarmed");
			this.languageConfig.add(SkillType.WOODCUTTING.toString(), "Woodcutting");
			this.languageConfig.add(SkillType.SMELTING.toString(), "Smelting");
			this.languageConfig.add("LEVEL", "Level");
			this.languageConfig.add("REQUIRED_XP", "Required XP");
			this.languageConfig.add("EARNED_XP", "Earned XP");
			this.languageConfig.add("POWER_LEVEL", "Power Level");
			this.languageConfig.add("SKILL_STATS", "Skill Stats");

			this.languageConfig.getColorTranslator().setColorChar(
					this.config.get("ColorCharacter", String.class).charAt(0));
			this.languageConfig.load();
		} catch (Exception e) {
			e.printStackTrace();
		}

		new StatsGuiCommand(this);

		this.getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable() {

	}

	public static StatsPlugin get() {
		return instance;
	}

	public ConfigLanguage getLanguage() {
		return this.languageConfig;
	}

	public StatsGui getGui(Player player) {
		if (!this.statsGui.containsKey(player.getName())) {
			this.statsGui.put(player.getName(), new StatsGui(this, this.languageConfig, player));
		}

		return this.statsGui.get(player.getName());
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		StatsGui gui = this.getGui(player);

		String path = "shown." + player.getName();
		YamlConfiguration config = this.config.getConfig();

		if (!StatsPermissions.hasUseGuiPermission(player) ||
				(config.contains(path) && !config.getBoolean(path)) ||
				(!config.contains(path) && this.showByDefault)) {
			return;
		}

		gui.show();
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		StatsGui gui = this.getGui(player);

		try {
			this.config.getConfig().set("shown." + player.getName(), gui.isShown());
			this.config.save();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		gui.remove();
		this.statsGui.remove(player.getName());
	}

	@EventHandler
	public void onMcMMOPlayerLevelUp(McMMOPlayerLevelUpEvent e) {
		if (StatsPermissions.hasSkillPermission(e.getPlayer(), e.getSkill())) {
			this.getGui(e.getPlayer()).sendSkillStats(e.getSkill());
		}
	}

	@EventHandler
	public void onMcMMOPlayerXpGain(McMMOPlayerXpGainEvent e) {
		if (StatsPermissions.hasSkillPermission(e.getPlayer(), e.getSkill())) {
			this.getGui(e.getPlayer()).sendSkillStats(e.getSkill());
		}
	}
}