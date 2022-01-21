package com.verygoodsecurity.vgscheckout.ui.fragment.method.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.verygoodsecurity.vgscheckout.R

internal class CardsAdapter constructor(
    private val cards: List<String>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (toEnum(viewType)) {
            ViewType.CARD -> CardViewHolder(
                inflater.inflate(
                    R.layout.vgs_checkout_card_layout,
                    parent,
                    false
                )
            )
            ViewType.ADD_CARD -> CardViewHolder(
                inflater.inflate(
                    R.layout.vgs_checkout_new_card_layout,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CardViewHolder -> holder.bind("")
            is AddCardViewHolder -> holder.bind()
        }
    }

    override fun getItemCount(): Int = cards.count().inc()

    override fun getItemViewType(position: Int): Int {
        Log.d("Test", "${if (cards.getOrNull(position) != null) ViewType.CARD.value else ViewType.ADD_CARD.value}")
        return if (cards.getOrNull(position) != null) ViewType.CARD.value else ViewType.ADD_CARD.value
    }

    private fun toEnum(viewType: Int) = when (viewType) {
        ViewType.CARD.value -> ViewType.CARD
        else -> ViewType.ADD_CARD
    }

    class CardViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(card: String) {

        }
    }

    class AddCardViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

        fun bind() {

        }
    }

    enum class ViewType constructor(val value: Int) {

        CARD(1),
        ADD_CARD(2)
    }
}