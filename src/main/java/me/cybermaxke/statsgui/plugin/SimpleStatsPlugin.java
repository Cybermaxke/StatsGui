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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import me.cybermaxke.statsgui.api.Stats;
import me.cybermaxke.statsgui.api.StatsAPI;
import me.cybermaxke.statsgui.api.utils.Config;
import me.cybermaxke.statsgui.plugin.hook.StatsHookManager;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleStatsPlugin extends JavaPlugin implements StatsAPI, Listener {
	private final Map<String, SimpleStatsGui> guis = new HashMap<String, SimpleStatsGui>();

	private Config config;
	private Character colorChar = ChatColor.COLOR_CHAR;

	private boolean hookMcMMO = true;
	private boolean hookMobArena = true;
	private boolean hookEconomy = true;
	private boolean showByDefault = true;

	@Override
	public void onEnable() {
		Stats.set(this);

		/**
		 * Create or load the config file.
		 */
		try {
			this.config = new Config(new File(this.getDataFolder(), "config.yml"));
			this.config.add("hook.mcmmo", this.hookMcMMO);
			this.config.add("hook.mobarena", this.hookMobArena);
			this.config.add("hook.economy", this.hookEconomy);

			this.config.add("colorcharacter", this.colorChar + "");
			this.config.add("showbydefault", this.showByDefault);

			this.config.load();

			this.hookMcMMO = this.config.get("hook.mcmmo", Boolean.class);
			this.hookMobArena = this.config.get("hook.mobarena", Boolean.class);
			this.hookEconomy = this.config.get("hook.economy", Boolean.class);

			this.showByDefault = this.config.get("showbydefault", Boolean.class);
			this.colorChar = this.config.get("colorcharacter", String.class).charAt(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.getServer().getPluginManager().registerEvents(this, this);
		this.getCommand("statsgui").setExecutor(new SimpleStatsCommand());

		/**
		 * Load all the hooks.
		 */
		StatsHookManager hookManager = new StatsHookManager();
		if (this.hookMcMMO) {
			hookManager.hookMcMMO(this, this.colorChar);
		}
		if (this.hookMobArena) {
			hookManager.hookMobArena(this);
		}
		if (this.hookEconomy) {
			hookManager.hookVault(this, this.colorChar);
		}
	}

	@Override
	public void onDisable() {
		Stats.set(null);
	}

	@Override
	public SimpleStatsGui getGui(Player player) {
		String key = player.getName();

		if (this.guis.containsKey(key)) {
			return this.guis.get(key);
		}

		SimpleStatsGui gui = new SimpleStatsGui(this, player);
		this.guis.put(key, gui);
		return gui;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		SimpleStatsGui gui = this.getGui(player);

		String path = "shown." + player.getName();
		YamlConfiguration config = this.config.getConfig();

		if (!SimpleStatsPermissions.hasUseGuiPermission(player) ||
				(config.contains(path) && !config.getBoolean(path)) ||
				(!config.contains(path) && !this.showByDefault)) {
			return;
		}

		gui.setShown(true);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		String key = player.getName();

		if (!this.guis.containsKey(key)) {
			return;
		}

		SimpleStatsGui gui = this.guis.remove(key);

		try {
			this.config.getConfig().set("shown." + player.getName(), gui.isShown());
			this.config.save();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		gui.clean();
	}
}