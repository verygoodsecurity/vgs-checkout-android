package com.verygoodsecurity.vgscheckout.ui.fragment.method.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.verygoodsecurity.vgscheckout.R

internal class PaymentMethodsAdapter constructor(
    private val paymentMethods: List<String>,
    private val listener: OnPaymentMethodClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (toEnum(viewType)) {
            ViewType.CARD -> CardViewHolder(
                inflater.inflate(
                    R.layout.vgs_checkout_new_card_layout,
                    parent,
                    false
                )
            )
            ViewType.ADD_CARD -> AddCardViewHolder(
                inflater.inflate(
                    R.layout.vgs_checkout_new_card_layout,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? CardViewHolder)?.bind(paymentMethods[position])
    }

    override fun getItemCount(): Int = paymentMethods.count().inc()

    override fun getItemViewType(position: Int): Int =
        if (paymentMethods.getOrNull(position) != null) ViewType.CARD.value else ViewType.ADD_CARD.value

    private fun toEnum(viewType: Int): ViewType = when (viewType) {
        ViewType.CARD.value -> ViewType.CARD
        else -> ViewType.ADD_CARD
    }

    inner class CardViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

        init {
            view.setOnClickListener {
                listener.onCardClick(paymentMethods[adapterPosition])
            }
        }

        fun bind(card: String) {
            // TODO: Implement
        }
    }

    inner class AddCardViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

        init {
            view.setOnClickListener {
                listener.onNewCardClick()
            }
        }
    }

    enum class ViewType constructor(val value: Int) {

        CARD(1),
        ADD_CARD(2)
    }

    interface OnPaymentMethodClickListener {

        fun onCardClick(card: String)

        fun onNewCardClick()
    }
}