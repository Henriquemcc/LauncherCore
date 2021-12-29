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
package net.technicpack.solder.cache

import com.google.common.cache.Cache
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
import net.technicpack.launchercore.install.LauncherDirectories
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
import net.technicpack.launchercore.install.InstallTasksQueue
import net.technicpack.launchercore.install.ITasksQueue
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
import net.technicpack.launchercore.install.IWeightedTasksQueue
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
import net.technicpack.utilslib.*
import org.apache.commons.io.FileUtils
import org.joda.time.DateTime

class CachedSolderPackApi(
    private val directories: LauncherDirectories,
    private val innerApi: ISolderPackApi?,
    private val cacheInSeconds: Int,
    private val packSlug: String?
) : ISolderPackApi {
    private var rootInfoCache: SolderPackInfo? = null
    private var lastInfoAccess = DateTime(0)
    private val buildCache: Cache<String, Modpack>
    private val deadBuildCache: Cache<String, Boolean>

    init {
        buildCache = CacheBuilder.newBuilder()
            .concurrencyLevel(4)
            .maximumSize(300)
            .expireAfterWrite(cacheInSeconds.toLong(), TimeUnit.SECONDS)
            .build()
        deadBuildCache = CacheBuilder.newBuilder()
            .concurrencyLevel(4)
            .maximumSize(300)
            .expireAfterWrite((cacheInSeconds / 10).toLong(), TimeUnit.SECONDS)
            .build()
    }

    override val mirrorUrl: String?
        get() = innerApi.getMirrorUrl()

    @get:Throws(RestfulAPIException::class)
    override val packInfoForBulk: SolderPackInfo?
        get() {
            if (rootInfoCache != null) return rootInfoCache
            loadForeverCache()
            return if (rootInfoCache != null) rootInfoCache else pullAndCache()
        }

    @get:Throws(RestfulAPIException::class)
    override val packInfo: SolderPackInfo?
        get() {
            if (Seconds.secondsBetween(lastInfoAccess, DateTime.now()).isLessThan(
                    Seconds.seconds(
                        cacheInSeconds
                    )
                )
            ) {
                if (rootInfoCache != null) return rootInfoCache
            }
            return if (Seconds.secondsBetween(lastInfoAccess, DateTime.now()).isLessThan(
                    Seconds.seconds(
                        cacheInSeconds / 10
                    )
                )
            ) rootInfoCache else try {
                pullAndCache()
            } catch (ex: RestfulAPIException) {
                ex.printStackTrace()
                packInfoForBulk
            }
        }

    @Throws(RestfulAPIException::class)
    private fun pullAndCache(): SolderPackInfo? {
        return try {
            rootInfoCache = innerApi.getPackInfoForBulk()
            saveForeverCache(rootInfoCache)
            rootInfoCache
        } finally {
            lastInfoAccess = DateTime.now()
        }
    }

    private fun loadForeverCache() {
        val cacheFile = File(File(File(directories.assetsDirectory, "packs"), packSlug), "soldercache.json")
        if (!cacheFile.exists()) return
        try {
            val packCache = FileUtils.readFileToString(cacheFile, Charset.forName("UTF-8"))
            rootInfoCache = Utils.getGson()!!
                .fromJson(packCache, SolderPackInfo::class.java)
            if (rootInfoCache != null) rootInfoCache!!.setLocal()
        } catch (ex: IOException) {
        } catch (ex: JsonSyntaxException) {
        }
    }

    private fun saveForeverCache(info: SolderPackInfo?) {
        val cacheFile = File(File(File(directories.assetsDirectory, "packs"), info.getName()), "soldercache.json")
        val packCache = Utils.getGson()!!.toJson(info)
        try {
            FileUtils.writeStringToFile(cacheFile, packCache, Charset.forName("UTF-8"))
        } catch (e: IOException) {
            return
        }
    }

    @Throws(BuildInaccessibleException::class)
    override fun getPackBuild(build: String): Modpack? {
        val isDead = deadBuildCache.getIfPresent(build)
        if (isDead != null && isDead.toBoolean()) return null
        var modpack = buildCache.getIfPresent(build)
        return modpack
            ?: try {
                modpack = innerApi!!.getPackBuild(build)
                if (modpack != null) {
                    buildCache.put(build, modpack)
                }
                modpack
            } finally {
                deadBuildCache.put(build, modpack == null)
            }
    }
}