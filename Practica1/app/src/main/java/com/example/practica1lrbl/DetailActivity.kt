package com.example.practica1lrbl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.practica1lrbl.databinding.ActivityDetailBinding
import com.example.practica1lrbl.databinding.ActivityMainBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val receivedValue = intent.extras?.getDouble("result")
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.resultView.text = String.format(getString(R.string.result_label))
        binding.textView.text = receivedValue.toString()
    }
}