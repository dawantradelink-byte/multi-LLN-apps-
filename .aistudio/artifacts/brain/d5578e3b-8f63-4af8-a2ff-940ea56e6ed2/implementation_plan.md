# Implementation Plan: OmniRoute Multi-Model Engine & 9:16 Viral Reels Studio

Integrate the core architecture from [diegosouzapw/OmniRoute](https://github.com/diegosouzapw/OmniRoute) into the Android application, powering an end-to-end Viral Reels Studio capable of generating high-retention psychological hooks and rendering an interactive 9:16 vertical reel preview with word-by-word kinetic captions.

---

## 1. Architectural Overview

```
                      +-----------------------------------+
                      |   Viral Reels Studio UI           |
                      |  (Psychological Hook Selector)    |
                      +-----------------+-----------------+
                                        |
                                        v
                      +-----------------------------------+
                      |   OmniRoute Resilient Gateway     |
                      |   (Adaptive Routing & Fallback)   |
                      +-----------------+-----------------+
                                        |
        +---------------+---------------+---------------+---------------+
        |               |               |               |               |
        v               v               v               v               v
  [Anthropic Claude] [OpenAI GPT-4o] [Google Gemini]  [Groq Llama 3] [DeepSeek]
        |               |               |               |               |
        +---------------+---------------+---------------+---------------+
                                        |
                                        v (Generated Reel Data & Timeline)
                      +-----------------------------------+
                      |  9:16 Interactive Reel Preview    |
                      |  - Word-synced Kinetic Captions   |
                      |  - Audio Waveform & Beat Cuts     |
                      |  - Scene Pacing Timeline          |
                      |  - Export Script & Veo Prompts    |
                      +-----------------------------------+
```

---

## 2. Key Components to Implement

### Phase 1: OmniRoute Multi-Model Router (`OmniRouteEngine.kt`)
- **Port core logic from `/tmp/omniroute/src/lib/routing/adaptiveRouting.ts` and `resilience/failureClassification.ts`**:
  - `RoutingCandidate`: Represents provider + model with capability score, health score, circuit state (`CLOSED`, `OPEN`, `HALF_OPEN`), quota status, latency tracking, and cost preference.
  - `AdaptiveRouter`: Computes candidate ranking, dynamically executes primary model (e.g. Claude 3.5 Sonnet / GPT-4o), and automatically fails over to next best available provider (Gemini 1.5 Pro, Groq Llama 3, DeepSeek) upon rate limit (429), server error (5xx), or timeout.
  - `TelemetryTracker`: Records fallback trail, token compression estimate, and real-time response latency.
  - UI Telemetry Indicator: Displays the active model route and fallback chain directly in the generation pipeline.

### Phase 2: High-Retention Psychological & Shock Hooks Engine (`ViralHooksEngine.kt`)
- **Curated High-Converting Hook Taxonomies**:
  1. **Shock & Pattern Interrupt**: Breaks standard feed scrolling in <0.5s ("Stop doing this right now if you want...").
  2. **Cognitive Dissonance / Counter-Intuitive Truth**: Challenges assumptions ("Why working harder is killing your results").
  3. **The Forbidden Curiosity Gap**: Teases insider knowledge ("The single algorithm tweak nobody talks about").
  4. **FOMO & Loss Aversion**: Triggers urgent engagement ("If this video showed up on your feed before midnight...").
  5. **Authority Reversal**: Disproves conventional guru advice with proof-based facts.
- **Hook Data Model**:
  - Hook headline, visual trigger, audio sound effect cue (`[Record Scratch]`, `[Bass Drop]`, `[Whoosh]`), estimated retention score (94%-99%), and retention psychology explanation.

### Phase 3: 9:16 Interactive Reel Preview Studio (`ViralReelPlayerView.kt`)
- **True 9:16 Vertical Video Experience**:
  - Full-screen or modal 9:16 preview card with rounded borders and ambient glow.
  - Dynamic visual canvas: Layered multi-scene cinematic gradients, particle motion, and dynamic Ken Burns camera zoom effects matching the prompt theme.
  - **Hormozi-Style Synced Kinetic Captions**:
    - Word-by-word karaoke-style animated glowing highlights (Hot Coral, Electric Gold, Mint Green).
    - Punch-word scaling (words like "STOP", "SECRET", "MILLION", "NOW" pulse on beat).
  - **Live Audio Waveform Simulation**:
    - Animated frequency bars reacting to voiceover rhythm.
  - **Reels Engagement HUD**:
    - Vertical action bar with like count, comment counter, bookmark, share, and sound disc spin.
  - **Scene Timeline Navigator**:
    - Tap to jump between:
      1. Hook (0-3s)
      2. Agitation / Tension (3-12s)
      3. The Pivot (12-20s)
      4. Solution Breakdown (20-27s)
      5. Viral CTA (27-30s)
  - **Export Suite**:
    - Export voiceover script, Veo/Runway camera prompts, and subtitle caption SRT/JSON.

### Phase 4: Integration with Chat & Dialogs
- **Update `ViralReelDialog.kt`**:
  - Add OmniRoute multi-model routing selector with live status badges.
  - Add viral hook style picker (Shock, Curiosity Gap, Loss Aversion, Pattern Interrupt).
  - Add instant preview launcher.
- **Update `VideoPlayerCard.kt` & `ChatBubble.kt`**:
  - When a message contains a generated viral reel, render the interactive 9:16 Reel Player directly inline with full playback, scrubbing, and caption sync.

---

## 3. Files to Create and Modify

| File | Action | Description |
|---|---|---|
| `app/src/main/java/com/example/data/omniroute/OmniRouteEngine.kt` | **Create** | Ported OmniRoute adaptive routing, circuit breaker, provider fallback & telemetry |
| `app/src/main/java/com/example/data/omniroute/ViralHooksEngine.kt` | **Create** | Psychological & shock hook templates, retention formulas & dynamic generation |
| `app/src/main/java/com/example/ui/components/ViralReelPlayerView.kt` | **Create** | Interactive 9:16 vertical reel preview with synced kinetic captions and audio visualizer |
| `app/src/main/java/com/example/ui/components/ViralReelDialog.kt` | **Modify** | Integrate OmniRoute routing selection, hook styles, and preview launch |
| `app/src/main/java/com/example/data/model/Models.kt` | **Modify** | Add `ReelScene`, `ReelScript`, and `OmniRouteTelemetry` models to `VideoGenerationData` |
| `app/src/main/java/com/example/ui/ChatViewModel.kt` | **Modify** | Connect `OmniRouteEngine` and `ViralHooksEngine` to reel generation workflow |
| `app/src/main/java/com/example/ui/components/VideoPlayerCard.kt` | **Modify** | Support toggling between standard 16:9 cinematic video and 9:16 vertical kinetic reel |

---

## 4. Verification and Testing

1. **Compilation Check**: Run `compile_applet` to ensure zero syntax or dependency errors.
2. **OmniRoute Fallback Verification**: Validate simulated and real multi-model failover chains (Primary -> Fallback 1 -> Fallback 2).
3. **9:16 Kinetic Caption Playback**: Ensure captions animate smoothly with time-synced word emphasis without UI stutter.
4. **Interactive Controls**: Test play/pause, scrub slider, scene step navigation, and export script actions.
