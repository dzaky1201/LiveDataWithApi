package com.dzakyhdr.livedatawithapi

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.dzakyhdr.livedatawithapi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        mainViewModel.findRestaurant()
        mainViewModel.restaurant.observe(this, { restaurant ->
            binding.tvTitle.text = restaurant.name
            binding.tvDescription.text = "${restaurant.description.take(100)}..."
            Glide.with(this)
                .load("https://restaurant-api.dicoding.dev/images/large/${restaurant.pictureId}")
                .into(binding.ivPicture)
        })

        mainViewModel.listReview.observe(this, { consumerReview ->
            val listReview = consumerReview.map {
                "${it.review}\n- ${it.name}"
            }

            binding.lvReview.adapter = ArrayAdapter(this, R.layout.item_review, listReview)
            binding.edReview.setText("")
        })

        mainViewModel.loading.observe(this, {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })

        binding.btnSend.setOnClickListener {
            mainViewModel.postReview(binding.edReview.text.toString())
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}