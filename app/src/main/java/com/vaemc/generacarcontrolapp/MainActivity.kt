package com.vaemc.generacarcontrolapp

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import at.markushi.ui.CircleButton
import com.google.android.material.textfield.TextInputEditText
import okhttp3.*

class MainActivity : AppCompatActivity(), View.OnTouchListener, View.OnClickListener {
    private var webSocket: WebSocket? = null
    private val etWs: TextInputEditText by lazy { findViewById(R.id.et_ws) }
    private val btnConnect: Button by lazy { findViewById(R.id.btn_connect) }

    private val toolbar: Toolbar by lazy { findViewById(R.id.toolbar) }
    private val btnForward: CircleButton by lazy { findViewById(R.id.btn_forward) }
    private val btnForwardLeft: CircleButton by lazy { findViewById(R.id.btn_forwardLeft) }
    private val btnForwardRight: CircleButton by lazy { findViewById(R.id.btn_forwardRight) }
    private val btnLeft: CircleButton by lazy { findViewById(R.id.btn_left) }
    private val btnCounterclockwise: CircleButton by lazy { findViewById(R.id.btn_counterclockwise) }
    private val btnClockwise: CircleButton by lazy { findViewById(R.id.btn_clockwise) }
    private val btnRight: CircleButton by lazy { findViewById(R.id.btn_right) }
    private val btnBackward: CircleButton by lazy { findViewById(R.id.btn_backward) }
    private val btnBackwardLeft: CircleButton by lazy { findViewById(R.id.btn_backwardLeft) }
    private val btnBackwardRight: CircleButton by lazy { findViewById(R.id.btn_backwardRight) }


    override fun onClick(v: View?) {
        if (webSocket == null) {
            Toast.makeText(this, "请先连接小车", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        if (webSocket != null) {
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    webSocket?.send(view.resources.getResourceName(view.id).split("_")[1])
                }

                MotionEvent.ACTION_UP -> {
                    webSocket?.send("stop")
                }
            }
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val sharedPref = this?.getPreferences(Context.MODE_PRIVATE)
        etWs.setText(sharedPref?.getString("ws", ""))

        btnForward.setOnTouchListener(this)
        btnForwardLeft.setOnTouchListener(this)
        btnForwardRight.setOnTouchListener(this)
        btnLeft.setOnTouchListener(this)
        btnCounterclockwise.setOnTouchListener(this)
        btnClockwise.setOnTouchListener(this)
        btnRight.setOnTouchListener(this)
        btnBackward.setOnTouchListener(this)
        btnBackwardLeft.setOnTouchListener(this)
        btnBackwardRight.setOnTouchListener(this)

        btnForward.setOnClickListener(this)
        btnForwardLeft.setOnClickListener(this)
        btnForwardRight.setOnClickListener(this)
        btnLeft.setOnClickListener(this)
        btnCounterclockwise.setOnClickListener(this)
        btnClockwise.setOnClickListener(this)
        btnRight.setOnClickListener(this)
        btnBackward.setOnClickListener(this)
        btnBackwardLeft.setOnClickListener(this)
        btnBackwardRight.setOnClickListener(this)

        btnConnect.setOnClickListener {
            var ws: String = etWs.text.toString()
            sharedPref?.edit()?.putString("ws", ws)?.apply()

            if (ws.isNullOrBlank()) {
                Toast.makeText(this, "请输入地址", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val client = OkHttpClient()
            val request = Request.Builder().url(ws).build()
            webSocket = client.newWebSocket(request, object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "连接小车成功", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onMessage(webSocket: WebSocket, text: String) {

                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    webSocket.close(code, reason)
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    t.printStackTrace()
                }
            })
        }
    }


}


