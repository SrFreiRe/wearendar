package ochat.wearendar.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ochat.wearendar.R
import java.time.LocalTime

@Serializable
data class Wear(
    val name: String,
    val price: Float,
    val url: String,
    val id: Int,
    @SerialName("brand") val brand: Brand,
    val img: String,
)

enum class Brand (val value: String, val drawable: Int){
    @SerialName("lefties")LEFTIES("lefties", R.drawable.lefties),
    @SerialName("massimo_dutti")MASSIMO_DUTTI("massimo dutti", R.drawable.massimodutti),
    @SerialName("stradivarius")STRADIVARIUS("stradivarius", R.drawable.stradivarius),
    @SerialName("zara")ZARA("zara",R.drawable.zara),
}
