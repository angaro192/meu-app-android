package com.arfa.blockfreecallspam.services

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase

import android.provider.ContactsContract
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.util.Locale
//import com.google.gson.Gson

class InterceptCall : BroadcastReceiver() {
    var number: String? = ""
    var db: SQLiteDatabase? = null
    override fun onReceive(context: Context?, intent: Intent?) {

        val phoneState = intent?.getStringExtra(TelephonyManager.EXTRA_STATE) ?: return
        val isNumberExist = intent?.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
        if (phoneState == "RINGING" && isNumberExist != null) {
            number = isNumberExist
            // Checa se o numero está na agenda.
            if(checkNumberForContacts(context?.applicationContext, number)){
                Toast.makeText(context, "O numero ($number) está na agenda", Toast.LENGTH_SHORT)
                    .show()
            }else {
                Toast.makeText(
                    context,
                    "O numero ($number) não está na agenda",
                    Toast.LENGTH_SHORT
                ).show()
                val telecomManager = context?.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
                if(ActivityCompat.checkSelfPermission(
                        context.applicationContext,
                        Manifest.permission.ANSWER_PHONE_CALLS
                ) != PackageManager.PERMISSION_GRANTED) {
                    return
                }
                if(telecomManager != null && telecomManager.endCall()) {
                    Toast.makeText(
                        context,
                        "Chamanda encerrada automaticamente$number",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
//        val isAirplaneModeEnabled = intent?.getBooleanExtra("state", false) ?: return
//
//        if (isAirplaneModeEnabled) {
//            Toast.makeText(context, "Airplane Mode Enabled", Toast.LENGTH_LONG).show()
//        } else {
//            Toast.makeText(context, "Airplane Mode Disabled", Toast.LENGTH_LONG).show()
//        }
    }

    private fun checkNumberForContacts(context: Context?, phoneNumber: String?): Boolean {
        return false;
    }
}