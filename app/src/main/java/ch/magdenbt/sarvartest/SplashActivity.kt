package ch.magdenbt.sarvartest

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class SplashActivity : AppCompatActivity() {

    private val requestNotificationPermissionLauncher = registerForActivityResult(
        RequestPermission(),
    ) {
        onGotNotificationPermissionResult(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (!hasNotificationPermisssion()) {
            requestNotificationPermissionLauncher.launch(POST_NOTIFICATIONS)
        }
    }

    private fun hasNotificationPermisssion() =
        ContextCompat.checkSelfPermission(
            this,
            POST_NOTIFICATIONS,
        ) == PackageManager.PERMISSION_GRANTED

    private fun onGotNotificationPermissionResult(isGranted: Boolean) {
        if (isGranted) {
            showShortToast(R.string.permission_allowed)
        } else {
            if (!shouldShowRequestPermissionRationale(POST_NOTIFICATIONS)) {
                askUserForOpeningAppSettings()
            } else {
                showShortToast(R.string.permission_denied)
            }
        }
    }

    private fun askUserForOpeningAppSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null),
        )
        if (packageManager.resolveActivity(
                appSettingsIntent,
                PackageManager.MATCH_DEFAULT_ONLY,
            ) == null
        ) {
            showShortToast(R.string.permission_denied_forever_message)
        } else {
            createAndShowDialogForSystemNotificationSettings(appSettingsIntent)
        }
    }

    private fun createAndShowDialogForSystemNotificationSettings(appSettingsIntent: Intent) {
        AlertDialog.Builder(this).setTitle(R.string.permission_denied)
            .setMessage(R.string.permission_denied_forever_message)
            .setPositiveButton(R.string.open) { _, _ ->
                startActivity(appSettingsIntent)
            }.create().show()
    }

    private fun showShortToast(resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }
}
