package com.elahee.startfullscreenintent.service

import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.elahee.startfullscreenintent.R
import java.util.*

class HeadsUpNotificationService : Service() {
    private val CHANNEL_ID = "VoipChannel"
    private val CHANNEL_NAME = "Voip Channel"
    private var player: MediaPlayer? = null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        //Bundle data = null;
        var data: String? = null
        var userType: String? = ""
        if (intent.extras != null) {
            data = intent.getStringExtra(ConstantApp.FCM_DATA_KEY)
            userType = intent.getStringExtra(ConstantApp.USERTYPE)
        }
        try {
            val receiveCallAction = Intent(
                applicationContext,
                HeadsUpNotificationActionReceiver::class.java
            )
            receiveCallAction.putExtra(
                ConstantApp.CALL_RESPONSE_ACTION_KEY,
                ConstantApp.CALL_RECEIVE_ACTION
            )
            receiveCallAction.putExtra(ConstantApp.FCM_DATA_KEY, data)
            receiveCallAction.putExtra(ConstantApp.USERTYPE, userType)
            receiveCallAction.action = "RECEIVE_CALL"
            val cancelCallAction = Intent(
                applicationContext,
                HeadsUpNotificationActionReceiver::class.java
            )
            cancelCallAction.putExtra(
                ConstantApp.CALL_RESPONSE_ACTION_KEY,
                ConstantApp.CALL_CANCEL_ACTION
            )
            cancelCallAction.putExtra(ConstantApp.FCM_DATA_KEY, data)
            cancelCallAction.putExtra(ConstantApp.USERTYPE, userType)
            cancelCallAction.action = "CANCEL_CALL"
            val receiveCallPendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                1200,
                receiveCallAction,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val cancelCallPendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                1201,
                cancelCallAction,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            createChannel()
            var notificationBuilder: NotificationCompat.Builder? = null
            if (data != null) {
                notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentText("New order placed")
                    .setContentTitle("Incoming  Call")
                    .setSmallIcon(R.drawable.ic_confirm)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_CALL)
                    .addAction(R.drawable.ic_confirm, "Receive Call", receiveCallPendingIntent)
                    .addAction(R.drawable.ic_reject, "Cancel Call", cancelCallPendingIntent)
                    .setAutoCancel(true) //.setOngoing(true)
                    //  .setSound(Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.loud_alarm))
                    .setFullScreenIntent(receiveCallPendingIntent, true)
            }
            var incomingCallNotification: Notification? = null
            if (notificationBuilder != null) {
                incomingCallNotification = notificationBuilder.build()
            }
            startForeground(120, incomingCallNotification)
            setSound()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return START_STICKY
    }

    private fun setSound() {
        player = MediaPlayer.create(
            this,
            R.raw.loud_alarm
        )
        //setting loop play to true
        //this will make the ringtone continuously playing
        player!!.isLooping = true
        player!!.setVolume(1.0f, 1.0f)

        //staring the player
        player!!.start()
    }

    /*
    Create noticiation channel if OS version is greater than or eqaul to Oreo
    */
    fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            channel.description = "Call Notifications"
            //            channel.setSound(Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.loud_alarm),
//                    new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                            .setLegacyStreamType(AudioManager.STREAM_RING)
//                            .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION).build());
            Objects.requireNonNull(
                applicationContext.getSystemService(
                    NotificationManager::class.java
                )
            ).createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //stopping the player when service is destroyed
        player!!.stop()
    }
}