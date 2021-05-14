package com.intsab.otpfetcher.listeners

import com.intsab.otpfetcher.models.MessageItem

/**
 * Created by intsabhaider
 * on 14,May,2021
 */
interface OtpListener {
    fun onReceived(messageItem: MessageItem)
    fun onTimeOut()
}