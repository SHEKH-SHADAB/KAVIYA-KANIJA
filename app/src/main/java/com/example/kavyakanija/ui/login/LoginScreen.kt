package com.example.kavyakanija.ui.login

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kavyakanija.ui.theme.EmeraldGreen
import com.example.kavyakanija.ui.theme.PremiumGradientEnd
import com.example.kavyakanija.ui.theme.PremiumGradientStart

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Animated Background logic
    val infiniteTransition = rememberInfiniteTransition(label = "bg")
    val animOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2000f,
        animationSpec = infiniteRepeatable(
            animation = tween(40000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset"
    )

    val gradient = Brush.linearGradient(
        colors = listOf(PremiumGradientStart, PremiumGradientEnd, EmeraldGreen, PremiumGradientStart),
        start = androidx.compose.ui.geometry.Offset(animOffset, 0f),
        end = androidx.compose.ui.geometry.Offset(animOffset + 1000f, 1000f)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        // Subtle floating decorative circles
        FloatingCircles(infiniteTransition)

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo with Glow
                Box(contentAlignment = Alignment.Center) {
                    Surface(
                        color = Color.White.copy(alpha = 0.15f),
                        shape = CircleShape,
                        modifier = Modifier.size(120.dp).shadow(24.dp, CircleShape, spotColor = Color.White)
                    ) {}
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        modifier = Modifier.size(56.dp),
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "ಕಾವ್ಯ ಕಣಜ",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = (-1).sp
                    )
                )
                Text(
                    text = "A Journey Through Verse",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White.copy(alpha = 0.8f),
                    letterSpacing = 4.sp
                )

                Spacer(modifier = Modifier.height(64.dp))

                // Premium Glass Input Card
                Surface(
                    color = Color.White.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(32.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TextField(
                            value = email,
                            onValueChange = { email = it },
                            placeholder = { Text("Email Address", color = Color.White.copy(alpha = 0.5f)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White.copy(alpha = 0.1f),
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            leadingIcon = { Icon(Icons.Default.Email, null, tint = Color.White.copy(alpha = 0.7f)) }
                        )

                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = { Text("Password", color = Color.White.copy(alpha = 0.5f)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            visualTransformation = PasswordVisualTransformation(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White.copy(alpha = 0.1f),
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            leadingIcon = { Icon(Icons.Default.Lock, null, tint = Color.White.copy(alpha = 0.7f)) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                Button(
                    onClick = onLoginSuccess,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .shadow(16.dp, RoundedCornerShape(24.dp), spotColor = Color.White.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = EmeraldGreen
                    )
                ) {
                    Text(
                        "START EXPLORING",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                TextButton(onClick = onLoginSuccess) {
                    Text(
                        "Continue as Guest",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun FloatingCircles(transition: InfiniteTransition) {
    val offset1 by transition.animateFloat(
        initialValue = 0f, targetValue = 100f,
        animationSpec = infiniteRepeatable(tween(5000), RepeatMode.Reverse), label = ""
    )
    val offset2 by transition.animateFloat(
        initialValue = 0f, targetValue = -80f,
        animationSpec = infiniteRepeatable(tween(7000), RepeatMode.Reverse), label = ""
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier.size(300.dp).offset(x = (-100).dp + offset1.dp, y = (-100).dp + offset2.dp),
            color = Color.White.copy(alpha = 0.05f),
            shape = CircleShape
        ) {}
        Surface(
            modifier = Modifier.size(200.dp).align(Alignment.BottomEnd).offset(x = 50.dp + offset2.dp, y = 50.dp + offset1.dp),
            color = Color.White.copy(alpha = 0.05f),
            shape = CircleShape
        ) {}
    }
}
