package com.elahee.startfullscreenintent

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.elahee.startfullscreenintent.service.ConstantApp
import com.elahee.startfullscreenintent.service.HeadsUpNotificationService
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnStart).setOnClickListener(View.OnClickListener {
            startFullScreenIntentNotification("test data","agent")
        })

    }

    fun startFullScreenIntentNotification(data: String, userType: String?) {
        try {
            val serviceIntent = Intent(
                this@MainActivity,
                HeadsUpNotificationService::class.java
            )
            serviceIntent.putExtra(
                ConstantApp.FCM_DATA_KEY,
                data
            )
            serviceIntent.putExtra(ConstantApp.USERTYPE, userType)
            ContextCompat.startForegroundService(applicationContext, serviceIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}