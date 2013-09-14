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

import org.bukkit.ChatColor;

public class ConfigColorTranslator {
	private Character colorChar;

	public ConfigColorTranslator(Character character) {
		this.colorChar = character;
	}

	/**
	 * Gets the color character that should be translated.
	 * @return character
	 */
	public Character getChar() {
		return this.colorChar;
	}

	/**
	 * Sets the color character that should be translated.
	 * @param character
	 */
	public void setColorChar(Character character) {
		this.colorChar = character;
	}

	/**
	 * Translates the all the color chars by the real one used the client.
	 * @param string
	 * @return translatedString
	 */
	public String translate(String string) {
		return this.colorChar == null ? string :
			string.replace(this.colorChar, ChatColor.COLOR_CHAR);
	}

	/**
	 * Fixes the client colors back to the ones used by the server.
	 * @param string
	 * @return fixedString
	 */
	public String fix(String string) {
		return this.colorChar == null ? string :
			string.replace(ChatColor.COLOR_CHAR, this.colorChar);
	}
}