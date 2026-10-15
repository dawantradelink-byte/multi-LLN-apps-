package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MarkdownContent(
    text: String,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val blocks = remember(text) { parseMarkdownBlocks(text) }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        blocks.forEach { block ->
            when (block) {
                is Block.CodeBlock -> {
                    CodeSnippetCard(
                        code = block.code,
                        language = block.language,
                        context = context
                    )
                }
                is Block.Header -> {
                    Text(
                        text = block.text,
                        color = textColor,
                        fontSize = when (block.level) {
                            1 -> 20.sp
                            2 -> 18.sp
                            else -> 16.sp
                        },
                        fontWeight = FontWeight.Bold,
                        lineHeight = 24.sp
                    )
                }
                is Block.BulletItem -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "•",
                            color = textColor,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 8.dp, start = 4.dp)
                        )
                        Text(
                            text = parseInlineFormatting(block.text, textColor),
                            color = textColor,
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                    }
                }
                is Block.Paragraph -> {
                    Text(
                        text = parseInlineFormatting(block.text, textColor),
                        color = textColor,
                        fontSize = 14.sp,
                        lineHeight = 21.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun CodeSnippetCard(
    code: String,
    language: String,
    context: Context
) {
    var copied by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF1E1E1E))
            .border(1.dp, Color(0xFF333333), RoundedCornerShape(8.dp))
    ) {
        // Header bar with language & copy button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF141414))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = language.ifBlank { "code" }.lowercase(),
                color = Color(0xFFAAAAAA),
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace
            )
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable {
                        val clipboard =
                            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("code", code))
                        Toast.makeText(context, "Code copied", Toast.LENGTH_SHORT).show()
                        copied = true
                        scope.launch {
                            delay(2000)
                            copied = false
                        }
                    }
                    .padding(horizontal = 6.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (copied) Icons.Default.Check else Icons.Default.ContentCopy,
                    contentDescription = "Copy code",
                    tint = if (copied) Color(0xFF5DB872) else Color(0xFFAAAAAA),
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (copied) "Copied" else "Copy",
                    color = if (copied) Color(0xFF5DB872) else Color(0xFFAAAAAA),
                    fontSize = 11.sp
                )
            }
        }

        // Code body
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(12.dp)
        ) {
            Text(
                text = code,
                color = Color(0xFFF0F0F0),
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
                lineHeight = 18.sp
            )
        }
    }
}

private sealed class Block {
    data class Paragraph(val text: String) : Block()
    data class Header(val level: Int, val text: String) : Block()
    data class BulletItem(val text: String) : Block()
    data class CodeBlock(val language: String, val code: String) : Block()
}

private fun parseMarkdownBlocks(raw: String): List<Block> {
    val blocks = mutableListOf<Block>()
    val lines = raw.lines()
    var inCodeBlock = false
    var currentLanguage = ""
    val codeBuilder = StringBuilder()
    val paragraphBuilder = StringBuilder()

    fun flushParagraph() {
        val text = paragraphBuilder.toString().trim()
        if (text.isNotEmpty()) {
            blocks.add(Block.Paragraph(text))
            paragraphBuilder.clear()
        }
    }

    for (line in lines) {
        if (line.trim().startsWith("```")) {
            if (inCodeBlock) {
                blocks.add(Block.CodeBlock(currentLanguage, codeBuilder.toString().trimEnd()))
                codeBuilder.clear()
                inCodeBlock = false
            } else {
                flushParagraph()
                inCodeBlock = true
                currentLanguage = line.trim().removePrefix("```").trim()
            }
            continue
        }

        if (inCodeBlock) {
            codeBuilder.append(line).append("\n")
            continue
        }

        val trimmed = line.trim()
        if (trimmed.startsWith("### ")) {
            flushParagraph()
            blocks.add(Block.Header(3, trimmed.removePrefix("### ")))
        } else if (trimmed.startsWith("## ")) {
            flushParagraph()
            blocks.add(Block.Header(2, trimmed.removePrefix("## ")))
        } else if (trimmed.startsWith("# ")) {
            flushParagraph()
            blocks.add(Block.Header(1, trimmed.removePrefix("# ")))
        } else if (trimmed.startsWith("- ") || trimmed.startsWith("* ")) {
            flushParagraph()
            blocks.add(Block.BulletItem(trimmed.substring(2)))
        } else if (trimmed.isEmpty()) {
            flushParagraph()
        } else {
            if (paragraphBuilder.isNotEmpty()) {
                paragraphBuilder.append("\n")
            }
            paragraphBuilder.append(line)
        }
    }

    if (inCodeBlock) {
        blocks.add(Block.CodeBlock(currentLanguage, codeBuilder.toString().trimEnd()))
    } else {
        flushParagraph()
    }

    return blocks
}

private fun parseInlineFormatting(text: String, defaultColor: Color) = buildAnnotatedString {
    var i = 0
    while (i < text.length) {
        if (text.startsWith("**", i)) {
            val end = text.indexOf("**", i + 2)
            if (end != -1) {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = defaultColor)) {
                    append(text.substring(i + 2, end))
                }
                i = end + 2
                continue
            }
        } else if (text.startsWith("`", i) && !text.startsWith("```", i)) {
            val end = text.indexOf("`", i + 1)
            if (end != -1) {
                withStyle(
                    SpanStyle(
                        fontFamily = FontFamily.Monospace,
                        background = Color(0x22888888),
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                ) {
                    append(text.substring(i + 1, end))
                }
                i = end + 1
                continue
            }
        } else if (text.startsWith("*", i)) {
            val end = text.indexOf("*", i + 1)
            if (end != -1) {
                withStyle(SpanStyle(fontStyle = FontStyle.Italic, color = defaultColor)) {
                    append(text.substring(i + 1, end))
                }
                i = end + 1
                continue
            }
        }

        append(text[i])
        i++
    }
}
