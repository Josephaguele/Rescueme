package com.example.rescueme

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import org.tensorflow.lite.Interpreter

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Create the location request
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000 // 10 seconds (adjust as needed)

        // Create the location callback
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    updateMarker(location)
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
        //createNotificationChannel()


    }

    private fun updateMarker(location: Location) {
        // Add a marker at the current location
        val currentLatLng = LatLng(location.latitude, location.longitude)

        mMap.clear()
        mMap.addMarker(
            MarkerOptions()
                .position(currentLatLng)
                .title("Current Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)) // Set marker icon here
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))

        // Create input data for the model
       val latitude = location.latitude.toFloat()
        val longitude = location.longitude.toFloat()
    /* val latitude = 59.0977972f
        val longitude = 7.1641361f*/
        val conditions = CustomModelDownloadConditions.Builder()
            .build()
        FirebaseModelDownloader.getInstance()
            .getModel("rescueisolation", DownloadType.LOCAL_MODEL, conditions)
            .addOnSuccessListener { model ->
                // Download complete. Create an interpreter and run inference
                val modelFile = model?.file
                try{ if (modelFile != null){

                    val interpreter = Interpreter(modelFile)

                    /* val input = Array(1) { FloatArray(2) }
                     input[0][0] = latitude
                     input[0][1] = longitude

                     val output = Array(1) { FloatArray(2) }
                     interpreter.run(input, output)


                     val result = output[0][0]*/
                    val input = floatArrayOf(latitude, longitude)
                    val output = Array(1) { FloatArray(1) }

                    interpreter.run(input, output)

                    val result = output[0][0] // Extract the prediction value


                    if (result > THRESHOLD) {
                        makeNotification()
                    }
                }

                } catch( e: Exception){
                    e.printStackTrace()
            }

            }
    }


    private fun makeNotification(){
        var channelID = "CHANNEL_ID_NOTIFICATION";
        var builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, channelID)
        builder.setSmallIcon(R.drawable.notification_important)
            .setContentTitle("It looks like you have not been here before")
            .setAutoCancel(false)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        var intent = Intent(applicationContext,TestActivity::class.java);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("data", "Just a test");

        var pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_MUTABLE)
        builder.setContentIntent(pendingIntent)
        var notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            var notificationChannel = notificationManager.getNotificationChannel(channelID)
            if(notificationChannel == null){
                var importance :Int = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = NotificationChannel(channelID, "some description", importance)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(true)
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }

        notificationManager.notify(0, builder.build())
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Check location permission
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        } else {
            startLocationUpdates()
        }
    }

    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Start receiving location updates
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            }
        }
    }



    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
        private const val CHANNEL_ID = "UnsafeLocationChannel"
        private const val NOTIFICATION_ID = 1
        private const val THRESHOLD = 0.000000001
        private const val UNKNOWN_AREA_THRESHOLD = 0.001
    }
}
