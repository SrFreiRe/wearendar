package ochat.wearendar.backend
import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.io.IOException
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.float
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ochat.wearendar.data.Brand
import ochat.wearendar.data.Event
import ochat.wearendar.data.EventType
import ochat.wearendar.data.Wear
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.File
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.floatOrNull

val client = HttpClient(CIO) {
    install(HttpTimeout) {
        requestTimeoutMillis = 300_000 // 5 minutos
        connectTimeoutMillis = 300_000
        socketTimeoutMillis = 300_000
    }
    expectSuccess = true
    install(ContentNegotiation)
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        })
    }
}

fun printJson(events: List<EventJson>) {
    val jsonPretty = Json { prettyPrint = true }.encodeToString(events)
    println(jsonPretty)
}

@Serializable
data class EventJson(val id:String, val title:String, val startTime:String, val endTime:String, val description:String, val location:String, val type:String)

suspend fun loadCalendarToEventMap(): HashMap<LocalDate, List<Event>> {

    val events: List<EventJson> = client.get("http://10.0.2.2:5000/events").body()

    val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME // ✅ Correct format

    val eventMap = events.map { eventCal ->
        val startDateTime = OffsetDateTime.parse(eventCal.startTime, formatter)
        val endDateTime = OffsetDateTime.parse(eventCal.endTime, formatter)

        Event(
            id = eventCal.id,
            title = eventCal.title,
            description = eventCal.description,
            startTime = startDateTime.toLocalDateTime(),
            endTime = endDateTime.toLocalDateTime(),
            location = eventCal.location,
            type = enumValues<EventType>().find { it.name.equals(eventCal.type, ignoreCase = true) } ?: EventType.CASUAL
        )
    }.groupBy { it.startTime.toLocalDate() }

    return HashMap(eventMap)
}

fun printEvents(eventMap: HashMap<LocalDate, List<Event>>) {
    eventMap.forEach { (date, events) ->
        println("Date: $date")
        events.forEach { event ->
            Log.d("d","  - ${event.title} at ${event.location}, ${event.startTime} → ${event.endTime}")
        }
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 *  FUNCIONES DE EVENTO A ROPA Y RERROL
 */
// Functions for "loadClothes_from_event"
fun eventListToJsonCalendar(event: Event): String {
    val gson = GsonBuilder().setPrettyPrinting().create()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val eventJson = mapOf(
        "id" to event.id.toString(),
        "title" to event.title,
        "description" to event.description,
        "startTime" to event.startTime.format(formatter),  // Usa la fecha real del evento
        "endTime" to event.endTime.format(formatter),
        "location" to event.location,
        "type" to "event"
    )

    return gson.toJson(listOf(eventJson))
}

fun extractWearLists(jsonString: String): List<List<Wear>> {
    val jsonParser = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
    }
    val wearMap: Map<String, List<Wear>> = jsonParser.decodeFromString(jsonString)
    return wearMap.values.toList()
}

object PriceAsFloatSerializer : KSerializer<Float> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("price", PrimitiveKind.FLOAT)

    override fun serialize(encoder: Encoder, value: Float) {
        encoder.encodeFloat(value)
    }

    override fun deserialize(decoder: Decoder): Float {
        val jsonElement = decoder.decodeSerializableValue(JsonElement.serializer())

        // Extraer el valor `current` dentro de `price.value`
        return jsonElement.jsonObject["value"]
            ?.jsonObject?.get("current")
            ?.jsonPrimitive?.floatOrNull
            ?: throw IllegalArgumentException("No se encontró el valor 'current' en el objeto price")
    }
}


// Función principal que obtiene los datos de la API y los procesa
suspend fun jsonCalendarToPrendas(event: Event): List<List<Wear>> {

    val json = eventListToJsonCalendar(event);

    try {
        val response: String = client.post("http://10.0.2.2:5001/generate_outfit") {
            contentType(ContentType.Application.Json)
            setBody(json)
        }.body()


        // Convertir JSON en listas de prendas
        val wearLists: List<List<Wear>> = extractWearLists(response)

        // Imprimir las prendas obtenidas
        wearLists.forEach { wearList ->
            println("🔹 Nueva lista de prendas:")
            wearList.forEach { wear ->
                println("  - ${wear.name} (${wear.price} ${wear.brand})")
            }
        }

        return wearLists

    } finally {
//        client.close()
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 *  FUNCIONES CAMARA
 */

/**
 * Sube una imagen a ImgBB y llama a un callback con la URL pública de la imagen. PARA GENERAR UNA
 * URL QUE SE LE PUEDA MANDAR A LA API DE ITX
 */
fun uploadImageToImgBB(imageFile: File, apiKey: String, callback: (String?) -> Unit) {
    val client = OkHttpClient()
    val requestBody = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("key", apiKey)
        .addFormDataPart("image", imageFile.name, RequestBody.create("image/*".toMediaType(), imageFile))
        .build()

    val request = Request.Builder()
        .url("https://api.imgbb.com/1/upload")
        .post(requestBody)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            callback(null)
        }

        override fun onResponse(call: Call, response: Response) {
            response.body?.string()?.let { json ->
                val url = Regex("\"url\":\"(.*?)\"").find(json)?.groups?.get(1)?.value
                callback(url) // URL pública de la imagen
            }
        }
    })
}

////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * FUNCIONES WISHLIST
 */

fun addFavourite(context: Context, prenda: Wear) {
    // Obtenemos la lista actual de favoritos o, si no existe, una lista vacía
    val favourites = listFavourites(context).toMutableList()
    // Añadimos la nueva prenda a la lista
    favourites.add(prenda)
    // Convertimos la lista actualizada a una cadena JSON
    val jsonString = Json.encodeToString(favourites)
    // Guardamos la cadena en el archivo de favoritos
    context.openFileOutput("favourites.json", Context.MODE_PRIVATE).use {
        it.write(jsonString.toByteArray())
    }
}

fun removeFavourite(context: Context, prenda: Wear) {
    // Obtenemos la lista actual de favoritos
    val favourites = listFavourites(context).toMutableList()
    // Filtramos la lista para eliminar la prenda con el mismo id
    val updatedFavourites = favourites.filterNot { it.id == prenda.id }
    // Convertimos la lista actualizada a JSON
    val jsonString = Json.encodeToString(updatedFavourites)
    // Guardamos el JSON en el archivo sobrescribiendo el anterior contenido
    context.openFileOutput("favourites.json", Context.MODE_PRIVATE).use {
        it.write(jsonString.toByteArray())
    }
}

fun listFavourites(context: Context): List<Wear> {
    return try {
        context.openFileInput("favourites.json").use { stream ->
            val jsonString = stream.readBytes().toString(Charsets.UTF_8)
            // Decodificamos la cadena JSON en una lista de objetos Wear
            Json.decodeFromString<List<Wear>>(jsonString)
        }
    } catch (e: Exception) {
        // Si ocurre cualquier excepción (por ejemplo, el archivo no existe), se retorna una lista vacía
        emptyList()
    }
}


fun uploadImageToImgBB(imageFile: File, callback: (String?) -> Unit) {
    val API_KEY = "ac0b6c9898da425be7a51079961d3e07"

    val client = OkHttpClient()
    val requestBody = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("key", "ac0b6c9898da425be7a51079961d3e07")
        .addFormDataPart("image", imageFile.name, RequestBody.create("image/*".toMediaType(), imageFile))
        .build()

    val request = Request.Builder()
        .url("https://api.imgbb.com/1/upload")
        .post(requestBody)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            callback(null)
        }

        override fun onResponse(call: Call, response: Response) {
            response.body?.string()?.let { json ->
                val url = Regex("\"url\":\"(.*?)\"").find(json)?.groups?.get(1)?.value
                print(url)
                callback(url)
            }
        }
    })
}

suspend fun main() {
    val eventMap = loadCalendarToEventMap()
    printEvents(eventMap)

    printEvents(eventMap)
}

