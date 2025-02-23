package ochat.wearendar.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ochat.wearendar.R
import ochat.wearendar.backend.PriceAsFloatSerializer
import java.time.LocalTime


@Serializable
data class Wear(
    val name: String,
    @Serializable(with = PriceAsFloatSerializer::class)
    val price: Float,
    val url: String? = null,  // Ahora opcional
    val img: String? = null,   // Ahora opcional
    val id: Int,
    @SerialName("brand") val brand: Brand
)

enum class Brand (val value: String, val drawable: Int){
    @SerialName("lefties")LEFTIES("lefties", R.drawable.lefties),
    @SerialName("massimo_dutti")MASSIMO_DUTTI("massimo dutti", R.drawable.massimodutti),
    @SerialName("stradivarius")STRADIVARIUS("stradivarius", R.drawable.stradivarius),
    @SerialName("zara")ZARA("zara",R.drawable.zara),
    @SerialName("bershka")BERSHKA("bershka",R.drawable.bershka),
    @SerialName("pullandbear")PULLANDBEAR("pullandbear",R.drawable.pullbear),
}
