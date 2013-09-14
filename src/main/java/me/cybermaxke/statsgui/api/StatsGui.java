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
package me.cybermaxke.statsgui.api;

import java.util.List;

import org.bukkit.entity.Player;

public interface StatsGui {

	/**
	 * Gets the owning player.
	 * @return player
	 */
	public Player getPlayer();

	/**
	 * Gets if the gui is changing after some seconds.
	 * @return changing
	 */
	public boolean isDynamicChanging();

	/**
	 * Sets if the gui is changing after some seconds.
	 * @param changing
	 */
	public void setDynamicChanging(boolean changing);

	/**
	 * Gets the amount of seconds before the gui should switch.
	 * @return seconds
	 */
	public int getDynamicChangeDelay();

	/**
	 * Sets the amount of seconds before the gui should switch.
	 * @param seconds
	 */
	public void setDynamicChangingDelay(int ticks);

	/**
	 * Adds a objective to the dynamic gui changing.
	 * @param objective
	 */
	public void addDynamicObjective(StatsGuiObjective objective);

	/**
	 * Removes a objective from the dynamic gui changing.
	 * @param objective
	 */
	public void removeDynamicObjective(StatsGuiObjective objective);

	/**
	 * Gets all the objectives in the dynamic gui changing.
	 * @return objectives
	 */
	public List<StatsGuiObjective> getDynamicObjectives();

	/**
	 * Gets the current objective.
	 * @return objective
	 */
	public StatsGuiObjective getObjective();

	/**
	 * Sets the current objective.
	 * @param objective
	 */
	public void setObjective(StatsGuiObjective objective);

	/**
	 * Sets the current objective, it's forced to be showed longer even
	 * if there is the next dynamic gui objective.
	 * @param delay
	 * @param objective
	 */
	public void setObjective(StatsGuiObjective objective, int delay);

	/**
	 * Adds a new stats gui check.
	 * @param check
	 */
	public void addCheck(StatsGuiCheck check);

	/**
	 * Removes a stats gui check.
	 * @param check
	 */
	public void removeCheck(StatsGuiCheck check);

	/**
	 * Gets all the checks wich are added.
	 * @return checks
	 */
	public List<StatsGuiCheck> getChecks();

	/**
	 * Sets if the stats gui should be shown.
	 * @param show
	 */
	public void setShown(boolean show);

	/**
	 * Gets if the stats gui is shown.
	 * @return shown
	 */
	public boolean isShown();

	/**
	 * Updates the current stats.
	 */
	public void update();
}