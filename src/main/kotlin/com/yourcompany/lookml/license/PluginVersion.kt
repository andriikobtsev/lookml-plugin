package com.yourcompany.lookml.license

/**
 * This plugin's own version, read from its bundled descriptor.
 *
 * Deliberately uses no platform API. Every plugin-descriptor lookup on `PluginManager` and
 * `PluginManagerCore` is `@ApiStatus.Internal`, which the Plugin Verifier rejects, and the
 * sanctioned replacement (`PluginDetailsService`) does not exist on our `sinceBuild` floor of 251.
 * The build patches the version into `META-INF/plugin.xml`, so reading it back from our own
 * classpath is version-proof.
 */
internal object PluginVersion {

    private const val PLUGIN_ID = "com.andriidev.lookml"
    private val VERSION_TAG = Regex("""<version>\s*([^<\s][^<]*?)\s*</version>""")

    /** Null when running from source, where the build has not patched a version in yet. */
    val current: String? by lazy { parse(readOwnDescriptor()) }

    /**
     * Split out from the resource read so it is testable without an IDE. Requires the descriptor to
     * carry our own plugin id, so another plugin's descriptor on the classpath cannot be mistaken
     * for ours.
     */
    fun parse(descriptor: String?): String? {
        if (descriptor == null || !descriptor.contains("<id>$PLUGIN_ID</id>")) {
            return null
        }
        return VERSION_TAG.find(descriptor)?.groupValues?.get(1)
    }

    private fun readOwnDescriptor(): String? =
        runCatching {
            javaClass.getResourceAsStream("/META-INF/plugin.xml")?.use { it.readBytes().decodeToString() }
        }.getOrNull()
}
