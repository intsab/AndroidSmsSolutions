package com.intsab.sample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.intsab.otpfetcher.OtpFetcher
import com.intsab.otpfetcher.listeners.OtpListener
import com.intsab.otpfetcher.models.MessageItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        allMessages.setOnClickListener {
            val messagesList = OtpFetcher.getInstance().getAllMessages(this)
            Toast.makeText(this@MainActivity, "" + messagesList, Toast.LENGTH_SHORT).show()

        }

        last3Messages.setOnClickListener {
            val messagesList = OtpFetcher.getInstance().getLastMessages(this, 3)
            Toast.makeText(this@MainActivity, "" + messagesList, Toast.LENGTH_SHORT).show()

        }

        getAllBySender.setOnClickListener {
            val messagesList = OtpFetcher.getInstance().getMessagesBySender(this, "971", 3)
            Toast.makeText(this@MainActivity, "" + messagesList, Toast.LENGTH_SHORT).show()

        }

        getAllContain.setOnClickListener {
            val messagesList = OtpFetcher.getInstance().getMessagesContaining(this, "OTP", 3)
            Toast.makeText(this@MainActivity, "" + messagesList, Toast.LENGTH_SHORT).show()

        }

        verifyOtpBySender.setOnClickListener {
            OtpFetcher.getInstance().verifyOtpByMatchingSender(this, "971", 25000, object : OtpListener {
                override fun onReceived(messageItem: MessageItem) {
                    Toast.makeText(this@MainActivity, "" + messageItem, Toast.LENGTH_SHORT).show()

                }

                override fun onTimeOut() {
                    Toast.makeText(this@MainActivity, "TimeOut", Toast.LENGTH_SHORT).show()

                }
            })
        }

        verifyOtpMatchString.setOnClickListener {
            OtpFetcher.getInstance().verifyOtpByMatchingString(this, "OTP", 21000, object : OtpListener {
                override fun onReceived(messageItem: MessageItem) {
                    Toast.makeText(this@MainActivity, "" + messageItem, Toast.LENGTH_SHORT).show()
                }

                override fun onTimeOut() {
                    Toast.makeText(this@MainActivity, "TimeOut", Toast.LENGTH_SHORT).show()

                }
            })
        }
    }
}