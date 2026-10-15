package com.zeeonworks.tinta.ui.reader.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeeonworks.tinta.ui.theme.LocalAppleBooksTheme
import org.commonmark.node.Block
import org.commonmark.node.BlockQuote
import org.commonmark.node.BulletList
import org.commonmark.node.Code
import org.commonmark.node.Document
import org.commonmark.node.Emphasis
import org.commonmark.node.FencedCodeBlock
import org.commonmark.node.HardLineBreak
import org.commonmark.node.Heading
import org.commonmark.node.IndentedCodeBlock
import org.commonmark.node.Link
import org.commonmark.node.ListItem
import org.commonmark.node.Node
import org.commonmark.node.OrderedList
import org.commonmark.node.Paragraph
import org.commonmark.node.SoftLineBreak
import org.commonmark.node.StrongEmphasis
import org.commonmark.node.ThematicBreak
import org.commonmark.parser.Parser

@Composable
fun MarkdownContent(
    markdown: String,
    modifier: Modifier = Modifier
) {
    val document = remember(markdown) {
        val parser = Parser.builder().build()
        parser.parse(markdown) as Document
    }

    Column(modifier = modifier.fillMaxWidth()) {
        var child = document.firstChild
        while (child != null) {
            RenderBlockNode(child)
            Spacer(modifier = Modifier.height(14.dp))
            child = child.next
        }
    }
}

@Composable
private fun RenderBlockNode(node: Node) {
    when (node) {
        is Heading -> {
            RenderHeading(node)
        }
        is Paragraph -> {
            RenderParagraph(node)
        }
        is BlockQuote -> {
            RenderBlockQuote(node)
        }
        is BulletList -> {
            RenderBulletList(node)
        }
        is OrderedList -> {
            RenderOrderedList(node)
        }
        is FencedCodeBlock -> {
            VintageCodeBlock(
                code = node.literal ?: "",
                language = node.info
            )
        }
        is IndentedCodeBlock -> {
            VintageCodeBlock(
                code = node.literal ?: "",
                language = "CODE"
            )
        }
        is ThematicBreak -> {
            EditorialDivider()
        }
        else -> {
            var child = node.firstChild
            while (child != null) {
                if (child is Block) {
                    RenderBlockNode(child)
                }
                child = child.next
            }
        }
    }
}

@Composable
private fun RenderHeading(heading: Heading) {
    val theme = LocalAppleBooksTheme.current
    val text = remember(heading, theme) { buildInlineAnnotatedString(heading, theme) }

    val baseScale = theme.fontSizeSp / 16f
    val (fontSize, lineHeight, topPadding, bottomPadding) = when (heading.level) {
        1 -> Quadruple(26f * baseScale, 32f * baseScale, 20.dp, 10.dp)
        2 -> Quadruple(22f * baseScale, 28f * baseScale, 16.dp, 8.dp)
        3 -> Quadruple(19f * baseScale, 25f * baseScale, 14.dp, 6.dp)
        else -> Quadruple(17f * baseScale, 23f * baseScale, 12.dp, 4.dp)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = topPadding, bottom = bottomPadding)
    ) {
        Text(
            text = text,
            fontSize = fontSize.sp,
            lineHeight = lineHeight.sp,
            fontFamily = theme.font.fontFamily,
            fontWeight = FontWeight.Bold,
            color = theme.colors.textPrimary
        )

        if (heading.level == 1) {
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(theme.colors.border)
            )
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
private fun RenderParagraph(paragraph: Paragraph) {
    val theme = LocalAppleBooksTheme.current
    val annotatedString = remember(paragraph, theme) { buildInlineAnnotatedString(paragraph, theme) }

    Text(
        text = annotatedString,
        fontSize = theme.fontSizeSp.sp,
        lineHeight = (theme.fontSizeSp * theme.lineSpacingMultiplier).sp,
        fontFamily = theme.font.fontFamily,
        fontWeight = theme.fontWeight,
        color = theme.colors.textPrimary
    )
}

@Composable
private fun RenderBlockQuote(blockQuote: BlockQuote) {
    val theme = LocalAppleBooksTheme.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(theme.colors.surface, RoundedCornerShape(8.dp))
            .drawBehind {
                drawLine(
                    color = theme.colors.accent,
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height),
                    strokeWidth = 4.dp.toPx()
                )
            }
            .padding(start = 16.dp, top = 12.dp, end = 12.dp, bottom = 12.dp)
    ) {
        Column {
            var child = blockQuote.firstChild
            while (child != null) {
                if (child is Block) {
                    RenderBlockNode(child)
                }
                child = child.next
            }
        }
    }
}

@Composable
private fun RenderBulletList(list: BulletList) {
    val theme = LocalAppleBooksTheme.current
    Column(modifier = Modifier.padding(start = 8.dp)) {
        var item = list.firstChild
        while (item != null) {
            if (item is ListItem) {
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)) {
                    Text(
                        text = "•",
                        fontSize = theme.fontSizeSp.sp,
                        color = theme.colors.accent,
                        modifier = Modifier.width(18.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        var child = item.firstChild
                        while (child != null) {
                            if (child is Block) {
                                RenderBlockNode(child)
                            }
                            child = child.next
                        }
                    }
                }
            }
            item = item.next
        }
    }
}

@Composable
private fun RenderOrderedList(list: OrderedList) {
    val theme = LocalAppleBooksTheme.current
    var index = list.startNumber
    Column(modifier = Modifier.padding(start = 8.dp)) {
        var item = list.firstChild
        while (item != null) {
            if (item is ListItem) {
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)) {
                    Text(
                        text = "$index.",
                        fontSize = (theme.fontSizeSp - 1f).sp,
                        fontFamily = theme.font.fontFamily,
                        fontWeight = FontWeight.Bold,
                        color = theme.colors.textSecondary,
                        modifier = Modifier.width(26.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        var child = item.firstChild
                        while (child != null) {
                            if (child is Block) {
                                RenderBlockNode(child)
                            }
                            child = child.next
                        }
                    }
                }
                index++
            }
            item = item.next
        }
    }
}

private fun buildInlineAnnotatedString(
    node: Node,
    theme: com.zeeonworks.tinta.ui.theme.AppleBooksThemeState
): AnnotatedString {
    return buildAnnotatedString {
        var child = node.firstChild
        while (child != null) {
            appendNode(child, theme)
            child = child.next
        }
    }
}

private fun AnnotatedString.Builder.appendNode(
    node: Node,
    theme: com.zeeonworks.tinta.ui.theme.AppleBooksThemeState
) {
    when (node) {
        is org.commonmark.node.Text -> {
            append(node.literal ?: "")
        }
        is StrongEmphasis -> {
            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
            var child = node.firstChild
            while (child != null) {
                appendNode(child, theme)
                child = child.next
            }
            pop()
        }
        is Emphasis -> {
            pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
            var child = node.firstChild
            while (child != null) {
                appendNode(child, theme)
                child = child.next
            }
            pop()
        }
        is Code -> {
            pushStyle(
                SpanStyle(
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    fontSize = (theme.fontSizeSp - 2f).coerceAtLeast(11f).sp,
                    background = theme.colors.codeBackground,
                    color = theme.colors.codeText
                )
            )
            append(" ${node.literal ?: ""} ")
            pop()
        }
        is Link -> {
            val destination = node.destination ?: ""
            val linkStyles = TextLinkStyles(
                style = SpanStyle(
                    color = theme.colors.accent,
                    textDecoration = TextDecoration.Underline
                )
            )
            pushLink(LinkAnnotation.Url(destination, styles = linkStyles))
            var child = node.firstChild
            while (child != null) {
                appendNode(child, theme)
                child = child.next
            }
            pop()
        }
        is HardLineBreak -> {
            append("\n")
        }
        is SoftLineBreak -> {
            append(" ")
        }
        else -> {
            var child = node.firstChild
            while (child != null) {
                appendNode(child, theme)
                child = child.next
            }
        }
    }
}
