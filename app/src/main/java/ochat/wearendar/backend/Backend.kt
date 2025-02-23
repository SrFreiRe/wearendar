package ochat.wearendar.backend
import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.io.IOException
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import ochat.wearendar.data.Event
import ochat.wearendar.data.EventType
import okhttp3.Call
import okhttp3.Callback
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

val client = HttpClient(CIO) {
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
    val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    val events: List<EventJson> = client.get("http://10.0.2.2:5000/events").body()
    client.close()

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

