package ochat.wearendar.utils

import ochat.wearendar.R
import ochat.wearendar.data.Brand
import ochat.wearendar.data.Event
import ochat.wearendar.data.EventType
import ochat.wearendar.data.Wear
import java.time.LocalDate
import java.time.LocalTime
import java.util.ArrayList

val eventMap = mapOf(
    LocalDate.of(2025, 2, 22) to listOf(
        Event(
            "Desayuno con la shory hiperbellaka",
            "Disfruta de un desayuno especial con la shory en una acogedora cafetería del centro de Madrid. Croissants, café y buena conversación.",
            LocalTime.of(9, 0),
            LocalTime.of(10, 0),
            "Madrid",
            EventType.SMARTLY
        ),
        Event(
            "Paseillo",
            "Un paseo matutino relajante por el casco histórico de Santiago, disfrutando de la arquitectura, el ambiente y un buen café para empezar el día.",
            LocalTime.of(11, 0),
            LocalTime.of(12, 0),
            "Santiago",
            EventType.CASUAL
        ),
        Event(
            "Tenis con Mat",
            "Partido de tenis amistoso con Mat en las pistas de A Coruña. Un poco de deporte y competición sana para pasar la tarde.",
            LocalTime.of(13, 0),
            LocalTime.of(14, 0),
            "A Coruña",
            EventType.SPORTIVE
        ),
        Event(
            "Funeral de Juanita",
            "Ceremonia en memoria de Juanita, despedida de amigos y familiares en un servicio solemne en el cementerio de Vigo.",
            LocalTime.of(18, 0),
            LocalTime.of(18, 30),
            "Vigo",
            EventType.FORMALLY
        ),
        Event(
            "Fiesta Diego",
            "Gran fiesta de cumpleaños en casa de Diego, con música, tragos y muchas risas hasta la madrugada en Lugo.",
            LocalTime.of(23, 0),
            LocalTime.of(3, 0),
            "Lugo",
            EventType.FESTIVE
        )
    )
)

val wears = listOf(
    Wear("Cazadora cuero", 23.5f, "https://example.com/cazadora", 1, Brand.LEFTIES, R.drawable.p1),
    Wear("Zapatillas deportivas", 45.0f, "https://example.com/zapatillas", 2, Brand.STRADIVARIUS, R.drawable.p2),
    Wear("Sudadera con capucha", 30.0f, "https://example.com/sudadera", 3, Brand.OYSHO, R.drawable.p3),
    Wear("Pantalón vaquero", 28.99f, "https://example.com/vaquero", 4, Brand.ZARA, R.drawable.p4),
    Wear("Camiseta estampada", 15.5f, "https://example.com/camiseta", 5, Brand.MASSIMO_DUTTI, R.drawable.p5),
    Wear("Abrigo de lana", 79.99f, "https://example.com/abrigo", 6, Brand.PULL_AND_BEAR, R.drawable.p6),
    Wear("Cazadora cuero", 23.5f, "https://example.com/cazadora", 7, Brand.LEFTIES, R.drawable.p1),
    Wear("Zapatillas deportivas", 45.0f, "https://example.com/zapatillas", 8, Brand.STRADIVARIUS, R.drawable.p2),
    Wear("Sudadera con capucha", 30.0f, "https://example.com/sudadera", 9, Brand.OYSHO, R.drawable.p3),
    Wear("Pantalón vaquero", 28.99f, "https://example.com/vaquero", 10, Brand.ZARA, R.drawable.p4),
    Wear("Camiseta estampada", 15.5f, "https://example.com/camiseta", 11, Brand.MASSIMO_DUTTI, R.drawable.p5),
    Wear("Abrigo de lana", 79.99f, "https://example.com/abrigo", 12, Brand.PULL_AND_BEAR, R.drawable.p6)
)

val wearsList = listOf(
    listOf(
        Wear("Cazadora cuero", 1.5f, "https://example.com/cazadora", 1, Brand.LEFTIES, R.drawable.p1),
        Wear("Cazadora cuero", 2.5f, "https://example.com/cazadora", 2, Brand.LEFTIES, R.drawable.p1),
        Wear("Cazadora cuero", 3.5f, "https://example.com/cazadora", 3, Brand.LEFTIES, R.drawable.p1),
        Wear("Cazadora cuero", 4.5f, "https://example.com/cazadora", 4, Brand.LEFTIES, R.drawable.p1),
        Wear("Cazadora cuero", 5.5f, "https://example.com/cazadora", 5, Brand.LEFTIES, R.drawable.p1),
    ),

    listOf(
        Wear("Zapatillas deportivas", 45.0f, "https://example.com/zapatillas", 6, Brand.STRADIVARIUS, R.drawable.p2),
        Wear("Zapatillas deportivas", 46.0f, "https://example.com/zapatillas", 7, Brand.STRADIVARIUS, R.drawable.p2),
        Wear("Zapatillas deportivas", 47.0f, "https://example.com/zapatillas", 8, Brand.STRADIVARIUS, R.drawable.p2),
        Wear("Zapatillas deportivas", 48.0f, "https://example.com/zapatillas", 9, Brand.STRADIVARIUS, R.drawable.p2),
        Wear("Zapatillas deportivas", 49.0f, "https://example.com/zapatillas", 10, Brand.STRADIVARIUS, R.drawable.p2),
    ),

    listOf(
        Wear("Cazadora cuero", 23.5f, "https://example.com/cazadora", 11, Brand.LEFTIES, R.drawable.p1),
        Wear("Cazadora cuero", 33.5f, "https://example.com/cazadora", 12, Brand.LEFTIES, R.drawable.p1),
        Wear("Cazadora cuero", 43.5f, "https://example.com/cazadora", 13, Brand.LEFTIES, R.drawable.p1),
        Wear("Cazadora cuero", 53.5f, "https://example.com/cazadora", 14, Brand.LEFTIES, R.drawable.p1),
        Wear("Cazadora cuero", 63.5f, "https://example.com/cazadora", 15, Brand.LEFTIES, R.drawable.p1),
    ),

    listOf(
        Wear("Sudadera con capucha", 31.0f, "https://example.com/sudadera", 16, Brand.OYSHO, R.drawable.p3),
        Wear("Sudadera con capucha", 32.0f, "https://example.com/sudadera", 17, Brand.OYSHO, R.drawable.p3),
        Wear("Sudadera con capucha", 33.0f, "https://example.com/sudadera", 18, Brand.OYSHO, R.drawable.p3),
        Wear("Sudadera con capucha", 34.0f, "https://example.com/sudadera", 19, Brand.OYSHO, R.drawable.p3),
        Wear("Sudadera con capucha", 35.0f, "https://example.com/sudadera", 20, Brand.OYSHO, R.drawable.p3),
    )
)