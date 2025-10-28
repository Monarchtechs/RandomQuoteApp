package com.example.internquoteapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.internquoteapp.ui.theme.InternquoteAppTheme

// Data class to represent a quote with its author
data class Quote(val text: String, val author: String)

// Expanded list of at least 20 quotes
private val quoteList = listOf(
    // Motivational Quotes
    Quote("The only way to do great work is to love what you do.", "Steve Jobs"),
    Quote("Believe you can and you're halfway there.", "Theodore Roosevelt"),
    Quote("The future belongs to those who believe in the beauty of their dreams.", "Eleanor Roosevelt"),
    Quote("Success is not final, failure is not fatal: it is the courage to continue that counts.", "Winston Churchill"),
    Quote("What you get by achieving your goals is not as important as what you become by achieving your goals.", "Zig Ziglar"),
    Quote("Act as if what you do makes a difference. It does.", "William James"),

    // Tech Quotes
    Quote("The advance of technology is based on making it fit in so that you don't even notice it, so it's part of everyday life.", "Bill Gates"),
    Quote("Technology is anything that wasn't around when you were born.", "Alan Kay"),
    Quote("Any sufficiently advanced technology is indistinguishable from magic.", "Arthur C. Clarke"),
    Quote("It's not a faith in technology. It's faith in people.", "Steve Jobs"),
    Quote("The Web as I envisaged it, we have not seen it yet. The future is still so much bigger than the past.", "Tim Berners-Lee"),
    Quote("Code is like humor. When you have to explain it, it’s bad.", "Cory House"),

    // Life Quotes
    Quote("The purpose of our lives is to be happy.", "Dalai Lama"),
    Quote("Life is what happens when you're busy making other plans.", "John Lennon"),
    Quote("Get busy living or get busy dying.", "Stephen King"),
    Quote("You only live once, but if you do it right, once is enough.", "Mae West"),
    Quote("Many of life's failures are people who did not realize how close they were to success when they gave up.", "Thomas A. Edison"),
    Quote("If you want to live a happy life, tie it to a goal, not to people or things.", "Albert Einstein"),
    Quote("Never let the fear of striking out keep you from playing the game.", "Babe Ruth"),
    Quote("The whole secret of a successful life is to find out what is one’s destiny to do, and then do it.", "Henry Ford")
)

// A few simple background colors for the transition effect
private val backgroundColors = listOf(
    Color(0xFFF0F4F8),
    Color(0xFFE6F0F3),
    Color(0xFFF8F0F4),
    Color(0xFFF4F8F0)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InternquoteAppTheme {
                QuoteScreen()
            }
        }
    }
}

@Composable
fun QuoteScreen() {
    // State for the current quote
    var currentQuote by remember { mutableStateOf(quoteList.random()) }
    // State for the background color
    var backgroundColor by remember { mutableStateOf(backgroundColors.random()) }

    // Animate the background color change
    val animatedBackgroundColor by animateColorAsState(targetValue = backgroundColor, label = "BackgroundColorAnimation")

    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = animatedBackgroundColor // Use the animated background color
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Card to display the quote
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                // AnimatedContent for fade-in/fade-out effect on text change
                AnimatedContent(
                    targetState = currentQuote,
                    transitionSpec = {
                        fadeIn(initialAlpha = 0.3f) togetherWith fadeOut()
                    },
                    label = "QuoteAnimation"
                ) { quote ->
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = "\"${quote.text}\"",
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "- ${quote.author}",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.End,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Row for the buttons
            Row {
                // Button to get a new random quote
                Button(onClick = {
                    currentQuote = quoteList.random()
                    backgroundColor = backgroundColors.random() // Change background color with the quote
                }) {
                    Text(text = "New Quote")
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Button to share the quote
                Button(onClick = {
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "\"${currentQuote.text}\" - ${currentQuote.author}")
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Quote Via"))
                }) {
                    Text(text = "Share Quote")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuoteScreenPreview() {
    InternquoteAppTheme {
        QuoteScreen()
    }
}
