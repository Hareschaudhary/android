package com.audiorecorder.app

import java.net.HttpURLConnection
import java.net.URL

object HeartbeatClient {

    // Server ko bolta hai "main yahan hoon", aur agar koi pending command ho (start/stop) to wo leta hai.
    // Return value: "start", "stop", ya "" (koi command nahi) ya null (connection fail hui)
    fun sendHeartbeat(fingerprint: String, deviceName: String): String? {
        return try {
            val url = URL("${Config.SERVER_BASE}/heartbeat")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true
            conn.connectTimeout = 5000
            conn.readTimeout = 5000

            val body = """{"fingerprint":"$fingerprint","deviceName":"$deviceName"}"""
            conn.outputStream.use { it.write(body.toByteArray()) }

            val responseCode = conn.responseCode
            if (responseCode != 200) {
                conn.disconnect()
                return null
            }

            val responseText = conn.inputStream.bufferedReader().use { it.readText() }
            conn.disconnect()

            extractCommand(responseText)
        } catch (e: Exception) {
            null // server se connect nahi ho paya (WiFi off, PC band, galat IP, etc.)
        }
    }

    // Response JSON jaisa: {"command":"start","isRecording":false}
    // Simple string search, jaisa Windows app ke upload.c mein kiya tha.
    private fun extractCommand(json: String): String {
        return when {
            json.contains("\"command\":\"start\"") -> "start"
            json.contains("\"command\":\"stop\"") -> "stop"
            else -> ""
        }
    }
}
