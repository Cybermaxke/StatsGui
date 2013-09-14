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

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class SimpleStatsPermissions {

	private SimpleStatsPermissions() {

	}

	public static boolean hasUseGuiPermission(Player player) {
		return player.hasPermission(new Permission("statsgui.usegui", PermissionDefault.TRUE));
	}

	public static boolean hasUseCommandPermission(Player player) {
		return player.hasPermission(new Permission("statsgui.useguicommand", PermissionDefault.TRUE));
	}
}