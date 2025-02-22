package ochat.wearendar.utils

import ochat.wearendar.data.Event
import ochat.wearendar.data.EventType
import java.time.LocalDate
import java.time.LocalTime

val eventMap = mapOf(
    LocalDate.of(2025, 2, 22) to listOf(
        Event("Desayuno con la shory hiperbellaka", LocalTime.of(9, 0), LocalTime.of(10, 0), EventType.SMARTLY),
        Event("Paseillo", LocalTime.of(11, 0), LocalTime.of(12, 0), EventType.CASUAL),
        Event("Tennis con mat", LocalTime.of(13, 0), LocalTime.of(14, 0), EventType.SPORTIVE),
        Event("Funeral de Juanita", LocalTime.of(18, 0), LocalTime.of(18, 30), EventType.FORMALLY),
        Event("Fiesta Diego", LocalTime.of(23, 0), LocalTime.of(3, 0), EventType.FESTIVE)
    )
)