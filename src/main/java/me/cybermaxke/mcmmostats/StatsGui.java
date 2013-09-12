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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.cybermaxke.mcmmostats.util.ConfigLanguage;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.datatypes.player.PlayerProfile;
import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.util.player.UserManager;

public class StatsGui {
	private final Plugin plugin;
	private final Player player;
	private final Scoreboard scoreboard;
	private final ConfigLanguage languageConfig;

	private McMMOPlayer mcplayer;
	private Objective skillStats;

	private int ticksBeforeReturn = 200;
	private Updater updater;

	private Map<String, List<String>> last = new HashMap<String, List<String>>();
	private Map<SkillType, Objective> skills = new HashMap<SkillType, Objective>();

	public StatsGui(Plugin plugin, ConfigLanguage languageConfig, Player player) {
		this.plugin = plugin;
		this.player = player;
		this.languageConfig = languageConfig;
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

		this.mcplayer = UserManager.getPlayer(player);
		if (this.mcplayer == null) {
			this.mcplayer = UserManager.addUser(player);
		}

		this.skillStats = this.scoreboard.registerNewObjective("McMMOSkillStats", "dummy");
		this.skillStats.setDisplayName(languageConfig.get("SKILL_STATS"));

		for (SkillType t : SkillType.values()) {
			Objective obj = this.scoreboard.registerNewObjective("Sk" + t.toString(), "dummy");
			obj.setDisplayName(languageConfig.get(t.toString()));
			this.skills.put(t, obj);
		}
	}

	public void resetUpdater() {
		if (this.updater != null) {
			this.updater.cancel();
		}

		this.updater = new Updater(this);
		this.updater.runTaskLater(this.plugin, this.ticksBeforeReturn);
	}

	public void sendSkillStats(SkillType type) {
		Map<String, Integer> m = new HashMap<String, Integer>();
		PlayerProfile pr = this.mcplayer.getProfile();

		m.put(this.languageConfig.get("LEVEL"), pr.getSkillLevel(type));
		m.put(this.languageConfig.get("REQUIRED_XP"), pr.getXpToLevel(type));
		m.put(this.languageConfig.get("EARNED_XP"), pr.getSkillXpLevel(type));

		this.sendScores(this.skills.get(type), m);
		this.resetUpdater();
	}

	public void sendAllStats() {
		Map<String, Integer> m = new HashMap<String, Integer>();
		m.put(this.languageConfig.get("POWER_LEVEL"), this.mcplayer.getPowerLevel());

		for (SkillType t : SkillType.values()) {
			if (StatsPermissions.hasSkillPermission(this.player, t)) {
				m.put(this.languageConfig.get(t.toString()),
						this.mcplayer.getProfile().getSkillLevel(t));
			}
		}

		this.sendScores(this.skillStats, m);
	}

	public void sendScores(Objective objective, Map<String, Integer> values) {
		if (this.last.containsKey(objective.getName())) {
			for (String old : this.last.get(objective.getName())) {
				this.scoreboard.resetScores(Bukkit.getOfflinePlayer(old));
			}
		}

		for (Entry<String, Integer> en : values.entrySet()) {
			objective.getScore(Bukkit.getOfflinePlayer(en.getKey())).setScore(en.getValue());
		}

		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		this.player.setScoreboard(this.scoreboard);
	}

	public void show() {
		this.sendAllStats();
	}

	public void hide() {
		this.scoreboard.clearSlot(DisplaySlot.SIDEBAR);
	}

	public boolean isShown() {
		return this.scoreboard.getObjective(DisplaySlot.SIDEBAR) != null;
	}

	public void remove() {
		this.hide();

		if (this.updater != null) {
			this.updater.cancel();
			this.updater = null;
		}
	}

	private class Updater extends BukkitRunnable {
		private StatsGui gui;

		public Updater(StatsGui gui) {
			this.gui = gui;
		}

		@Override
		public void run() {
			this.gui.sendAllStats();
			this.gui.resetUpdater();
		}
	}
}