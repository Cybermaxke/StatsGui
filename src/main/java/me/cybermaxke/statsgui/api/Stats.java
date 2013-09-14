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

public class Stats {
	private static StatsAPI API;

	/**
	 * Gets the plugin without checking if its enabled.
	 * @return api
	 */
	public static StatsAPI get() {
		return Stats.API;
	}

	/**
	 * Sets the api, called when the api is enabled.
	 * @param api
	 */
	public static void set(StatsAPI api) {
		Stats.API = api;
	}
}