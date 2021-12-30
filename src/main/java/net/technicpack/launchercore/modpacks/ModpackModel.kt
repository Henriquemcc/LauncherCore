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
package net.technicpack.launchercore.modpacks

import net.technicpack.rest.RestObject
import net.technicpack.platform.io.PlatformPackInfo
import net.technicpack.platform.io.FeedItem
import kotlin.Throws
import net.technicpack.launchercore.exception.BuildInaccessibleException
import net.technicpack.rest.io.Modpack
import com.google.gson.Gson
import net.technicpack.rest.RestfulAPIException
import net.technicpack.launchercore.TechnicConstants
import java.net.SocketTimeoutException
import java.net.MalformedURLException
import com.google.gson.JsonParseException
import java.io.IOException
import com.google.gson.JsonElement
import com.google.gson.JsonArray
import kotlin.jvm.JvmOverloads
import java.util.LinkedHashMap
import net.technicpack.solder.io.SolderPackInfo
import net.technicpack.rest.io.PackInfo
import net.technicpack.solder.ISolderPackApi
import net.technicpack.launchercore.auth.UserModel
import net.technicpack.solder.ISolderApi
import java.util.HashMap
import net.technicpack.solder.http.HttpSolderPackApi
import net.technicpack.solder.io.Solder
import java.util.LinkedList
import net.technicpack.solder.io.FullModpacks
import net.technicpack.solder.cache.CachedSolderApi.CacheTuple
import com.google.common.cache.CacheBuilder
import java.util.concurrent.TimeUnit
import net.technicpack.solder.cache.CachedSolderPackApi
import org.joda.time.Seconds
import java.io.File
import java.nio.charset.Charset
import com.google.gson.JsonSyntaxException
import net.technicpack.launchercore.modpacks.sources.IPackSource
import net.technicpack.discord.io.MemberInfo
import net.technicpack.discord.io.ChannelInfo
import net.technicpack.discord.io.GameInfo
import net.technicpack.launchercore.modpacks.ModpackModel
import net.technicpack.discord.IDiscordCallback
import net.technicpack.discord.IDiscordApi
import javax.swing.SwingWorker
import java.lang.InterruptedException
import java.util.concurrent.ExecutionException
import net.technicpack.platform.io.AuthorshipInfo
import net.technicpack.platform.io.NewsArticle
import net.technicpack.launchercore.mirror.MirrorStore
import net.technicpack.platform.IPlatformApi
import net.technicpack.platform.io.NewsData
import net.technicpack.platform.IPlatformSearchApi
import net.technicpack.platform.io.SearchResultsData
import java.net.URLEncoder
import java.io.UnsupportedEncodingException
import net.technicpack.platform.PlatformPackInfoRepository
import net.technicpack.platform.packsources.SearchResultPackInfo
import net.technicpack.launchercore.modpacks.sources.IAuthoritativePackSource
import net.technicpack.launchercore.modpacks.InstalledPack
import net.technicpack.launchercore.modpacks.packinfo.CombinedPackInfo
import org.eclipse.aether.repository.RemoteRepository
import org.eclipse.aether.RepositorySystem
import org.eclipse.aether.DefaultRepositorySystemSession
import org.eclipse.aether.impl.DefaultServiceLocator
import org.apache.maven.repository.internal.MavenRepositorySystemUtils
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory
import org.eclipse.aether.spi.connector.transport.TransporterFactory
import org.eclipse.aether.transport.file.FileTransporterFactory
import org.eclipse.aether.transport.http.HttpTransporterFactory
import org.eclipse.aether.repository.LocalRepository
import net.technicpack.launchercore.util.DownloadListener
import org.eclipse.aether.collection.CollectRequest
import org.eclipse.aether.graph.DependencyNode
import org.eclipse.aether.resolution.DependencyRequest
import net.technicpack.utilslib.maven.MavenListenerAdapter
import org.eclipse.aether.collection.DependencyCollectionException
import org.eclipse.aether.resolution.DependencyResolutionException
import org.eclipse.aether.transfer.TransferListener
import org.eclipse.aether.transfer.TransferCancelledException
import org.eclipse.aether.transfer.TransferEvent
import org.eclipse.aether.transfer.TransferResource
import com.google.gson.GsonBuilder
import net.technicpack.launchercore.exception.DownloadException
import java.io.DataOutputStream
import java.io.BufferedReader
import java.lang.StringBuffer
import java.lang.ProcessBuilder
import java.lang.Process
import java.lang.StringBuilder
import java.lang.Runnable
import java.io.FileInputStream
import org.apache.commons.codec.digest.DigestUtils
import java.io.FileNotFoundException
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.ParserConfigurationException
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.nio.channels.ClosedByInterruptException
import java.util.Collections
import java.util.zip.ZipException
import java.awt.image.BufferedImage
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.Desktop
import java.net.URISyntaxException
import java.lang.RuntimeException
import java.lang.Void
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import java.text.DateFormat
import java.util.Locale
import java.text.SimpleDateFormat
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonPrimitive
import java.lang.IllegalArgumentException
import com.google.gson.JsonSerializationContext
import com.google.gson.TypeAdapterFactory
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonWriter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import net.technicpack.autoupdate.io.StreamUrls
import net.technicpack.autoupdate.io.LauncherResource
import net.technicpack.autoupdate.IUpdateStream
import net.technicpack.autoupdate.io.StreamVersion
import net.technicpack.autoupdate.Relauncher
import net.technicpack.launchercore.install.tasks.IInstallTask
import net.technicpack.launchercore.install.tasks.DownloadFileTask
import net.technicpack.launchercore.install.verifiers.IFileVerifier
import net.technicpack.launchercore.install.verifiers.MD5FileVerifier
import net.technicpack.autoupdate.tasks.DownloadUpdate
import java.util.Arrays
import javax.swing.JOptionPane
import java.net.URLDecoder
import net.technicpack.launchercore.auth.IUserType
import net.technicpack.launchercore.auth.IUserStore
import net.technicpack.launchercore.auth.IGameAuthService
import net.technicpack.launchercore.auth.IAuthListener
import net.technicpack.launchercore.exception.AuthenticationNetworkFailureException
import net.technicpack.launchercore.auth.UserModel.AuthError
import net.technicpack.launchercore.auth.IAuthResponse
import net.technicpack.launchercore.image.IImageStore
import net.technicpack.launchercore.image.IImageMapper
import java.util.concurrent.atomic.AtomicReference
import net.technicpack.launchercore.image.IImageJobListener
import javax.imageio.ImageIO
import net.technicpack.launchercore.image.ImageJob
import net.technicpack.launchercore.launch.java.IVersionSource
import net.technicpack.launchercore.launch.java.JavaVersionRepository
import net.technicpack.launchercore.launch.java.version.FileBasedJavaVersion
import net.technicpack.launchercore.launch.java.source.FileJavaSource
import net.technicpack.launchercore.launch.java.source.os.WinRegistryJavaSource
import net.technicpack.launchercore.launch.java.source.os.MacInstalledJavaSource
import net.technicpack.launchercore.launch.java.IJavaVersion
import net.technicpack.launchercore.launch.java.version.CurrentJavaVersion
import net.technicpack.launchercore.launch.ProcessExitListener
import net.technicpack.launchercore.launch.ProcessMonitorThread
import net.technicpack.launchercore.launch.GameProcess
import net.technicpack.launchercore.mirror.secure.rest.ValidateRequest
import net.technicpack.launchercore.mirror.secure.rest.ValidateResponse
import net.technicpack.launchercore.mirror.secure.rest.ISecureMirror
import java.nio.channels.ReadableByteChannel
import net.technicpack.launchercore.mirror.download.Download.MonitorThread
import net.technicpack.launchercore.exception.PermissionDeniedException
import net.technicpack.launchercore.mirror.download.Download.StreamThread
import java.util.concurrent.atomic.AtomicBoolean
import java.net.SocketException
import net.technicpack.launchercore.mirror.download.Download
import net.technicpack.launchercore.mirror.secure.SecureToken
import net.technicpack.launchercore.install.tasks.ListenerTask
import net.technicpack.launchercore.install.tasks.UnzipFileTask
import net.technicpack.launchercore.install.tasks.TaskGroup
import net.technicpack.launchercore.install.tasks.WriteRundataFile
import com.google.gson.JsonObject
import com.google.gson.JsonNull
import net.technicpack.launchercore.install.tasks.EnsureFileTask
import java.util.logging.LogRecord
import java.io.PrintWriter
import java.util.logging.StreamHandler
import net.technicpack.launchercore.modpacks.MemoryModpackContainer
import net.technicpack.launchercore.modpacks.resources.resourcetype.IModpackResourceType
import java.lang.NumberFormatException
import net.technicpack.launchercore.modpacks.RunData
import net.technicpack.launchercore.modpacks.sources.IInstalledPackRepository
import net.technicpack.launchercore.modpacks.IModpackContainer
import net.technicpack.launchercore.modpacks.sources.IModpackTagBuilder
import net.technicpack.launchercore.modpacks.PackLoadJob
import java.util.concurrent.ConcurrentHashMap
import net.technicpack.autoupdate.IBuildNumber
import net.technicpack.launchercore.install.*
import net.technicpack.rest.io.Resource
import net.technicpack.utilslib.*
import org.apache.commons.io.FileUtils
import java.util.ArrayList
import java.util.logging.Level

class ModpackModel protected constructor() {
    private var installedPack: InstalledPack? = null
    private var packInfo: PackInfo? = null
    private var installedPackRepository: IInstalledPackRepository? = null
    private var directories: LauncherDirectories? = null
    private val tags: MutableCollection<String?> = ArrayList()
    private var buildName: String
    private var isPlatform: Boolean
    private var installedDirectory: File? = null
    private var priority: Int = -2

    constructor(
        installedPack: InstalledPack?,
        info: PackInfo?,
        installedPackRepository: IInstalledPackRepository?,
        directories: LauncherDirectories?
    ) : this() {
        this.installedPack = installedPack
        packInfo = info
        this.installedPackRepository = installedPackRepository
        this.directories = directories
    }

    init {
        buildName = InstalledPack.Companion.RECOMMENDED
        isPlatform = true
    }

    fun isOfficial(): Boolean {
        if (packInfo == null) return false
        return packInfo!!.isOfficial
    }

    fun getInstalledPack(): InstalledPack? {
        return installedPack
    }

    fun getPackInfo(): PackInfo? {
        return packInfo
    }

    fun setInstalledPack(pack: InstalledPack?, packRepo: IInstalledPackRepository?) {
        installedPack = pack
        installedPackRepository = packRepo
    }

    fun setPackInfo(packInfo: PackInfo?) {

        //HACK
        //I need to rework the way platform & solder data interact to produce a complete pack, but until I do so, this
        //awesome hack will combine platform & solder data where necessary
        if (packInfo is SolderPackInfo && this.packInfo is PlatformPackInfo) {
            this.packInfo = CombinedPackInfo(packInfo, this.packInfo)
        } else if (packInfo is PlatformPackInfo && this.packInfo is SolderPackInfo) {
            this.packInfo = CombinedPackInfo(this.packInfo, packInfo)
        } else if (packInfo is SolderPackInfo && this.packInfo is CombinedPackInfo) {
            this.packInfo = CombinedPackInfo(packInfo, this.packInfo)
        } else if (packInfo is PlatformPackInfo && this.packInfo is CombinedPackInfo) {
            this.packInfo = CombinedPackInfo(this.packInfo, packInfo)
        } else {
            this.packInfo = packInfo
        }
    }

    fun getDiscordId(): String? {
        if (packInfo != null) return packInfo!!.discordId else return null
    }

    fun getName(): String? {
        if (packInfo != null) {
            return packInfo!!.name
        } else if (installedPack != null) {
            return installedPack!!.getName()
        } else return null
    }

    fun getDisplayName(): String? {
        if (packInfo != null) {
            return packInfo!!.displayName
        } else if (installedPack != null) {
            return installedPack!!.getName()
        } else return ""
    }

    fun setBuild(build: String) {
        if (installedPack != null) {
            installedPack!!.setBuild(build)
            save()
        } else buildName = build
    }

    fun getBuild(): String? {
        if (installedPack != null) {
            return installedPack!!.getBuild()
        } else return buildName
    }

    fun getBuilds(): List<String?>? {
        if (packInfo != null && packInfo!!.builds != null) return packInfo!!.builds
        val oneBuild: MutableList<String?> = ArrayList(1)
        val version: Version? = getInstalledVersion()
        if (version != null) oneBuild.add(version.getVersion()) else oneBuild.add(getBuild())
        return oneBuild
    }

    fun getRecommendedBuild(): String? {
        if (packInfo != null && packInfo!!.recommended != null) return packInfo!!.recommended else return getBuild()
    }

    fun getLatestBuild(): String? {
        if (packInfo != null && packInfo!!.latest != null) return packInfo!!.latest else return getBuild()
    }

    fun getWebSite(): String? {
        if (getPackInfo() == null) return null
        return getPackInfo()?.webSite
    }

    fun getIcon(): Resource? {
        if (packInfo == null) return null
        return packInfo.getIcon()
    }

    fun getLogo(): Resource? {
        if (packInfo == null) return null
        return packInfo.getLogo()
    }

    fun getBackground(): Resource? {
        if (packInfo == null) return null
        return packInfo.getBackground()
    }

    fun getFeed(): ArrayList<FeedItem?>? {
        if (packInfo == null) return ArrayList()
        return packInfo.getFeed()
    }

    fun isLocalOnly(): Boolean {
        if (packInfo == null) return true
        return packInfo!!.isLocal()
    }

    fun getInstalledVersion(): Version? {
        val version: Version? = null
        val versionFile: File = File(getBinDir(), "version")
        if (versionFile.exists()) {
            return Version.Companion.load(versionFile)
        } else {
            return null
        }
    }

    fun hasRecommendedUpdate(): Boolean {
        if (installedPack == null || packInfo == null) return false
        val installedVersion: Version? = getInstalledVersion()
        if (installedVersion == null) return false
        val installedBuild: String? = installedVersion.getVersion()
        val allBuilds: List<String?>? = packInfo.getBuilds()
        if (!allBuilds!!.contains(installedBuild)) return true
        for (build: String? in allBuilds) {
            if (build.equals(packInfo.getRecommended(), ignoreCase = true)) {
                return false
            } else if (build.equals(installedBuild, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    fun setIsPlatform(isPlatform: Boolean) {
        if (installedPack == null) {
            this.isPlatform = isPlatform
        }
    }

    fun getDescription(): String? {
        if (packInfo == null) return ""
        return packInfo.getDescription()
    }

    fun isServerPack(): Boolean {
        if (packInfo == null) return false
        return packInfo!!.isServerPack()
    }

    fun getLikes(): Int? {
        if (packInfo == null) return null
        return packInfo.getLikes()
    }

    fun getRuns(): Int? {
        if (packInfo == null) return null
        return packInfo.getRuns()
    }

    fun getDownloads(): Int? {
        if (packInfo == null) return null
        return packInfo.getDownloads()
    }

    fun getRunData(): RunData? {
        val runDataFile: File = File(getBinDir(), "runData")
        if (!runDataFile.exists()) return null
        var runData: String? = "{}"
        try {
            runData = FileUtils.readFileToString(runDataFile)
        } catch (ex: IOException) {
            return null
        }
        return Utils.getGson()!!.fromJson(runData, RunData::class.java)
    }

    fun getInstalledDirectory(): File? {
        if (installedPack == null) return null
        if (installedDirectory == null) {
            var rawDir: String? = installedPack!!.getDirectory()
            if (rawDir != null && rawDir.startsWith(InstalledPack.Companion.LAUNCHER_DIR)) {
                rawDir = File(
                    directories!!.getLauncherDirectory(),
                    rawDir.substring(InstalledPack.Companion.LAUNCHER_DIR.length)
                ).getAbsolutePath()
            }
            if (rawDir != null && rawDir.startsWith(InstalledPack.Companion.MODPACKS_DIR)) {
                rawDir = File(
                    directories!!.getModpacksDirectory(),
                    rawDir.substring(InstalledPack.Companion.MODPACKS_DIR.length)
                ).getAbsolutePath()
            }
            setInstalledDirectory(File(rawDir))
        }
        return installedDirectory
    }

    fun getBinDir(): File? {
        val installedDir: File? = getInstalledDirectory()
        if (installedDir == null) return null
        return File(installedDir, "bin")
    }

    fun getModsDir(): File? {
        val installedDir: File? = getInstalledDirectory()
        if (installedDir == null) return null
        return File(installedDir, "mods")
    }

    fun getCoremodsDir(): File? {
        val installedDir: File? = getInstalledDirectory()
        if (installedDir == null) return null
        return File(installedDir, "coremods")
    }

    fun getCacheDir(): File? {
        val installedDir: File? = getInstalledDirectory()
        if (installedDir == null) return null
        return File(installedDir, "cache")
    }

    fun getConfigDir(): File? {
        val installedDir: File? = getInstalledDirectory()
        if (installedDir == null) return null
        return File(installedDir, "config")
    }

    fun getResourcesDir(): File? {
        val installedDir: File? = getInstalledDirectory()
        if (installedDir == null) return null
        return File(installedDir, "resources")
    }

    fun getSavesDir(): File? {
        val installedDir: File? = getInstalledDirectory()
        if (installedDir == null) return null
        return File(installedDir, "saves")
    }

    fun initDirectories() {
        getBinDir()!!.mkdirs()
        getModsDir()!!.mkdirs()
        getCoremodsDir()!!.mkdirs()
        getConfigDir()!!.mkdirs()
        getCacheDir()!!.mkdirs()
        getResourcesDir()!!.mkdirs()
        getSavesDir()!!.mkdirs()
    }

    fun setInstalledDirectory(targetDirectory: File?) {
        if (installedDirectory != null && installedDirectory!!.exists()) {
            try {
                FileUtils.copyDirectory(installedDirectory, targetDirectory)
                FileUtils.cleanDirectory(installedDirectory)
            } catch (ex: IOException) {
                Utils.getLogger().log(Level.SEVERE, ex.message, ex)
                return
            }
        }
        installedDirectory = targetDirectory
        val path: String = installedDirectory!!.getAbsolutePath()
        if ((path == directories!!.getModpacksDirectory().getAbsolutePath())) {
            installedPack!!.setDirectory(InstalledPack.Companion.MODPACKS_DIR)
        } else if ((path == directories!!.getLauncherDirectory().getAbsolutePath())) {
            installedPack!!.setDirectory(InstalledPack.Companion.LAUNCHER_DIR)
        } else if (path.startsWith(directories!!.getModpacksDirectory().getAbsolutePath())) {
            installedPack!!.setDirectory(
                InstalledPack.Companion.MODPACKS_DIR + path.substring(
                    directories!!.getModpacksDirectory().getAbsolutePath().length + 1
                )
            )
        } else if (path.startsWith(directories!!.getLauncherDirectory().getAbsolutePath())) {
            installedPack!!.setDirectory(
                InstalledPack.Companion.LAUNCHER_DIR + path.substring(
                    directories!!.getLauncherDirectory().getAbsolutePath().length + 1
                )
            )
        } else installedPack!!.setDirectory(path)
        save()
    }

    fun save() {
        if (installedPack == null) {
            installedPack = InstalledPack(getName(), getBuild())
        }
        installedPackRepository!!.put(installedPack)
        installedPackRepository!!.save()
    }

    fun isSelected(): Boolean {
        val selectedSlug: String? = installedPackRepository!!.getSelectedSlug()
        if (selectedSlug == null) select()
        return (selectedSlug == null || selectedSlug.equals(getName(), ignoreCase = true))
    }

    fun select() {
        installedPackRepository!!.setSelectedSlug(getName())
        installedPackRepository!!.save()
    }

    fun getTags(): Collection<String?> {
        return tags
    }

    fun updateTags(tagBuilder: IModpackTagBuilder?) {
        tags.clear()
        if (tagBuilder != null) {
            for (tag: String? in tagBuilder.getModpackTags(this)) {
                tags.add(tag)
            }
        }
    }

    fun getPriority(): Int {
        return priority
    }

    fun updatePriority(priority: Int) {
        if (this.priority < priority) {
            this.priority = priority
        }
        if ((this.priority == -1) && (packInfo != null) && packInfo!!.isComplete()) {
            if (packInfo!!.isOfficial()) this.priority = 5000 else this.priority = 1000
        }
    }

    fun resetPack() {
        if (installedPack != null && getBinDir() != null) {
            val version: File = File(getBinDir(), "version")
            if (version.exists()) version.delete()
        }
    }

    fun delete() {
        if (getInstalledDirectory() != null && getInstalledDirectory()!!.exists()) {
            try {
                FileUtils.deleteDirectory(getInstalledDirectory())
            } catch (ex: IOException) {
                Utils.getLogger().log(Level.SEVERE, ex.message, ex)
            }
        }
        val assets: File = File(directories!!.getAssetsDirectory(), getName())
        if (assets.exists()) {
            try {
                FileUtils.deleteDirectory(assets)
            } catch (ex: IOException) {
                Utils.getLogger().log(Level.SEVERE, ex.message, ex)
            }
        }
        installedPackRepository!!.remove(getName())
        installedPack = null
        installedDirectory = null
    }
}