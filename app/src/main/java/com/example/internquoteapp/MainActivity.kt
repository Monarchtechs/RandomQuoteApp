package com.example.internquoteapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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

// --- Data Layer ---

/**
 * Represents a single quote, containing the text and its author.
 */
data class Quote(val text: String, val author: String)

/**
 * Provides the data source for the application, including quotes and background colors.
 * Encapsulating this in an object makes the data source clear and easy to manage.
 */
object QuoteData {
    val quotes = listOf(
        Quote("The only way to do great work is to love what you do.", "Steve Jobs"),
        Quote("Believe you can and you're halfway there.", "Theodore Roosevelt"),
        Quote("The future belongs to those who believe in the beauty of their dreams.", "Eleanor Roosevelt"),
        Quote("Success is not final, failure is not fatal: it is the courage to continue that counts.", "Winston Churchill"),
        Quote("What you get by achieving your goals is not as important as what you become by achieving your goals.", "Zig Ziglar"),
        Quote("Act as if what you do makes a difference. It does.", "William James"),
        Quote("The advance of technology is based on making it fit in so that you don't even notice it, so it's part of everyday life.", "Bill Gates"),
        Quote("Technology is anything that wasn't around when you were born.", "Alan Kay"),
        Quote("Any sufficiently advanced technology is indistinguishable from magic.", "Arthur C. Clarke"),
        Quote("It's not a faith in technology. It's faith in people.", "Steve Jobs"),
        Quote("The Web as I envisaged it, we have not seen it yet. The future is still so much bigger than the past.", "Tim Berners-Lee"),
        Quote("Code is like humor. When you have to explain it, it’s bad.", "Cory House"),
        Quote("The purpose of our lives is to be happy.", "Dalai Lama"),
        Quote("Life is what happens when you're busy making other plans.", "John Lennon"),
        Quote("Get busy living or get busy dying.", "Stephen King"),
        Quote("You only live once, but if you do it right, once is enough.", "Mae West"),
        Quote("Many of life's failures are people who did not realize how close they were to success when they gave up.", "Thomas A. Edison"),
        Quote("If you want to live a happy life, tie it to a goal, not to people or things.", "Albert Einstein"),
        Quote("Never let the fear of striking out keep you from playing the game.", "Babe Ruth"),
        Quote("The whole secret of a successful life is to find out what is one’s destiny to do, and then do it.", "Henry Ford")
    )

    val backgroundColors = listOf(
        Color(0xFFF0F4F8),
        Color(0xFFE6F0F3),
        Color(0xFFF8F0F4),
        Color(0xFFF4F8F0)
    )
}

// --- App Entry Point ---

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

// --- UI Layer ---

/**
 * The main screen of the application.
 * Manages the state for the current quote and background color, and arranges the UI components.
 */
@Composable
fun QuoteScreen() {
    // State for the currently displayed quote
    var currentQuote by remember { mutableStateOf(QuoteData.quotes.random()) }
    // State for the background color, which changes with each new quote
    var backgroundColor by remember { mutableStateOf(QuoteData.backgroundColors.random()) }

    // Animate background color changes smoothly over 1000ms
    val animatedBackgroundColor by animateColorAsState(
        targetValue = backgroundColor,
        animationSpec = tween(durationMillis = 1000),
        label = "BackgroundColorAnimation"
    )

    val context = LocalContext.current

    /**
     * Creates and returns a share intent for the given quote.
     */
    fun getShareIntent(quote: Quote): Intent {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "\"${quote.text}\" - ${quote.author}")
            type = "text/plain"
        }
        return Intent.createChooser(sendIntent, "Share Quote Via")
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = animatedBackgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            QuoteCard(quote = currentQuote)

            Spacer(modifier = Modifier.height(32.dp))

            ActionButtons(
                onNewQuoteClick = {
                    currentQuote = QuoteData.quotes.random()
                    backgroundColor = QuoteData.backgroundColors.random()
                },
                onShareClick = {
                    context.startActivity(getShareIntent(currentQuote))
                }
            )
        }
    }
}

/**
 * A composable that displays a single quote inside a Card.
 * It features a fade-in/fade-out animation when the quote content changes.
 *
 * @param quote The quote to be displayed.
 */
@Composable
private fun QuoteCard(quote: Quote) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        AnimatedContent(
            targetState = quote,
            transitionSpec = {
                fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
            },
            label = "QuoteAnimation"
        ) { targetQuote ->
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "\"${targetQuote.text}\"",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "- ${targetQuote.author}",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.End,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

/**
 * A composable for the action buttons ("New Quote" and "Share Quote").
 * This component is stateless and relies on callbacks to handle user interactions.
 *
 * @param onNewQuoteClick Lambda to be invoked when the "New Quote" button is clicked.
 * @param onShareClick Lambda to be invoked when the "Share Quote" button is clicked.
 */
@Composable
private fun ActionButtons(
    onNewQuoteClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Row {
        Button(onClick = onNewQuoteClick) {
            Text(text = "New Quote")
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(onClick = onShareClick) {
            Text(text = "Share Quote")
        }
    }
}

// --- Previews ---

@Preview(showBackground = true)
@Composable
fun QuoteScreenPreview() {
    InternquoteAppTheme {
        QuoteScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun QuoteCardPreview() {
    InternquoteAppTheme {
        QuoteCard(quote = Quote("This is a preview quote.", "Preview Author"))
    }
}
