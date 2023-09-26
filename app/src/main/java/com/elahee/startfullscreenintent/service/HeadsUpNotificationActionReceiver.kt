package com.elahee.startfullscreenintent.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class HeadsUpNotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent != null && intent.extras != null) {
            val action = intent.getStringExtra(ConstantApp.CALL_RESPONSE_ACTION_KEY)
            val data = intent.getStringExtra(ConstantApp.FCM_DATA_KEY)
            val userType = intent.getStringExtra(ConstantApp.USERTYPE)
            action?.let { performClickAction(context, it, data, userType) }

            // Close the notification after the click action is performed.

//            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
//            context.sendBroadcast(it);
//            context.stopService(new Intent(context, HeadsUpNotificationService.class));
        }
    }

    private fun performClickAction(
        context: Context,
        action: String,
        data: String?,
        userType: String?
    ) {
        if (action == ConstantApp.CALL_RECEIVE_ACTION && data != null && userType.contentEquals("agent")) {
            var openIntent: Intent? = null
            try {
//                openIntent = Intent(context, IncomingCallActivity::class.java)
//                openIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                openIntent.putExtra("usertype", "agent")
//                openIntent.putExtra("basic_data", data)
//                context.startActivity(openIntent)
                context.stopService(Intent(context, HeadsUpNotificationService::class.java))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (action == ConstantApp.CALL_RECEIVE_ACTION && data != null && userType.contentEquals(
                "rider"
            )
        ) {
            var openIntent: Intent? = null
            try {
//                openIntent = Intent(context, RiderIncomingCallActivity::class.java)
//                openIntent.putExtra("usertype", "rider")
//                openIntent.putExtra("basic_data", data)
//                openIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                context.startActivity(openIntent)
                context.stopService(Intent(context, HeadsUpNotificationService::class.java))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (action == ConstantApp.CALL_CANCEL_ACTION) {
            try {
                context.stopService(Intent(context, HeadsUpNotificationService::class.java))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            //            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
//            context.sendBroadcast(it);
        }
    }
}