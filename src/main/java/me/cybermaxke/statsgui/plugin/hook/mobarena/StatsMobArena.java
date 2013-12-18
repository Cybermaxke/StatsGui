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
package me.cybermaxke.statsgui.plugin.hook.mobarena;

import me.cybermaxke.statsgui.api.Stats;
import me.cybermaxke.statsgui.api.StatsGui;
import me.cybermaxke.statsgui.api.StatsGuiCheck;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import com.garbagemule.MobArena.MobArena;

public class StatsMobArena implements Listener, StatsGuiCheck {
	private final MobArena mobArena;
	private final Plugin plugin;

	public StatsMobArena(Plugin plugin) {
		this.plugin = plugin;

		if (this.plugin.getServer().getPluginManager().isPluginEnabled("MobArena")) {
			this.mobArena = (MobArena) plugin.getServer().getPluginManager().getPlugin("MobArena");
			this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
		} else {
			this.mobArena = null;
		}
	}

	@Override
	public boolean canShow(StatsGui gui) {
		return this.mobArena.getArenaMaster().getArenaWithPlayer(gui.getPlayer()) == null;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Stats.get().getGui(e.getPlayer()).addCheck(this);
	}
}