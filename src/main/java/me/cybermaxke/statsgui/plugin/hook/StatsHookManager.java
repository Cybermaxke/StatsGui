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
package me.cybermaxke.statsgui.plugin.hook;

import me.cybermaxke.statsgui.plugin.hook.economy.StatsEconomy;
import me.cybermaxke.statsgui.plugin.hook.mcmmo.StatsMcMMO;
import me.cybermaxke.statsgui.plugin.hook.mobarena.StatsMobArena;

import org.bukkit.plugin.Plugin;

public class StatsHookManager {

	public void hookMcMMO(Plugin plugin, Character colorChar) {
		new StatsMcMMO(plugin, colorChar);
	}

	public void hookMobArena(Plugin plugin) {
		new StatsMobArena(plugin);
	}

	public void hookVault(Plugin plugin, Character colorChar) {
		new StatsEconomy(plugin, colorChar);
	}
}