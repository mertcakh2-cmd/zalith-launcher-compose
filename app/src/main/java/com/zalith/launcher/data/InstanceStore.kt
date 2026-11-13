package com.zalith.launcher.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

/** Stores launcher instance definitions locally; does not install loaders or game files. */
object InstanceStore {
    private const val PREFERENCES = "zalith_instances"
    private const val KEY_INSTANCES = "instances_v1"

    fun load(context: Context): List<GameInstance> {
        val raw = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).getString(KEY_INSTANCES, null)
            ?: return emptyList()
        return runCatching {
            val array = JSONArray(raw)
            buildList {
                for (i in 0 until array.length()) {
                    val item = array.getJSONObject(i)
                    add(GameInstance(item.getString("name"), item.getString("version"), item.getString("loader")))
                }
            }
        }.getOrDefault(emptyList())
    }

    fun save(context: Context, instances: List<GameInstance>) {
        val array = JSONArray()
        instances.forEach { instance ->
            array.put(JSONObject().put("name", instance.name).put("version", instance.version).put("loader", instance.loader))
        }
        context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            .edit().putString(KEY_INSTANCES, array.toString()).apply()
    }
}

data class GameInstance(val name: String, val version: String, val loader: String)

val supportedLoaderChoices = listOf("Vanilla", "Fabric", "Forge", "NeoForge", "OptiFine")
