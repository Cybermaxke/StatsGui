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

public class ConfigLanguage {
	private final File folder;
	private final File file;

	private final YamlConfiguration config;
	private final ConfigColorTranslator colorTranslator = new ConfigColorTranslator(null);

	private final Map<String, String> names = new HashMap<String, String>();

	public ConfigLanguage(File file) {
		this.folder = file.getParentFile();
		this.file = file;
		this.config = new YamlConfiguration();
	}

	/**
	 * Gets the color translator used by the language config.
	 * @return colorTranslator
	 */
	public ConfigColorTranslator getColorTranslator() {
		return this.colorTranslator;
	}

	/**
	 * Loads all the translated strings. Missing ones are added and existing ones are loaded.
	 * @throws IOException
	 * @throws InvalidConfigurationException
	 */
	public void load() throws IOException, InvalidConfigurationException {
		if (!this.folder.exists()) {
			this.folder.mkdirs();
		}

		if (!this.file.exists()) {
			this.file.createNewFile();
		}

		this.config.load(this.file);

		for (Entry<String, String> en : this.names.entrySet()) {
			String key = en.getKey().toLowerCase();

			if (!this.config.contains(key)) {
				this.config.set(key, this.colorTranslator.translate(en.getValue()));
			} else {
				this.names.put(key, this.colorTranslator.fix(this.config.getString(key)));
			}
		}

		this.config.save(this.file);
	}

	/**
	 * Adds a new translatable string.
	 * @param path
	 * @param def
	 */
	public void add(String path, String def) {
		this.names.put(path.toLowerCase(), def);
	}

	/**
	 * Gets the translated string.
	 * @param path
	 * @return string
	 */
	public String get(String path) {
		String key = path.toLowerCase();
		return this.names.containsKey(key) ? this.names.get(key) : null;
	}
}