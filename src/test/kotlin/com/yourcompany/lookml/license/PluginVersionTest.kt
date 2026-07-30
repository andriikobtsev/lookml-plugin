package com.yourcompany.lookml.license

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Descriptor parsing behind the once-per-version Pro notice. The properties that matter: read the
 * real version when the build patched one in, and return null rather than a wrong value in every
 * other case, since null simply skips the notice.
 */
class PluginVersionTest {

    private fun descriptor(body: String) = """
        <idea-plugin>
        $body
          <id>com.andriidev.lookml</id>
          <name>LookML Support</name>
        </idea-plugin>
    """.trimIndent()

    @Test
    fun readsThePatchedVersion() {
        assertEquals("2026.2.1", PluginVersion.parse(descriptor("  <version>2026.2.1</version>")))
    }

    @Test
    fun toleratesSurroundingWhitespace() {
        assertEquals("2026.2.1", PluginVersion.parse(descriptor("  <version>\n    2026.2.1\n  </version>")))
    }

    @Test
    fun returnsNullWhenRunningFromSourceWithNoVersionPatchedIn() {
        assertNull(PluginVersion.parse(descriptor("  <vendor>AK Software</vendor>")))
    }

    @Test
    fun ignoresAnotherPluginsDescriptor() {
        val foreign = """
            <idea-plugin>
              <version>9.9.9</version>
              <id>com.example.other</id>
            </idea-plugin>
        """.trimIndent()
        assertNull(PluginVersion.parse(foreign))
    }

    @Test
    fun missingDescriptorIsNotAnError() {
        assertNull(PluginVersion.parse(null))
    }

    @Test
    fun emptyVersionIsRejected() {
        assertNull(PluginVersion.parse(descriptor("  <version></version>")))
    }
}
