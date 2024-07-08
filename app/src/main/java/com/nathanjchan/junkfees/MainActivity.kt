package com.nathanjchan.junkfees

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import java.util.*

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

@OptIn(ExperimentalMaterial3Api::class, MapboxExperimental::class)
@Composable
fun MapWithBottomSheet() {
    val sheetPeekHeight = 48.dp
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            BottomSheetContent()
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
fun BottomSheetContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Recently Reported",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline
            )
        Spacer(modifier = Modifier.height(16.dp))
        // Add your menu items here
        BottomSheetItem("Liholiho Yacht Club", "400 Eddy St, San Francisco, CA 94109",
            Timestamp(System.currentTimeMillis()), 0.25f)
        BottomSheetItem("Liholiho Yacht Club", "400 Eddy St, San Francisco, CA 94109",
            Timestamp(System.currentTimeMillis()), 0.25f)
        BottomSheetItem("Liholiho Yacht Club", "400 Eddy St, San Francisco, CA 94109",
            Timestamp(System.currentTimeMillis()), 0.25f)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

fun lastUpdatedText(timestamp: Timestamp): String {
    val currentTime = Calendar.getInstance().timeInMillis
    val elapsedTime = currentTime - timestamp.time
    val timeAgoText = when {
        elapsedTime < 60 * 60 * 1000 -> {
            val minutesAgo = elapsedTime / (60 * 1000)
            if (minutesAgo == 1L) "1 minute ago" else "$minutesAgo minutes ago"
        }
        elapsedTime < 24 * 60 * 60 * 1000 -> {
            val hoursAgo = elapsedTime / (60 * 60 * 1000)
            if (hoursAgo == 1L) "1 hour ago" else "$hoursAgo hours ago"
        }
        else -> {
            val daysAgo = elapsedTime / (24 * 60 * 60 * 1000)
            if (daysAgo == 1L) "1 day ago" else "$daysAgo days ago"
        }
    }
    return timeAgoText
}

@Composable
fun BottomSheetItem(title: String, description: String, timestamp: Timestamp, fee: Float) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
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
                Text(title, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(description, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Reported ${lastUpdatedText(timestamp)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            // Right column
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "${"%.0f".format(fee * 100)}%",
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
fun BottomSheetItemPreview() {
    JunkFeesTheme {
        BottomSheetItem("Liholiho Yacht Club", "400 Eddy St, San Francisco, CA 94109",
            Timestamp(System.currentTimeMillis()), 0.25f)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JunkFeesTheme {
        MapWithBottomSheet()
    }
}