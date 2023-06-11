package com.example.banktest.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.banktest.R
import com.example.banktest.databinding.FragmentTransactionDetailsBinding

class TransactionDetailsFragment : Fragment() {

    private var _binding: FragmentTransactionDetailsBinding? = null
    private val binding get() = _binding!!

    private val args :TransactionDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTransactionDetailsBinding.inflate(inflater, container, false)

        requireActivity().window.statusBarColor = this.resources.getColor(R.color.background)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            tvTransactionId.text = "ID: " + args.transactionDetails.id
            tvTransactionAmount.text = "Amount: " + args.transactionDetails.amount
            tvTransactionState.text = "Status: " + args.transactionDetails.isPending
            tvTransactionDescription.text = "Description: " + args.transactionDetails.description
            tvTransactionCategory.text = "Category: " + args.transactionDetails.category
            tvTransactionDate.text = "Date: " + args.transactionDetails.effectiveDate
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}