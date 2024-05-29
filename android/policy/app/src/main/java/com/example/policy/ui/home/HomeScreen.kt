package com.example.policy.ui.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessAlarm
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.policy.ui.AppViewModelProvider
import com.example.policy.ui.theme.PolicyTheme
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Element



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    rootNavHostController: NavHostController
) {

    SideEffect {
        viewModel.typeListFun()
        viewModel.policyListFun()
    }

    var selectedTab by remember { mutableStateOf(0) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "科技政策一点通")
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TabRow(
                        selectedTabIndex = selectedTab,
                        modifier = Modifier
                    ) {
                        Tab(
                            selected = (selectedTab == 0),
                            onClick = {
                                selectedTab = 0
                            },
                            text = { Text(text = "政策全文") }
                        )
                        Tab(
                            selected = (selectedTab == 1),
                            onClick = {
                                selectedTab = 1
                            },
                            text = { Text(text = "政策要点") }
                        )
                    }
                }
            }
        },
    ) { innerpadding ->
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxHeight()
                .padding(innerpadding),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            when (selectedTab) {
                0 -> Policy1Body(
                    viewModel = viewModel,
                    typeList = viewModel.typeList.value,
                    rootNavHostController = rootNavHostController
                )

                1 -> policy2(viewModel = viewModel)
            }
        }
    }
}


//政策全文页面
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Policy1Body(
    typeList: List<Map<String, Any>>,
    viewModel: HomeViewModel,
    rootNavHostController: NavHostController
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(300.dp)
                    .background(MaterialTheme.colorScheme.tertiary)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    itemsIndexed(typeList) { index, title ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            val checkedState = remember { mutableStateOf(false) }
                            Checkbox(
                                checked = checkedState.value,
                                onCheckedChange = { isChecked ->
                                    checkedState.value = isChecked
                                    viewModel.onCheckChange(
                                        title.getOrDefault("type", "无类型").toString(), isChecked
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Row {
                                Text(text = title.getOrDefault("type", "无类型").toString())
                                Text(text = title.get("num").toString().replace(".0", ""))
                            }
                        }
                    }
                }
            }
        },
        content = {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        enabled = true,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                            .height(55.dp) // 输入框的高度
                            .clip(CircleShape) // 将输入框的形状裁剪为椭圆形
                            .background(color = Color.LightGray), // 输入框的背景颜色
                        singleLine = true, // 文字在单行中显示
                        maxLines = 1, // 限制输入框的行数为1，确保文字占据整个输入框
                        placeholder = { Text(text = "请输入关键字") }, // 默认文字
                        value = viewModel.form.value.input!!,
                        //输入的值发生变化时
                        onValueChange = { viewModel.onInputChange(it) }
                    )
                    OutlinedButton(onClick = { viewModel.policyListFun() }) {
                        Text(text = "搜索")

                    }
                    IconButton(onClick = {
                        scope.launch { drawerState.open() }
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = "Open Menu")
                    }
                }

                LazyRow {
                    itemsIndexed(viewModel.form.value.checkList) { index, item ->
                        OutlinedButton(
                            onClick = {},

                            ) {
                            Text(text = item)
//                            Icon(
//                                Icons.Default.Close,
//                                contentDescription = "删除",
//                                modifier = Modifier
//                                    .size(5.dp)
//                                    .clickable { }
//                            )
                        }
                    }
                }

                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .padding(vertical = 8.dp)
                        .clickable {}
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        itemsIndexed(viewModel.policyList.value) { index, policy ->


                            Card(
                                modifier = Modifier
                                    .padding(16.dp),
                                onClick = {
                                    rootNavHostController.navigate("detail/" + index) {
                                        launchSingleTop = true
                                    }
                                }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {

                                            rootNavHostController.navigate("detail/" + index) {
                                                launchSingleTop = true
                                            }
                                            Log.d("lhw","dianji")
                                        }
                                ) {
                                    TextField(
                                        value = policy.getOrDefault("name", "Kong").toString(),
                                        onValueChange = {},
                                        readOnly = true,
//                                        singleLine = true,
//                                        maxLines = 1,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {

                                                rootNavHostController.navigate("detail/" + index) {
                                                    launchSingleTop = true
                                                }
                                                Log.d("lhw","dianji")
                                            },
                                        colors = TextFieldDefaults.textFieldColors(
                                            disabledTextColor = Color.Black,
//                                            backgroundColor = Color.Transparent,
                                            cursorColor = Color.Transparent,
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                            disabledIndicatorColor = Color.Transparent
                                        )
                                    )
                                    Row {
                                        Icon(
                                            imageVector = Icons.Default.AccessAlarm,
                                            contentDescription = "Clock Icon",
                                            modifier = Modifier
                                                .clickable {

                                                    rootNavHostController.navigate("detail/" + index) {
                                                        launchSingleTop = true
                                                    }
                                                    Log.d("lhw","dianji")
                                                }
                                        )
                                        Text(
                                            text = policy.getOrDefault("viadata", "kong").toString()
                                        )
                                    }

                                }

                            }
                        }
                    }
                }
            }
        }
    )
}


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DetailScreen(
//    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
//    index: Int
//) {
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.primaryContainer,
//                    titleContentColor = MaterialTheme.colorScheme.primary,
//                ),
//                title = {
//                    Text(text = "政策全文")
//                }
//            )
//        }
//    ) { innerpadding ->
//        val htmlContent = viewModel.policyList.value.get(1).getOrDefault("text", "kong").toString()
//        HtmlText(html = htmlContent, modifier = Modifier.padding(innerpadding))
//    }
//}
//
//@Composable
//fun HtmlText(html: String, modifier: Modifier = Modifier) {
//    val annotatedString = parseHtml(html)
//    Text(text = annotatedString, modifier = modifier)
//}
//
//fun parseHtml(html: String): AnnotatedString {
//    val document = Jsoup.parse(html)
//    val body = document.body()
//    return buildAnnotatedString {
//        appendHtmlElement(body)
//    }
//}
//
//fun AnnotatedString.Builder.appendHtmlElement(element: Element) {
//    when (element.tagName()) {
//        "b", "strong" -> withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
//            element.childNodes().forEach { appendHtmlNode(it) }
//        }
//
//        "i", "em" -> withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
//            element.childNodes().forEach { appendHtmlNode(it) }
//        }
//
//        "u" -> withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
//            element.childNodes().forEach { appendHtmlNode(it) }
//        }
//
//        "br" -> append("\n")
//        else -> element.childNodes().forEach { appendHtmlNode(it) }
//    }
//}
//
//fun AnnotatedString.Builder.appendHtmlNode(node: org.jsoup.nodes.Node) {
//    when (node) {
//        is org.jsoup.nodes.TextNode -> append(node.text())
//        is Element -> appendHtmlElement(node)
//    }
//}


//政策要点页面
@Composable
fun policy2(
    viewModel: HomeViewModel
) {
    Text(text = "456")
}

@Preview
@Composable
fun HomeScreenPrevice() {
    PolicyTheme {
//
    }
}