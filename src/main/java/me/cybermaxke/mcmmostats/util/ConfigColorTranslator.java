/**
 * 
 * This software is part of the mcMMOStatsGui
 * 
 * mcMMOStatsGui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * any later version.
 *  
 * mcMMOStatsGui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mcMMOStatsGui. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package me.cybermaxke.mcmmostats.util;

import org.bukkit.ChatColor;

public class ConfigColorTranslator {
	private Character colorChar;

	public ConfigColorTranslator(Character character) {
		this.colorChar = character;
	}

	public Character getChar() {
		return this.colorChar;
	}

	public void setColorChar(Character character) {
		this.colorChar = character;
	}

	public String translate(String string) {
		return this.colorChar == null ? string : string.replace(this.colorChar, ChatColor.COLOR_CHAR);
	}

	public String fix(String string) {
		return this.colorChar == null ? string : string.replace(ChatColor.COLOR_CHAR, this.colorChar);
	}
}