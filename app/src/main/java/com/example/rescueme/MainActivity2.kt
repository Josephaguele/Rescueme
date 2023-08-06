package com.example.rescueme

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.tensorbuffer.TensorBufferFloat
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
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


 /*   private fun loadModelFile(assetManager: AssetManager, modelPath: String): ByteBuffer {
        val assetFileDescriptor: AssetFileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel: FileChannel = inputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }*/
    /*private fun doInference(latitude: Float, longitude: Float): Float {
        val inputVal = ByteBuffer.allocateDirect(4 * 2) // Allocate a direct ByteBuffer
        inputVal.order(ByteOrder.nativeOrder()) // Set the byte order
        inputVal.putFloat(latitude) // Put latitude value
        inputVal.putFloat(longitude) // Put longitude value

        val outputVal = ByteBuffer.allocateDirect(4 * 2) // Allocate a direct ByteBuffer for output
        outputVal.order(ByteOrder.nativeOrder()) // Set the byte order

        tflite.run(inputVal, outputVal) // Run the inference

        outputVal.rewind() // Rewind the output ByteBuffer
        return outputVal.float // Retrieve the output value
    }*/






/*private fun checkAnomaly(): Boolean {
    // Load the TensorFlow Lite model
    val modelBuffer = loadModelBuffer(this, "autoencoder_model.tflite")
    val interpreter = org.tensorflow.lite.Interpreter(modelBuffer)

    // Prepare input data
    val inputFeatureShape = intArrayOf(1, 2)
    val inputFeatureDataType = DataType.FLOAT32
    val inputFeatureBuffer = TensorBufferFloat.createFixedSize(inputFeatureShape, inputFeatureDataType)

    // Set input data

    val latitudeData = latitudeText.text.toString().toFloat()
    val longitudeData = longitudeText.text.toString().toFloat()
    val locationData = floatArrayOf(latitudeData, longitudeData)  // Example location data
    inputFeatureBuffer.loadArray(locationData)

    // Run inference
    val outputFeatureShape = interpreter.getOutputTensor(0).shape()
    val outputFeatureDataType = interpreter.getOutputTensor(0).dataType()
    val outputFeatureBuffer = TensorBufferFloat.createFixedSize(outputFeatureShape, outputFeatureDataType)

    interpreter.run(inputFeatureBuffer.buffer, outputFeatureBuffer.buffer)

    // Calculate the MSE between input and output
    val mse = outputFeatureBuffer.floatArray.average()

    // Set the threshold for anomaly detection (you can adjust this threshold as needed)
    val threshold = 0.05f

    Log.i("MSE", "$mse")
    // Return true if anomaly, false if normal
    return mse > threshold
}*/

/* private fun loadModelBuffer(context: Context, modelPath: String): ByteBuffer {
     val inputStream: InputStream = context.assets.open(modelPath)
     val modelBytes = inputStream.readBytes()
     val buffer = ByteBuffer.allocateDirect(modelBytes.size)
     buffer.order(ByteOrder.nativeOrder())
     buffer.put(modelBytes)
     buffer.rewind()
     return buffer
 }*/


/*
class MainActivity2 : AppCompatActivity() {

    private lateinit var interpreter: Interpreter
    private lateinit var textResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        textResult = findViewById(R.id.textResult)

        // Load the TensorFlow Lite model
        interpreter = loadModelFile(assets, "autoencoder_model.tflite")

    }


    fun onPredictButtonClicked(view: View) {
        // Create input tensor buffer
        val inputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 3), DataType.FLOAT32)

        // Set input data
        // You need to provide your own input data here
        val inputData = floatArrayOf(36.70146f, -118.755997f, 232f)
        inputBuffer.loadArray(inputData)

        // Run inference
        val outputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 1), DataType.FLOAT32)
        interpreter.run(inputBuffer.buffer, outputBuffer.buffer)

        // Get the inference result
        val prediction = outputBuffer.floatArray[0]
        val predictionInInteger = prediction.toInt()

        Log.i("TAG","prediction 1 is : ${prediction.toString()}")

        // Display the prediction result
        if (predictionInInteger == -1) {
            textResult.text = "unsafe destination"
        } else {
            textResult.text = "safe destination"
        }


    }

    private fun loadModelFile(assetManager: AssetManager, modelPath: String): Interpreter {
        val fileDescriptor: AssetFileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel: FileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        val mappedByteBuffer: MappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        return Interpreter(mappedByteBuffer)
    }

}
*/


/*
class MainActivity2 : AppCompatActivity() {
    private var buttonPredict: Button? = null
    private var textPrediction: TextView? = null
    private var modelHelper: IsolationForestModelHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the Button and TextView
        buttonPredict = findViewById(R.id.buttonPredict)
        textPrediction = findViewById(R.id.textResult)



        // Initialize the IsolationForestModelHelper and load the model
        modelHelper = IsolationForestModelHelper()
        modelHelper!!.loadModel(applicationContext)

        // Set the click listener for the Predict button
        buttonPredict!!.setOnClickListener { // Call the predict function and display the outcome in the TextView
            val prediction = predictOutcome()
            displayPrediction(prediction)
        }
    }

    private fun predictOutcome(): Float {
        // Replace with your logic to get input features (latitude, longitude, altitude)
        val latitude = -33.869843f
        val longitude = 151.20828f
        val altitude = 26f
        val inputFeatures = floatArrayOf(latitude, longitude, altitude)
        return modelHelper!!.predict(inputFeatures)
    }

    private fun displayPrediction(prediction: Float) {
        val outcome = if (prediction.equals(-1)) "Unsafe" else "Safe"
        textPrediction!!.text = "Outcome: $outcome"
    }

    override fun onDestroy() {
        super.onDestroy();
        // Close the model and release resources
        modelHelper?.close();
    }
}*/
