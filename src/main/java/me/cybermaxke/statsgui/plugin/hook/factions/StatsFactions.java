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
package me.cybermaxke.statsgui.plugin.hook.factions;

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

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;

public class StatsFactions implements Listener, StatsGuiObjective {
	private ConfigLanguage languageConfig;

	public StatsFactions(Plugin plugin, Character colorChar) {
		if (!plugin.getServer().getPluginManager().isPluginEnabled("Factions")) {
			return;
		}

		this.languageConfig = new ConfigLanguage(
				new File(plugin.getDataFolder(), "language-factions.yml"));
		this.languageConfig.getColorTranslator().setColorChar(colorChar);

		this.languageConfig.add("landcount", "Land");
		this.languageConfig.add("power", "Power");
		this.languageConfig.add("players", "Players");
		this.languageConfig.add("onlineplayers", "Online Players");

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
		Faction faction = UPlayer.get(player).getFaction();

		Map<String, Integer> stats = new HashMap<String, Integer>();

		stats.put(this.languageConfig.get("power"), faction.getPowerRounded());
		stats.put(this.languageConfig.get("landcount"), faction.getLandCount());
		stats.put(this.languageConfig.get("players"), faction.getUPlayers().size());
		stats.put(this.languageConfig.get("onlineplayers"), faction.getOnlinePlayers().size());

		return stats;
	}

	@Override
	public String getTitle(StatsGui gui) {
		return UPlayer.get(gui.getPlayer()).getFaction().getName();
	}

	@Override
	public boolean isUsable(StatsGui gui) {
		return UPlayer.get(gui.getPlayer()).hasFaction();
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Stats.get().getGui(e.getPlayer()).addDynamicObjective(this);
	}
}