package com.verygoodsecurity.democheckout

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.vgscheckout.VGSCheckoutForm
import com.verygoodsecurity.vgscheckout.config.networking.VGSVaultRouteConfig
import com.verygoodsecurity.vgscheckout.config.networking.request.VGSVaultRequestConfig

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tvWelcome).setOnClickListener {
            VGSCheckoutForm.Builder("some_tenant")
                .setRouteConfig(
                    VGSVaultRouteConfig.Builder()
                        .setRequestConfig(
                            VGSVaultRequestConfig.Builder().build()
                        )
                        .build()
                )
                .build()
                .start(this)
        }
    }
}