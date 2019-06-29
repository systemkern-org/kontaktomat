package com.systemkern.kontactomat.mail

import com.google.api.services.gmail.Gmail
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat

@Service
class GmailService(
        val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("EEEEE dd MMMMM yyyy")
){
    internal lateinit var gmail: Gmail
    internal lateinit var userId: String

    internal fun getInbox() {
        println("----------------------------------------- INBOX ----------------------------------------")
        gmail
            .users()
            .threads()
            .list(userId)
            .setQ("is:inbox")
            .setMaxResults(15)
            .execute()
            .threads?.map {
                gmail.users()
                    .threads()
                    .get(userId, it.id)
                    .execute()
            }?.forEach {
                val first = it.messages.first()
                val from = first.payload.headers.filter { header -> header["name"] == "From" }.map { fromHeader -> fromHeader.value }
                val emailDate = simpleDateFormat.format(first.internalDate)
                println("Date: $emailDate     Sender: $from     Content: ${first.snippet}...")
            }
        println("------------------------------------------------------------------------------------------")
    }

    internal fun getSent() {
        println("----------------------------------------- SENT ----------------------------------------")
        gmail
                .users()
                .messages()
                .list(userId)
                .setQ("is:sent")
                .setMaxResults(15)
                .execute()
                .messages?.map {
            gmail.users()
                    .messages()
                    .get(userId, it.id)
                    .execute()
        }?.forEach {
            val from = it.payload.headers.filter { header -> header["name"] == "To" }.map { fromHeader -> fromHeader.value }
            val emailDate = simpleDateFormat.format(it.internalDate)
            println("Date: $emailDate     Sender: $from     Content: ${it.snippet}...")
        }
        println("------------------------------------------------------------------------------------------")
    }

    internal fun getFrom(from: String) {
        println("----------------------------------------- SENT ----------------------------------------")
        gmail
                .users()
                .threads()
                .list(userId)
                .setQ("from:$from")
                .setMaxResults(15)
                .execute()
                .threads?.map {
            gmail.users()
                    .threads()
                    .get(userId, it.id)
                    .execute()
        }?.forEach {
            val first = it.messages.first()
            val emailDate = simpleDateFormat.format(first.internalDate)
            println("Date: $emailDate    Content: ${first.snippet}...")
        }
        println("------------------------------------------------------------------------------------------")
    }

}