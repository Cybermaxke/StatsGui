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
package me.cybermaxke.statsgui.api.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
	private final Map<String, Object> defaults = new HashMap<String, Object>();
	private final File folder;
	private final File file;
	private final YamlConfiguration config;

	public Config(File file) {
		this.folder = file.getParentFile();
		this.file = file;
		this.config = new YamlConfiguration();
	}

	public void load() throws IOException, InvalidConfigurationException {
		if (!this.folder.exists()) {
			this.folder.mkdirs();
		}

		if (!this.file.exists()) {
			this.file.createNewFile();
		}

		this.config.load(this.file);

		for (Entry<String, Object> en : this.defaults.entrySet()) {
			String k = en.getKey();

			if (!this.config.contains(k)) {
				this.config.set(k, en.getValue());
			} else {
				this.defaults.put(k, this.config.get(k));
			}
		}

		this.config.save(this.file);
	}

	public YamlConfiguration getConfig() {
		return this.config;
	}

	public <T> T get(String path, Class<T> clazz) {
		return this.config.contains(path) ? clazz.cast(this.config.get(path)) : null;
	}

	public Object get(String path) {
		return this.config.contains(path) ? this.config.get(path) : null;
	}

	public <T> void add(String path, T def) {
		this.defaults.put(path, def);
	}

	public void save() throws IOException {
		if (!this.folder.exists()) {
			this.folder.mkdirs();
		}

		if (!this.file.exists()) {
			this.file.createNewFile();
		}

		this.config.save(this.file);
	}
}