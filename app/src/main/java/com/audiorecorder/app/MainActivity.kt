package com.audiorecorder.app

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var fingerprintText: TextView
    private lateinit var deviceNameText: TextView
    private lateinit var statusText: TextView

    private var keepRunning = true
    private var isRecording = false // abhi sirf command print karenge, recording agle step mein

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fingerprintText = findViewById(R.id.fingerprintText)
        deviceNameText = findViewById(R.id.deviceNameText)
        statusText = findViewById(R.id.statusText)

        val fingerprint = DeviceInfo.getFingerprint(this)
        val deviceName = DeviceInfo.getDeviceName()

        fingerprintText.text = "Fingerprint: $fingerprint"
        deviceNameText.text = "Device: $deviceName"
        statusText.text = "Status: waiting for command..."

        startHeartbeatLoop(fingerprint, deviceName)
    }

    // Windows app ke main.c ke while(1) loop jaisa hi — background thread mein,
    // kyunki Android main (UI) thread pe network call allowed nahi hai.
    private fun startHeartbeatLoop(fingerprint: String, deviceName: String) {
        Thread {
            while (keepRunning) {
                val command = HeartbeatClient.sendHeartbeat(fingerprint, deviceName)

                runOnUiThread {
                    when {
                        command == null -> {
                            statusText.text = "Status: connection failed (check WiFi / IP / server)"
                        }
                        command == "start" && !isRecording -> {
                            isRecording = true
                            statusText.text = "Status: START command mila (recording code agle step mein)"
                        }
                        command == "stop" && isRecording -> {
                            isRecording = false
                            statusText.text = "Status: STOP command mila"
                        }
                        else -> {
                            statusText.text = if (isRecording)
                                "Status: recording... (simulated)"
                            else
                                "Status: waiting for command..."
                        }
                    }
                }

                Thread.sleep(Config.HEARTBEAT_INTERVAL_MS)
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        keepRunning = false
    }
}
