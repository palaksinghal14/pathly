package com.example.pathly.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun StartScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFFFFF), // White
                        Color(0xFFE3F0FF), // Light blue
                        Color(0xFFD1C4E9)  // Light purple
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 48.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Pathly",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Path to your next opportunity",
                    fontSize = 18.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.weight(2f))
            Button(
                onClick = {  },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(text = "Get Started", fontSize = 18.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    // Dummy NavController for preview
    // You may need to adjust this if you have a custom NavController provider
    // or use androidx.navigation.compose.rememberNavController()
    // For now, we use a lambda to avoid errors in preview
    StartScreen()
}
