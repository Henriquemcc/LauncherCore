/*
 * This file is part of Technic Launcher Core.
 * Copyright ©2015 Syndicate, LLC
 *
 * Technic Launcher Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Technic Launcher Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * as well as a copy of the GNU Lesser General Public License,
 * along with Technic Launcher Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.technicpack.launchercore.modpacks.packinfo

import net.technicpack.platform.io.FeedItem
import net.technicpack.rest.io.Modpack
import net.technicpack.rest.io.PackInfo
import net.technicpack.rest.io.Resource

class CombinedPackInfo(private val solderPackInfo: PackInfo?, private val platformPackInfo: PackInfo?) : PackInfo {
    override val name: String?
        get() {
            if (platformPackInfo != null)
                return platformPackInfo.name
            if (solderPackInfo != null)
                return solderPackInfo.name

            return null
        }
    override val displayName: String?
        get() {
            if (platformPackInfo != null)
                return platformPackInfo.displayName
            if (solderPackInfo != null)
                return solderPackInfo.displayName

            return null
        }
    override val webSite: String?
        get() {
            if (platformPackInfo != null)
                return platformPackInfo.webSite
            if (solderPackInfo != null)
                return solderPackInfo.webSite

            return null
        }
    override val icon: Resource?
        get() {
            if (platformPackInfo != null)
                return platformPackInfo.icon

            return null
        }
    override val background: Resource?
        get() {
            if (platformPackInfo != null)
                return platformPackInfo.background
            return null
        }
    override val logo: Resource?
        get() {
            if (platformPackInfo != null)
                return platformPackInfo.logo

            return null
        }
    override val recommended: String?
        get() {
            if (solderPackInfo != null)
                return solderPackInfo.recommended
            if (platformPackInfo != null)
                return platformPackInfo.recommended

            return null
        }
    override val latest: String?
        get() {
            if (solderPackInfo != null)
                return solderPackInfo.latest
            if (platformPackInfo != null)
                return platformPackInfo.recommended

            return null
        }
    override val builds: List<String?>?
        get() {
            if (solderPackInfo != null) return solderPackInfo.builds
            return if (platformPackInfo != null) platformPackInfo.builds else ArrayList(0)
        }

    override fun shouldForceDirectory(): Boolean {
        if (platformPackInfo != null)
            return platformPackInfo.shouldForceDirectory()

        return false
    }

    override val feed: ArrayList<FeedItem?>?
        get() {
            if (platformPackInfo != null)
                return platformPackInfo.feed

            return null;
        }
    override val description: String?
        get() {
            if (platformPackInfo != null)
                return platformPackInfo.description

            return null
        }
    override val runs: Int?
        get() {
            if (platformPackInfo != null)
                return platformPackInfo.runs
            return null
        }
    override val downloads: Int?
        get() {
            if (platformPackInfo != null)
                return platformPackInfo.downloads

            return null
        }
    override val likes: Int?
        get() {
            if (platformPackInfo != null)
                return platformPackInfo.likes

            return null;
        }

    override fun getModpack(build: String): Modpack? {
        if (solderPackInfo != null)
            return solderPackInfo.getModpack(build)
        if (platformPackInfo != null)
            return platformPackInfo.getModpack(build)

        return null
    }

    override val isComplete: Boolean
        get() = platformPackInfo != null
    override val isLocal: Boolean
        get() {
            if (platformPackInfo == null || platformPackInfo.isLocal)
                return true
            if (solderPackInfo == null || solderPackInfo.isLocal)
                return true
            return false
        }
    override val isServerPack: Boolean
        get() {
            if (platformPackInfo != null)
                return platformPackInfo.isServerPack

            return false
        }
    override val isOfficial: Boolean
        get() {
            if (platformPackInfo != null)
                return platformPackInfo.isOfficial

            return false
        }
    override val discordId: String?
        get() {
            if (platformPackInfo != null)
                return platformPackInfo.discordId

            return null
        }

}