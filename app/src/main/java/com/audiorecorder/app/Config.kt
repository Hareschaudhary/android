package com.audiorecorder.app

object Config {
    // IMPORTANT: "localhost" yahan kaam nahi karega, phone aur PC alag devices hain.
    // PC ka LAN IP daalo (CMD mein "ipconfig" chalao, "IPv4 Address" dekho).
    // Example: "192.168.1.5"
    const val SERVER_HOST = "10.125.232.194"
    const val SERVER_PORT = 3000
    const val SERVER_BASE = "http://$SERVER_HOST:$SERVER_PORT"

    const val HEARTBEAT_INTERVAL_MS = 15000L // har 15 sec
}
