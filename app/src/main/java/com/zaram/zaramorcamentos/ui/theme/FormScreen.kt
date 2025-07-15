package com.zaram.orcamentos.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(onSubmit: () -> Unit) {
    val context = LocalContext.current

    var nome by remember { mutableStateOf("") }
    var dataEntrega by remember { mutableStateOf("") }
    var erro by remember { mutableStateOf<String?>(null) }

    val listaProdutos = listOf(
        "Topo de Bolo",
        "Convites de Casamento",
        "Agendas",
        "Impressões Diversas",
        "Outros"
    )
    val produtosSelecionados = remember { mutableStateMapOf<String, String>() }
    var outroProdutoDescricao by remember { mutableStateOf("") }

    // Formatação automática da data (dd/mm/aaaa)
    fun formatarData(digitos: String): String {
        val somenteNumeros = digitos.filter { it.isDigit() }.take(8)
        return buildString {
            for (i in somenteNumeros.indices) {
                append(somenteNumeros[i])
                if (i == 1 || i == 3) append("/")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        erro?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Seu nome") },
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            "Selecione os produtos desejados e informe as quantidades:",
            style = MaterialTheme.typography.titleMedium
        )

        listaProdutos.forEach { produto ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Checkbox(
                        checked = produtosSelecionados.containsKey(produto),
                        onCheckedChange = { isChecked ->
                            if (isChecked) {
                                produtosSelecionados[produto] = ""
                            } else {
                                produtosSelecionados.remove(produto)
                            }
                        }
                    )
                    Text(produto)
                }

                if (produtosSelecionados.containsKey(produto)) {
                    OutlinedTextField(
                        value = produtosSelecionados[produto] ?: "",
                        onValueChange = { produtosSelecionados[produto] = it },
                        label = { Text("Qtd.") },
                        modifier = Modifier.width(100.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )
                }
            }

            if (produto == "Outros" && produtosSelecionados.containsKey("Outros")) {
                OutlinedTextField(
                    value = outroProdutoDescricao,
                    onValueChange = { outroProdutoDescricao = it },
                    label = { Text("Descreva o produto") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        OutlinedTextField(
            value = dataEntrega,
            onValueChange = { input ->
                dataEntrega = formatarData(input)
            },
            label = { Text("Data desejada para entrega (dd/mm/aaaa)") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onClick@{
                if (
                    nome.isBlank() ||
                    dataEntrega.length < 10 ||  // formato dd/mm/aaaa
                    produtosSelecionados.isEmpty() ||
                    produtosSelecionados.values.any { it.isBlank() }
                ) {
                    erro = "Por favor, preencha todos os campos obrigatórios."
                    return@onClick
                }

                erro = null

                val numeroWhatsApp = "+552433813798"
                val mensagem = buildString {
                    append("ZARAM ARTES & CIA\n")
                    append("Pedido de Orçamento\n\n")
                    append("Cliente: $nome\n")
                    append("Data desejada para entrega: $dataEntrega\n\n")
                    append("Produtos solicitados:\n")
                    produtosSelecionados.forEach { (produto, qtd) ->
                        val desc = if (produto == "Outros") " (descrição: $outroProdutoDescricao)" else ""
                        append("- $produto$desc: $qtd unidade(s)\n")
                    }
                    append("\nEste pedido foi gerado automaticamente pelo sistema Zaram Artes & Cia.")
                }

                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(
                        "https://api.whatsapp.com/send?phone=$numeroWhatsApp&text=${Uri.encode(mensagem)}"
                    )
                }

                context.startActivity(intent)
                onSubmit()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar Pedido")
        }
    }
}
