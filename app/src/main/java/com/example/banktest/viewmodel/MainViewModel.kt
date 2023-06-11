package com.example.banktest.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.banktest.model.AccountDetailModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream

class MainViewModel : ViewModel() {

    private val _accountDetails = MutableLiveData<AccountDetailModel>()
    val accountDetails: LiveData<AccountDetailModel> = _accountDetails

    fun loadAccountDetailsFromAssets(context: Context) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                // Read the JSON file from assets folder
                val inputStream: InputStream = context.assets.open("exercise.json")
                val size = withContext(Dispatchers.IO) {
                    inputStream.available()
                }
                val buffer = ByteArray(size)
                withContext(Dispatchers.IO) {
                    inputStream.read(buffer)
                    inputStream.close()
                }

                // Convert the buffer into a JSON string
                val json = String(buffer, Charsets.UTF_8)

                val gson = Gson()

                _accountDetails.value = gson.fromJson(json, AccountDetailModel::class.java)

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}