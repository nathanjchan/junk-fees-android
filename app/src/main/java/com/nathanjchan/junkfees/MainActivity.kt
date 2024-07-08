package com.nathanjchan.junkfees

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.nathanjchan.junkfees.ui.theme.JunkFeesTheme
import java.sql.Timestamp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JunkFeesTheme {
                MapWithBottomSheet()
            }
        }
    }
}

data class Place(
    val id: String,
    val title: String,
    val description: String,
    val timestamp: Timestamp,
    val fee: Float
)
val places = listOf(
    Place("1", "Liholiho Yacht Club", "400 Eddy St, San Francisco, CA 94109",
        Timestamp(System.currentTimeMillis()), 0.15f),
    Place("2", "Chittychitty Boat Barn", "123 Main St, San Francisco, CA 94109",
        Timestamp(System.currentTimeMillis()), 0.06f),
    Place("3", "Rizzy Rowing Company", "987 Elm St, San Francisco, CA 94109",
        Timestamp(System.currentTimeMillis()), 0.10f),
)

@OptIn(ExperimentalMaterial3Api::class, MapboxExperimental::class)
@Composable
fun MapWithBottomSheet() {
    val sheetPeekHeight = 48.dp
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            BottomSheetContent(places)
        },
        sheetPeekHeight = sheetPeekHeight
    ) {
        MapboxMap(
            Modifier.fillMaxSize(),
            mapViewportState = MapViewportState().apply {
                setCameraOptions {
                    zoom(11.0)
                    // San Francisco coordinates
                    center(Point.fromLngLat(-122.4194, 37.7749))
                    pitch(0.0)
                    bearing(0.0)
                }
            },
        )
    }
}

@Composable
fun BottomSheetContent(data: List<Place>) {
    var selectedPlaceId by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Recently Reported",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.height(16.dp))
        data.forEach { place ->
            PlaceItem(
                place = place,
                onClick = { selectedPlaceId = place.id }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceItem(place: Place, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left column
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(place.title, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(place.description, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Reported ${lastUpdatedText(place.timestamp)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            // Right column
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "${"%.0f".format(place.fee * 100)}%",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Junk Fee",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PlaceItemPreview() {
    JunkFeesTheme {
        PlaceItem(places[0]) {}
    }
}

@Preview(showBackground = true)
@Composable
fun MapWithBottomSheetPreview() {
    JunkFeesTheme {
        MapWithBottomSheet()
    }
}