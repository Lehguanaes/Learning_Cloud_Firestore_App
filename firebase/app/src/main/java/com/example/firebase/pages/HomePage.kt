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

// Esse código monta uma página inicial (HomePage) em Jetpack Compose, conectada ao Firestore, com menu, exibição de registros e rodapé fixo.
@Composable
fun HomePage(
    userName: String = "Usuário",  // Nome do usuário (exibido na tela inicial)
    onLogout: () -> Unit           // Callback chamado quando usuário encerra a sessão
) {
    // Estado do menu suspenso
    var menuExpanded by remember { mutableStateOf(false) }
    // Controle de exibição da lista de registros
    var mostrarRegistros by remember { mutableStateOf(false) }
    // Referência ao banco de dados Firestore
    val db = Firebase.firestore
    // Lista reativa onde ficam os documentos carregados do Firestore
    val banco = remember { mutableStateListOf<Map<String, Any>>() }
    // Estado do scroll da tela
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor) // Cor de fundo da tela
    ) {
        // ======= TOPO COM MENU ==========
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            // Botão que abre o menu
            IconButton(onClick = { menuExpanded = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu",
                    tint = primaryColor
                )
            }

            // Menu suspenso com duas opções
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
                offset = DpOffset(x = (-30).dp, y = -10.dp) // Ajuste de posição
            ) {
                // Opção 1: Consultar registros do Firestore
                DropdownMenuItem(
                    text = { Text("Consultar Registros") },
                    onClick = {
                        menuExpanded = false
                        db.collection("banco")
                            .get()
                            .addOnSuccessListener { result ->
                                banco.clear() // limpa registros anteriores
                                for (document in result) {
                                    banco.add(document.data) // adiciona cada documento
                                }
                                mostrarRegistros = true // troca tela para "Cadastros"
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
                // Opção 2: Encerrar navegação (logout)
                DropdownMenuItem(
                    text = { Text("Encerrar Navegação") },
                    onClick = {
                        menuExpanded = false
                        onLogout() // chama função recebida por parâmetro
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

        // ======= LOGO E TÍTULO ==========
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Troca o ícone conforme a tela
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

        // ======= CONTEÚDO PRINCIPAL ==========
        Column(
            modifier = Modifier
                .weight(1f) // ocupa espaço entre topo e rodapé
                .fillMaxWidth()
                .verticalScroll(scrollState) // scroll ativado
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Caso 1: tela inicial de boas-vindas
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

            // Caso 2: exibição dos registros buscados no Firestore
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
                            // Ícone do registro
                            Image(
                                painter = painterResource(id = R.drawable.point_icon),
                                contentDescription = "Registro Ícone",
                                modifier = Modifier
                                    .size(70.dp)
                                    .padding(end = 12.dp)
                            )

                            // Informações do registro
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

        // ======= RODAPÉ FIXO ==========
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
