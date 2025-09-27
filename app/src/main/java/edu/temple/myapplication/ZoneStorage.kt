package edu.temple.myapplication

import android.content.Context
import android.net.Uri

object ZoneStorage {
    private const val PREF = "zones"
    private fun prefs(ctx: Context) = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)

    fun saveReference(ctx: Context, zone: String, uri: Uri) {
        prefs(ctx).edit().putString("ref_$zone", uri.toString()).apply()
    }

    fun getReference(ctx: Context, zone: String): Uri? =
        prefs(ctx).getString("ref_$zone", null)?.let(Uri::parse)
}