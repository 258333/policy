package com.example.policy.ui.home

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {

    SideEffect {
        viewModel.typeListFun()
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
                    typeList = viewModel.typeList.value
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
    viewModel: HomeViewModel
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
                    IconButton(onClick = {
                        scope.launch { drawerState.open() }
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = "Open Menu")
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
                        items(5) { index ->
                            OutlinedCard(onClick = { /*TODO*/ }) {
                                Text(text = "nihoa")
                            }
                        }
                    }
                }
            }
        }
    )
}


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
        HomeScreen()
    }
}