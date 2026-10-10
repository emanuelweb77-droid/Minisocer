package com.example.minisocceraim

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
 override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState)
  val layout = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(32,64,32,32) }
  layout.addView(TextView(this).apply { text = "Mini Soccer Aim — prototipo 0.1\n\nGuía manual de tiro para superponer sobre Mini Soccer Star. No detecta automáticamente la pelota ni predice goles."; textSize = 19f })
  layout.addView(Button(this).apply { text = "Iniciar guía flotante"; setOnClickListener {
   if (!Settings.canDrawOverlays(this@MainActivity)) startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
   else startService(Intent(this@MainActivity, OverlayService::class.java))
  } })
  layout.addView(Button(this).apply { text = "Cerrar guía"; setOnClickListener { stopService(Intent(this@MainActivity, OverlayService::class.java)) } })
  setContentView(layout)
 }
}
