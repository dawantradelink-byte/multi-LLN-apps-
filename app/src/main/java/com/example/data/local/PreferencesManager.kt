package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.ApiConfig
import com.example.data.model.ApiProvider
import com.example.data.model.DAWAN_AI_SYSTEM_PROMPT
import com.example.data.model.LayoutThemeOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("dawan_ai_prefs", Context.MODE_PRIVATE)

    private val _config = MutableStateFlow(loadConfig())
    val config: StateFlow<ApiConfig> = _config.asStateFlow()

    private val _layoutTheme = MutableStateFlow(loadLayoutTheme())
    val layoutTheme: StateFlow<LayoutThemeOption> = _layoutTheme.asStateFlow()

    private fun loadLayoutTheme(): LayoutThemeOption {
        val themeId = prefs.getString(KEY_LAYOUT_THEME, LayoutThemeOption.MOTIONSIGHT_AURORA.id)
            ?: LayoutThemeOption.MOTIONSIGHT_AURORA.id
        return LayoutThemeOption.fromId(themeId)
    }

    fun setLayoutTheme(theme: LayoutThemeOption) {
        prefs.edit().putString(KEY_LAYOUT_THEME, theme.id).apply()
        _layoutTheme.value = theme
    }

    private fun loadConfig(): ApiConfig {
        val providerStr = prefs.getString(KEY_PROVIDER, ApiProvider.OPENROUTER.name) ?: ApiProvider.OPENROUTER.name
        val provider = try {
            ApiProvider.valueOf(providerStr)
        } catch (e: Exception) {
            ApiProvider.OPENROUTER
        }

        return ApiConfig(
            provider = provider,
            openRouterKey = prefs.getString(KEY_OPENROUTER_KEY, "") ?: "",
            omniRouteKey = prefs.getString(KEY_OMNIROUTE_KEY, "") ?: "",
            openRouterBaseUrl = prefs.getString(KEY_OPENROUTER_BASE_URL, "https://openrouter.ai/api/v1") ?: "https://openrouter.ai/api/v1",
            omniRouteBaseUrl = prefs.getString(KEY_OMNIROUTE_BASE_URL, "http://10.0.2.2:20128/v1") ?: "http://10.0.2.2:20128/v1",
            selectedModel = prefs.getString(KEY_SELECTED_MODEL, "openrouter/auto") ?: "openrouter/auto",
            temperature = prefs.getFloat(KEY_TEMPERATURE, 0.7f),
            systemPrompt = prefs.getString(KEY_SYSTEM_PROMPT, DAWAN_AI_SYSTEM_PROMPT) ?: DAWAN_AI_SYSTEM_PROMPT
        )
    }

    fun saveConfig(newConfig: ApiConfig) {
        prefs.edit()
            .putString(KEY_PROVIDER, newConfig.provider.name)
            .putString(KEY_OPENROUTER_KEY, newConfig.openRouterKey)
            .putString(KEY_OMNIROUTE_KEY, newConfig.omniRouteKey)
            .putString(KEY_OPENROUTER_BASE_URL, newConfig.openRouterBaseUrl)
            .putString(KEY_OMNIROUTE_BASE_URL, newConfig.omniRouteBaseUrl)
            .putString(KEY_SELECTED_MODEL, newConfig.selectedModel)
            .putFloat(KEY_TEMPERATURE, newConfig.temperature)
            .putString(KEY_SYSTEM_PROMPT, newConfig.systemPrompt)
            .apply()
        _config.value = newConfig
    }

    fun setSelectedModel(modelId: String) {
        val updated = _config.value.copy(selectedModel = modelId)
        saveConfig(updated)
    }

    companion object {
        private const val KEY_PROVIDER = "provider"
        private const val KEY_OPENROUTER_KEY = "openrouter_key"
        private const val KEY_OMNIROUTE_KEY = "omniroute_key"
        private const val KEY_OPENROUTER_BASE_URL = "openrouter_base_url"
        private const val KEY_OMNIROUTE_BASE_URL = "omniroute_base_url"
        private const val KEY_SELECTED_MODEL = "selected_model"
        private const val KEY_TEMPERATURE = "temperature"
        private const val KEY_SYSTEM_PROMPT = "system_prompt"
        private const val KEY_LAYOUT_THEME = "layout_theme"
    }
}
