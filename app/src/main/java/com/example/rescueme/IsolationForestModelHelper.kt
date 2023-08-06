package com.example.rescueme

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel


class IsolationForestModelHelper {
    private var tfliteInterpreter: Interpreter? = null
    fun loadModel(context: Context) {
        try {
            // Load the TFLite model from the assets folder
            val assetManager = context.assets
            val modelBuffer = loadModelFile(assetManager)

            // Create the TFLite interpreter
            val options = Interpreter.Options()
            tfliteInterpreter = Interpreter(modelBuffer, options)
        } catch (e: IOException) {
            Log.e("IsolationForestModel", "Failed to load TFLite model: " + e.message)
        }
    }

    @Throws(IOException::class)
    private fun loadModelFile(assetManager: AssetManager): MappedByteBuffer {
        val fileDescriptor = assetManager.openFd(MODEL_PATH)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun predict(inputFeatures: FloatArray?): Float {
        val outputScores = FloatArray(1)
        try {
            // Create a TensorBuffer for the input features
            val inputBuffer =
                TensorBuffer.createFixedSize(intArrayOf(1, NUM_FEATURES), DataType.FLOAT32)
            inputBuffer.loadArray(inputFeatures)

            // Run the inference
            tfliteInterpreter!!.run(inputBuffer.buffer, outputScores)
        } catch (e: Exception) {
            Log.e("IsolationForestModel", "Failed to make predictions: " + e.message)
        }
        return outputScores[0]
    }

    fun close() {
        if (tfliteInterpreter != null) {
            tfliteInterpreter!!.close()
            tfliteInterpreter = null
        }
    }

    companion object {
        private const val MODEL_PATH = "isolation2_forest_model.tflite"
        private const val NUM_FEATURES = 3
    }
}
