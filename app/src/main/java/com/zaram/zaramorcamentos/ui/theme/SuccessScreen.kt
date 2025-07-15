package com.zaram.orcamentos.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SuccessScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Pedido enviado com sucesso!", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Obrigado por escolher a Zaram Artes e Cia 💕", fontSize = 16.sp)
    }
}
