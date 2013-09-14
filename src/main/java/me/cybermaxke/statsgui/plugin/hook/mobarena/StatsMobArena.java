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

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class StatsMobArena implements Listener, StatsGuiCheck {
	private Plugin mobArena;

	public StatsMobArena(Plugin plugin) {
		this.mobArena = plugin.getServer().getPluginManager().getPlugin("MobArena");

		if (this.mobArena == null) {
			return;
		}

		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@Override
	public boolean canShow(StatsGui gui) {

		/**
		 * There is no maven repo, so reflection...
		 */
		try {
			Object arenaMaster = this.mobArena.getClass()
					.getMethod("getArenaMaster", new Class[] {})
					.invoke(this.mobArena, new Object[] {});
			Object arena = arenaMaster.getClass()
					.getMethod("getArenaWithPlayer", Player.class)
					.invoke(arenaMaster, gui.getPlayer());

			if (arena != null) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Stats.get().getGui(e.getPlayer()).addCheck(this);
	}
}