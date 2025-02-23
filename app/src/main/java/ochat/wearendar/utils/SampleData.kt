package ochat.wearendar.utils

import ochat.wearendar.data.Brand
import ochat.wearendar.data.Event
import ochat.wearendar.data.EventType
import ochat.wearendar.data.Wear
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

val eventMap = mapOf(
    LocalDate.of(2025, 2, 20) to listOf(
        Event("2",
            "Reunión de trabajo",
            "Discusión sobre el nuevo proyecto y planificación de tareas con el equipo.",
            LocalDateTime.of(2025, 2, 20, 10, 0),
            LocalDateTime.of(2025, 2, 20, 11, 30),
            "Oficina central",
            EventType.FORMALLY
        ),
        Event("3",
            "Cena con amigos",
            "Cena en un restaurante italiano con el grupo de siempre.",
            LocalDateTime.of(2025, 2, 20, 20, 30),
            LocalDateTime.of(2025, 2, 20, 23, 0),
            "Restaurante Trattoria",
            EventType.CASUAL
        )
    ),

    LocalDate.of(2025, 2, 21) to listOf(
        Event("4",
            "Sesión de gimnasio",
            "Entrenamiento de fuerza y cardio para mantenerse en forma.",
            LocalDateTime.of(2025, 2, 21, 7, 0),
            LocalDateTime.of(2025, 2, 21, 8, 30),
            "Gimnasio FitLife",
            EventType.SPORTIVE
        ),
        Event("5",
            "Tarde de cine",
            "Ver la última película de Marvel con palomitas y refresco.",
            LocalDateTime.of(2025, 2, 21, 18, 0),
            LocalDateTime.of(2025, 2, 21, 20, 30),
            "Cine Yelmo",
            EventType.CASUAL
        )
    ),

    LocalDate.of(2025, 2, 22) to listOf(
        Event("6",
            "Fiesta de cumpleaños",
            "Celebración del cumpleaños de Ana con música y karaoke.",
            LocalDateTime.of(2025, 2, 22, 21, 0),
            LocalDateTime.of(2025, 2, 23, 2, 0),
            "Casa de Ana",
            EventType.FESTIVE
        )
    ),

    LocalDate.of(2025, 2, 23) to listOf(
        Event("7",
            "Desayuno con la shory hiperbellaka",
            "Disfruta de un desayuno especial con la shory en una acogedora cafetería del centro de Madrid. Croissants, café y buena conversación.",
            LocalDateTime.of(2025, 2, 23, 9, 0),
            LocalDateTime.of(2025, 2, 23, 10, 30),
            "Madrid",
            EventType.SMARTLY
        ),
        Event("8",
            "Tarde de compras",
            "Recorrido por tiendas en busca de ropa nueva para la temporada.",
            LocalDateTime.of(2025, 2, 23, 17, 0),
            LocalDateTime.of(2025, 2, 23, 19, 0),
            "Centro comercial Gran Vía",
            EventType.CASUAL
        )
    ),

    LocalDate.of(2025, 2, 24) to listOf(
        Event("9",
            "Entrevista de trabajo",
            "Entrevista para el puesto de Desarrollador Full-Stack en una startup.",
            LocalDateTime.of(2025, 2, 24, 11, 0),
            LocalDateTime.of(2025, 2, 24, 12, 0),
            "Oficinas de TechHub",
            EventType.FORMALLY
        )
    ),

    LocalDate.of(2025, 2, 25) to listOf(
        Event("10",
            "Clases de inglés",
            "Clase avanzada de inglés enfocada en conversación y pronunciación.",
            LocalDateTime.of(2025, 2, 25, 18, 0),
            LocalDateTime.of(2025, 2, 25, 19, 30),
            "Academia Oxford",
            EventType.CASUAL
        ),
        Event("11",
            "Cena romántica",
            "Cena especial en un restaurante de lujo con la pareja.",
            LocalDateTime.of(2025, 2, 25, 21, 0),
            LocalDateTime.of(2025, 2, 25, 23, 0),
            "Restaurante El Cielo",
            EventType.SMARTLY
        )
    ),

    LocalDate.of(2025, 2, 26) to listOf(
        Event("12",
            "Día de home office",
            "Trabajo remoto desde casa con reuniones por videollamada.",
            LocalDateTime.of(2025, 2, 26, 9, 0),
            LocalDateTime.of(2025, 2, 26, 18, 0),
            "Casa",
            EventType.CASUAL
        )
    ),

    LocalDate.of(2025, 2, 27) to listOf(
        Event("13",
            "Visita al médico",
            "Chequeo de rutina con el doctor.",
            LocalDateTime.of(2025, 2, 27, 10, 0),
            LocalDateTime.of(2025, 2, 27, 11, 0),
            "Clínica Sanitas",
            EventType.CASUAL
        ),
        Event("14",
            "Concierto de rock",
            "Asistir al concierto de una banda legendaria.",
            LocalDateTime.of(2025, 2, 27, 20, 0),
            LocalDateTime.of(2025, 2, 27, 23, 0),
            "WiZink Center",
            EventType.FESTIVE
        )
    ),

    LocalDate.of(2025, 2, 28) to listOf(
        Event("15",
            "Salida a la montaña",
            "Excursión a la montaña con amigos, con picnic incluido.",
            LocalDateTime.of(2025, 2, 28, 7, 0),
            LocalDateTime.of(2025, 2, 28, 17, 0),
            "Sierra de Madrid",
            EventType.SPORTIVE
        ),
        Event("16",
            "Torneo de videojuegos",
            "Competencia amistosa de Super Smash Bros con premios para los ganadores.",
            LocalDateTime.of(2025, 2, 28, 19, 0),
            LocalDateTime.of(2025, 2, 28, 22, 0),
            "Casa de Jorge",
            EventType.CASUAL
        )
    ),

    LocalDate.of(2025, 3, 1) to listOf(
        Event("17",
            "Boda de un amigo",
            "Celebración de la boda de Daniel y Laura con fiesta hasta la madrugada.",
            LocalDateTime.of(2025, 3, 1, 17, 0),
            LocalDateTime.of(2025, 3, 2, 3, 0),
            "Finca La Encantada",
            EventType.FORMALLY
        )
    ),

    LocalDate.of(2025, 3, 2) to listOf(
        Event("18",
            "Tarde de lectura",
            "Momento de relax con un libro y un café en casa.",
            LocalDateTime.of(2025, 3, 2, 16, 0),
            LocalDateTime.of(2025, 3, 2, 18, 0),
            "Casa",
            EventType.CASUAL
        )
    ),

    LocalDate.of(2025, 3, 3) to listOf(
        Event("19",
            "Partido de fútbol",
            "Jugar un partido con el equipo del barrio.",
            LocalDateTime.of(2025, 3, 3, 19, 0),
            LocalDateTime.of(2025, 3, 3, 21, 0),
            "Polideportivo Municipal",
            EventType.SPORTIVE
        )
    ),

    LocalDate.of(2025, 3, 4) to listOf(
        Event("20",
            "Día de spa",
            "Un día completo de relajación con masajes y jacuzzi.",
            LocalDateTime.of(2025, 3, 4, 11, 0),
            LocalDateTime.of(2025, 3, 4, 15, 0),
            "Spa Zen",
            EventType.SMARTLY
        )
    )
)


val wears = listOf(
    Wear("Cazadora bomber efecto ante", 19.99f, "https://www.zara.com/es/es/cazadora-bomber-efecto-ante-p08574300.html?v1=410581055&v2=2436823", "https://static.zara.net/assets/public/7580/5976/39c24fc3b643/032c38cc1568/08574300505-e1/08574300505-e1.jpg?ts=1733819688145&w=563", 1, Brand.ZARA, ),
    Wear("Jeans Slim Fit", 25.95f, "https://www.zara.com/es/es/jeans-slim-fit-p00774340.html?v1=410570241&v2=2436823", "https://static.zara.net/assets/public/3173/0190/07c344ef9f4a/4b9018f986e7/00774340427-e1/00774340427-e1.jpg?ts=1722593597920&w=563", 2, Brand.ZARA),
    Wear("Sudadera Básica Cuello Cremallera", 27.95f, "https://www.zara.com/es/es/sudadera-basica-cuello-cremallera-p06462333.html?v1=410585694&v2=2436823", "https://static.zara.net/assets/public/5665/2d69/b481493d8351/ef5648cb3cc8/06462333251-e1/06462333251-e1.jpg?ts=1739197197802&w=563", 3, Brand.ZARA),
    Wear("Pantalón Cargo", 29.95f, "https://www.zara.com/es/es/pantalon-cargo-p00108302.html?v1=410575276&v2=2436823", "https://static.zara.net/assets/public/512f/37d8/f89f4c51be02/1455655cea68/00108302506-a1/00108302506-a1.jpg?ts=1722445703370&w=750", 4, Brand.ZARA),
    Wear("Sudadera Heavy Weigth", 39.95f, "https://www.zara.com/es/es/sudadera-heavy-weight-cremallera-limited-edition-p04393414.html?v1=423709813&v2=2436823", "https://static.zara.net/assets/public/5abc/0b12/8600442f9648/c04ee0ef127d/04393414600-a1/04393414600-a1.jpg?ts=1738063253344&w=750", 5, Brand.ZARA),
    )



var wearsList = listOf(
    listOf(
        Wear("Camisa Cuadros", 59.95f, "https://www.massimodutti.com/es/camisa-cuadros-relaxed-fit-l00167707?pelement=47753799", "https://static.massimodutti.net/assets/public/ac3e/a079/09df430d9ad6/f850014a2721/00167707700-o1/00167707700-o1.jpg?ts=1739457597457&w=850", 6, Brand.MASSIMO_DUTTI),
        Wear("Jeans Slim Fit", 49.95f, "https://www.massimodutti.com/es/camisa-regular-fit-mezcla-algodon-l00151717?pelement=45845006", "https://static.massimodutti.net/assets/public/4b4b/9c04/b076487781c2/5cc5922bda99/00151717403-o1/00151717403-o1.jpg?ts=1737638212715&w=850", 7, Brand.MASSIMO_DUTTI),
        Wear("Pantalón balloon", 39.99f, "https://www.bershka.com/es/pantal%C3%B3n-balloon-print-c0p180124636.html?colorId=712&stylismId=16", "https://static.bershka.net/assets/public/18c0/fb8d/e174465d9550/ced91d9987b6/00415054712-16-a4o/00415054712-16-a4o.jpg?ts=1740068669958&w=800", 8, Brand.BERSHKA),
        Wear("Camisa Sakamoto Store", 29.95f, "https://www.pullandbear.com/es/camiseta-sakamoto-store-l03245539?cS=251&pelement=664248899", "https://static.pullandbear.net/assets/public/9416/cc9b/817a46989659/342502564392/03245539251-a20m/03245539251-a20m.jpg?ts=1737122644100&w=882&f=auto", 9, Brand.PULLANDBEAR),
        Wear("Sudadera Heavy Weigth", 39.95f, "https://www.massimodutti.com/es/chaqueta-piel-napa-efecto-desgastado-l03375576?pelement=48134911", "https://static.massimodutti.net/assets/public/7f7d/89cd/12d74f14908a/2a57177f090e/03375576700-o1/03375576700-o1.jpg?ts=1737638395118&w=850", 10, Brand.MASSIMO_DUTTI),
        ),

    listOf(
        Wear("Cazadora bomber efecto ante", 19.99f, "https://www.pullandbear.com/es/camiseta-sakamoto-store-l03245539?cS=251&pelement=664248899", "https://static.pullandbear.net/assets/public/9416/cc9b/817a46989659/342502564392/03245539251-a20m/03245539251-a20m.jpg?ts=1737122644100&w=882&f=auto", 1, Brand.BERSHKA, ),
        Wear("Cazadora bomber efecto ante", 19.99f, "https://www.pullandbear.com/es/camiseta-sakamoto-store-l03245539?cS=251&pelement=664248899", "https://static.pullandbear.net/assets/public/9416/cc9b/817a46989659/342502564392/03245539251-a20m/03245539251-a20m.jpg?ts=1737122644100&w=882&f=auto", 1, Brand.ZARA, ),
        Wear("Cazadora bomber efecto ante", 19.99f, "https://www.pullandbear.com/es/camiseta-sakamoto-store-l03245539?cS=251&pelement=664248899", "https://static.pullandbear.net/assets/public/9416/cc9b/817a46989659/342502564392/03245539251-a20m/03245539251-a20m.jpg?ts=1737122644100&w=882&f=auto", 1, Brand.ZARA, ),
        Wear("Cazadora bomber efecto ante", 19.99f, "https://www.pullandbear.com/es/camiseta-sakamoto-store-l03245539?cS=251&pelement=664248899", "https://static.pullandbear.net/assets/public/9416/cc9b/817a46989659/342502564392/03245539251-a20m/03245539251-a20m.jpg?ts=1737122644100&w=882&f=auto", 1, Brand.ZARA, ),
        Wear("Cazadora bomber efecto ante", 19.99f, "https://www.pullandbear.com/es/camiseta-sakamoto-store-l03245539?cS=251&pelement=664248899", "https://static.pullandbear.net/assets/public/9416/cc9b/817a46989659/342502564392/03245539251-a20m/03245539251-a20m.jpg?ts=1737122644100&w=882&f=auto", 1, Brand.ZARA, ),

        ),

    listOf(
        Wear("Jeans Slim Fit", 25.95f, "https://www.zara.com/es/es/jeans-slim-fit-p00774340.html?v1=410570241&v2=2436823", "https://static.zara.net/assets/public/3173/0190/07c344ef9f4a/4b9018f986e7/00774340427-e1/00774340427-e1.jpg?ts=1722593597920&w=563", 2, Brand.ZARA),
        Wear("Sudadera Básica Cuello Cremallera", 27.95f, "https://www.zara.com/es/es/sudadera-basica-cuello-cremallera-p06462333.html?v1=410585694&v2=2436823", "https://static.zara.net/assets/public/5665/2d69/b481493d8351/ef5648cb3cc8/06462333251-e1/06462333251-e1.jpg?ts=1739197197802&w=563", 3, Brand.PULLANDBEAR),
        Wear("Sudadera Básica Cuello Cremallera", 27.95f, "https://www.zara.com/es/es/sudadera-basica-cuello-cremallera-p06462333.html?v1=410585694&v2=2436823", "https://static.zara.net/assets/public/5665/2d69/b481493d8351/ef5648cb3cc8/06462333251-e1/06462333251-e1.jpg?ts=1739197197802&w=563", 3, Brand.ZARA),
        Wear("Sudadera Básica Cuello Cremallera", 27.95f, "https://www.zara.com/es/es/sudadera-basica-cuello-cremallera-p06462333.html?v1=410585694&v2=2436823", "https://static.zara.net/assets/public/5665/2d69/b481493d8351/ef5648cb3cc8/06462333251-e1/06462333251-e1.jpg?ts=1739197197802&w=563", 3, Brand.ZARA),
        Wear("Sudadera Básica Cuello Cremallera", 27.95f, "https://www.zara.com/es/es/sudadera-basica-cuello-cremallera-p06462333.html?v1=410585694&v2=2436823", "https://static.zara.net/assets/public/5665/2d69/b481493d8351/ef5648cb3cc8/06462333251-e1/06462333251-e1.jpg?ts=1739197197802&w=563", 3, Brand.ZARA),
        ),

    listOf(
        Wear("Pantalón Cargo", 29.95f, "https://www.zara.com/es/es/pantalon-cargo-p00108302.html?v1=410575276&v2=2436823", "https://static.zara.net/assets/public/512f/37d8/f89f4c51be02/1455655cea68/00108302506-a1/00108302506-a1.jpg?ts=1722445703370&w=750", 4, Brand.LEFTIES),
        Wear("Pantalón Cargo", 29.95f, "https://www.zara.com/es/es/pantalon-cargo-p00108302.html?v1=410575276&v2=2436823", "https://static.zara.net/assets/public/512f/37d8/f89f4c51be02/1455655cea68/00108302506-a1/00108302506-a1.jpg?ts=1722445703370&w=750", 4, Brand.ZARA),
        Wear("Pantalón Cargo", 29.95f, "https://www.zara.com/es/es/pantalon-cargo-p00108302.html?v1=410575276&v2=2436823", "https://static.zara.net/assets/public/512f/37d8/f89f4c51be02/1455655cea68/00108302506-a1/00108302506-a1.jpg?ts=1722445703370&w=750", 4, Brand.ZARA),
        Wear("Pantalón Cargo", 29.95f, "https://www.zara.com/es/es/pantalon-cargo-p00108302.html?v1=410575276&v2=2436823", "https://static.zara.net/assets/public/512f/37d8/f89f4c51be02/1455655cea68/00108302506-a1/00108302506-a1.jpg?ts=1722445703370&w=750", 4, Brand.ZARA),
        Wear("Pantalón Cargo", 29.95f, "https://www.zara.com/es/es/pantalon-cargo-p00108302.html?v1=410575276&v2=2436823", "https://static.zara.net/assets/public/512f/37d8/f89f4c51be02/1455655cea68/00108302506-a1/00108302506-a1.jpg?ts=1722445703370&w=750", 4, Brand.ZARA),

        )
)