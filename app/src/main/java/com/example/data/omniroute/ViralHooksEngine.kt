package com.example.data.omniroute

import com.example.data.model.KineticWord
import com.example.data.model.ReelScene
import com.example.data.model.ReelScript

/**
 * High-Retention Psychological & Shock Viral Hooks Engine
 *
 * Implements formulas designed for 9:16 vertical short-form video (TikTok, Instagram Reels, YouTube Shorts).
 * Focuses on thumb-stopping pattern interrupts in 0-3s, cognitive dissonance, and Hormozi-style kinetic caption pacing.
 */
object ViralHooksEngine {

    enum class HookCategory(
        val displayName: String,
        val subtitle: String,
        val retentionBoost: String,
        val icon: String,
        val defaultColor: String
    ) {
        SHOCK_INTERRUPT(
            displayName = "Shock & Pattern Interrupt",
            subtitle = "Shatters scrolling autopilot in < 0.5s",
            retentionBoost = "99% 3s Retention",
            icon = "⚡",
            defaultColor = "#FF5C5C"
        ),
        COGNITIVE_DISSONANCE(
            displayName = "Cognitive Dissonance",
            subtitle = "Contrarian truth that shatters common consensus",
            retentionBoost = "97% Rewatch Rate",
            icon = "🧠",
            defaultColor = "#FFDD00"
        ),
        FORBIDDEN_CURIOSITY(
            displayName = "Curiosity Gap",
            subtitle = "Withholds vital high-value information until the payoff",
            retentionBoost = "96% Completion Rate",
            icon = "👁️",
            defaultColor = "#00FFA3"
        ),
        LOSS_AVERSION(
            displayName = "FOMO & Loss Aversion",
            subtitle = "Forces attention by calculating what they are losing right now",
            retentionBoost = "98% Action Rate",
            icon = "🔥",
            defaultColor = "#FF7A00"
        ),
        AUTHORITY_REVERSAL(
            displayName = "Authority Reversal",
            subtitle = "Disproves industry standard advice with cold raw metrics",
            retentionBoost = "95% Share Velocity",
            icon = "🏆",
            defaultColor = "#00E5FF"
        )
    }

    data class HookTemplate(
        val category: HookCategory,
        val titleFormat: String,
        val openingWords: String,
        val visualPrompt: String,
        val sfxCue: String,
        val psychologyReason: String
    )

    val curatedHooks: List<HookTemplate> = listOf(
        HookTemplate(
            category = HookCategory.SHOCK_INTERRUPT,
            titleFormat = "STOP doing %s immediately if you want results.",
            openingWords = "STOP! If you are still doing this in 2026, you are burning your potential alive.",
            visualPrompt = "Close-up cinematic macro shot, sudden glitch cut, high-contrast red warning light, intense eye contact",
            sfxCue = "[Record Scratch + Sub-bass Drop]",
            psychologyReason = "Survival amygdala trigger: commands an instant pause by introducing sudden danger or error."
        ),
        HookTemplate(
            category = HookCategory.COGNITIVE_DISSONANCE,
            titleFormat = "Why 99%% of people fail at %s (and the contrarian truth).",
            openingWords = "Everything you were taught about this is 100% backwards. Here is why the winners do the exact opposite.",
            visualPrompt = "Split-screen comparison: left side monochrome chaos vs right side sleek hyper-efficient dark studio",
            sfxCue = "[Deep Sonic Whoosh + Reverse Cymbal]",
            psychologyReason = "Ego preservation bias: viewers must watch to ensure they aren't part of the 99% failing majority."
        ),
        HookTemplate(
            category = HookCategory.FORBIDDEN_CURIOSITY,
            titleFormat = "The hidden %s loophole that nobody is talking about.",
            openingWords = "I wasn't supposed to share this, but this hidden loophole changed everything in under 48 hours.",
            visualPrompt = "Mysterious glowing vault door sliding open, holographic data streams illuminating creator's face",
            sfxCue = "[Camera Shutter Snap + Low Cinematic Rumble]",
            psychologyReason = "Information gap theory: the brain experiences acute discomfort until unresolved secrets are answered."
        ),
        HookTemplate(
            category = HookCategory.LOSS_AVERSION,
            titleFormat = "You are losing thousands every single week ignoring %s.",
            openingWords = "Right now, as you scroll this video, you are bleeding leverage. Look at these exact numbers.",
            visualPrompt = "Rapid digital stock chart plummeting with glowing red laser grids, transitioning to exponential green spikes",
            sfxCue = "[Cash Register Clink + Heavy Heartbeat SFX]",
            psychologyReason = "Loss aversion: psychologists proved human beings fight twice as hard to avoid loss as to achieve gain."
        ),
        HookTemplate(
            category = HookCategory.AUTHORITY_REVERSAL,
            titleFormat = "I audited 100 top creators on %s. The gurus lied.",
            openingWords = "I spent 60 days auditing top performers, and the most common guru advice is completely fake.",
            visualPrompt = "Dramatic documents flying off desk, bold highlighter marks crossing out fake advice in bright neon red",
            sfxCue = "[Heavy Thud + High-Frequency Risers]",
            psychologyReason = "Underdog vs Goliath trope: instantly positions creator as an honest whistleblower protecting the audience."
        )
    )

    /**
     * Generates a complete 30-second viral reel script with 5 structured scenes
     * and word-by-word kinetic captions synced with Hormozi-style pacing.
     */
    fun buildReelScript(
        topic: String,
        category: HookCategory = HookCategory.SHOCK_INTERRUPT,
        voiceoverVoice: String = "Deep Authority Male",
        durationSeconds: Int = 30
    ): ReelScript {
        val cleanTopic = if (topic.isBlank()) "Scaling High-Impact Creative Pipelines" else topic
        val template = curatedHooks.find { it.category == category } ?: curatedHooks.first()

        val hookHeadline = template.titleFormat.replace("%s", cleanTopic)

        // Generate 5 key pacing phases:
        // Scene 1: 0 - 3s (The Hook)
        // Scene 2: 3 - 11s (The Tension / Problem Agitation)
        // Scene 3: 11 - 19s (The Pivot / Surprising Insight)
        // Scene 4: 19 - 26s (The Tactical Solution Breakdown)
        // Scene 5: 26 - 30s (The Viral CTA & Loop Anchor)

        val s1Words = listOf(
            KineticWord("STOP!", 0, 500, isPunchWord = true, colorHex = "#FF5C5C"),
            KineticWord("If", 500, 750),
            KineticWord("you", 750, 1000),
            KineticWord("are", 1000, 1200),
            KineticWord("still", 1200, 1500),
            KineticWord("struggling", 1500, 2000, isPunchWord = true, colorHex = "#FFDD00"),
            KineticWord("with", 2000, 2200),
            KineticWord("this,", 2200, 2600),
            KineticWord("LISTEN.", 2600, 3000, isPunchWord = true, colorHex = "#00FFA3")
        )

        val s2Words = listOf(
            KineticWord("Most", 3000, 3400),
            KineticWord("people", 3400, 3800),
            KineticWord("waste", 3800, 4200, isPunchWord = true, colorHex = "#FF5C5C"),
            KineticWord("10", 4200, 4600),
            KineticWord("hours", 4600, 5000),
            KineticWord("a", 5000, 5200),
            KineticWord("day", 5200, 5600),
            KineticWord("doing", 5600, 6000),
            KineticWord("the", 6000, 6200),
            KineticWord("WRONG", 6200, 6800, isPunchWord = true, colorHex = "#FF5C5C"),
            KineticWord("steps.", 6800, 7500),
            KineticWord("They", 7500, 7800),
            KineticWord("chase", 7800, 8200),
            KineticWord("vanity", 8200, 8800),
            KineticWord("instead", 8800, 9300),
            KineticWord("of", 9300, 9500),
            KineticWord("SYSTEMS.", 9500, 11000, isPunchWord = true, colorHex = "#FFDD00")
        )

        val s3Words = listOf(
            KineticWord("Here", 11000, 11400),
            KineticWord("is", 11400, 11600),
            KineticWord("the", 11600, 11800),
            KineticWord("secret", 11800, 12500, isPunchWord = true, colorHex = "#00FFA3"),
            KineticWord("that", 12500, 12800),
            KineticWord("the", 12800, 13000),
            KineticWord("TOP", 13000, 13500, isPunchWord = true, colorHex = "#FFDD00"),
            KineticWord("1%", 13500, 14200, isPunchWord = true, colorHex = "#FFDD00"),
            KineticWord("actually", 14200, 14800),
            KineticWord("deploy.", 14800, 15500),
            KineticWord("They", 15500, 15800),
            KineticWord("automate", 15800, 16600, isPunchWord = true, colorHex = "#00E5FF"),
            KineticWord("the", 16600, 16900),
            KineticWord("entire", 16900, 17400),
            KineticWord("friction", 17400, 18200),
            KineticWord("loop.", 18200, 19000)
        )

        val s4Words = listOf(
            KineticWord("First:", 19000, 19600, isPunchWord = true, colorHex = "#FFDD00"),
            KineticWord("Lock", 19600, 20100),
            KineticWord("the", 20100, 20300),
            KineticWord("hook.", 20300, 21000),
            KineticWord("Second:", 21000, 21600, isPunchWord = true, colorHex = "#FFDD00"),
            KineticWord("Deliver", 21600, 22200),
            KineticWord("instant", 22200, 22800, isPunchWord = true, colorHex = "#00FFA3"),
            KineticWord("tactical", 22800, 23400),
            KineticWord("proof.", 23400, 24200),
            KineticWord("Third:", 24200, 24800, isPunchWord = true, colorHex = "#FFDD00"),
            KineticWord("Stack", 24800, 25400),
            KineticWord("exponential", 25400, 26100, isPunchWord = true, colorHex = "#FF7A00"),
            KineticWord("leverage.", 26100, 27000)
        )

        val s5Words = listOf(
            KineticWord("SAVE", 27000, 27600, isPunchWord = true, colorHex = "#FF5C5C"),
            KineticWord("this", 27600, 27900),
            KineticWord("before", 27900, 28400),
            KineticWord("it", 28400, 28600),
            KineticWord("vanishes.", 28600, 29200),
            KineticWord("Comment", 29200, 29600),
            KineticWord("'VIRAL'", 29600, 30000, isPunchWord = true, colorHex = "#00FFA3")
        )

        val scenes = listOf(
            ReelScene(
                sceneIndex = 1,
                phaseName = "0-3s Hook",
                visualPrompt = template.visualPrompt,
                spokenText = "${template.openingWords} Today we unpack: $cleanTopic.",
                sfxCue = template.sfxCue,
                startSecond = 0f,
                endSecond = 3f,
                kineticWords = s1Words
            ),
            ReelScene(
                sceneIndex = 2,
                phaseName = "3-11s Tension Agitation",
                visualPrompt = "Dynamic montage of fast jump cuts, chaotic clock ticking, red alert overlays, frustrated creator working late",
                spokenText = "Most creators burn out because they treat $cleanTopic with outdated manual methods that doom retention.",
                sfxCue = "[Fast Jump Cuts + Heartbeat Pacing]",
                startSecond = 3f,
                endSecond = 11f,
                kineticWords = s2Words
            ),
            ReelScene(
                sceneIndex = 3,
                phaseName = "11-19s The Pivot",
                visualPrompt = "Smooth 4K camera track-in, cinematic neon studio lighting turning from dark red to radiant golden emerald",
                spokenText = "Here is what changes the game: when you apply the automated OmniRoute pipeline, velocity triples.",
                sfxCue = "[Bass Drop + Vinyl Rewind]",
                startSecond = 11f,
                endSecond = 19f,
                kineticWords = s3Words
            ),
            ReelScene(
                sceneIndex = 4,
                phaseName = "19-27s Solution Breakdown",
                visualPrompt = "Split-screen B-roll: high-tempo screen recording of rapid AI generation with kinetic subtitle animations popping",
                spokenText = "Step one: nail the pattern interrupt. Step two: deliver undeniable value. Step three: ignite share velocity.",
                sfxCue = "[Cash Register Dings + Kinetic Audio Blips]",
                startSecond = 19f,
                endSecond = 27f,
                kineticWords = s4Words
            ),
            ReelScene(
                sceneIndex = 5,
                phaseName = "27-30s Loop & Viral CTA",
                visualPrompt = "Impact macro typography taking over screen with pulsing border glow, seamless seamless loop transition back to opening frame",
                spokenText = "Bookmark this immediately, and comment 'VIRAL' to get the full automation blueprint.",
                sfxCue = "[Reverse Reverb Whoosh -> Seamless Loop]",
                startSecond = 27f,
                endSecond = 30f,
                kineticWords = s5Words
            )
        )

        val fullVoiceover = scenes.joinToString(" ") { it.spokenText }

        return ReelScript(
            hookTitle = hookHeadline,
            hookCategory = category.displayName,
            retentionScore = when (category) {
                HookCategory.SHOCK_INTERRUPT -> 99
                HookCategory.COGNITIVE_DISSONANCE -> 97
                HookCategory.FORBIDDEN_CURIOSITY -> 96
                HookCategory.LOSS_AVERSION -> 98
                HookCategory.AUTHORITY_REVERSAL -> 95
            },
            psychologyReason = template.psychologyReason,
            fullVoiceover = fullVoiceover,
            musicTrack = when (category) {
                HookCategory.SHOCK_INTERRUPT -> "Phonk Drift 140BPM (Hyper-Aggressive)"
                HookCategory.COGNITIVE_DISSONANCE -> "Dark Synthwave Tension Pulse"
                HookCategory.FORBIDDEN_CURIOSITY -> "Cinematic Mystery Ambient Bass"
                HookCategory.LOSS_AVERSION -> "Fast Tech House 128BPM"
                HookCategory.AUTHORITY_REVERSAL -> "Epic Cinematic Orchestral Trap"
            },
            sfxPunches = listOf(template.sfxCue, "[Bass Drop]", "[Kinetic Whoosh]", "[Cash Register]"),
            scenes = scenes,
            totalDurationSeconds = durationSeconds
        )
    }
}
