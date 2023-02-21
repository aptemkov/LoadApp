package com.github.aptemkov.loadapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.aptemkov.loadapp.databinding.ActivityDetailBinding
import kotlin.random.Random

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val title = intent.getStringExtra("title").toString()
        val content = intent.getStringExtra("content").toString()

        binding.fileName.text = content

        binding.backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)
        }


        val random = (Math.random() * 2).toInt()
        when(random) {
            1 -> {
                binding.status.text = getString(R.string.succes_message)
                binding.status.setTextColor(Color.BLACK)
            }
            else -> {
                binding.status.text = getString(R.string.failure_message)
                binding.status.setTextColor(Color.RED)
            }
        }
    }

}
