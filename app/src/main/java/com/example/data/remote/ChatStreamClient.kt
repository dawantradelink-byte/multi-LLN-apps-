package com.example.data.remote

import com.example.data.model.ApiConfig
import com.example.data.model.ApiProvider
import com.example.data.model.ChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

class ChatStreamClient {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    fun streamChatCompletion(
        config: ApiConfig,
        messages: List<ChatMessage>,
        customSystemPrompt: String? = null
    ): Flow<String> = flow {
        val (baseUrl, apiKey) = when (config.provider) {
            ApiProvider.OMNIROUTE -> Pair(
                config.omniRouteBaseUrl.trimEnd('/'),
                config.omniRouteKey.trim()
            )
            ApiProvider.OPENROUTER -> Pair(
                config.openRouterBaseUrl.trimEnd('/'),
                config.openRouterKey.trim()
            )
        }

        // If no API key is configured, yield an informative message or fallback demo
        if (apiKey.isEmpty() && config.provider == ApiProvider.OPENROUTER) {
            emit("⚠️ **OpenRouter API Key Missing**\n\nPlease tap the **Settings** (gear) icon in the top right to configure your OpenRouter API key.\n\n*Alternatively, you can test DAWAN aI in Demo Mode or switch provider to OmniRoute in Settings.*")
            return@flow
        }

        // Construct request payload
        val messagesArray = JSONArray()

        val promptToUse = customSystemPrompt ?: config.systemPrompt
        if (promptToUse.isNotBlank()) {
            val systemMsg = JSONObject()
            systemMsg.put("role", "system")
            systemMsg.put("content", promptToUse)
            messagesArray.put(systemMsg)
        }

        // Add history messages
        messages.forEach { msg ->
            if (msg.role == "user" || msg.role == "assistant") {
                val m = JSONObject()
                m.put("role", msg.role)
                m.put("content", msg.content)
                messagesArray.put(m)
            }
        }

        val requestJson = JSONObject().apply {
            put("model", config.selectedModel)
            put("messages", messagesArray)
            put("stream", true)
            put("temperature", config.temperature.toDouble())
        }

        val requestBody = requestJson.toString()
            .toRequestBody("application/json; charset=utf-8".toMediaType())

        val requestBuilder = Request.Builder()
            .url("$baseUrl/chat/completions")
            .post(requestBody)
            .addHeader("Content-Type", "application/json")
            .addHeader("HTTP-Referer", "https://dawan.ai")
            .addHeader("X-Title", "DAWAN aI")

        if (apiKey.isNotEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $apiKey")
        }

        val request = requestBuilder.build()

        val response = try {
            client.newCall(request).execute()
        } catch (e: Exception) {
            throw Exception("Failed to connect to ${config.provider.displayName} ($baseUrl): ${e.localizedMessage ?: e.message}")
        }

        if (!response.isSuccessful) {
            val errorBody = response.body?.string().orEmpty()
            val parsedError = try {
                val errObj = JSONObject(errorBody)
                errObj.optJSONObject("error")?.optString("message", errorBody) ?: errorBody
            } catch (e: Exception) {
                errorBody
            }
            throw Exception("HTTP ${response.code}: $parsedError")
        }

        val responseBody = response.body ?: throw Exception("Empty response from server")
        val reader = BufferedReader(InputStreamReader(responseBody.byteStream()))

        var line: String?
        while (reader.readLine().also { line = it } != null) {
            val currentLine = line?.trim() ?: continue
            if (currentLine.isEmpty() || !currentLine.startsWith("data:")) continue

            val dataContent = currentLine.removePrefix("data:").trim()
            if (dataContent == "[DONE]") {
                break
            }

            try {
                val json = JSONObject(dataContent)
                val choices = json.optJSONArray("choices")
                if (choices != null && choices.length() > 0) {
                    val delta = choices.getJSONObject(0).optJSONObject("delta")
                    val textToken = delta?.optString("content", "") ?: ""
                    if (textToken.isNotEmpty()) {
                        emit(textToken)
                    }
                }
            } catch (e: Exception) {
                // Ignore chunk parse errors, keep streaming
            }
        }
        reader.close()
    }.flowOn(Dispatchers.IO)

    /**
     * Demo streaming generator for testing conversational aesthetics, token-by-token
     * streaming animation, and markdown rendering without requiring an external API key.
     */
    fun streamDemoResponse(prompt: String, model: String, persona: String = "dawan_core"): Flow<String> = flow {
        val sampleResponse = if (persona == "madison_agency") {
            """# 🏆 EXECUTIVE AD CAMPAIGN BLUEPRINT
**Agency:** MADISON Agency • Million-Dollar Brand & Performance Division  
**Account:** Strategic Growth Initiative  
**Creative Director:** Ishak Dawan  

---

### 1. 🎯 THE BIG IDEA & MARKET POSITIONING
> *"Stop competing on features. Win on identity and emotional inevitability."*

We position this product not as an incremental alternative, but as the **undisputed category benchmark**. The core psychological trigger is **status elevation paired with effortless dominance**.

---

### 2. 🎬 GOOGLE VEO 2 COMMERCIAL BLUEPRINT
Here is the exact video commercial storyboard engineered to be fed directly into **Google Veo**:

**Scene 1 (0:00 - 0:03) — The 3-Second Thumb-Stopper:**
- **Veo Prompt:** `Cinematic slow-motion push-in on sleek executive subject under moody volumetric neon studio fog, high-contrast gold rim reflections, photorealistic 8K, 60fps`
- **Voiceover:** *"Everything you were told about this market was built for yesterday."*

**Scene 2 (0:03 - 0:06) — The Paradigm Shift:**
- **Veo Prompt:** `Dynamic 360-degree orbit shot gliding through a glass skyscraper penthouse, holographic glowing metrics, fluid optical flow`
- **Voiceover:** *"Meet the platform that rewrites the standard."*

**Scene 3 (0:06 - 0:08) — The Climax & Conversion:**
- **Veo Prompt:** `Fast crane pan up into twilight Tokyo skyline, golden hour light blooms, bold minimalist typography lockup`
- **On-Screen CTA:** *"DAWAN aI — Claim Your Unfair Advantage."*

---

### 3. ✍️ HIGH-CONVERTING COPY MATRIX (Estimated 5.4x ROAS)
- **Hook A (Provocative):** *"94% of brands waste ad spend on the wrong metric. Here is how the top 1% scale."*
- **Hook B (Curiosity Gap):** *"We analyzed 10,000 ad creatives. Only ONE visual pattern generated 8-figure pipeline."*
- **Body:** *"Built for velocity, engineered for conversion. Stop guessing and execute with institutional certainty."*
- **CTA:** *"Book Your Private Strategic Briefing."*

---

### 4. 📊 360° CHANNEL MEDIA ALLOCATION
| Channel | Share | Role | Projected ROAS |
| :--- | :--- | :--- | :--- |
| **TikTok / Reels** | 45% | Viral Veo Short-Form Video Hooks | 4.8x |
| **Meta (IG/FB)** | 35% | Direct Response Retargeting & Carousels | 5.6x |
| **YouTube Pre-roll** | 20% | High-Authority 4K Brand Cinematic | 3.9x |

*💡 Tip: Tap **🎬 Generate Video** in the plus menu to immediately render this Veo commercial!*"""
        } else {
            when {
                prompt.contains("python", ignoreCase = true) || prompt.contains("code", ignoreCase = true) -> {
                    """Here is an elegant Python solution crafted for DAWAN aI:

```python
import asyncio
import httpx

async def fetch_insights(topic: str) -> dict:
    url = "https://api.dawan.ai/v1/insights"
    headers = {"Authorization": "Bearer sk-dawan-token"}
    
    async with httpx.AsyncClient(timeout=10.0) as client:
        response = await client.post(url, json={"query": topic}, headers=headers)
        response.raise_for_status()
        return response.json()

if __name__ == "__main__":
    result = asyncio.run(fetch_insights("Quantum Computing"))
    print(f"Result: {result}")
```

### Key Highlights:
1. **Asynchronous I/O** ensures zero UI thread blocking.
2. **Context Management** handles connection cleanup gracefully.
3. Fully compliant with modern Python 3.12+ async standards."""
                }
                prompt.contains("email", ignoreCase = true) -> {
                    """Here is a crisp, professional email draft:

**Subject:** Collaboration Update & Strategic Next Steps — DAWAN aI

Hi Team,

I wanted to touch base following our recent milestones. Our team has finalized the integration roadmap for the new platform, and preliminary testing shows a **40% increase in response velocity**.

**Upcoming Deliverables:**
- Complete end-to-end regression validation by Thursday.
- Finalize the streaming inference latency benchmarks.
- Deploy the updated model configuration for testing.

Please let me know if you would like to review the architecture deck ahead of time.

Best regards,  
*Ishak Dawan*"""
                }
                else -> {
                    """Greetings! I am **DAWAN aI**, running on **$model**.

I’m designed with the clean, minimalist mobile aesthetic of modern conversational systems. Here is what I can assist you with right now:

- 🧠 **Reasoning & Problem Solving**: Deep breakdown of technical, financial, or scientific problems.
- 💻 **Software Engineering**: Full-stack code architecture in Kotlin, Python, TypeScript, and Rust.
- 🎬 **Google Veo Video Studio**: Tap '+' to create photorealistic 4K video clips with custom camera motions.
- 🏆 **MADISON Ad Agency Partner**: Switch to MADISON Agency mode in the header to run multi-million dollar ad campaigns!
- ⚡ **Direct API Routing**: Seamlessly switch between OpenRouter and OmniRoute via the settings menu.

How may I assist your thinking journey today?"""
                }
            }
        }

        val tokens = sampleResponse.split(Regex("(?<=\\s)|(?<=\\n)"))
        for (token in tokens) {
            delay(24)
            emit(token)
        }
    }.flowOn(Dispatchers.Default)
}
