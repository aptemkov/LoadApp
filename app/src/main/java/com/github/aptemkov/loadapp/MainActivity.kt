package com.github.aptemkov.loadapp

import android.animation.ObjectAnimator
import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.github.aptemkov.loadapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    private lateinit var binding: ActivityMainBinding
    private lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        binding.customButton.setOnClickListener {
            download(url)
        }

        binding.testButton.setOnClickListener {
            colorizer()
        }
        binding.customButton.isClickable = false
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            binding.customButton.isClickable = true

            url = when(checkedId) {
                R.id.rbGlide -> GLIDE_URL
                R.id.rbLoadApp -> LOADAPP_URL
                R.id.rbRetrofit -> RETROFIT_URL
                else -> ""
            }

            Toast.makeText(this, url, Toast.LENGTH_SHORT).show()
        }

    }


    private fun colorizer() {
        val animator = ObjectAnimator.ofArgb(binding.testButton,
            "backgroundColor", Color.BLACK, Color.RED)
        animator.setDuration(500)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.start()
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        }
    }

    private fun download(url: String) {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }


    companion object {

        private const val GLIDE_URL = "https://github.com/bumptech/glide"
        private const val LOADAPP_URL = "https://github.com/aptemkov/LoadApp"
        private const val RETROFIT_URL = "https://github.com/square/retrofit"

        private const val CHANNEL_ID = "channelId"
    }

}
