package ochat.wearendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ochat.wearendar.ui.NavGraph
import ochat.wearendar.ui.theme.WearendarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WearendarTheme {
                NavGraph()
            }
        }
    }
}
