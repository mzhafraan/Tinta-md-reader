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
import androidx.compose.material3.MaterialTheme
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
import com.zeeonworks.tinta.ui.theme.TintaBorderHairline
import com.zeeonworks.tinta.ui.theme.TintaCharcoalInk
import com.zeeonworks.tinta.ui.theme.TintaCodeBackground
import com.zeeonworks.tinta.ui.theme.TintaCodeText
import com.zeeonworks.tinta.ui.theme.TintaMutedInk
import com.zeeonworks.tinta.ui.theme.TintaPaperCard
import com.zeeonworks.tinta.ui.theme.TintaTerracotta
import com.zeeonworks.tinta.ui.theme.TypewriterMonospace
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
            Spacer(modifier = Modifier.height(12.dp))
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
    val text = remember(heading) { buildInlineAnnotatedString(heading) }
    val (style, topPadding, bottomPadding, hasUnderline) = when (heading.level) {
        1 -> Quadruple(
            MaterialTheme.typography.headlineLarge.copy(fontSize = 24.sp, lineHeight = 30.sp),
            16.dp,
            8.dp,
            true
        )
        2 -> Quadruple(
            MaterialTheme.typography.headlineMedium.copy(fontSize = 20.sp, lineHeight = 26.sp),
            14.dp,
            6.dp,
            false
        )
        3 -> Quadruple(
            MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp, lineHeight = 24.sp),
            12.dp,
            4.dp,
            false
        )
        else -> Quadruple(
            MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp, lineHeight = 22.sp),
            10.dp,
            4.dp,
            false
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = topPadding, bottom = bottomPadding)
    ) {
        Text(
            text = text,
            style = style,
            color = TintaCharcoalInk
        )
        if (hasUnderline) {
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(TintaBorderHairline)
            )
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
private fun RenderParagraph(paragraph: Paragraph) {
    val annotatedString = remember(paragraph) { buildInlineAnnotatedString(paragraph) }

    Text(
        text = annotatedString,
        style = MaterialTheme.typography.bodyLarge.copy(
            lineHeight = 26.sp,
            color = TintaCharcoalInk
        )
    )
}

@Composable
private fun RenderBlockQuote(blockQuote: BlockQuote) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(TintaPaperCard, RoundedCornerShape(2.dp))
            .drawBehind {
                // Editorial thick vertical border on left
                drawLine(
                    color = TintaTerracotta,
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height),
                    strokeWidth = 4.dp.toPx()
                )
                // Hairline border on remaining sides
                drawLine(
                    color = TintaBorderHairline,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
                drawLine(
                    color = TintaBorderHairline,
                    start = Offset(size.width, 0f),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.dp.toPx()
                )
                drawLine(
                    color = TintaBorderHairline,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }
            .padding(start = 16.dp, end = 12.dp, top = 12.dp, bottom = 12.dp)
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
private fun RenderBulletList(bulletList: BulletList) {
    Column(modifier = Modifier.fillMaxWidth().padding(start = 8.dp)) {
        var item = bulletList.firstChild
        while (item != null) {
            if (item is ListItem) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                ) {
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = TintaTerracotta,
                        modifier = Modifier.padding(end = 8.dp)
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
private fun RenderOrderedList(orderedList: OrderedList) {
    var index = orderedList.markerStartNumber ?: 1
    Column(modifier = Modifier.fillMaxWidth().padding(start = 8.dp)) {
        var item = orderedList.firstChild
        while (item != null) {
            if (item is ListItem) {
                val numLabel = "$index."
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                ) {
                    Text(
                        text = numLabel,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
                        color = TintaMutedInk,
                        modifier = Modifier.width(24.dp)
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

private fun buildInlineAnnotatedString(parent: Node): AnnotatedString {
    return buildAnnotatedString {
        appendInlineNodes(parent.firstChild)
    }
}

private fun AnnotatedString.Builder.appendInlineNodes(firstNode: Node?) {
    var current = firstNode
    while (current != null) {
        when (current) {
            is org.commonmark.node.Text -> {
                append(current.literal)
            }
            is StrongEmphasis -> {
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                appendInlineNodes(current.firstChild)
                pop()
            }
            is Emphasis -> {
                pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                appendInlineNodes(current.firstChild)
                pop()
            }
            is Code -> {
                pushStyle(
                    SpanStyle(
                        fontFamily = TypewriterMonospace,
                        background = TintaCodeBackground,
                        color = TintaCodeText,
                        fontSize = 13.sp
                    )
                )
                append(" ${current.literal} ")
                pop()
            }
            is Link -> {
                val url = current.destination ?: ""
                val linkAnnotation = LinkAnnotation.Url(
                    url = url,
                    styles = TextLinkStyles(
                        style = SpanStyle(
                            color = TintaTerracotta,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                )
                withLink(linkAnnotation) {
                    appendInlineNodes(current.firstChild)
                }
            }
            is HardLineBreak -> {
                append("\n")
            }
            is SoftLineBreak -> {
                append(" ")
            }
            else -> {
                appendInlineNodes(current.firstChild)
            }
        }
        current = current.next
    }
}
