/*
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.rescueme.IsolationForestModelHelper
import com.example.rescueme.R
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.view.View
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class MainActivity : AppCompatActivity() {

    private lateinit var interpreter: Interpreter
    private lateinit var textResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        textResult = findViewById(R.id.textResult)

        // Load the TensorFlow Lite model
        interpreter = loadModelFile(assets, "ml/isolation_forest_model.tflite")
    }

    fun onPredictButtonClicked(view: View) {
        // Create input tensor buffer
        val inputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 3), DataType.FLOAT32)

        // Set input data
        // You need to provide your own input data here
        val inputData = floatArrayOf(48.85888f, 2.3200f, 2628f)
        inputBuffer.loadArray(inputData)

        // Run inference
        val outputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 1), DataType.FLOAT32)
        interpreter.run(inputBuffer.buffer, outputBuffer.buffer)

        // Get the inference result
        val prediction = outputBuffer.floatArray[0]

        // Display the prediction result
        if (prediction < 0) {
            textResult.text = "City is unsafe"
        } else {
            textResult.text = "City is safe"
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
class MainActivity : AppCompatActivity() {
    private var buttonPredict: Button? = null
    private var textPrediction: TextView? = null
    private var modelHelper: IsolationForestModelHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the Button and TextView
        buttonPredict = findViewById(R.id.buttonPredict)
        textPrediction = findViewById(R.id.textPrediction)



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
        val latitude = 51.4063964f
        val longitude = -2.6467239f
        val altitude = 2628f
        val inputFeatures = floatArrayOf(latitude, longitude, altitude)
        return modelHelper!!.predict(inputFeatures)
    }

    private fun displayPrediction(prediction: Float) {
        val outcome = if (prediction < 0) "Unsafe" else "Safe"
        textPrediction!!.text = "Outcome: $outcome"
    }

    override fun onDestroy() {
        super.onDestroy();
        // Close the model and release resources
        modelHelper?.close();
    }
}*/

