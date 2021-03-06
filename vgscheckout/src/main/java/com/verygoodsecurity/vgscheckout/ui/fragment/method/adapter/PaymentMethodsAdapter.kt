package com.verygoodsecurity.vgscheckout.ui.fragment.method.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.model.VGSCheckoutCardBrand
import com.verygoodsecurity.vgscheckout.model.Card

// TODO: Switch to ListAdapter to add delete animation
internal class PaymentMethodsAdapter constructor(
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var cards: MutableList<Card> = mutableListOf()

    private var selectedPosition: Int = 0

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
        (holder as? CardViewHolder)?.bind(cards[position])
    }

    override fun getItemCount(): Int = cards.count().inc()

    override fun getItemViewType(position: Int): Int =
        if (cards.getOrNull(position) != null) ViewType.CARD.value else ViewType.ADD_CARD.value

    fun getItems(): List<Card> = cards

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: MutableList<Card>) {
        this.cards = items
        notifyDataSetChanged()
    }

    fun getSelectedPosition() = selectedPosition

    fun getSelectedCard(): Card? = cards.getOrNull(selectedPosition)

    fun setSelectedPosition(position: Int) {
        val oldPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(oldPosition)
        notifyItemChanged(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeItem(card: Card) {
        cards.remove(card)
        selectedPosition = 0
        notifyDataSetChanged()
        listener.onCardRemoved(cards.isEmpty())
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeItem(id: String) {
        cards.find { it.finId == id }?.let {
            cards.remove(it)
            selectedPosition = 0
            notifyDataSetChanged()
            listener.onCardRemoved(cards.isEmpty())
        }
    }

    private fun toEnum(viewType: Int): ViewType = when (viewType) {
        ViewType.CARD.value -> ViewType.CARD
        else -> ViewType.ADD_CARD
    }

    inner class CardViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

        private val ivCardBrand: AppCompatImageView? = view.findViewById(R.id.ivCardBrand)
        private val mtvCardHolderName: MaterialTextView? = view.findViewById(R.id.mtvCardHolderName)
        private val mtvCardNumber: MaterialTextView? = view.findViewById(R.id.mtvCardNumber)
        private val mtvExpiry: MaterialTextView? = view.findViewById(R.id.mtvExpiry)
        private val radioButton: RadioButton? = view.findViewById(R.id.radioButton)

        init {

            view.setOnClickListener {
                setSelectedPosition(adapterPosition)
            }
        }

        fun bind(card: Card) {
            ivCardBrand?.setImageResource(VGSCheckoutCardBrand.getBrandIcon(card.brand))
            mtvCardHolderName?.text = card.holderName
            mtvCardNumber?.text = getFormattedCardNumber(card.last4)
            mtvExpiry?.text = getFormattedExpiry(card.expiryMonth, card.twoDigitExpiryYear)
            radioButton?.isChecked = adapterPosition == selectedPosition
        }

        private fun getFormattedCardNumber(lastFour: String): String {
            return itemView.resources.getString(R.string.vgs_checkout_card_number, lastFour)
        }

        private fun getFormattedExpiry(month: Int, year: String): String {
            return itemView.resources.getString(R.string.vgs_checkout_expiry, month, year)
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

    interface OnItemClickListener {

        fun onNewCardClick()

        fun onCardRemoved(isEmpty: Boolean)
    }
}