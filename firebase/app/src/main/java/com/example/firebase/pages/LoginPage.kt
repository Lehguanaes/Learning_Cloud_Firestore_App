package com.example.firebase.pages

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.firebase.ui.theme.backgroundColor
import com.example.firebase.ui.theme.primaryColor
import com.example.firebase.ui.theme.textColor
import com.example.firebase.ui.theme.cardBackground
import com.example.firebase.ui.theme.labelColor
import com.example.firebase.R
import com.example.firebase.components.CustomDarkTextField
import com.example.firebase.ui.theme.detailsColor
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

// Esse LoginPage é uma tela de login customizada em Jetpack Compose integrada ao Firestore.
@Composable
fun LoginPage(
    onLogin: (String) -> Unit,   // Callback ao logar com sucesso (passa apelido ou email)
    onRegisterClick: () -> Unit  // Callback quando o usuário clica em "Cadastrar-se"
) {
    // ======= ESTADOS ==========
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var mostrarSenha by remember { mutableStateOf(false) } // controle de visibilidade da senha
    var errorMessage by remember { mutableStateOf("") }    // mensagem de erro exibida na tela
    val db = Firebase.firestore // referência ao Firestore

    // ======= LAYOUT PRINCIPAL ==========
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween, // separa conteúdo e rodapé
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ---------- CONTEÚDO PRINCIPAL ----------
            Column(
                modifier = Modifier
                    .weight(1f) // ocupa espaço antes do rodapé
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Logo
                Image(
                    painter = painterResource(id = R.drawable.login_icon),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(230.dp)
                        .padding(bottom = 16.dp)
                )

                // Título
                Text(
                    "Conecte-se conosco",
                    fontFamily = FontFamily.Serif,
                    fontSize = 26.sp,
                    color = primaryColor,
                    modifier = Modifier.padding(vertical = 24.dp)
                )

                // Mensagem de erro
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // Campo de E-mail
                CustomDarkTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "E-mail",
                    backgroundColor = cardBackground,
                    textColor = textColor,
                    labelColor = labelColor
                )

                // Campo de Senha (com ícone de mostrar/ocultar)
                CustomDarkTextField(
                    value = senha,
                    onValueChange = { senha = it },
                    label = "Senha",
                    backgroundColor = cardBackground,
                    textColor = textColor,
                    labelColor = labelColor,
                    isPassword = !mostrarSenha, // se false → mostra senha
                    trailingIcon = {
                        IconButton(onClick = { mostrarSenha = !mostrarSenha }) {
                            Icon(
                                painter = painterResource(
                                    id = if (mostrarSenha) R.drawable.eyes_yes_icon else R.drawable.eyes_no_icon
                                ),
                                contentDescription = "Mostrar/ocultar senha",
                                tint = Color.Unspecified
                            )
                        }
                    }
                )

                // Mensagem de boas-vindas
                Text(
                    "Seja bem-vindo!",
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp,
                    color = detailsColor,
                    modifier = Modifier.padding(vertical = 5.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botão de Login
                Button(
                    onClick = {
                        // Validação inicial
                        if (email.isBlank() || senha.isBlank()) {
                            errorMessage = "Preencha todos os campos"
                            return@Button
                        }

                        // Consulta ao Firestore para verificar credenciais
                        db.collection("banco")
                            .whereEqualTo("email", email)
                            .whereEqualTo("senha", senha)
                            .get()
                            .addOnSuccessListener { documents ->
                                if (documents.isEmpty) {
                                    // Nenhum usuário encontrado
                                    errorMessage = "Credenciais inválidas"
                                } else {
                                    // Login válido → pega o apelido ou email
                                    val nomeUsuario =
                                        documents.documents[0].getString("apelido") ?: email
                                    onLogin(nomeUsuario)
                                }
                            }
                            .addOnFailureListener { exception ->
                                errorMessage = "Erro ao fazer login: ${exception.message}"
                                Log.w("Login", "Erro ao verificar login", exception)
                            }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Entrar", fontSize = 18.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Botão para tela de cadastro
                Button(
                    onClick = onRegisterClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = primaryColor
                    ),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, primaryColor)
                ) {
                    Text("Não tem conta? Cadastre-se", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // ---------- RODAPÉ ----------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(primaryColor),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "© 2025 | Letícia Guanaes Moreira",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Todos os direitos reservados.",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
