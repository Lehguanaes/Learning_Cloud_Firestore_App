package com.example.firebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.firebase.ui.theme.FirebaseTheme
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.firebase.pages.HomePage
import com.example.firebase.pages.LoginPage
import com.example.firebase.pages.RegisterPage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirebaseTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier) { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier.padding(padding)
                    ) {
                        composable("login") {
                            LoginPage(
                                onLogin = { userName ->
                                    navController.navigate("home/${userName}")
                                },
                                onRegisterClick = {
                                    navController.navigate("register")
                                }
                            )
                        }
                        composable("register") {
                            RegisterPage(
                                onRegisterComplete = {
                                    navController.navigate("login")
                                },
                                onLoginClick = {
                                    navController.navigate("login")
                                }
                            )
                        }
                        composable(
                            "home/{userName}",
                            arguments = listOf(navArgument("userName") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            val userName = backStackEntry.arguments?.getString("userName") ?: ""
                            HomePage(
                                userName = userName,
                                onLogout = {
                                    navController.navigate("login") {
                                        popUpTo("home/{userName}") { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}