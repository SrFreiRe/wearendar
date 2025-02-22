package ochat.wearendar.utils

import ochat.wearendar.data.Brand
import ochat.wearendar.data.Event
import ochat.wearendar.data.EventType
import ochat.wearendar.data.Wear
import java.time.LocalDate
import java.time.LocalTime

/*val eventMap = mapOf(
    LocalDate.of(2025, 2, 22) to listOf(
        Event("1",
            "Desayuno con la shory hiperbellaka",
            "Disfruta de un desayuno especial con la shory en una acogedora cafetería del centro de Madrid. Croissants, café y buena conversación.",
            LocalTime.of(9, 0),
            LocalTime.of(10, 0),
            "Madrid",
            EventType.SMARTLY
        ),
        Event("2",
            "Paseillo",
            "Un paseo matutino relajante por el casco histórico de Santiago, disfrutando de la arquitectura, el ambiente y un buen café para empezar el día.",
            LocalTime.of(11, 0),
            LocalTime.of(12, 0),
            "Santiago",
            EventType.CASUAL
        ),
        Event("3",
            "Tenis con Mat",
            "Partido de tenis amistoso con Mat en las pistas de A Coruña. Un poco de deporte y competición sana para pasar la tarde.",
            LocalTime.of(13, 0),
            LocalTime.of(14, 0),
            "A Coruña",
            EventType.SPORTIVE
        ),
        Event("4",
            "Funeral de Juanita",
            "Ceremonia en memoria de Juanita, despedida de amigos y familiares en un servicio solemne en el cementerio de Vigo.",
            LocalTime.of(18, 0),
            LocalTime.of(18, 30),
            "Vigo",
            EventType.FORMALLY
        ),
        Event("5",
            "Fiesta Diego",
            "Gran fiesta de cumpleaños en casa de Diego, con música, tragos y muchas risas hasta la madrugada en Lugo.",
            LocalTime.of(23, 0),
            LocalTime.of(3, 0),
            "Lugo",
            EventType.FESTIVE
        )
    )
)*/

val wears = listOf(
    Wear("Cazadora cuero", 1.00f, "https://www.zara.com/es/es/cazadora-acolchada-lavada-p04575411.html?v1=417748086&v2=2436823", 1, Brand.LEFTIES, "https://static.zara.net/assets/public/930d/e6c9/d8684f02ab5c/675e06873cd8/07484480800-e2/07484480800-e2.jpg?ts=1737018902566&w=344"),
    Wear("Zapatillas deportivas", 1.05f, "https://www.lefties.com/es/mujer/ropa/vestidos/mono-largo-c1030267514p659642900.html?colorId=712&parentId=660198311", 6, Brand.STRADIVARIUS, "https://static.lefties.com/assets/public/2c23/a986/0d93432b88f0/bfe692ab5159/01062300712-A6/01062300712-A6.jpg?ts=1737716993194&w=300&f=auto"),
    Wear("Cazadora cuero", 1.10f, "https://www.massimodutti.com/es/chaqueta-denim-cremallera-100-algodon-l02267068?pelement=48639352", 11, Brand.LEFTIES, "https://static.massimodutti.net/assets/public/f044/8f64/621643e2a574/516cb183df02/02267068405-o1/02267068405-o1.jpg?ts=1737994067683"),
    Wear("Sudadera con capucha", 1.15f, "https://www.bershka.com/es/jersey-cropped-c0p172274332.html?colorId=829", 16, Brand.OYSHO, "https://static.bershka.net/assets/public/6358/8c94/31a54aa98ec8/a5dc93f63ba1/02210376829-a4o/02210376829-a4o.jpg?ts=1733398296035&w=800"),
)

val wearsList = listOf(
    listOf(
        Wear("Cazadora cuero", 1.00f, "https://www.zara.com/es/es/cazadora-acolchada-lavada-p04575411.html?v1=417748086&v2=2436823", 1, Brand.LEFTIES, "https://static.zara.net/assets/public/4c4b/4dfa/f4bd489eb64f/357e4e190eab/01538412030-e1/01538412030-e1.jpg?ts=1740134060108&w=344"),
        Wear("Cazadora cuero", 1.00f, "https://www.zara.com/es/es/cazadora-acolchada-lavada-p04575411.html?v1=417748086&v2=2436823", 1, Brand.LEFTIES, "https://static.zara.net/assets/public/4c4b/4dfa/f4bd489eb64f/357e4e190eab/01538412030-e1/01538412030-e1.jpg?ts=1740134060108&w=344"),
        Wear("Cazadora cuero", 1.00f, "https://www.zara.com/es/es/cazadora-acolchada-lavada-p04575411.html?v1=417748086&v2=2436823", 1, Brand.LEFTIES, "https://static.zara.net/assets/public/4c4b/4dfa/f4bd489eb64f/357e4e190eab/01538412030-e1/01538412030-e1.jpg?ts=1740134060108&w=344"),
        Wear("Cazadora cuero", 1.00f, "https://www.zara.com/es/es/cazadora-acolchada-lavada-p04575411.html?v1=417748086&v2=2436823", 1, Brand.LEFTIES, "https://static.zara.net/assets/public/4c4b/4dfa/f4bd489eb64f/357e4e190eab/01538412030-e1/01538412030-e1.jpg?ts=1740134060108&w=344"),
        Wear("Cazadora cuero", 1.00f, "https://www.zara.com/es/es/cazadora-acolchada-lavada-p04575411.html?v1=417748086&v2=2436823", 1, Brand.LEFTIES, "https://static.zara.net/assets/public/4c4b/4dfa/f4bd489eb64f/357e4e190eab/01538412030-e1/01538412030-e1.jpg?ts=1740134060108&w=344"),
       ),

    listOf(
        Wear("Zapatillas deportivas", 1.05f, "https://www.lefties.com/es/mujer/ropa/vestidos/mono-largo-c1030267514p659642900.html?colorId=712&parentId=660198311", 6, Brand.STRADIVARIUS, "https://static.lefties.com/assets/public/2c23/a986/0d93432b88f0/bfe692ab5159/01062300712-A6/01062300712-A6.jpg?ts=1737716993194&w=300&f=auto"),
        Wear("Zapatillas deportivas", 1.06f, "https://www.lefties.com/es/mujer/ropa/vestidos/mono-largo-c1030267514p659642900.html?colorId=712&parentId=660198311", 7, Brand.STRADIVARIUS, "https://static.lefties.com/assets/public/2c23/a986/0d93432b88f0/bfe692ab5159/01062300712-A6/01062300712-A6.jpg?ts=1737716993194&w=300&f=auto"),
        Wear("Zapatillas deportivas", 1.07f, "https://www.lefties.com/es/mujer/ropa/vestidos/mono-largo-c1030267514p659642900.html?colorId=712&parentId=660198311", 8, Brand.STRADIVARIUS, "https://static.lefties.com/assets/public/2c23/a986/0d93432b88f0/bfe692ab5159/01062300712-A6/01062300712-A6.jpg?ts=1737716993194&w=300&f=auto"),
        Wear("Zapatillas deportivas", 1.08f, "https://www.lefties.com/es/mujer/ropa/vestidos/mono-largo-c1030267514p659642900.html?colorId=712&parentId=660198311", 9, Brand.STRADIVARIUS, "https://static.lefties.com/assets/public/2c23/a986/0d93432b88f0/bfe692ab5159/01062300712-A6/01062300712-A6.jpg?ts=1737716993194&w=300&f=auto"),
        Wear("Zapatillas deportivas", 1.09f, "https://www.lefties.com/es/mujer/ropa/vestidos/mono-largo-c1030267514p659642900.html?colorId=712&parentId=660198311", 10, Brand.STRADIVARIUS, "https://static.lefties.com/assets/public/2c23/a986/0d93432b88f0/bfe692ab5159/01062300712-A6/01062300712-A6.jpg?ts=1737716993194&w=300&f=auto"),
        ),

    listOf(
        Wear("Cazadora cuero", 1.10f, "https://www.massimodutti.com/es/chaqueta-denim-cremallera-100-algodon-l02267068?pelement=48639352", 11, Brand.ZARA, "https://static.massimodutti.net/assets/public/f044/8f64/621643e2a574/516cb183df02/02267068405-o1/02267068405-o1.jpg?ts=1737994067683"),
        Wear("Cazadora cuero", 1.11f, "https://www.massimodutti.com/es/chaqueta-denim-cremallera-100-algodon-l02267068?pelement=48639352", 12, Brand.ZARA, "https://static.massimodutti.net/assets/public/f044/8f64/621643e2a574/516cb183df02/02267068405-o1/02267068405-o1.jpg?ts=1737994067683"),
        Wear("Cazadora cuero", 1.12f, "https://www.massimodutti.com/es/chaqueta-denim-cremallera-100-algodon-l02267068?pelement=48639352", 13, Brand.ZARA, "https://static.massimodutti.net/assets/public/f044/8f64/621643e2a574/516cb183df02/02267068405-o1/02267068405-o1.jpg?ts=1737994067683"),
        Wear("Cazadora cuero", 1.13f, "https://www.massimodutti.com/es/chaqueta-denim-cremallera-100-algodon-l02267068?pelement=48639352", 14, Brand.ZARA, "https://static.massimodutti.net/assets/public/f044/8f64/621643e2a574/516cb183df02/02267068405-o1/02267068405-o1.jpg?ts=1737994067683"),
        Wear("Cazadora cuero", 1.14f, "https://www.massimodutti.com/es/chaqueta-denim-cremallera-100-algodon-l02267068?pelement=48639352", 15, Brand.ZARA, "https://static.massimodutti.net/assets/public/f044/8f64/621643e2a574/516cb183df02/02267068405-o1/02267068405-o1.jpg?ts=1737994067683"),
        ),

    listOf(
        Wear("Sudadera con capucha", 1.15f, "https://www.bershka.com/es/jersey-cropped-c0p172274332.html?colorId=829", 16, Brand.OYSHO, "https://static.bershka.net/assets/public/6358/8c94/31a54aa98ec8/a5dc93f63ba1/02210376829-a4o/02210376829-a4o.jpg?ts=1733398296035&w=800"),
        Wear("Sudadera con capucha", 1.16f, "https://www.bershka.com/es/jersey-cropped-c0p172274332.html?colorId=829", 17, Brand.OYSHO, "https://static.bershka.net/assets/public/6358/8c94/31a54aa98ec8/a5dc93f63ba1/02210376829-a4o/02210376829-a4o.jpg?ts=1733398296035&w=800"),
        Wear("Sudadera con capucha", 1.17f, "https://www.bershka.com/es/jersey-cropped-c0p172274332.html?colorId=829", 18, Brand.OYSHO, "https://static.bershka.net/assets/public/6358/8c94/31a54aa98ec8/a5dc93f63ba1/02210376829-a4o/02210376829-a4o.jpg?ts=1733398296035&w=800"),
        Wear("Sudadera con capucha", 1.18f, "https://www.bershka.com/es/jersey-cropped-c0p172274332.html?colorId=829", 19, Brand.OYSHO, "https://static.bershka.net/assets/public/6358/8c94/31a54aa98ec8/a5dc93f63ba1/02210376829-a4o/02210376829-a4o.jpg?ts=1733398296035&w=800"),
        Wear("Sudadera con capucha", 1.19f, "https://www.bershka.com/es/jersey-cropped-c0p172274332.html?colorId=829", 20, Brand.OYSHO, "https://static.bershka.net/assets/public/6358/8c94/31a54aa98ec8/a5dc93f63ba1/02210376829-a4o/02210376829-a4o.jpg?ts=1733398296035&w=800"),
    )
)