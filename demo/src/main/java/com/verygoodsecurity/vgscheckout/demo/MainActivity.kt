package com.verygoodsecurity.vgscheckout.demo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.verygoodsecurity.vgscheckout.demo.custom.CustomCheckoutActivity
import com.verygoodsecurity.vgscheckout.demo.orchestrtion.addcard.AddCardCheckoutActivity

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<MaterialButton>(R.id.mbStartCustom).setOnClickListener {
            startActivity(Intent(this, CustomCheckoutActivity::class.java))
        }
        findViewById<MaterialButton>(R.id.mbStartAddCard).setOnClickListener {
            startActivity(Intent(this, AddCardCheckoutActivity::class.java))
        }
    }
}