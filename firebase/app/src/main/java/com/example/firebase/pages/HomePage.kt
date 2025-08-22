package com.example.firebase.pages

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.firebase.ui.theme.backgroundColor
import com.example.firebase.ui.theme.primaryColor
import com.example.firebase.ui.theme.textColor
import com.example.firebase.ui.theme.cardBackground
import com.example.firebase.ui.theme.detailsColor
import com.example.firebase.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun HomePage(
    userName: String = "Usuário",
    onLogout: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var mostrarRegistros by remember { mutableStateOf(false) }
    val db = Firebase.firestore
    val banco = remember { mutableStateListOf<Map<String, Any>>() }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // Topo com botão de menu
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            IconButton(onClick = { menuExpanded = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu",
                    tint = primaryColor
                )
            }

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
                offset = DpOffset(x = (-30).dp, y = -10.dp)
            ) {
                DropdownMenuItem(
                    text = { Text("Consultar Registros") },
                    onClick = {
                        menuExpanded = false
                        db.collection("banco")
                            .get()
                            .addOnSuccessListener { result ->
                                banco.clear()
                                for (document in result) {
                                    banco.add(document.data)
                                }
                                mostrarRegistros = true
                            }
                            .addOnFailureListener { exception ->
                                Log.w("Firestore", "Erro ao buscar documentos.", exception)
                            }
                    },
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.pen_icon),
                            contentDescription = "Consultar",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                )
                DropdownMenuItem(
                    text = { Text("Encerrar Navegação") },
                    onClick = {
                        menuExpanded = false
                        onLogout()
                    },
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.close_icon),
                            contentDescription = "Fechar",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                )
            }
        }

        // Logo centralizado com texto alinhado verticalmente
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(
                    id = if (mostrarRegistros) R.drawable.bag_icon else R.drawable.home_icon
                ),
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp)
            )
            if (mostrarRegistros) {
                Text(
                    text = "Cadastros",
                    color = detailsColor,
                    fontFamily = FontFamily.Serif,
                    fontSize = 26.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        // Conteúdo principal rolável com peso 1 para ocupar espaço restante
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!mostrarRegistros) {
                Text(
                    "Seja Bem-vindo, $userName!",
                    fontFamily = FontFamily.Serif,
                    fontSize = 26.sp,
                    color = primaryColor,
                    modifier = Modifier.padding(vertical = 20.dp)
                )

                Text(
                    text = "Use o menu no canto superior direito para novas possibilidades!",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .padding(horizontal = 24.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.idea_icon),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(150.dp)
                        .padding(bottom = 16.dp)
                )
            }

            if (mostrarRegistros) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 40.dp)
                ) {
                    banco.forEachIndexed { index, registro ->
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .background(cardBackground, shape = RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.point_icon),
                                contentDescription = "Registro Ícone",
                                modifier = Modifier
                                    .size(70.dp)
                                    .padding(end = 12.dp)
                            )

                            Column {
                                Text("Cadastro ${index + 1}", color = primaryColor, fontSize = 18.sp)
                                Text("Nome: ${registro["nome"]}", color = textColor)
                                Text("Apelido: ${registro["apelido"]}", color = textColor)
                                Text("Email: ${registro["email"]}", color = textColor)
                                Text("Telefone: ${registro["telefone"]}", color = textColor)
                            }
                        }
                    }
                }
            }
        }

        // Rodapé fixo no final da tela
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
                    fontSize = 13.sp,
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