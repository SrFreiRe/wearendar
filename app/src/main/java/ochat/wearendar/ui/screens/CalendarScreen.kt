package ochat.wearendar.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.wajahatkarim.flippable.Flippable
import com.wajahatkarim.flippable.rememberFlipController
import kotlinx.coroutines.delay
import ochat.wearendar.data.Event
import ochat.wearendar.data.Wear
import ochat.wearendar.ui.theme.MontserratFontFamily
import ochat.wearendar.ui.theme.WearendarTheme
import ochat.wearendar.utils.eventMap
import ochat.wearendar.utils.formatDate
import ochat.wearendar.utils.openUrl
import ochat.wearendar.utils.wearsList
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Preview
@Composable
fun CalendarPreview(){
    WearendarTheme {
        CalendarScreen()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CalendarScreen() {

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
                MonthView()
            }
        }
    }
}

@Composable
fun MonthView(){

    // PRE CALCULATIONS
    val today = LocalDate.now()
    val columnCount = 7

    val startMonth = today.minusYears(1).withDayOfMonth(1)
    val months = (0 until 25).map { startMonth.plusMonths(it.toLong()) }

    val allDays = mutableListOf<LocalDate?>()
    val monthLabels = mutableMapOf<Int, String>()

    // DAYS ARRAY CREATION
    months.forEach { month ->
        val monthDays = (1..month.lengthOfMonth()).map { month.withDayOfMonth(it) }
        val firstDayWeekday = (monthDays.first().dayOfWeek.value + 6) % 7

        val labelIndex = allDays.size
        monthLabels[labelIndex] = month.month.name.lowercase()
            .replaceFirstChar { it.uppercase() } + " " + month.year

        val lastColumn = allDays.size % columnCount
        if (lastColumn > 0) {
            repeat(columnCount - lastColumn) { allDays.add(null) }
        }

        repeat(firstDayWeekday) { allDays.add(null) }

        allDays.addAll(monthDays)

        val lastColumnAfterMonth = allDays.size % columnCount
        if (lastColumnAfterMonth > 0) {
            repeat(columnCount - lastColumnAfterMonth) { allDays.add(null) }
        }
    }

    var expandedDay by remember { mutableStateOf<LocalDate?>(null) }
    var clickedPosition by remember { mutableStateOf<Offset?>(null) }
    var monthCardSize by remember { mutableStateOf<IntSize?>(null) }
    var monthCardOffset by remember { mutableStateOf<Offset?>(null) }

    val dayPositions = remember { mutableStateMapOf<LocalDate, Offset>() }
    val listState = rememberLazyListState()

    val startOfCurrentMonthIndex = monthLabels.keys.firstOrNull { key ->
        allDays.getOrNull(key)?.month == today.month && allDays.getOrNull(key)?.year == today.year
    } ?: allDays.indexOfFirst { it?.month == today.month && it?.year == today.year }

    LaunchedEffect(startOfCurrentMonthIndex) {
        val correctedRowIndex = startOfCurrentMonthIndex / columnCount
        listState.scrollToItem(correctedRowIndex)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .onGloballyPositioned { coordinates ->
            monthCardOffset = coordinates.positionInRoot()
            monthCardSize = coordinates.size
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "CALENDAR",
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
                modifier = Modifier.fillMaxSize(),

                ) {
                val weeks = allDays.chunked(columnCount)

                // MONTH LABELS
                itemsIndexed(weeks) { index, week ->
                    monthLabels[index * columnCount]?.let {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(start = 8.dp, bottom = 8.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = it.uppercase(),
                                fontFamily = MontserratFontFamily,
                                fontStyle = FontStyle.Normal,
                                fontSize = 20.sp,
                                color = Color.Black
                            )
                        }
                    }

                    // WEEKS
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        week.forEach { day ->
                            if (day == null) {
                                Spacer(modifier = Modifier.weight(1f).aspectRatio(1f))
                            } else {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .alpha(if (expandedDay == day) 0f else 1f)
                                        .onGloballyPositioned { coordinates ->
                                            monthCardOffset?.let { boxOffset ->
                                                val absolutePosition = coordinates.boundsInRoot().topLeft
                                                val relativePosition = absolutePosition - boxOffset
                                                dayPositions[day] = relativePosition
                                            }
                                        }
                                        .clickable {
                                            clickedPosition = dayPositions[day]
                                            expandedDay = day
                                        }
                                        .border(1.dp, Color.Black)
                                        .background(if(day == today) Color.Black else Color.White)
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${day.dayOfMonth}",
                                            color = if(day == today) Color.White else Color.Black,
                                            fontFamily = MontserratFontFamily,
                                            fontStyle = FontStyle.Normal,
                                            fontSize = 18.sp,
                                        )
                                    }
                                }
                            }
                        }
                    }

                    val isLastRowOfMonth = week.any { it != null && it.dayOfMonth == it.month.length(it.isLeapYear) }

                    if (isLastRowOfMonth) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            repeat(columnCount) {
                                Spacer(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(2f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    
    if (expandedDay != null && clickedPosition != null && monthCardSize != null) {
        DayView(
            day = expandedDay!!,
            clickedPosition = clickedPosition!!,
            monthSize = monthCardSize!!,
            events = eventMap[expandedDay!!] ?: emptyList(),
            onBack = {
                expandedDay = null
                clickedPosition = null
            }
        )
    }
}


@Composable
fun DayView(
    day: LocalDate,
    clickedPosition: Offset,
    monthSize: IntSize,
    events: List<Event>,
    onBack: () -> Unit
) {
    var moveToCenterStarted by remember { mutableStateOf(false) }
    var sizeAnimationStarted by remember { mutableStateOf(false) }
    var isClosing by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }
    var showContent by remember { mutableStateOf(false) }

    var selectedEvent by rememberSaveable { mutableStateOf<Event?>(null) }
    var eventCardHeight by remember { mutableStateOf(0) }

    val eventPositions = remember { mutableStateMapOf<Event, Offset>() }

        // OFFSET CALCULATIONS
    val density = LocalDensity.current
    val columnSpacing = 8.dp
    val columnSpacingPx = with(density) { columnSpacing.toPx() }
    val dayWidthPx = (monthSize.width - (columnSpacingPx * (7 - 1))) / 7f
    val dayWidth = with(density) { dayWidthPx.toDp() }

    val initialXOffsetDp = with(density) { clickedPosition.x.toDp() }
    val initialYOffsetDp = with(density) { clickedPosition.y.toDp() }

    val monthSizeX = with(density) { (monthSize.width).toDp() }
    val monthSizeY = with(density) { (monthSize.height).toDp() }

    val centerX = with(density) { (monthSize.width / 2).toDp() - (dayWidth / 2) }
    val centerY = with(density) { (monthSize.height / 2).toDp() - (dayWidth / 2) }

    // ANIMATIONS
    val offsetX by animateDpAsState(
        targetValue = if (moveToCenterStarted) centerX else initialXOffsetDp,
        animationSpec = tween(durationMillis = 100, easing = FastOutSlowInEasing),
        label = "OffsetX"
    )

    val offsetY by animateDpAsState(
        targetValue = if (moveToCenterStarted) centerY else initialYOffsetDp,
        animationSpec = tween(durationMillis = 100, easing = FastOutSlowInEasing),
        label = "OffsetY"
    )

    val width by animateDpAsState(
        targetValue = if (sizeAnimationStarted) monthSizeX else dayWidth,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        finishedListener = { if (!isClosing) showContent = true },
        label = "WidthAnimation"
    )

    val height by animateDpAsState(
        targetValue = if (sizeAnimationStarted) monthSizeY else dayWidth,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "HeightAnimation"
    )

    val adjustedOffsetX by animateDpAsState(
        targetValue = if (sizeAnimationStarted) 0.dp else offsetX,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "AdjustedOffsetX"
    )

    val adjustedOffsetY by animateDpAsState(
        targetValue = if (sizeAnimationStarted) 0.dp else offsetY,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "AdjustedOffsetY"
    )

    LaunchedEffect(Unit) {
        Log.d("ExpandingDayView", "Moving to center for day $day from position: $clickedPosition")
        moveToCenterStarted = true
        delay(300)

        Log.d("ExpandingDayView", "Expanding day view for $day")
        sizeAnimationStarted = true
        delay(300)

        isExpanded = true
    }

    LaunchedEffect(isClosing) {
        if (isClosing) {
            showContent = false
            isExpanded = false
            sizeAnimationStarted = false
            delay(300)
            moveToCenterStarted = false
            delay(300)
            onBack()
        }
    }

    // COMPOSE
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = adjustedOffsetX, y = adjustedOffsetY)
                .size(
                    if (!isExpanded) width else monthSizeX ,
                    if (!isExpanded) height else monthSizeY - 32.dp
                )
                .graphicsLayer(scaleX = 1f, scaleY = 1f, shape = RoundedCornerShape(16.dp))
                .clickable {
                    isClosing = true
                }
                .background(Color.White)
                .border(1.dp, Color.Black),
        ) {

            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(animationSpec = tween(200)) +
                        slideInVertically(initialOffsetY = { it / 4 }, animationSpec = tween(200)),
                exit = fadeOut(animationSpec = tween(200)) +
                        slideOutVertically(targetOffsetY = { it / 4 }, animationSpec = tween(200))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    Text(
                        text = day.formatDate().uppercase(),
                        fontFamily = MontserratFontFamily,
                        fontStyle = FontStyle.Normal,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, start = 16.dp, bottom = 8.dp),
                        color = Color.Black
                    )

                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(events) { event ->
                            val isEventSelected = selectedEvent == event

                            EventCard(
                                event,
                                onEventClick = { height, coordinates ->
                                    selectedEvent = event
                                    eventCardHeight = height
                                    eventPositions[event] = coordinates },
                                    alpha = if (isEventSelected) 0f else 1f
                            )
                        }
                    }
                }
            }
        }
        selectedEvent?.let { event ->

            val monthSizeXPx = with(density) { monthSizeX.toPx().toInt() }
            val monthSizeYPx = with(density) { (monthSizeY - 32.dp).toPx().toInt() }
            val daySize = IntSize(monthSizeXPx, monthSizeYPx)

            val eventClickedPosition = eventPositions[event] ?: Offset.Zero

            EventView(
                event = event,
                clickedPosition = eventClickedPosition,
                daySize = daySize,
                eventHeight = eventCardHeight,
                onDismiss = { selectedEvent = null }
            )
        }
    }
}

@Composable
fun EventCard(event: Event, alpha: Float, onEventClick: (Int, Offset) -> Unit) {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    var cardHeight by remember { mutableStateOf(0) }
    var position by remember { mutableStateOf(Offset.Zero) }

    val borderWidth = if (alpha == 1f) 1.dp else 0.dp
    val borderColor = if (alpha == 1f) Color.Black else Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color.White)
            .border(borderWidth, borderColor)
            .alpha(alpha)
            .onGloballyPositioned { layoutCoordinates ->
                cardHeight = layoutCoordinates.size.height
                position = layoutCoordinates.positionInRoot()
            }
            .clickable {
                val correctedPosition = position.copy(y = position.y - cardHeight) // ✅ Adjust Y position
                onEventClick(cardHeight, correctedPosition)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(event.type.color),
            )

            Text(
                text = event.title.uppercase(),
                fontFamily = MontserratFontFamily,
                fontStyle = FontStyle.Normal,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                color = Color.Black
            )

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = event.startTime.format(timeFormatter),
                    fontFamily = MontserratFontFamily,
                    fontStyle = FontStyle.Normal,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = event.endTime.format(timeFormatter),
                    fontFamily = MontserratFontFamily,
                    fontStyle = FontStyle.Normal,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun EventView(event: Event, clickedPosition: Offset, daySize: IntSize, eventHeight: Int, onDismiss: () -> Unit) {
    var moveToCenterStarted by remember { mutableStateOf(false) }
    var sizeAnimationStarted by remember { mutableStateOf(false) }
    var isClosing by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }
    val flipController =  rememberFlipController()
    var isFlipped by remember { mutableStateOf(false) }

    // OFFSET CALCULATIONS
    val density = LocalDensity.current


    val initialHeightDp = with(density) { eventHeight.toDp() }

    val initialYOffsetDp = with(density) { clickedPosition.y.toDp() }

    val dayWidth = with(density) { daySize.width.toDp() }
    val dayHeight = with(density) { daySize.height.toDp() }

    val centerY = with(density) { (dayHeight / 2) - (initialHeightDp / 2) }

    // ANIMATIONS
    val offsetY by animateDpAsState(
        targetValue = if (moveToCenterStarted) centerY else initialYOffsetDp,
        animationSpec = tween(durationMillis = 100, easing = FastOutSlowInEasing),
        label = "OffsetY"
    )

    val height by animateDpAsState(
        targetValue = if (sizeAnimationStarted) dayHeight else initialHeightDp,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "HeightAnimation"
    )

    val adjustedOffsetY by animateDpAsState(
        targetValue = if (sizeAnimationStarted) 0.dp else offsetY,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "AdjustedOffsetY"
    )

    LaunchedEffect(Unit) {
        moveToCenterStarted = true
        delay(300)
        sizeAnimationStarted = true
        delay(300)
        isExpanded = true
    }

    LaunchedEffect(isClosing) {
        if (isClosing) {
            isExpanded = false
            sizeAnimationStarted = false
            delay(300)
            moveToCenterStarted = false
            delay(300)
            onDismiss()
        }
    }
    val borderColor = if (isExpanded) Color.Transparent else Color.Black
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clickable { isFlipped = !isFlipped }
        ,
    ) {
        Box(
            modifier = Modifier
                .offset(y = adjustedOffsetY)
                .size(
                    width = dayWidth,
                    if (!isExpanded) height else dayHeight - 32.dp
                )
                .graphicsLayer(scaleX = 1f, scaleY = 1f, shape = RoundedCornerShape(16.dp))
                .clickable {
                    isClosing = true
                }
                .border(1.dp, borderColor)
                .background(Color.White)
                .alpha(1f),
        ) {
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn(animationSpec = tween(200)) +
                        slideInVertically(initialOffsetY = { it / 4 }, animationSpec = tween(200)),
                exit = fadeOut(animationSpec = tween(200)) +
                        slideOutVertically(targetOffsetY = { it / 4 }, animationSpec = tween(200))
            ) {

                // LLAMADA
                val wearsList = wearsList

                Flippable(
                    frontSide = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .border(1.dp, Color.Black)
                        ) {
                            FrontSideContent(event)
                        }
                    },
                    backSide = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .border(1.dp, Color.Black)
                        ) {
                            BackSideContent(onBack = { isClosing = true }, wearsList, isFlipped = isFlipped)
                        }
                    },
                    flipController = flipController,
                    flipDurationMs = 300,
                    cameraDistance = 200f,
                    onFlippedListener = { isFlipped = !isFlipped }
                )
            }
        }
    }
}

@Composable
fun FrontSideContent(event: Event){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = event.title.uppercase(),
            fontFamily = MontserratFontFamily,
            fontSize = 20.sp,
            fontStyle = FontStyle.Normal,
            color = Color.Black,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = event.description,
            fontFamily = MontserratFontFamily,
            fontSize = 18.sp,
            fontStyle = FontStyle.Normal,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "Start: ${event.startTime.format(DateTimeFormatter.ofPattern("HH:mm"))}",
            fontFamily = MontserratFontFamily,
            fontSize = 16.sp,
            fontStyle = FontStyle.Normal,
            color = Color.Black,
        )

        Text(
            text = "End: ${event.endTime.format(DateTimeFormatter.ofPattern("HH:mm"))}",
            fontFamily = MontserratFontFamily,
            fontSize = 16.sp,
            fontStyle = FontStyle.Normal,
            color = Color.Black
        )

        Text(
            text = "Location: O Grove",
            fontFamily = MontserratFontFamily,
            fontSize = 16.sp,
            fontStyle = FontStyle.Normal,
            color = Color.Black
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BackSideContent(onBack: () -> Unit, wears: List<List<Wear>>, isFlipped: Boolean) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 🔹 First Row (80% of height) - Contains 2 Columns (Each with 2 Vertical Pagers)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f)
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 🔹 First Column (2 VerticalPagers inside)
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val pagerState1 = rememberPagerState { wears[0].size }
                val pagerState2 = rememberPagerState { wears[1].size }

                VerticalPager(
                    state = pagerState1,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                ) { page ->
                    WearItemView(wear = wears[0][page], isFlipped)
                }

                VerticalPager(
                    state = pagerState2,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                ) { page ->
                    WearItemView(
                        wear = wears[1][page], isFlipped = isFlipped
                    )
                }
            }

            // 🔹 Second Column (2 VerticalPagers inside)
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val pagerState3 = rememberPagerState { wears[2].size }
                val pagerState4 = rememberPagerState { wears[3].size }

                VerticalPager(
                    state = pagerState3,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                ) { page ->
                    WearItemView(wear = wears[2][page], isFlipped)
                }

                VerticalPager(
                    state = pagerState4,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                ) { page ->
                    WearItemView(wear = wears[3][page], isFlipped)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { onBack() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(0.dp),
                ) {
                    Text(
                        text = "REGEN",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = MontserratFontFamily,
                    )
                }

                Spacer(modifier = Modifier.size(8.dp))

                Button(
                    onClick = { onBack() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(0.dp),
                ) {
                    Text(
                        text = "CLOSE",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = MontserratFontFamily,
                    )
                }
            }
        }
    }
}

@Composable
fun WearItemView(wear: Wear, isFlipped: Boolean, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .then(if (isFlipped) Modifier.clickable { openUrl(context, wear.url) } else Modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = wear.img,
            contentDescription = wear.name,
            modifier = Modifier
                .weight(0.8f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Image(
            painter = painterResource(id = wear.brand.drawable),
            contentDescription = wear.brand.name,
            modifier = Modifier
                .weight(0.3f)
        )

        Text(
            text = "${wear.price}€",
            fontSize = 20.sp,
            fontFamily = MontserratFontFamily,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(Alignment.CenterVertically)
                .wrapContentWidth(Alignment.CenterHorizontally)
                .weight(0.2f)
        )
    }
}

