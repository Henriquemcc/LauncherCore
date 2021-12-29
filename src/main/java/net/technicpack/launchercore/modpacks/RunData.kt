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
import net.technicpack.utilslib.Memory
import java.io.FileInputStream
import org.apache.commons.codec.digest.DigestUtils
import java.io.FileNotFoundException
import net.technicpack.utilslib.MD5Utils
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.ParserConfigurationException
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.FileOutputStream
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

class RunData constructor() {
    private val java: String? = null
    private val memory: String? = null
    fun getJava(): String? {
        return java
    }

    fun getMemory(): Long {
        try {
            return memory!!.toLong()
        } catch (ex: NumberFormatException) {
            return 0
        }
    }

    fun getMemoryObject(): Memory? {
        return getMemorySetting(getMemory())
    }

    fun isJavaValid(testString: String?): Boolean {
        var compareString: String? = java
        if (compareString == null || compareString.length == 0) compareString = "1.6"
        return isJavaVersionAtLeast(testString, compareString)
    }

    fun isMemoryValid(memory: Long): Boolean {
        return getMemory() <= memory
    }

    fun isRunDataValid(memory: Long, java: String?): Boolean {
        return isMemoryValid(memory) && isJavaValid(java)
    }

    fun getValidJavaVersion(repository: JavaVersionRepository): IJavaVersion? {
        val requires64Bit: Boolean = getMemory() > Memory.Companion.MAX_32_BIT_MEMORY
        val best64Bit: IJavaVersion? = repository.getBest64BitVersion()
        if (best64Bit != null && isJavaValid(best64Bit.getVersionNumber())) return best64Bit else if (best64Bit == null && requires64Bit) return null
        var bestVersion: IJavaVersion? = null
        for (version: IJavaVersion? in repository.getVersions()) {
            if (isJavaValid(version!!.getVersionNumber()) && (!requires64Bit || version.is64Bit())) bestVersion =
                version
        }
        return bestVersion
    }

    fun getValidMemory(repository: JavaVersionRepository): Memory? {
        val can64Bit: Boolean = (repository.getBest64BitVersion() != null)
        val required: Memory? = getMemorySetting(getMemory())
        val available: Memory? = Memory.Companion.getClosestAvailableMemory(required, can64Bit)
        if (available!!.getMemoryMB() < required!!.getMemoryMB()) return null
        return available
    }

    private fun getMemorySetting(memory: Long): Memory? {
        for (setting: Memory in Memory.Companion.memoryOptions) {
            if (setting.getMemoryMB() >= memory) return setting
        }
        return null
    }

    companion object {
        fun isJavaVersionAtLeast(testString: String?, compareString: String): Boolean {
            val compareVersion: Array<String> = compareString.split("[._]").toTypedArray()
            val testVersion: Array<String> = testString!!.split("[._]").toTypedArray()
            val compareLength: Int = Math.min(compareVersion.size, testVersion.size)
            for (i in 0 until compareLength) {
                val refVal: Int = compareVersion.get(i).toInt()
                val testVal: Int = testVersion.get(i).toInt()
                if (refVal == testVal) continue
                return testVal > refVal
            }
            if (compareVersion.size == testVersion.size) return true
            return compareVersion.size < testVersion.size
        }
    }
}