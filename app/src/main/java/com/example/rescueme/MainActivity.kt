package com.example.rescueme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class MainActivity : AppCompatActivity() {

    private lateinit var informationImageView: ImageView
    private lateinit var locationImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         locationImageView = findViewById(R.id.location_imageView)
        informationImageView = findViewById(R.id.information_imageView)

        locationImageView.setOnClickListener{

            var intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }
}