package com.systemkern.kontactomat.mail

import com.google.api.services.gmail.Gmail
import org.springframework.stereotype.Service

@Service
class GmailService {
    internal lateinit var gmail: Gmail
    internal lateinit var userId: String
    var isAuthenticated: Boolean = false

    internal fun getInbox() {
        //Added code for test purposes
        val listResponse = gmail.users().labels().list(userId).execute()
        listResponse.labels.forEach {
            println(it)
        }


    }
}