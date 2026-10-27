package com.audiorecorder.app

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings

object DeviceInfo {

    // ANDROID_ID: har device ke liye unique, app uninstall/reinstall ke baad bhi same rehta hai
    // (factory reset tak). Windows app mein jaisa Machine GUID use kiya tha, waisa hi yahan ye hai.
    @SuppressLint("HardwareIds")
    fun getFingerprint(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            ?: "unknown-device"
    }

    // Device ka readable naam, jaise "samsung SM-G991B"
    fun getDeviceName(): String {
        return "${Build.MANUFACTURER} ${Build.MODEL}"
    }
}
