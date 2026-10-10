package com.example.minisocceraim

import android.app.Service
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PixelFormat
import android.content.Context
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import kotlin.math.hypot

class OverlayService : Service() {
 private lateinit var wm: WindowManager
 private var guide: AimView? = null
 private var panel: View? = null
 override fun onBind(intent: Intent?): IBinder? = null
 override fun onCreate() { super.onCreate(); wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
  val flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
  val params = WindowManager.LayoutParams(-1,-1,WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,flags,PixelFormat.TRANSLUCENT)
  guide = AimView(this); wm.addView(guide,params)
  val menu = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(12,12,12,12); setBackgroundColor(Color.argb(220,20,24,42)) }
  menu.addView(TextView(this).apply { text="⚽ Guía manual"; setTextColor(Color.WHITE); textSize=16f })
  menu.addView(Button(this).apply { text="Mostrar / ocultar"; setOnClickListener { guide?.visible = !(guide?.visible ?: true); guide?.invalidate() } })
  menu.addView(Button(this).apply { text="Dejar pasar toques"; setOnClickListener { val editing = !(guide?.editMode ?: true); guide?.editMode = editing; params.flags = flags or (if(editing) 0 else WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE); guide?.let { wm.updateViewLayout(it, params) }; text=if(editing) "Dejar pasar toques" else "Editar guía" } })
  menu.addView(Button(this).apply { text="Cerrar todo ✕"; setOnClickListener { stopSelf() } })
  panel=menu
  val mp=WindowManager.LayoutParams(-2,-2,WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,flags,PixelFormat.TRANSLUCENT).apply { gravity=Gravity.TOP or Gravity.START; x=20; y=90 }
  wm.addView(menu,mp)
  menu.setOnTouchListener(object: View.OnTouchListener { var sx=0f; var sy=0f; var ox=0; var oy=0
   override fun onTouch(v:View,e:MotionEvent):Boolean { when(e.actionMasked) { MotionEvent.ACTION_DOWN -> {sx=e.rawX;sy=e.rawY;ox=mp.x;oy=mp.y;return true}; MotionEvent.ACTION_MOVE -> {mp.x=ox+(e.rawX-sx).toInt();mp.y=oy+(e.rawY-sy).toInt();wm.updateViewLayout(menu,mp);return true} };return false }
  })
 }
 override fun onDestroy() { panel?.let { wm.removeView(it) }; guide?.let { wm.removeView(it) }; super.onDestroy() }
}

class AimView(context:Context):View(context) {
 var visible=true
 var editMode=true
 private var initialized=false
 private var x1=0f; private var y1=0f; private var x2=0f; private var y2=0f
 private var dragging=0
 private val line=Paint(3).apply { color=Color.CYAN; strokeWidth=7f; strokeCap=Paint.Cap.ROUND }
 private val dot=Paint(3).apply { color=Color.YELLOW; style=Paint.Style.STROKE; strokeWidth=6f }
 private val label=Paint(3).apply { color=Color.WHITE; textSize=36f; setShadowLayer(4f,1f,2f,Color.BLACK) }
 override fun onDraw(c:Canvas) { if(!initialized) { x1=width*.5f;y1=height*.7f;x2=width*.5f;y2=height*.38f;initialized=true }
  if(!visible)return
  c.drawLine(x1,y1,x2,y2,line); c.drawCircle(x1,y1,27f,dot);c.drawCircle(x2,y2,27f,dot)
  c.drawText("Pelota",x1+34,y1,label);c.drawText("Objetivo",x2+34,y2,label)
 }
 override fun onTouchEvent(e:MotionEvent):Boolean { if(!editMode || !visible)return false
  when(e.actionMasked) {
   MotionEvent.ACTION_DOWN -> { val a=hypot(e.x-x1,e.y-y1);val b=hypot(e.x-x2,e.y-y2); dragging=if(a<b)1 else 2; return true }
   MotionEvent.ACTION_MOVE -> { if(dragging==1) {x1=e.x;y1=e.y} else {x2=e.x;y2=e.y};invalidate();return true }
   MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL -> {dragging=0;return true}
  };return true
 }
}
