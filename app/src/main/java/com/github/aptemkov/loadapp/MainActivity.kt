package com.github.aptemkov.loadapp

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.aptemkov.loadapp.databinding.ActivityMainBinding
import com.github.aptemkov.loadapp.notifications.NotificationService

class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private lateinit var binding: ActivityMainBinding
    private var chosenOption: OPTIONS = OPTIONS.ERROR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val service = NotificationService(applicationContext)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

//        binding.testButton.setOnClickListener {
//            colorize()
//        }

        binding.customButton.isClickable = false

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            binding.customButton.isClickable = true
            binding.customButton.visibility = View.VISIBLE

            chosenOption = when (checkedId) {
                R.id.rbGlide -> OPTIONS.GLIDE
                R.id.rbLoadApp -> OPTIONS.LOADAPP
                R.id.rbRetrofit -> OPTIONS.RETROFIT
                else -> OPTIONS.ERROR
            }
        }

        binding.customButton.setOnClickListener {
            download(
                chosenOption.url
                //url = "https://github.com/bumptech/glide"
            )
            service.showNotification(chosenOption.title, chosenOption.content)
        }
    }

    /* used for testing color animation

        private fun colorize() {
            val animator = ObjectAnimator.ofArgb(binding.testButton,
                "backgroundColor", Color.BLACK, Color.RED)
            animator.setDuration(500)
            animator.repeatCount = 1
            animator.repeatMode = ObjectAnimator.REVERSE
            animator.start()
        }

    */
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        }
    }

    private fun download(url: String) {
        val request =
            DownloadManager.Request(Uri.parse("https://github.com/bumptech/glide"))
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

        private enum class OPTIONS(val title: String, val content: String, val url: String) {
            GLIDE (
                title = "GLIDE",
                content = "Glide: Image Loading Library By BumpTech",
                url = "https://github.com/bumptech/glide",
            ),
            LOADAPP (
                title = "LOADAPP",
                content = "LoadApp - current repository by Udacity",
                url = "https://github.com/aptemkov/LoadApp",
            ),
            RETROFIT (
                title = "RETROFIT",
                content = "Retrofit: Type-safe HTTP client by Square, Inc",
                url = "https://github.com/square/retrofit",
            ),
            ERROR (
                title = "ERROR",
                content = "Something went wrong...",
                url = "https://github.com/",
            ),
        }
    }

}