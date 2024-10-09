package com.example.listadecomprasapp

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ArticuloViewModel : ViewModel() {
    // Lista de artículos
    val articulos = mutableStateListOf<Articulo>()
    val historialArticulos = mutableStateListOf<Articulo>()
    val papeleraArticulos = mutableStateListOf<Articulo>()

    // Variable para manejar el estado del tema (oscuro o claro)
    private val _isDarkTheme = MutableStateFlow(false) // Inicialmente modo claro
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    // Función para alternar el tema oscuro y claro
    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    // Funciones para manejar los artículos
    fun agregarAlHistorial(articulo: Articulo) {
        if (!historialArticulos.contains(articulo)) {
            historialArticulos.add(articulo)
        }
    }

    fun moverALaPapelera(articulo: Articulo) {
        papeleraArticulos.add(articulo)
        articulos.remove(articulo)
    }

    fun actualizarArticulo(articulo: Articulo, isChecked: Boolean) {
        val index = articulos.indexOf(articulo)
        if (index != -1) {
            articulos[index] = articulo.copy(comprado = isChecked)
        }
        agregarAlHistorial(articulo)
    }

    fun obtenerArticulosPorCategoria(): Map<String, List<Articulo>> {
        return articulos.groupBy { obtenerCategoria(it) }
    }

    private fun obtenerCategoria(articulo: Articulo): String {
        return when (articulo.nombre.lowercase()) {
            "manzana", "naranja", "plátano" -> "Frutas"
            "zanahoria", "lechuga" -> "Verduras"
            "refresco", "jugo" -> "Bebidas"
            else -> "Otros"
        }
    }
}
