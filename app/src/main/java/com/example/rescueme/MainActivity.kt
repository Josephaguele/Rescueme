package com.example.rescueme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class MainActivity : AppCompatActivity() {

    private lateinit var informationImageView: ImageView
    private lateinit var locationImageView: ImageView
    private lateinit var emergencyView: ImageView
    private lateinit var aboutView : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         locationImageView = findViewById(R.id.location_imageView)
        informationImageView = findViewById(R.id.information_imageView)
        emergencyView = findViewById(R.id.emergencyImageView)
        aboutView = findViewById(R.id.aboutImageView)
        locationImageView.setOnClickListener{

            var intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        informationImageView.setOnClickListener{
            var intent = Intent(this, InformationActivity::class.java)
            startActivity(intent)
        }

        emergencyView.setOnClickListener{

        }

        aboutView.setOnClickListener{
            var intent = Intent(this, About::class.java)
                startActivity(intent)
        }

    }
}