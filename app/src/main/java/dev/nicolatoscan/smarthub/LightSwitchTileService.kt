package dev.nicolatoscan.smarthub

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LightSwitchTileService: TileService() {
    override fun onTileAdded() {
        super.onTileAdded()
        qsTile.state = Tile.STATE_INACTIVE
        qsTile.updateTile()
    }

    override fun onClick() {
        super.onClick()
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.1.69/reley1"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                var json = response
                val gson = Gson()
                val mapType = object : TypeToken<Map<String, Any>>() {}.type
                var mapped: Map<String, Any> = gson.fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
                if (mapped.contains("reley1")) {
                    qsTile.state = (if (mapped.get("reley1") == true) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE)
                    Toast.makeText(this.applicationContext, if (mapped.get("reley1") == true) "Reley ON" else "Reley OFF", Toast.LENGTH_SHORT).show()
                } else {
                    qsTile.state = Tile.STATE_INACTIVE
                    Toast.makeText(this.applicationContext, "Invalid JSON response", Toast.LENGTH_SHORT).show()
                }
                qsTile.updateTile()
            },
            Response.ErrorListener {
                qsTile.state = Tile.STATE_INACTIVE
                qsTile.updateTile()
                Toast.makeText(this.applicationContext, "Failed Request", Toast.LENGTH_SHORT).show()
            })

        queue.add(stringRequest)

    }
}