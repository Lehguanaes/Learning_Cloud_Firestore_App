package com.example.firebase.components

// Importações necessárias para compor a UI
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.firebase.ui.theme.detailsColor

// Esse CustomDarkTextField é um TextField reutilizável e estilizado. Ele pode ser usado em diferentes telas para entrada de texto, recebendo cores, rótulos, ícones e podendo ser configurado como campo de senha.
// Função composable personalizada que cria um TextField estilizado
@Composable
fun CustomDarkTextField(
    value: String,                          // Valor atual do campo de texto
    onValueChange: (String) -> Unit,        // Callback chamado quando o valor muda
    label: String,                          // Texto do rótulo (label)
    backgroundColor: Color,                 // Cor de fundo do campo
    textColor: Color,                       // Cor do texto digitado
    labelColor: Color,                      // Cor do label quando não está focado
    isPassword: Boolean = false,            // Define se o campo é de senha (esconde o texto)
    trailingIcon: @Composable (() -> Unit)? = null // Ícone opcional no lado direito do campo
) {
    TextField(
        value = value,                      // Valor do campo de texto
        onValueChange = onValueChange,      // Atualiza o valor quando o usuário digita
        label = { Text(label, color = labelColor) }, // Label exibido dentro do campo
        modifier = Modifier
            .fillMaxWidth()                 // Faz o campo ocupar toda a largura disponível
            .padding(vertical = 8.dp),      // Espaçamento vertical entre campos
        visualTransformation = if (isPassword) 
            PasswordVisualTransformation()  // Se for senha, mostra ••• no lugar dos caracteres
        else 
            VisualTransformation.None,      // Caso contrário, mostra o texto normalmente
        colors = TextFieldDefaults.colors(  // Define as cores personalizadas do TextField
            focusedContainerColor = backgroundColor,   // Fundo quando focado
            unfocusedContainerColor = backgroundColor, // Fundo quando não focado
            focusedTextColor = textColor,              // Cor do texto quando focado
            unfocusedTextColor = textColor,            // Cor do texto quando não focado
            cursorColor = detailsColor,                // Cor do cursor de digitação
            focusedLabelColor = detailsColor,          // Cor do label ao focar
            unfocusedLabelColor = labelColor,          // Cor do label quando não focado
            focusedIndicatorColor = detailsColor,      // Cor da linha inferior ao focar
            unfocusedIndicatorColor = Color.Gray       // Cor da linha inferior sem foco
        ),
        trailingIcon = trailingIcon          // Ícone opcional exibido no canto direito
    )
}
