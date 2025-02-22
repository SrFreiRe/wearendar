package ochat.wearendar.utils

import ochat.wearendar.data.Event
import ochat.wearendar.data.EventType
import java.time.LocalDate
import java.time.LocalTime

val eventMap = mapOf(
    LocalDate.of(2025, 2, 22) to listOf(
        Event(
            12,
            "Desayuno con la shory hiperbellaka",
            "Disfruta de un desayuno especial con la shory en una acogedora cafetería del centro de Madrid. Croissants, café y buena conversación.",
            LocalTime.of(9, 0),
            LocalTime.of(10, 0),
            "Madrid",
            EventType.SMARTLY
        ),
        Event(
            28,
            "Paseillo",
            "Un paseo matutino relajante por el casco histórico de Santiago, disfrutando de la arquitectura, el ambiente y un buen café para empezar el día.",
            LocalTime.of(11, 0),
            LocalTime.of(12, 0),
            "Santiago",
            EventType.CASUAL
        ),
        Event(
            33,
            "Tenis con Mat",
            "Partido de tenis amistoso con Mat en las pistas de A Coruña. Un poco de deporte y competición sana para pasar la tarde.",
            LocalTime.of(13, 0),
            LocalTime.of(14, 0),
            "A Coruña",
            EventType.SPORTIVE
        ),
        Event(
            46,
            "Funeral de Juanita",
            "Ceremonia en memoria de Juanita, despedida de amigos y familiares en un servicio solemne en el cementerio de Vigo.",
            LocalTime.of(18, 0),
            LocalTime.of(18, 30),
            "Vigo",
            EventType.FORMALLY
        ),
        Event(
            57,
            "Fiesta Diego",
            "Gran fiesta de cumpleaños en casa de Diego, con música, tragos y muchas risas hasta la madrugada en Lugo.",
            LocalTime.of(23, 0),
            LocalTime.of(3, 0),
            "Lugo",
            EventType.FESTIVE
        )
    )
)