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
package me.cybermaxke.statsgui.plugin.hook.economy;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import me.cybermaxke.statsgui.api.Stats;
import me.cybermaxke.statsgui.api.StatsGui;
import me.cybermaxke.statsgui.api.StatsGuiObjective;
import me.cybermaxke.statsgui.api.utils.ConfigLanguage;

public class StatsEconomy implements Listener, StatsGuiObjective {
	private Economy economy;
	private ConfigLanguage languageConfig;

	public StatsEconomy(Plugin plugin, Character colorChar) {
		if (!plugin.getServer().getPluginManager().isPluginEnabled("Vault")) {
			return;
		}

		this.economy = plugin.getServer().getServicesManager()
				.getRegistration(Economy.class).getProvider();

		this.languageConfig = new ConfigLanguage(
				new File(plugin.getDataFolder(), "language-economy.yml"));
		this.languageConfig.getColorTranslator().setColorChar(colorChar);

		this.languageConfig.add("economy", "Economy");
		this.languageConfig.add("balance", "Balance");

		try {
			this.languageConfig.load();
		} catch (Exception e) {
			e.printStackTrace();
		}

		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@Override
	public Map<String, Integer> getStats(StatsGui gui) {
		Player player = gui.getPlayer();
		Map<String, Integer> stats = new HashMap<String, Integer>();

		stats.put(this.languageConfig.get("balance"),
				(int) this.economy.getBalance(player.getName()));

		return stats;
	}

	@Override
	public String getTitle(StatsGui gui) {
		return this.languageConfig.get("economy");
	}

	@Override
	public boolean isUsable(StatsGui gui) {
		return true;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Stats.get().getGui(e.getPlayer()).addDynamicObjective(this);
	}
}