package com.example.banktest.adapter

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Build
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.banktest.R
import com.example.banktest.databinding.DateItemLayoutBinding
import com.example.banktest.databinding.TransactionItemLayoutBinding
import com.example.banktest.model.DateItem
import com.example.banktest.model.ListItem
import com.example.banktest.model.ListItem.Companion.TYPE_DATE
import com.example.banktest.model.ListItem.Companion.TYPE_GENERAL
import com.example.banktest.model.Transaction
import com.example.banktest.util.Utils.BUSINESS
import com.example.banktest.util.Utils.CASH
import com.example.banktest.util.Utils.EATINGOUT
import com.example.banktest.util.Utils.ENTERTAINMENT
import com.example.banktest.util.Utils.GROCERIES
import com.example.banktest.util.Utils.SHOPPING
import com.example.banktest.util.Utils.TRANSPORT
import com.example.banktest.util.Utils.UNCATEGORISED
import com.example.banktest.util.Utils.convertDateFormat
import com.example.banktest.util.Utils.howManyDaysAgo
import com.example.banktest.util.Utils.isPendingDescriptionFormat
import com.example.banktest.util.Utils.refineAmount

class TransactionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var transactionList = mutableListOf<ListItem>()

    interface EventListener {
        fun onItemClick(position: Int, item: Transaction)
    }

    private lateinit var mEventListener: EventListener

    fun setEventListener(eventListener: EventListener) {
        mEventListener = eventListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_DATE -> {
                val itemBinding = DateItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                DateViewHolder(itemBinding.root)
            }

            else -> {
                val itemBinding = TransactionItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                TransactionViewHolder(itemBinding.root)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = transactionList[position]
        when (holder.itemViewType) {
            TYPE_DATE -> {
                (holder as DateViewHolder).binding.apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        tvDate.text = convertDateFormat((currentItem as DateItem).date)
                        tvDaysAgo.text = howManyDaysAgo(currentItem.date)
                    }
                }
            }

            TYPE_GENERAL -> {

                (holder as TransactionViewHolder).binding.apply {
                    tvTransactionAmount.text =
                        refineAmount((currentItem as Transaction).amount)

                    val image = getCategoryImage(currentItem.category)

                    ivCategoryIcon.setImageResource(image)

                    root.setOnClickListener {
                        mEventListener.onItemClick(position, currentItem)
                    }

                    tvTransactionDescription.text = isPendingDescriptionFormat(
                        currentItem.isPending,
                        currentItem.description
                    )

                    try {
                        setBoldText(tvTransactionDescription, "Pending: ")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }
        }
    }


    private fun setBoldText(textView: TextView, boldText: String) {
        val fullText = textView.text.toString()
        val spannableStringBuilder = SpannableStringBuilder(fullText)

        val startIndex = fullText.indexOf(boldText)
        val endIndex = startIndex + boldText.length

        val styleSpan = StyleSpan(Typeface.BOLD)
        spannableStringBuilder.setSpan(
            styleSpan,
            startIndex,
            endIndex,
            SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        textView.text = spannableStringBuilder
    }

    private fun getCategoryImage(category: String): Int {
        return when (category) {
            SHOPPING -> R.drawable.icon_category_shopping
            BUSINESS -> R.drawable.icon_category_business
            ENTERTAINMENT -> R.drawable.icon_category_entertainment
            GROCERIES -> R.drawable.icon_category_groceries
            EATINGOUT -> R.drawable.icon_category_eating_out
            TRANSPORT -> R.drawable.icon_category_transportation
            CASH -> R.drawable.icon_category_cash
            UNCATEGORISED -> R.drawable.icon_category_uncategorised
            else -> R.drawable.icon_category_uncategorised
        }
    }

    override fun getItemViewType(position: Int): Int {
        return transactionList[position].Type
    }

    override fun getItemCount(): Int = transactionList.size

    @SuppressLint("NotifyDataSetChanged")
    fun addAll(mData: List<ListItem>) {
        transactionList.clear()
        transactionList.addAll(mData)
        notifyDataSetChanged()
    }

    class TransactionViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var binding: TransactionItemLayoutBinding = TransactionItemLayoutBinding.bind(itemView)
    }

    class DateViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var binding: DateItemLayoutBinding = DateItemLayoutBinding.bind(itemView)
    }

}