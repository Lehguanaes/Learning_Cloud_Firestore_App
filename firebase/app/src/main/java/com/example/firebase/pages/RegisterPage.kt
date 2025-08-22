package com.example.firebase.pages

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.firebase.R
import com.example.firebase.ui.theme.backgroundColor
import com.example.firebase.ui.theme.primaryColor
import com.example.firebase.ui.theme.textColor
import com.example.firebase.ui.theme.cardBackground
import com.example.firebase.ui.theme.labelColor
import com.example.firebase.components.CustomDarkTextField
import com.example.firebase.ui.theme.detailsColor
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

// Tela de cadastro (RegisterPage)
// Recebe duas funções como parâmetro:
// - onRegisterComplete: chamada quando o cadastro é concluído com sucesso
// - onLoginClick: chamada quando o usuário decide ir para a tela de login
@Composable
fun RegisterPage(
    onRegisterComplete: () -> Unit,
    onLoginClick: () -> Unit
) {
    // Estados para armazenar os valores digitados pelo usuário
    var nome by remember { mutableStateOf("") }
    var apelido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Referência ao Firestore
    val db = Firebase.firestore

    // Layout principal
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // Conteúdo principal com rolagem
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp) // Espaço para não sobrepor o rodapé
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Card central que contém o formulário de cadastro
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.9f), // Ocupa 90% da largura
                colors = CardDefaults.cardColors(
                    containerColor = backgroundColor
                ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Ícone de cadastro
                    Image(
                        painter = painterResource(id = R.drawable.register_icon),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(160.dp)
                            .padding(bottom = 5.dp)
                    )

                    // Título
                    Text(
                        "Cadastre-se",
                        fontFamily = FontFamily.Serif,
                        fontSize = 26.sp,
                        color = primaryColor,
                    )

                    // Texto auxiliar
                    Text(
                        "Role para baixo...",
                        fontFamily = FontFamily.Serif,
                        fontSize = 15.sp,
                        color = detailsColor,
                        modifier = Modifier.padding(vertical = 15.dp)
                    )

                    // Mensagem de erro (se houver)
                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = Color.Red,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    // Campos de texto (usando componente customizado)
                    CustomDarkTextField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = "Nome",
                        backgroundColor = cardBackground,
                        textColor = textColor,
                        labelColor = labelColor
                    )

                    CustomDarkTextField(
                        value = apelido,
                        onValueChange = { apelido = it },
                        label = "Apelido",
                        backgroundColor = cardBackground,
                        textColor = textColor,
                        labelColor = labelColor
                    )

                    CustomDarkTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "E-mail",
                        backgroundColor = cardBackground,
                        textColor = textColor,
                        labelColor = labelColor
                    )

                    CustomDarkTextField(
                        value = senha,
                        onValueChange = { senha = it },
                        label = "Senha",
                        backgroundColor = cardBackground,
                        textColor = textColor,
                        labelColor = labelColor,
                        isPassword = true // Campo de senha
                    )

                    CustomDarkTextField(
                        value = telefone,
                        onValueChange = { telefone = it },
                        label = "Telefone",
                        backgroundColor = cardBackground,
                        textColor = textColor,
                        labelColor = labelColor
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Botão de cadastrar
                    Button(
                        onClick = {
                            // Validação: campos obrigatórios não podem estar vazios
                            if (nome.isBlank() || apelido.isBlank() || email.isBlank() || senha.isBlank()) {
                                errorMessage = "Preencha todos os campos obrigatórios"
                                return@Button
                            }

                            // Cria o objeto usuário para enviar ao Firestore
                            val usuario = hashMapOf(
                                "nome" to nome,
                                "apelido" to apelido,
                                "email" to email,
                                "senha" to senha,
                                "telefone" to telefone
                            )

                            // Adiciona o usuário no Firestore
                            db.collection("banco")
                                .add(usuario)
                                .addOnSuccessListener {
                                    Log.d("Firestore", "Documento adicionado com ID: ${it.id}")
                                    onRegisterComplete() // Navega para próxima tela
                                }
                                .addOnFailureListener { e ->
                                    errorMessage = "Erro ao cadastrar: ${e.message}"
                                    Log.w("Firestore", "Erro ao adicionar documento", e)
                                }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryColor,
                            contentColor = textColor
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Cadastrar", fontSize = 18.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Botão para ir à tela de login
                    Button(
                        onClick = onLoginClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = primaryColor
                        ),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, primaryColor)
                    ) {
                        Text("Já tem uma conta? Faça login", fontSize = 16.sp)
                    }
                }
            }
        }

        // Rodapé fixo no fim da tela
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .align(Alignment.BottomCenter)
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
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Text(
                    text = "Todos os direitos reservados.",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Normal,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}
