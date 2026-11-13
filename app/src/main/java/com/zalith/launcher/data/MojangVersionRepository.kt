package com.zalith.launcher.data

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Reads public version metadata published by Mojang. Does not download or launch game files. */
object MojangVersionRepository {
    private const val MANIFEST_URL = "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json"

    suspend fun fetchManifest(): MojangVersionManifest = withContext(Dispatchers.IO) {
        val connection = (URL(MANIFEST_URL).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 12_000
            readTimeout = 12_000
            setRequestProperty("Accept", "application/json")
        }
        try {
            if (connection.responseCode !in 200..299) {
                error("Mojang returned HTTP ${connection.responseCode}")
            }
            val json = JSONObject(connection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() })
            val latest = json.getJSONObject("latest")
            val source = json.getJSONArray("versions")
            val versions = buildList {
                for (index in 0 until source.length()) {
                    val item = source.getJSONObject(index)
                    add(MojangVersion(item.getString("id"), item.getString("type"), item.getString("url"), item.optString("releaseTime")))
                }
            }
            MojangVersionManifest(latest.getString("release"), latest.getString("snapshot"), versions)
        } finally {
            connection.disconnect()
        }
    }
}

data class MojangVersionManifest(val latestRelease: String, val latestSnapshot: String, val versions: List<MojangVersion>)
data class MojangVersion(val id: String, val type: String, val url: String, val releaseTime: String)
