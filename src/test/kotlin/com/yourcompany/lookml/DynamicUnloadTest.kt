package com.yourcompany.lookml

import com.intellij.ide.plugins.DynamicPlugins
import com.intellij.ide.plugins.IdeaPluginDescriptorImpl
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.PluginId
import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * Empirical dynamic-plugin check: asks the platform's own [DynamicPlugins] whether this plugin can be
 * loaded/unloaded (installed, updated, enabled, disabled) WITHOUT an IDE restart - the same predicate the
 * IDE evaluates when you disable/enable a plugin. A `null` reason means unload-safe.
 *
 * Finding (Jul 2026): the ONLY blocker is that this is a PAID plugin. IntelliJ forbids dynamic load/unload
 * for any plugin carrying a `<product-descriptor>` (our `code="PLOOKMLSUPPORT"`) - a licensing safeguard,
 * not a code defect. So a restart is required on install/update, and there is nothing to fix while paid.
 *
 * This test therefore asserts the plugin is structurally dynamic-clean: the platform must report EITHER no
 * blocker at all, OR only the paid-plugin blocker. Any OTHER reason (a component, a leaked reference, a
 * non-dynamic extension) is a real regression and fails here.
 */
class DynamicUnloadTest : BasePlatformTestCase() {

    fun testOnlyBlockerIsThePaidFlag() {
        val descriptor =
            PluginManagerCore.getPlugin(PluginId.getId(PLUGIN_ID)) as? IdeaPluginDescriptorImpl
        assertNotNull(
            "plugin '$PLUGIN_ID' must be loaded in the test runtime to check unload-safety",
            descriptor,
        )
        val reason = DynamicPlugins.checkCanUnloadWithoutRestart(descriptor!!)
        val onlyPaidBlockerOrNone = reason == null || reason.contains("paid plugin")
        assertTrue(
            "plugin has an unexpected dynamic-unload blocker (expected none or the paid-plugin flag): $reason",
            onlyPaidBlockerOrNone,
        )
    }

    private companion object {
        private const val PLUGIN_ID = "com.andriidev.lookml"
    }
}
