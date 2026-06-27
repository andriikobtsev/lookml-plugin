package com.yourcompany.lookml.formatting

import com.intellij.application.options.IndentOptionsEditor
import com.intellij.application.options.SmartIndentOptionsEditor
import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider
import com.yourcompany.lookml.LookMLLanguage

/**
 * Provides default code style settings for LookML
 */
class LookMLLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {

    override fun getLanguage(): Language = LookMLLanguage

    override fun getCodeSample(settingsType: SettingsType): String {
        return """
            view: users {
              dimension: id {
                type: number
                primary_key: yes
              }

              measure: count {
                type: count
              }
            }
        """.trimIndent()
    }

    @Suppress("DEPRECATION")
    override fun getDefaultCommonSettings(): CommonCodeStyleSettings {
        val defaultSettings = CommonCodeStyleSettings(language)
        val indentOptions = defaultSettings.initIndentOptions()
        indentOptions.INDENT_SIZE = 2
        indentOptions.CONTINUATION_INDENT_SIZE = 2
        indentOptions.TAB_SIZE = 2
        indentOptions.USE_TAB_CHARACTER = false
        return defaultSettings
    }

    override fun getIndentOptionsEditor(): IndentOptionsEditor {
        return SmartIndentOptionsEditor()
    }

    override fun customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: SettingsType) {
        when (settingsType) {
            SettingsType.SPACING_SETTINGS -> {
                consumer.showStandardOptions(
                    "SPACE_AFTER_COLON",
                    "SPACE_BEFORE_COLON"
                )
            }
            SettingsType.BLANK_LINES_SETTINGS -> {
                consumer.showStandardOptions(
                    "KEEP_BLANK_LINES_IN_CODE"
                )
            }
            else -> {}
        }
    }
}
