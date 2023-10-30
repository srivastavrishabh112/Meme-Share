package com.harsh.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar

import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import org.json.JSONException

class MainActivity : AppCompatActivity() {
   private lateinit var imageView: ImageView
    lateinit var progressBar: ProgressBar
    lateinit var share: Button
   lateinit var next: Button
   lateinit var  imgurl: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.image)
        share = findViewById(R.id.share)
        next = findViewById(R.id.next)
        progressBar = findViewById(R.id.progressBar)
        fetchmemes()
        next.setOnClickListener(View.OnClickListener { fetchmemes() })
        share.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "Checkout this amazing meme \n$imgurl")
            startActivity(Intent.createChooser(intent, "Share this meme using"))
        })
    }

    private fun fetchmemes() {

// ...
        progressBar!!.visibility = View.VISIBLE
        // Instantiate the RequestQueue.
        val queue: RequestQueue
        queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.com/gimme"

// Request a string response from the provided URL.
        val req = JsonObjectRequest(
            Request.Method.GET, url, null,
            { obj ->
                try {
                    imgurl = obj.getString("url")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                Glide.with(this@MainActivity).load(imgurl)
                    .listener(object : RequestListener<Drawable?> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable?>,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar!!.visibility = View.GONE
                            //                                Toast.makeText(MainActivity.this,"Unable to load Meme Check Your Internet",Toast.LENGTH_LONG).show();
                            return false
                        }


                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable?>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar!!.visibility = View.GONE
                            return false
                        }
                    }).into(imageView!!)
            }) { }
        queue.add(req)
    }
}