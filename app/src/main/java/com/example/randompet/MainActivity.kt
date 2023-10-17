package com.example.randompet

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import com.example.randompet.R

class MainActivity : ComponentActivity() {
    private val client = AsyncHttpClient() // Declaring the client here
    private lateinit var petAdapter: PetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val button: Button = findViewById(R.id.petButton)
        val petListView: RecyclerView = findViewById(R.id.pet_list)

        petAdapter = PetAdapter(emptyList())
        petListView.adapter = petAdapter
        petListView.layoutManager = LinearLayoutManager(this)

        button.setOnClickListener {
            getDogImages { pets ->
                if (pets.isNotEmpty()) {
                    petAdapter.updateData(pets)
                } else {
                    Toast.makeText(this@MainActivity, "Failed to load images", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getDogImages(callback: (List<Pet>) -> Unit) {
        client.get("https://dog.ceo/api/breeds/image/random/20", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                val jsonArray = response?.optJSONArray("message")
                val pets = jsonArray?.let { array ->
                    List(array.length()) { index ->
                        val imageUrl = array.getString(index)
                        val name = imageUrl.split("/").last().split(".")[0]
                        Pet(name, "Unknown", imageUrl)
                    }
                } ?: emptyList()
                Log.d("Dog", "Response successful $response")
                callback(pets)
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, throwable: Throwable?, errorResponse: JSONObject?) {
                Toast.makeText(this@MainActivity, "Error fetching images", Toast.LENGTH_SHORT).show()
                Log.e("Dog Error", "Status code: $statusCode")
                throwable?.printStackTrace()
            }
        })
    }
}
















