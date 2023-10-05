package com.example.randompet

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.ComponentActivity
import com.bumptech.glide.Glide
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import android.util.Log
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import com.bumptech.glide.load.engine.DiskCacheStrategy

class MainActivity : ComponentActivity() {
    private val client = AsyncHttpClient()
    var petImageURLs = listOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val button: Button = findViewById(R.id.petButton)
        val imageView1: ImageView = findViewById(R.id.petImage1)
        val imageView2: ImageView = findViewById(R.id.petImage2)
        val imageView3: ImageView = findViewById(R.id.petImage3)

        button.setOnClickListener {
            getDogImages {
                if (petImageURLs.size >= 3) {
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
        client.get("https://dog.ceo/api/breeds/image/random/3", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONArray?) {
                petImageURLs = response?.let { array ->
                    List(array.length()) { index ->
                        array.getString(index)
                    }
                } ?: emptyList()

                Log.d("Dog", "Response successful$response")
                callback() // Execute the callback after fetching images
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, errorResponse: String?, throwable: Throwable?) {
                Log.e("Dog Error", "Status code: $statusCode")
                Log.e("Dog Error", "Error response: $errorResponse")
                throwable?.printStackTrace()
            }
        })
    }
}










