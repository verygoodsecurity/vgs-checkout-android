package com.verygoodsecurity.democheckout

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.vgscheckout.VGSCheckout
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutVaultConfiguration

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tvWelcome).setOnClickListener {
            VGSCheckout("some_tenant").present(
                this,
                1,
                VGSCheckoutVaultConfiguration.Builder().build()
            )
        }
    }
}