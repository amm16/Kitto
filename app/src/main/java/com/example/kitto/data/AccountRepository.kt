package com.example.kitto.data

import android.os.Bundle
import com.facebook.AccessToken
import com.facebook.GraphRequest
import org.json.JSONObject


class AccountRepository {

    private val accessToken: AccessToken = AccessToken.getCurrentAccessToken()


    companion object {
        private var sInstance: AccountRepository? = null
        fun instance(): AccountRepository {
            if (sInstance == null) {
                synchronized(AccountRepository) {
                    sInstance = AccountRepository()
                }
            }
            return sInstance!!
        }
    }

    fun fetchAccount(completion: (JSONObject?, Error?) -> Unit) {
        val graphRequest = GraphRequest.newMeRequest(accessToken
        ) { obj, response ->
            println("Response: $response")
            println("Object $obj")
            completion(obj, null)
        }

        val bundle = Bundle()
        bundle.putString(
            "fields",
            "id,first_name,last_name,email,birthday"
        )
        graphRequest.parameters = bundle
        graphRequest.executeAsync()
    }

}
