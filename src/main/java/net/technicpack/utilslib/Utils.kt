/*
 * This file is part of Technic Launcher.
 * Copyright ©2015 Syndicate, LLC
 *
 * Technic Launcher is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Technic Launcher is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Technic Launcher.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.technicpack.utilslib

import net.technicpack.rest.RestObject
import net.technicpack.platform.io.PlatformPackInfo
import net.technicpack.platform.io.FeedItem
import kotlin.Throws
import net.technicpack.launchercore.exception.BuildInaccessibleException
import net.technicpack.rest.io.Modpack
import com.google.gson.Gson
import net.technicpack.rest.RestfulAPIException
import net.technicpack.launchercore.TechnicConstants
import com.google.gson.JsonParseException
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
import java.lang.StringBuffer
import java.lang.ProcessBuilder
import java.lang.Process
import java.lang.StringBuilder
import java.lang.Runnable
import net.technicpack.utilslib.Memory
import org.apache.commons.codec.digest.DigestUtils
import net.technicpack.utilslib.MD5Utils
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.ParserConfigurationException
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import java.nio.channels.ClosedByInterruptException
import net.technicpack.utilslib.IZipFileFilter
import java.util.Collections
import java.util.zip.ZipException
import net.technicpack.utilslib.SHA1Utils
import java.awt.image.BufferedImage
import net.technicpack.utilslib.ImageUtils
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.Desktop
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
import net.technicpack.utilslib.OperatingSystem
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
import org.apache.commons.io.IOUtils
import java.io.*
import java.net.*
import java.util.logging.Logger

object Utils {
    private val gson: Gson? = null
    private val logger = Logger.getLogger("net.technicpack.launcher.Main")

    init {
        val builder = GsonBuilder()
        net.technicpack.utilslib.builder.setPrettyPrinting()
        gson = net.technicpack.utilslib.builder.create()
    }

    fun getGson(): Gson? {
        return gson
    }

    fun getLogger(): Logger {
        return logger
    }

    /**
     * Establishes an HttpURLConnection from a URL, with the correct configuration to receive content from the given URL.
     *
     * @param url The URL to set up and receive content from
     * @return A valid HttpURLConnection
     * @throws IOException The openConnection() method throws an IOException and the calling method is responsible for handling it.
     */
    @Throws(IOException::class)
    fun openHttpConnection(url: URL?): HttpURLConnection {
        val conn = url!!.openConnection() as HttpURLConnection
        conn.doInput = true
        conn.doOutput = false
        System.setProperty("http.agent", TechnicConstants.getUserAgent())
        conn.setRequestProperty("User-Agent", TechnicConstants.getUserAgent())
        conn.useCaches = false
        return conn
    }

    /**
     * Opens an HTTP connection to a web URL and tests that the response is a valid 200-level code
     * and we can successfully open a stream to the content.
     *
     * @param urlLoc The HTTP URL indicating the location of the content.
     * @return True if the content can be accessed successfully, false otherwise.
     */
    fun pingHttpURL(urlLoc: String?, mirrorStore: MirrorStore): Boolean {
        try {
            val url = mirrorStore.getFullUrl(urlLoc)
            var conn = openHttpConnection(url)
            var responseCode = conn.responseCode
            var responseFamily = responseCode / 100
            if (responseFamily == 3) {
                val newUrl = conn.getHeaderField("Location")
                var redirectUrl: URL? = null
                try {
                    redirectUrl = URL(newUrl)
                } catch (ex: MalformedURLException) {
                    throw DownloadException("Invalid Redirect URL: $url", ex)
                }
                conn = openHttpConnection(redirectUrl)
                responseCode = conn.responseCode
                responseFamily = responseCode / 100
            }
            if (responseFamily == 2) {
                conn.inputStream.use { stream -> return true }
            } else {
                return false
            }
        } catch (e: IOException) {
            return false
        }
    }

    fun sendTracking(category: String, action: String?, label: String?, clientId: String): Boolean {
        val url = "https://www.google-analytics.com/collect"
        try {
            val urlObj = URL(url)
            val con = urlObj.openConnection() as HttpURLConnection
            con.requestMethod = "POST"
            val urlParameters = "v=1&tid=UA-30896795-3&cid=$clientId&t=event&ec=$category&ea=$action&el=$label"
            con.doOutput = true
            val wr = DataOutputStream(con.outputStream)
            wr.writeBytes(urlParameters)
            wr.flush()
            wr.close()
            val responseCode = con.responseCode
            getLogger().info("Analytics Response [$category]: $responseCode")
            val `in` = BufferedReader(
                InputStreamReader(con.inputStream)
            )
            var inputLine: String?
            val response = StringBuffer()
            while ((`in`.readLine().also { inputLine = it }) != null) {
                response.append(inputLine)
            }
            `in`.close()
            return true
        } catch (e: IOException) {
            return false
        }
    }

    /**
     *
     * Run a command on the local command line and return the program output.
     * THIS COMMAND IS BLOCKING!  Only run for short command line stuff, or I guess run on a thread.
     *
     * @param command List of args to run on the command line
     * @return The newline-separated program output
     */
    fun getProcessOutput(vararg command: String?): String? {
        var out: String? = null
        try {
            val pb = ProcessBuilder(*command)
            pb.redirectErrorStream(true)
            val process = pb.start()
            val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))
            val response = StringBuilder()
            Thread(Runnable {
                try {
                    var line: String
                    while ((bufferedReader.readLine().also { line = it }) != null) {
                        response.append(line + "\n")
                    }
                } catch (ex: IOException) {
                    //Don't let other process' problems concern us
                } finally {
                    IOUtils.closeQuietly(bufferedReader)
                }
            }).start()
            process.waitFor()
            if (response.toString().length > 0) {
                out = response.toString().trim { it <= ' ' }
            }
        } catch (e: IOException) {
            //Some kind of problem running java -version or getting output, just assume the version is bad
            return null
        } catch (ex: InterruptedException) {
            //Something booted us while we were waiting on java -version to complete, just assume
            //this version is bad
            return null
        }
        return out
    }
}