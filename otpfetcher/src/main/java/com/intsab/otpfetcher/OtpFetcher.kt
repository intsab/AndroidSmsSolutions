package com.intsab.otpfetcher

import android.content.Context
import android.net.Uri
import android.os.CountDownTimer
import com.intsab.otpfetcher.listeners.OtpListener
import com.intsab.otpfetcher.models.MessageItem

/**
 * Created by intsabhaider
 * on 14,May,2021
 */
class OtpFetcher {



    companion object {

        fun getInstance(tickTime: Long? = null): OtpFetcher {
            if (tickTime != null) {
                this.tickTime = tickTime
            }
            verifyCounter= null
            isMessageReceived=false

            if (fetcher == null) {
                fetcher = OtpFetcher();
            }
            return fetcher!!
        }

        var isMessageReceived: Boolean = false
        private var tickTime: Long = 3000
        private var fetcher: OtpFetcher? = null
        private var verifyCounter: CountDownTimer? = null
    }

    /*
    Get Messages Methods
    */
    fun getLastMessages(context: Context, limit: Int): List<MessageItem> {
        return getInboxMessagesList(context, limit)
    }

    fun getMessagesContaining(context: Context, stringToMatch: String, limit: Int = 0): List<MessageItem> {
        return getLastMessages(context, limit).filter {
            it.message.contains(stringToMatch)
        }
    }

    fun getMessagesBySender(context: Context, sender: String, limit: Int = 0): List<MessageItem> {
        return getLastMessages(context, limit).filter {
            it.sender == sender
        }
    }

    fun getAllMessages(context: Context): List<MessageItem> {
        return getLastMessages(context, 0)
    }

    /*
    OTP  Messages
    */
    fun verifyOtpByMatchingString(context: Context, stringToMatch: String, cutOfTime: Long, otpListener: OtpListener) {
        startMessagesService(context, stringToMatch, null, cutOfTime, otpListener)
    }

    fun verifyOtpByMatchingSender(context: Context, sender: String, cutOfTime: Long, otpListener: OtpListener) {
        startMessagesService(context, null, sender, cutOfTime, otpListener)
    }

    private fun getInboxMessagesList(context: Context, limit: Int): List<MessageItem> {
        try {
            Thread.interrupted()
            val sortOrder = if (limit > 0) {
                "date desc limit $limit"
            } else {
                null
            }

            val cursor = context.contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, sortOrder)
            if (cursor != null && cursor.moveToFirst()) {
                val message: ArrayList<MessageItem> = arrayListOf()
                do {
                    val dateStr = cursor.getString(4)
                    val msgData = cursor.getString(12)
                    val msgId = cursor.getString(0)
                    val msgSender = cursor.getString(2)

                    message.add(MessageItem(msgId.toInt(), msgData, dateStr.toLong(), msgSender))

                } while (cursor.moveToNext())

                return message
            } else {
                /*
                In Case No SMS In Inbox
                */
                return arrayListOf()
            }
        } catch (exp: Exception) {
            /*
               In Case Exception occur
               */
            return arrayListOf()
        }
    }

    private fun startMessagesService(context: Context, stringToMatch: String?, sender: String?, cutOfTime: Long, otpListener: OtpListener) {
        verifyCounter = object : CountDownTimer(cutOfTime, tickTime) {
            override fun onTick(millisUntilFinished: Long) {
                val messagesList = if (stringToMatch != null) {
                    getMessagesContaining(context, stringToMatch, 3).take(1)
                } else {
                    getMessagesBySender(context, sender!!, 3).take(1)
                }

                if (messagesList.isNotEmpty()) {
                    val timeBefore = 10000L
                    val message = messagesList[0]
                    val diffTime: Long = try {
                        System.currentTimeMillis() - message.dateTime
                    } catch (exp: Exception) {
                        -1
                    }
                    if (diffTime < timeBefore && !isMessageReceived) {
                        verifyCounter = null
                        isMessageReceived = true
                        otpListener.onReceived(message)
                    }
                }

            }

            override fun onFinish() {
                otpListener.onTimeOut()
            }
        }
        verifyCounter?.start()
    }
}