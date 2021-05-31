package com.verygoodsecurity.democheckout

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.vgscheckout.VGSCheckoutForm

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tvWelcome).setOnClickListener {
            VGSCheckoutForm.Builder("some_tenant")
                .build()
                .start(this)
        }
    }
}