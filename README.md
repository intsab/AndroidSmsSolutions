# Android All In One Get SMS Solution
Android all-in-one SMS solution library provides all basic methods to a developer to get SMS messages from inbox. It is the Easiest way to integrate SMS solutions in your application.

The Library Features:
- Integrating verify OTP Feature in your application.
- Get all SMS from the inbox.
- Search all SMS by the same sender.
- Search all SMS with specific text.
- Get **N** number of last messages (By Sender, By Text)
- Easy to integrate and Easy to Use.

### Usage

Add Following Dependencies:

Step 1. Add the JitPack repository to your build file

```groovy
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}

```

  Step 2. Add the dependency

```groovy

dependencies {
	implementation 'com.github.intsab:AndroidSmsSolutions:1.0'
	}

```

### Permissions
Add Following Permissions and start using the library.
```xml
  <uses-permission android:name="android.permission.READ_SMS" />
  <uses-permission android:name="android.permission.RECEIVE_SMS" />
```

##Samples:


**To get Aall the available SMS** in your inbox use method getAllMessages(this) and pass Activity as parameter.

```kotlin
 val messagesList = OtpFetcher.getInstance().getAllMessages(this)
```
To get all SMS From Inbox Heaving text "Hello" Limit Paramater is **optional**
```kotlin
val messagesList = OtpFetcher.getInstance().getMessagesContaining(this, "Hello", 3)

```
Get All SMS from Inbox by Sender "971" Limit Paramater is **optional**
```kotlin
val messagesList = OtpFetcher.getInstance().getMessagesBySender(this, "971", 3)

```
##Get OTP, Verify OTP
This is How to receive OTP from a specific sender. The Method has 4 Params
- Context
- Sender Number
- Time for Listening SMS
-  Listener

```kotlin
 OtpFetcher.getInstance().verifyOtpByMatchingSender(this, "971", 25000, object : OtpListener {
                override fun onReceived(messageItem: MessageItem) {
                    Toast.makeText(this@MainActivity, "" + messageItem, Toast.LENGTH_SHORT).show()

                }

                override fun onTimeOut() {
                    Toast.makeText(this@MainActivity, "TimeOut", Toast.LENGTH_SHORT).show()

                }
            })
```
This is How to receive OTP If Sender Not Confirm But You Know Message Format. The Method has 4 Params
- Context
- Search String
- Time for Listening SMS
-  Listener

```kotlin
OtpFetcher.getInstance().verifyOtpByMatchingString(this, "OTP", 21000, object : OtpListener {
                override fun onReceived(messageItem: MessageItem) {
                    Toast.makeText(this@MainActivity, "" + messageItem, Toast.LENGTH_SHORT).show()
                }

                override fun onTimeOut() {
                    Toast.makeText(this@MainActivity, "TimeOut", Toast.LENGTH_SHORT).show()

                }
            })
```
