package com.example.banktest.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.banktest.R
import com.example.banktest.adapter.TransactionAdapter
import com.example.banktest.databinding.FragmentAccountDetailsBinding
import com.example.banktest.model.Account
import com.example.banktest.model.DateItem
import com.example.banktest.model.ListItem
import com.example.banktest.model.Transaction
import com.example.banktest.util.Utils.formatAccountNumber
import com.example.banktest.util.Utils.refineAmount
import com.example.banktest.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountDetailsFragment : Fragment() {

    private var _binding: FragmentAccountDetailsBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAccountDetailsBinding.inflate(inflater, container, false)

        requireActivity().window.statusBarColor = this.resources.getColor(R.color.background)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * this fun commented below is to refresh data. But its conflict with recyclerview scrolling.
         */

//        binding.refreshLayout.setOnRefreshListener {
//            loadAccountData()
//            Toast.makeText(requireContext(), "Data Reloaded", Toast.LENGTH_SHORT).show()
//        }

        loadAccountData()
        observeAccountData()

    }

    private fun loadAccountData() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                binding.refreshLayout.isRefreshing = true
                mainViewModel.loadAccountDetailsFromAssets(requireContext())
                delay(300)
            }
            binding.refreshLayout.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()
        loadAccountData()
    }

    private fun observeAccountData() {
        mainViewModel.accountDetails.observe(requireActivity()) {
            if (it != null) {
                showAccountDetails(it.account)
                showTransactionDetails(it.transactions)
            }
        }
    }

    private fun showAccountDetails(account: Account) {
        binding.apply {
            tvAccountDetails.text = account.accountName
            tvAvailableAmount.text = refineAmount(account.available)
            tvBalanceAmount.text = refineAmount(account.balance)
            tvPendingAmount.text = "0"
            tvBsbNumber.text = account.bsb
            tvAccountNumber.text = formatAccountNumber(account.accountNumber)
        }
    }

    private fun showTransactionDetails(transactionList: List<Transaction>) {

        val transactionsGroupByDate = transactionList.groupBy {
            it.effectiveDate
        }

        val consolidatedList = mutableListOf<ListItem>()

        for (date: String in transactionsGroupByDate.keys) {
            val groupItems: List<Transaction>? = transactionsGroupByDate[date]
            val sortedGroupItems: List<Transaction>? =
                groupItems?.sortedByDescending { it.effectiveDate }
            consolidatedList.add(DateItem(date = date))
            sortedGroupItems?.forEach {
                consolidatedList.add(
                    Transaction(
                        amount = it.amount,
                        id = it.id,
                        isPending = it.isPending,
                        description = it.description,
                        category = it.category,
                        atmId = it.atmId,
                        effectiveDate = it.effectiveDate,
                    )
                )
            }
        }

        transactionAdapter = TransactionAdapter()
        binding.rvAccountDetails.adapter = transactionAdapter

        binding.rvAccountDetails.setHasFixedSize(true)

        transactionAdapter.addAll(consolidatedList)

        transactionAdapter.setEventListener(object : TransactionAdapter.EventListener {
            override fun onItemClick(position: Int, item: Transaction) {
                showTransactionDetail(item)
            }
        })
    }

    private fun showTransactionDetail(transaction: Transaction) {
        findNavController().navigate(
            AccountDetailsFragmentDirections.actionAccountDetailsFragmentToTransactionDetailsFragment(
                transaction
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}