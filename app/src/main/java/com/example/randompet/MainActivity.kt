package com.example.randompet

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.bumptech.glide.Glide
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import android.util.Log

import org.json.JSONArray
import com.bumptech.glide.load.engine.DiskCacheStrategy
import org.json.JSONObject
import cz.msebera.android.httpclient.Header

class MainActivity : ComponentActivity() {
    private val client = AsyncHttpClient()
    private var petImageURLs = listOf<String>()
    private val NUM_IMAGES = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val button: Button = findViewById(R.id.petButton)
        val imageView1: ImageView = findViewById(R.id.petImage1)
        val imageView2: ImageView = findViewById(R.id.petImage2)
        val imageView3: ImageView = findViewById(R.id.petImage3)

        button.setOnClickListener {
            getDogImages {
                if (petImageURLs.size >= NUM_IMAGES) {
                    Glide.with(this@MainActivity)
                        .load(petImageURLs[0])
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(imageView1)

                    Glide.with(this@MainActivity)
                        .load(petImageURLs[1])
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(imageView2)

                    Glide.with(this@MainActivity)
                        .load(petImageURLs[2])
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(imageView3)
                } else {
                    Toast.makeText(this@MainActivity, "Failed to load enough images", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel all pending requests when the activity is destroyed
        client.cancelAllRequests(true)
    }

    private fun getDogImages(callback: () -> Unit) {
        client.get("https://dog.ceo/api/breeds/image/random/$NUM_IMAGES", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                val jsonArray = response?.optJSONArray("message")
                petImageURLs = jsonArray?.let { array ->
                    List(array.length()) { index ->
                        array.getString(index)
                    }
                } ?: emptyList()

                Log.d("Dog", "Response successful$response")
                callback() // Execute the callback after fetching images
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, errorResponse: String?, throwable: Throwable?) {
                Toast.makeText(this@MainActivity, "Error fetching images", Toast.LENGTH_SHORT).show()
                Log.e("Dog Error", "Status code: $statusCode")
                Log.e("Dog Error", "Error response: $errorResponse")
                throwable?.printStackTrace()
            }
        })
    }
}











