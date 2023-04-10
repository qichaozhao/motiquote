package com.example.motivate

import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.Fade
import android.transition.TransitionManager
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    private lateinit var quoteText: TextView
    private lateinit var backgroundImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        quoteText = findViewById(R.id.quote_text)
        backgroundImage = findViewById(R.id.background_image)

        val rootView = findViewById<View>(R.id.root_view)

        setNewQuote()

        rootView.setOnClickListener {
            changeQuoteWithFadeTransition()
        }
    }

    private fun changeQuoteWithFadeTransition() {
        val duration = 500L

        // Fade out
        quoteText.animate().alpha(0f).setDuration(duration).start()
        backgroundImage.animate().alpha(0f).setDuration(duration).start()

        // Wait for the fade-out animation to finish
        Handler(Looper.getMainLooper()).postDelayed({
            // Change the quote and image
            setNewQuote()

            // Fade in
            quoteText.animate().alpha(1f).setDuration(duration).start()
            backgroundImage.animate().alpha(1f).setDuration(duration).start()
        }, duration)
    }


    private fun setNewQuote() {
        val quoteList = loadQuotesFromJson()
        val randomQuote = getRandomQuote(quoteList)

        randomQuote?.let {
            quoteText.text = it.quote
            quoteText.typeface = Typeface.create(it.font_style, Typeface.NORMAL)
            quoteText.setTextSize(TypedValue.COMPLEX_UNIT_SP, it.font_size.toFloat())
            quoteText.x = it.x_position.toFloat()
            quoteText.y = it.y_position.toFloat()

            val imageResourceId = resources.getIdentifier(it.image_resource, "drawable", packageName)
            backgroundImage.setImageResource(imageResourceId)
        }
    }

    private fun loadQuotesFromJson(): QuoteList? {
        return try {
            val inputStream = assets.open("quotes.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val json = String(buffer, Charset.forName("UTF-8"))

            val gson = Gson()
            gson.fromJson(json, QuoteList::class.java)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun getRandomQuote(quoteList: QuoteList?): Quote? {
        return if (quoteList != null && quoteList.quotes.isNotEmpty()) {
            quoteList.quotes.random()
        } else {
            null
        }
    }
}

