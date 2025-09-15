package com.example.firebase.pages

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.firebase.R
import com.example.firebase.components.CustomDarkTextField
import com.example.firebase.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun RegisterPage(
    onRegisterComplete: () -> Unit,
    onLoginClick: () -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var apelido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.9f),
                colors = CardDefaults.cardColors(containerColor = backgroundColor),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.register_icon),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(160.dp)
                            .padding(bottom = 5.dp)
                    )

                    Text(
                        "Cadastre-se",
                        fontFamily = FontFamily.Serif,
                        fontSize = 26.sp,
                        color = primaryColor,
                    )
                    Text(
                        "Role para baixo...",
                        fontFamily = FontFamily.Serif,
                        fontSize = 15.sp,
                        color = detailsColor,
                        modifier = Modifier.padding(vertical = 15.dp)
                    )

                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = Color.Red,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

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
                        isPassword = true
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

                    Button(
                        onClick = {
                            if (nome.isBlank() || apelido.isBlank() || email.isBlank() || senha.isBlank()) {
                                errorMessage = "Preencha todos os campos obrigatórios"
                                return@Button
                            }

                            auth.createUserWithEmailAndPassword(email, senha)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                                        val usuario = hashMapOf(
                                            "uid" to userId,
                                            "nome" to nome,
                                            "apelido" to apelido,
                                            "email" to email,
                                            "telefone" to telefone
                                        )

                                        db.collection("banco")
                                            .document(userId)
                                            .set(usuario)
                                            .addOnSuccessListener {
                                                Log.d("Firestore", "Usuário cadastrado com sucesso")
                                                onRegisterComplete()
                                            }
                                            .addOnFailureListener { e ->
                                                errorMessage = "Erro ao salvar dados: ${e.message}"
                                            }
                                    } else {
                                        errorMessage = "Erro: ${task.exception?.message}"
                                    }
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
