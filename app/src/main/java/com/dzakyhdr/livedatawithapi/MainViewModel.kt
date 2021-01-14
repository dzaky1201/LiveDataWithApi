package com.dzakyhdr.livedatawithapi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dzakyhdr.livedatawithapi.data.ApiConfig
import com.dzakyhdr.livedatawithapi.model.CustomerReviewsItem
import com.dzakyhdr.livedatawithapi.model.PostReviewResponse
import com.dzakyhdr.livedatawithapi.model.Restaurant
import com.dzakyhdr.livedatawithapi.model.RestaurantResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _restaurant = MutableLiveData<Restaurant>()
    val restaurant: LiveData<Restaurant> = _restaurant

    private val _listReview = MutableLiveData<List<CustomerReviewsItem>>()
    val listReview: LiveData<List<CustomerReviewsItem>> = _listReview

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    companion object {
        private const val TAG = "MainViewModel"
        private const val RESTAURANT_ID = "uewq1zg2zlskfw1e867"
    }

    fun findRestaurant() {
        _loading.value = true
        val client = ApiConfig.getApiService().getRestaurant(RESTAURANT_ID)
        client.enqueue(object : Callback<RestaurantResponse> {
            override fun onResponse(
                call: Call<RestaurantResponse>,
                response: Response<RestaurantResponse>
            ) {
                _loading.value = false
                if (response.isSuccessful) {
                    _restaurant.value = response.body()?.restaurant
                    _listReview.value = response.body()?.restaurant?.customerReviews
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RestaurantResponse>, t: Throwable) {
                _loading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun postReview(review: String) {
        _loading.value = true
        val client = ApiConfig.getApiService().postReview(RESTAURANT_ID, "Dicoding", review)
        client.enqueue(object : Callback<PostReviewResponse> {
            override fun onResponse(
                call: Call<PostReviewResponse>,
                response: Response<PostReviewResponse>
            ) {
                _loading.value = false
                if (response.isSuccessful) {
                    _listReview.value = response.body()?.customerReviews
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }

            }

            override fun onFailure(call: Call<PostReviewResponse>, t: Throwable) {
                _loading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}