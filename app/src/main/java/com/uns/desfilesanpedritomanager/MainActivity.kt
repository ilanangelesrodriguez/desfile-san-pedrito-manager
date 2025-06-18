package com.uns.desfilesanpedritomanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.uns.desfilesanpedritomanager.ui.theme.DesfileSanPedritoManagerTheme
import androidx.compose.material.icons.filled.Star
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.uns.desfilesanpedritomanager.presentation.components.BottomNavigationBar
import com.uns.desfilesanpedritomanager.presentation.screens.ListScreen
import com.uns.desfilesanpedritomanager.presentation.screens.RegisterScreen
import com.uns.desfilesanpedritomanager.presentation.screens.StatisticsScreen
import com.uns.desfilesanpedritomanager.presentation.viewmodel.PersonViewModel
import com.uns.desfilesanpedritomanager.presentation.viewmodel.PersonViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: PersonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        println("onCreate")

        viewModel = ViewModelProvider(this, PersonViewModelFactory())[PersonViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            DesfileSanPedritoManagerTheme {
                FormularioDesfileApp(viewModel)
            }
        }
    }
}

sealed class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object RegisterRoute : Screen("register", "Registro", Icons.Default.Add)
    object ListRoute : Screen("list", "Participantes", Icons.AutoMirrored.Filled.List)
    object StatisticsRoute : Screen("statistics", "Estadísticas",  Icons.Default.Star)
}

@Composable
fun FormularioDesfileApp(viewModel: PersonViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.StatisticsRoute.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.RegisterRoute.route) {
                RegisterScreen(
                    navigateToList = {
                        navController.navigate(Screen.ListRoute.route)
                    },
                    viewModel = viewModel
                )
            }

            composable(Screen.ListRoute.route) {
                ListScreen(
                    navigateToRegister = {
                        navController.navigate(Screen.RegisterRoute.route)
                    },
                    viewModel = viewModel
                )
            }

            composable(Screen.StatisticsRoute.route) {
                StatisticsScreen(viewModel = viewModel)
            }
        }
    }
}