package ochat.wearendar.data

import ochat.wearendar.R
import java.time.LocalTime

data class Wear(
    val name: String,
    val price: Float,
    val url: String,
    val id: Int,
    val brand: Brand,
    val img: Int,
)

enum class Brand (val drawable: Int){
    LEFTIES(R.drawable.lefties),
    MASSIMO_DUTTI(R.drawable.massimodutti),
    OYSHO(R.drawable.oysho),
    PULL_AND_BEAR(R.drawable.pullbear),
    STRADIVARIUS(R.drawable.stradivarius),
    ZARA(R.drawable.zara),
    ZARA_HOME(R.drawable.zarahome)
}
