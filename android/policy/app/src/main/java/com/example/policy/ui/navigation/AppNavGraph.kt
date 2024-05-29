package com.example.learnrecord.ui.navigation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.policy.ui.AppViewModelProvider
import com.example.policy.ui.home.DetailScreen
import com.example.policy.ui.home.HomeScreen
import com.example.policy.ui.home.HomeViewModel


@Composable
fun AppNavHost(
    rootNavController: NavHostController = rememberNavController(),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    viewModel:HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    NavHost(
        navController = rootNavController,
        startDestination = "home",
        modifier = modifier,

    ) {

        composable("home") {
            HomeScreen(
                viewModel=viewModel,
                rootNavHostController = rootNavController
            )
        }

        composable("detail/{index}") { backStackEntry ->
            val index = backStackEntry.arguments?.getString("index")
            DetailScreen(
//                rootNavHostController = rootNavController,
                viewModel=viewModel,
                index = index!!.toInt()
            )
        }
    }
}