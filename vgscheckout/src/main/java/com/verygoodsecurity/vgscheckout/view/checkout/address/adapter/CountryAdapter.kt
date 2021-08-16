package com.verygoodsecurity.vgscheckout.view.checkout.address.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import com.google.android.material.textview.MaterialTextView
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.view.checkout.address.util.country.model.Country
import com.verygoodsecurity.vgscheckout.util.extension.layoutInflater

internal class CountryAdapter constructor(
    context: Context,
    countries: List<Country>
) : ArrayAdapter<Country>(context, 0, countries) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflateView(R.layout.vgs_checkout_spinner_header, parent)
        view.findViewById<MaterialTextView>(R.id.mtvTitle).text = getItem(position)?.name
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflateView(R.layout.vgs_checkout_spinner_item, parent)
        view.findViewById<MaterialTextView>(R.id.mtvTitle).text = getItem(position)?.name
        return view
    }

    private fun inflateView(@LayoutRes id: Int, parent: ViewGroup): View {
        return context.layoutInflater.inflate(id, parent, false)
    }
}