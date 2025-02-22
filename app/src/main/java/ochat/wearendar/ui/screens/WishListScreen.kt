package ochat.wearendar.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ochat.wearendar.data.Wear
import ochat.wearendar.ui.theme.MontserratFontFamily
import ochat.wearendar.ui.theme.WearendarTheme
import ochat.wearendar.utils.openUrl
import ochat.wearendar.utils.wears

@Preview
@Composable
fun WishListPreview(){
    WearendarTheme {
        WishListScreen()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WishListScreen() {

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
            ) {
                WishListView()
            }
        }
    }
}

@Composable
fun WishListView() {
    val listState = rememberLazyListState()
    val wearsList = remember { mutableStateListOf(*wears.toTypedArray()) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "LISTA DE DESEOS",
                fontFamily = MontserratFontFamily,
                fontStyle = FontStyle.Normal,
                fontSize = 24.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                color = Color.Black
            )

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                items(wears, key = { it.id }) { wear ->
                    var isRemoved by remember { mutableStateOf(false) }
                    val offsetX by animateDpAsState(
                        targetValue = if (isRemoved) (-300).dp else 0.dp,
                        animationSpec = tween(durationMillis = 300),
                        finishedListener = {
                            wearsList.remove(wear)
                        }, label = "wears"
                    )

                    AnimatedVisibility(
                        visible = !isRemoved,
                        exit = fadeOut(animationSpec = tween(300))
                    ) {
                        WishListItem(
                            wear = wear,
                            onRemove = { isRemoved = true },
                            modifier = Modifier.offset(x = offsetX)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WishListItem(wear: Wear, onRemove: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .height(86.dp)
            .border(1.dp, Color.Black)
            .clickable{ openUrl(context = context, wear.url)},
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(0.5f)
                .padding(8.dp)

        ) {
            Text(
                text = wear.name,
                fontSize = 18.sp,
                fontFamily = MontserratFontFamily
            )
            Text(
                text = "${wear.price}€",
                fontSize = 18.sp,
                fontFamily = MontserratFontFamily
            )
        }

        Row(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {

            Image(
                painter = painterResource(id = wear.brand.drawable),
                contentDescription = "Brand Logo",
                modifier = Modifier
                    .width(64.dp)
                    .graphicsLayer(rotationZ = -90f)

            )

            AsyncImage(
                model = wear.img,
                contentDescription = "Product Image",
                modifier = Modifier
                    .fillMaxHeight()
            )

            Box(
                modifier = Modifier
                    .width(64.dp)
                    .fillMaxHeight()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { onRemove() },
                    shape = RoundedCornerShape(0.dp),
                    modifier = Modifier.fillMaxSize(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                ) {
                    Text(
                        text = "X",
                        fontFamily = MontserratFontFamily,
                        fontStyle = FontStyle.Normal,
                        fontSize = 18.sp,
                    )
                }
            }
        }
    }
}