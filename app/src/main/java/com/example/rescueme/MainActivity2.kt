/*
package com.example.rescueme

import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.tensorflow.lite.Interpreter
import android.os.Bundle
import com.google.firebase.FirebaseApp
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader

class MainActivity2 : AppCompatActivity() {
    private lateinit var buttonPredict: Button
    private lateinit var textViewResult: TextView
    private lateinit var editTextLatitude: EditText
    private lateinit var editTextLongitude: EditText
    private lateinit var isAnomalytext: TextView
    private lateinit var tflite: Interpreter
    private val THRESHOLD = 0.486668898

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        FirebaseApp.initializeApp(applicationContext)
        buttonPredict = findViewById(R.id.buttonPredict)
        textViewResult = findViewById(R.id.textResult)
        editTextLatitude = findViewById(R.id.latitude)
        editTextLongitude = findViewById(R.id.longitude)
        isAnomalytext = findViewById(R.id.isAnomaly)
        // Initialize FirebaseApp
        buttonPredict.setOnClickListener {
            val latitude = editTextLatitude.text.toString().toFloat()
            val longitude = editTextLongitude.text.toString().toFloat()

            val conditions = CustomModelDownloadConditions.Builder()
                .requireWifi()
                .build()
            FirebaseModelDownloader.getInstance()
                .getModel("rescueisolation", DownloadType.LOCAL_MODEL, conditions)
                .addOnSuccessListener { model ->
                    // Download complete. Depending on your app, you could enable the ML
                    // feature, or switch from the local model to the remote model, etc.
                    // Create an interpreter with the downloaded model
                    val interpreter = Interpreter(model.file!!)

                    // Create input data as a 2D array of floats with shape [1, 2]
                    val input = Array(1) { FloatArray(2) }
                    input[0][0] = latitude
                    input[0][1] = longitude

                    // Create output buffer
                    val output = Array(1) { FloatArray(2) }

                    // Run inference
                    interpreter.run(input, output)

                    // Get the result
                    val result = output[0][0]

                    // Use the result to determine if the place is safe or unsafe
                    if (result > THRESHOLD) {
                        isAnomalytext.text = "Place is safe"
                    } else {
                        isAnomalytext.text = "Place is unsafe"
                    }
                }
        }
    }
}







*/
