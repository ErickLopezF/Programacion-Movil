package com.example.listadecomprasapp

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.listadecomprasapp.ui.theme.ListaDeComprasAppTheme
import kotlinx.coroutines.launch

data class Articulo(val nombre: String, var comprado: Boolean = false)

class MainActivity : ComponentActivity() {
    private val articuloViewModel: ArticuloViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Detectar si el sistema está en modo oscuro o claro
        val isDarkTheme = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

        setContent {
            var darkTheme by remember { mutableStateOf(isDarkTheme) }

            // Aplicar el tema
            ListaDeComprasAppTheme(darkTheme = darkTheme) {
                ListaDeComprasApp(articuloViewModel)
            }
        }
    }
}

@Composable
fun ListaDeComprasApp(viewModel: ArticuloViewModel) {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("Lista de Compras") },
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
                    }
                }
            )
        },
        drawerContent = {
            DrawerContent(navController = navController)
        }
    ) {
        NavHost(navController = navController, startDestination = "listaDeCompras") {
            composable("listaDeCompras") { ListaDeComprasContent(navController, viewModel) }
            composable("categorias") { CategoriasScreen(navController, viewModel) }
            composable("historial") { HistorialScreen(navController, viewModel) }
            composable("papelera") { PapeleraScreen(navController, viewModel) }
        }
    }
}

@Composable
fun ListaDeComprasContent(navController: NavHostController, viewModel: ArticuloViewModel) {
    var nuevoArticulo by remember { mutableStateOf(TextFieldValue("")) }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = nuevoArticulo,
            onValueChange = { nuevoArticulo = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Añadir artículo") }
        )

        Button(
            onClick = {
                if (nuevoArticulo.text.isNotEmpty()) {
                    viewModel.articulos.add(Articulo(nuevoArticulo.text))
                    nuevoArticulo = TextFieldValue("")
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Agregar")
        }

        Spacer(modifier = Modifier.height(16.dp))
        viewModel.articulos.forEach { articulo ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(articulo.nombre)
                Checkbox(
                    checked = articulo.comprado,
                    onCheckedChange = { viewModel.actualizarArticulo(articulo, it) }
                )
                IconButton(onClick = { viewModel.moverALaPapelera(articulo) }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar artículo")
                }
            }
        }
    }
}

@Composable
fun DrawerContent(navController: NavHostController) {
    Column {
        Text("Menú", style = MaterialTheme.typography.h6, modifier = Modifier.padding(16.dp))
        Divider()
        Button(onClick = { navController.navigate("categorias") }) {
            Text("Agrupación por Categorías")
        }
        Button(onClick = { navController.navigate("historial") }) {
            Text("Historial")
        }
        Button(onClick = { navController.navigate("papelera") }) {
            Text("Papelera")
        }
    }
}

@Composable
fun CategoriasScreen(navController: NavHostController, viewModel: ArticuloViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Categorías") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("listaDeCompras") }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Text("Artículos por categoría", style = MaterialTheme.typography.h5)
            viewModel.obtenerArticulosPorCategoria().forEach { (categoria, articulos) ->
                Text(categoria, style = MaterialTheme.typography.h6)
                articulos.forEach { articulo ->
                    Text("- ${articulo.nombre}")
                }
            }
        }
    }
}

@Composable
fun HistorialScreen(navController: NavHostController, viewModel: ArticuloViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("listaDeCompras") }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Text("Historial de productos", style = MaterialTheme.typography.h5)
            viewModel.historialArticulos.forEach { articulo ->
                Text("- ${articulo.nombre}")
            }
        }
    }
}

@Composable
fun PapeleraScreen(navController: NavHostController, viewModel: ArticuloViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Papelera") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("listaDeCompras") }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Text("Productos eliminados", style = MaterialTheme.typography.h5)
            viewModel.papeleraArticulos.forEach { articulo ->
                Text("- ${articulo.nombre}")
            }
        }
    }
}
