package com.example.policy.ui.home

import android.text.Html
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.policy.ui.AppViewModelProvider
import com.example.policy.ui.theme.PolicyTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: HomeViewModel,
    index: Int
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(text = "政策全文")
                }
            )
        }
    ) { innerpadding ->
        OutlinedCard(
            onClick = { },
            modifier = Modifier
        ) {
            Column {
                val htmlContent =
                    viewModel.policyList.value.get(index).getOrDefault("text", "kong").toString()
                HtmlText(html = htmlContent, modifier = Modifier.padding(innerpadding))
            }

        }

    }
}

@Composable
fun HtmlText(html: String, modifier: Modifier = Modifier) {
    val annotatedString = parseHtml(html)
    Text(text = annotatedString, modifier = modifier)
}

fun parseHtml(html: String): AnnotatedString {
    val document = Jsoup.parse(html)
    val body = document.body()
    return buildAnnotatedString {
        appendHtmlElement(body)
    }
}

fun AnnotatedString.Builder.appendHtmlElement(element: Element) {
    when (element.tagName()) {
        "b", "strong" -> withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            element.childNodes().forEach { appendHtmlNode(it) }
        }

        "i", "em" -> withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
            element.childNodes().forEach { appendHtmlNode(it) }
        }

        "u" -> withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
            element.childNodes().forEach { appendHtmlNode(it) }
        }

        "br" -> append("\n")
        else -> element.childNodes().forEach { appendHtmlNode(it) }
    }
}

fun AnnotatedString.Builder.appendHtmlNode(node: org.jsoup.nodes.Node) {
    when (node) {
        is org.jsoup.nodes.TextNode -> append(node.text())
        is Element -> appendHtmlElement(node)
    }
}

@Preview
@Composable
fun DetailScreenPreview() {
    PolicyTheme {
//        DetailScreen(index = 1)
    }
}
