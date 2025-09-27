package edu.temple.myapplication

import android.content.Context

object PointsManager {
    private const val PREF_NAME = "points_prefs"
    private const val KEY_POINTS = "points"

    fun add(context: Context, value: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val newPoints = get(context) + value
        prefs.edit().putInt(KEY_POINTS, newPoints).apply()
    }

    fun get(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_POINTS, 0)
    }

    fun reset(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_POINTS, 0).apply()
    }
}