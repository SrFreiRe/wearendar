package ochat.wearendar.backend

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Esta es la clase principal de la aplicación.
 * Se encarga de la lógica principal.
 */
class MainActivity : AppCompatActivity() {

    // Propiedades de la clase
    private var myVariable: String = "Hola Mundo"

    // Comentarios de documentación para la función
    /**
     * Esta función se llama cuando se crea la actividad.
     * @param savedInstanceState Un Bundle que contiene el estado guardado de la actividad.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Aquí va el código de inicialización
        println(myVariable)
    }

    // Otras funciones de la clase
    fun myFuncion() {
        // Aquí va el código de la función
    }
}

// Funciones fuera de la clase
fun otraFuncion() {
    // Aquí va el código de la función
}

// Punto de entrada principal si se ejecuta como una aplicación independiente
fun main() {
    println("Hola desde la función main")
}