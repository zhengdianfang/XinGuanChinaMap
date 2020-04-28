package com.thoughtworks.xinguanmap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mapView by lazy { ChinaMapView(this) }

    private val SERVER_URL = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapFrame.addView(mapView)
        mapView.setDataUrl(SERVER_URL)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.stopLoading()
        mapView.removeAllViews()
        mapView.destroy()
    }
}
