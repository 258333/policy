package com.example.policy.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.policy.logic.model.Type
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {

    SideEffect {
        viewModel.typeListFun()
    }

    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("Tab 1", "Tab 2")

    Box(modifier = Modifier.fillMaxSize()) {
        // 其他UI元素可以放在这里

        // 将TabRow放置在屏幕底部
        TabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier.align(Alignment.BottomCenter)
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

    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        when (selectedTab) {
            //判断选择的是路线 还是 线路 菜单
            0 -> Policy1Body(
                viewModel = viewModel,
                typeList = viewModel.typeList.value
            )

            1 -> policy2(viewModel = viewModel)
        }
    }

}

//政策全文页面
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Policy1Body(
    typeList: List<Type>,
    viewModel: HomeViewModel
) {
    policy1Content(
        form = viewModel.form.value,
        onInputChange = viewModel::onInputChange
    )


    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val tabTitles = listOf("Tab 1", "Tab 2")
    val checkedState = remember { mutableStateListOf(false, false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
//                Spacer(modifier = Modifier.height(16.dp))
                itemsIndexed(typeList) { index, title ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Checkbox(
                            checked = checkedState[index],
                            onCheckedChange = { isChecked ->
                                checkedState[index] = isChecked
                                // 执行复选框选择后的操作，例如：viewModel.clearMetroStations()
                            }
                        )
                        Row {
                            title.type?.let { Text(text = it) }
                            Text(text = title.num.toString())
                        }

                    }
                }
            }
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("") },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch { drawerState.open() }
                            }) {
                                Icon(Icons.Default.Menu, contentDescription = "Open Menu")
                            }
                        }
                    )
                },
                content = { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Column(modifier = Modifier.align(Alignment.TopStart)) {
                            Text(
                                text = "",
                                fontSize = 24.sp,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            )
        }
    )


}

@Composable
fun policy1Content(
    form: SearchForm,
    onInputChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            enabled = true,
            modifier = Modifier,
            label = { Text(text = "请输入关键字") },
            value = form.input!!,
            //输入的值发生变化时
            onValueChange = { onInputChange(it) }
        )

    }

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