package ochat.wearendar.backend
import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import ochat.wearendar.data.Event
import ochat.wearendar.data.EventType
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

suspend fun main() {
    val eventMap = loadCalendarToEventMap()
    printEvents(eventMap)

    printEvents(eventMap)
}

