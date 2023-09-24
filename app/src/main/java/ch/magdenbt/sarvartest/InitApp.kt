package ch.magdenbt.sarvartest

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import timber.log.Timber

class InitApp : Application() {

    private val IMPORTANT_NOTIFICATION_CHANNEL_ID = "SarvarTestChannel"
    private val IMPORTANT_NOTIFICATION_CHANNEL_NAME = "Very very important things"

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        createImportantChannel()
    }

    private fun createImportantChannel() {
        val channel = NotificationChannel(
            IMPORTANT_NOTIFICATION_CHANNEL_ID,
            IMPORTANT_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH,
        )
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}
