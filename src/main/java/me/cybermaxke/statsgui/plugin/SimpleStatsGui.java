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
package me.cybermaxke.statsgui.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import me.cybermaxke.statsgui.api.StatsGui;
import me.cybermaxke.statsgui.api.StatsGuiCheck;
import me.cybermaxke.statsgui.api.StatsGuiObjective;

public class SimpleStatsGui implements StatsGui {
	private final static boolean DYNAMIC_CHANGING = true;
	private final static int DYNAMIC_DELAY = 5; /** Delay in seconds. */

	private final List<StatsGuiObjective> objectivesDynamic = new ArrayList<StatsGuiObjective>();
	private final List<StatsGuiCheck> checks = new ArrayList<StatsGuiCheck>();

	private final Map<String, StatsGuiObjective> objectivesById =
			new HashMap<String, StatsGuiObjective>();
	private final Map<StatsGuiObjective, String> objectiveIdsByObjective =
			new HashMap<StatsGuiObjective, String>();

	private final Scoreboard scoreboard;
	private final Player player;
	private final Plugin plugin;

	private StatsGuiObjective current;
	private StatsUpdater updater;

	private boolean show = false;

	private boolean dynamicChanging = DYNAMIC_CHANGING;
	private int dynamicDelay = DYNAMIC_DELAY;
	private int dynamicTimer = 0;
	private int dynamicIndex = 0;

	/**
	 * The timer that will be used to keep the current objective showed up.
	 */
	private int forcedTimer = 0;

	public SimpleStatsGui(Plugin plugin, Player player) {
		this.player = player;
		this.plugin = plugin;
		this.updater = new StatsUpdater();
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
	}

	@Override
	public Player getPlayer() {
		return this.player;
	}

	@Override
	public boolean isDynamicChanging() {
		return this.dynamicChanging;
	}

	@Override
	public void setDynamicChanging(boolean changing) {
		this.dynamicChanging = changing;
	}

	@Override
	public int getDynamicChangeDelay() {
		return this.dynamicDelay;
	}

	@Override
	public void setDynamicChangingDelay(int seconds) {
		this.dynamicDelay = seconds;

		if (this.dynamicTimer > seconds) {
			this.dynamicTimer = seconds;
		}
	}

	@Override
	public void addDynamicObjective(StatsGuiObjective objective) {
		if (objective == null || this.objectivesDynamic.contains(objective)) {
			return;
		}

		this.objectivesDynamic.add(objective);
		this.addObjective(objective);
	}

	@Override
	public void removeDynamicObjective(StatsGuiObjective objective) {
		this.objectivesDynamic.remove(objective);
	}

	@Override
	public List<StatsGuiObjective> getDynamicObjectives() {
		return this.objectivesDynamic;
	}

	@Override
	public void setShown(boolean show) {
		if (!show) {
			this.player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
		}

		this.show = show;
		this.update();
	}

	@Override
	public boolean isShown() {
		return this.show && this.player.getScoreboard() == this.scoreboard;
	}

	@Override
	public StatsGuiObjective getObjective() {
		return this.current;
	}

	@Override
	public void setObjective(StatsGuiObjective objective) {
		this.current = objective;

		if (this.current != null) {
			this.addObjective(objective); /** Be sure that it's added. */
		}

		this.update(); /** Update directly. */
	}

	@Override
	public void setObjective(StatsGuiObjective objective, int delay) {
		this.setObjective(objective);
		this.forcedTimer = delay;
	}

	@Override
	public void addCheck(StatsGuiCheck check) {
		if (this.checks.contains(check)) {
			return;
		}

		this.checks.add(check);
	}

	@Override
	public void removeCheck(StatsGuiCheck check) {
		this.checks.remove(check);
	}

	@Override
	public List<StatsGuiCheck> getChecks() {
		return this.checks;
	}

	@Override
	public void update() {
		if (this.current == null) {
			this.scoreboard.clearSlot(DisplaySlot.SIDEBAR);
			return;
		}

		/**
		 * Clear all the old stats.
		 */
		for (OfflinePlayer player : this.scoreboard.getPlayers()) {
			this.scoreboard.resetScores(player);
		}

		/**
		 * Add the new stats.
		 */
		Objective objective = this.getObjective(this.current);
		for (Entry<String, Integer> en : this.current.getStats(this).entrySet()) {
			objective.getScore(Bukkit.getOfflinePlayer(en.getKey())).setScore(en.getValue());
		}

		/**
		 * Set the title and make it visible on the scoreboard.
		 */
		objective.setDisplayName(this.current.getTitle(this));
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);

		if (this.canShow() && this.show) {
			this.player.setScoreboard(this.scoreboard);
		}
	}

	public void clean() {
		if (this.updater != null) {
			this.updater.cancel();
			this.updater = null;
		}
	}

	public boolean canShow() {
		for (StatsGuiCheck check : this.checks) {
			if (!check.canShow(this)) {
				return false;
			}
		}

		return true;
	}

	public Objective getObjective(StatsGuiObjective objective) {
		return this.scoreboard.getObjective(this.objectiveIdsByObjective.get(objective));
	}

	public String getId(StatsGuiObjective objective) {
		return this.objectiveIdsByObjective.get(objective);
	}

	public void addObjective(StatsGuiObjective objective) {
		/**
		 * Check if it already exists.
		 */
		if (this.getId(objective) != null) {
			return;
		}

		/**
		 * Generating a custom id, with a max length of 16 chars.
		 */
		String id = null;

		while (true) {
			UUID uuid = UUID.randomUUID();
			id = uuid.toString().substring(0, 16);

			if (!this.objectivesById.containsKey(id)) {
				this.objectivesById.put(id, objective);
				this.objectiveIdsByObjective.put(objective, id);
				break;
			}
		}

		/**
		 * Registering a new objective to the scoreboard.
		 */
		this.scoreboard.registerNewObjective(id, "dummy");
	}

	public class StatsUpdater extends BukkitRunnable {

		public StatsUpdater() {
			this.runTaskTimer(SimpleStatsGui.this.plugin, 20, 20);
		}

		@Override
		public void run() {
			if (SimpleStatsGui.this.forcedTimer > 0) {
				SimpleStatsGui.this.forcedTimer--;
				SimpleStatsGui.this.update();
				return;
			}

			SimpleStatsGui.this.dynamicTimer--;
			if (SimpleStatsGui.this.dynamicChanging && SimpleStatsGui.this.dynamicTimer <= 0) {
				SimpleStatsGui.this.dynamicTimer = SimpleStatsGui.this.dynamicDelay;

				if (SimpleStatsGui.this.objectivesDynamic.isEmpty()) {
					SimpleStatsGui.this.setObjective(null);
					return;
				}

				int index = SimpleStatsGui.this.dynamicIndex;

				StatsGuiObjective objective = null;
				while (true) {
					index++;

					if (index >= SimpleStatsGui.this.objectivesDynamic.size()) {
						index = 0;
					}

					StatsGuiObjective objective1 = SimpleStatsGui.this.objectivesDynamic
							.get(index);
					if (objective1.isUsable(SimpleStatsGui.this)) {
						objective = objective1;
						break;
					}

					/**
					 * There aren't any usable guis. :(
					 */
					if (index == SimpleStatsGui.this.dynamicIndex) {
						break;
					}
				}

				SimpleStatsGui.this.dynamicIndex = index;
				SimpleStatsGui.this.setObjective(objective);
				return;
			}

			SimpleStatsGui.this.update();
		}
	}
}