package net.technicpack.launchercore.modpacks.sources

import net.technicpack.launchercore.modpacks.MemoryModpackContainer
import net.technicpack.rest.io.PackInfo
import java.util.*


class NameFilterPackSource(private val baseModpacks: MemoryModpackContainer, private val filterTerms: String) : IPackSource {

    override val sourceName: String
        get() = "Installed packs filtered by '$filterTerms'";

    override val publicPacks: Collection<PackInfo?>
        get() {
            val info = LinkedList<PackInfo?>()

            for (modpack in baseModpacks.getModpacks()) {
                if (modpack!!.getDisplayName()!!.uppercase(Locale.getDefault()).contains(filterTerms)) {
                    info.add(modpack.getPackInfo())
                }
            }

            return info
        }

    override fun getPriority(packInfo: PackInfo?): Int {
        for (modpack in baseModpacks.getModpacks()) {
            if (modpack!!.getName() == packInfo!!.name) return modpack.getPriority()
        }

        return 0
    }

}