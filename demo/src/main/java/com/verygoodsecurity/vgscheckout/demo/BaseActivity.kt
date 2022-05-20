package com.verygoodsecurity.vgscheckout.demo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.vgscheckout.demo.settings.CheckoutSettingsActivity

abstract class BaseActivity constructor(@LayoutRes layoutId: Int) : AppCompatActivity(layoutId) {

    protected abstract val type: CheckoutType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.base, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.itemConfig -> {
                CheckoutSettingsActivity.start(this, type)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}