/**
 * 
 * This software is part of the StatsGui
 * 
 * StatsGui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * any later version.
 *  
 * StatsGui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with StatsGui. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package me.cybermaxke.statsgui.plugin.hook.mcmmo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import me.cybermaxke.statsgui.api.Stats;
import me.cybermaxke.statsgui.api.StatsGui;
import me.cybermaxke.statsgui.api.StatsGuiObjective;
import me.cybermaxke.statsgui.api.utils.ConfigLanguage;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.datatypes.player.PlayerProfile;
import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.events.experience.McMMOPlayerLevelUpEvent;
import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
import com.gmail.nossr50.util.player.UserManager;

public class StatsMcMMO implements Listener, StatsGuiObjective {
	private final static int SKILL_DELAY = 4; /** Seconds to show the skill gui. */

	private final Map<SkillType, StatsGuiMcMMOSkill> skills =
			new HashMap<SkillType, StatsGuiMcMMOSkill>();

	private ConfigLanguage languageConfig;

	public StatsMcMMO(Plugin plugin, Character colorChar) {
		if (!plugin.getServer().getPluginManager().isPluginEnabled("mcMMO")) {
			return;
		}

		this.languageConfig = new ConfigLanguage(
				new File(plugin.getDataFolder(), "language-mcmmo.yml"));
		this.languageConfig.getColorTranslator().setColorChar(colorChar);

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
		this.languageConfig.add("level", "Level");
		this.languageConfig.add("required_xp", "Required XP");
		this.languageConfig.add("earned_xp", "Earned XP");
		this.languageConfig.add("power_level", "Power Level");
		this.languageConfig.add("skill_stats", "Skill Stats");

		try {
			this.languageConfig.load();
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (SkillType type : SkillType.values()) {
			this.skills.put(type, new StatsGuiMcMMOSkill(type));
		}

		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@Override
	public Map<String, Integer> getStats(StatsGui gui) {
		Player player = gui.getPlayer();

		McMMOPlayer mcplayer = UserManager.getPlayer(player);
		if (mcplayer == null) {
			mcplayer = UserManager.addUser(player);
		}

		PlayerProfile pr = mcplayer.getProfile();
		Map<String, Integer> stats = new HashMap<String, Integer>();

		stats.put(this.languageConfig.get("power_level"), mcplayer.getPowerLevel());

		for (SkillType type : SkillType.values()) {
			if (StatsMcMMOPermissions.hasSkillPermission(player, type)) {
				stats.put(this.languageConfig.get(type.toString()), pr.getSkillLevel(type));
			}
		}

		return stats;
	}

	@Override
	public String getTitle(StatsGui gui) {
		return this.languageConfig.get("skill_stats");
	}

	@Override
	public boolean isUsable(StatsGui gui) {
		return StatsMcMMOPermissions.hasUseGuiPermission(gui.getPlayer());
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Stats.get().getGui(e.getPlayer()).addDynamicObjective(this);
	}

	@EventHandler
	public void onMcMMOPlayerLevelUp(McMMOPlayerLevelUpEvent e) {
		Player player = e.getPlayer();
		SkillType type = e.getSkill();

		if (StatsMcMMOPermissions.hasSkillPermission(player, type)) {
			Stats.get().getGui(player).setObjective(this.skills.get(type), SKILL_DELAY);
		}
	}

	@EventHandler
	public void onMcMMOPlayerXpGain(McMMOPlayerXpGainEvent e) {
		Player player = e.getPlayer();
		SkillType type = e.getSkill();

		if (StatsMcMMOPermissions.hasSkillPermission(player, type)) {
			Stats.get().getGui(player).setObjective(this.skills.get(type), SKILL_DELAY);
		}
	}

	public class StatsGuiMcMMOSkill implements StatsGuiObjective {
		private final SkillType type;

		public StatsGuiMcMMOSkill(SkillType type) {
			this.type = type;
		}

		@Override
		public Map<String, Integer> getStats(StatsGui gui) {
			Player player = gui.getPlayer();

			McMMOPlayer mcplayer = UserManager.getPlayer(player);
			if (mcplayer == null) {
				mcplayer = UserManager.addUser(player);
			}

			PlayerProfile pr = mcplayer.getProfile();
			Map<String, Integer> stats = new HashMap<String, Integer>();

			stats.put(StatsMcMMO.this.languageConfig.get("level"),
					pr.getSkillLevel(this.type));
			stats.put(StatsMcMMO.this.languageConfig.get("required_xp"),
					pr.getXpToLevel(this.type));
			stats.put(StatsMcMMO.this.languageConfig.get("earned_xp"),
					pr.getSkillXpLevel(this.type));

			return stats;
		}

		@Override
		public String getTitle(StatsGui gui) {
			return StatsMcMMO.this.languageConfig.get(this.type.toString());
		}

		@Override
		public boolean isUsable(StatsGui gui) {
			return true;
		}
	}
}