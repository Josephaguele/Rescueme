package com.example.rescueme

import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room.databaseBuilder
import com.example.rescueme.ui.AppDatabase
class InformationActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var saveButton: Button
    private var appDatabase: AppDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)
        nameEditText = findViewById(R.id.name_input)
        saveButton = findViewById(R.id.button)

        appDatabase = databaseBuilder<AppDatabase>(
            applicationContext,
            AppDatabase::class.java, "app-database"
        ).build()

        saveButton.setOnClickListener {
            val userInput: String = nameEditText.text.toString()
            val userText = UserText()
            userText.text = userInput

            // Insert the text into the database
            InsertUserTextTask().execute(userText)
        }
    }

    inner class InsertUserTextTask : AsyncTask<UserText?, Void?, Void?>() {
        override fun doInBackground(vararg userTexts: UserText?): Void? {
            appDatabase?.userTextDao()?.insert(userTexts[0])
            return null
        }
    }
}
