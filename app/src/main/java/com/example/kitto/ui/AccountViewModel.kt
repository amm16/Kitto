package com.example.kitto.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.kitto.data.AccountRepository

class AccountViewModel: ViewModel() {
    private val account = AccountRepository.instance()
    var firstName = MutableLiveData<String>()
    var lastName = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    var birthday = MutableLiveData<String>()
    var id = MutableLiveData<String>()

    init {
        loadAccount()
    }
    private fun loadAccount(){
        account.fetchAccount{ obj, error ->
            if(error == null){
                id.value = obj?.getString("id") ?: "N/A"
                firstName.value = obj?.getString("first_name") ?: "N/A"
                lastName.value= obj?.getString("last_name") ?: "N/A"
                email.value = obj?.getString("email") ?: "N/A"
                birthday.value = obj?.getString("birthday") ?: "N/A"
            }
        }
    }




}
