package com.example.aplikasigithubusernavigationdanapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.aplikasigithubusernavigationdanapi.adapter.SectionsPagerAdapter
import com.example.aplikasigithubusernavigationdanapi.data.DetailResponse
import com.example.aplikasigithubusernavigationdanapi.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_follower,
            R.string.tab_text_following
        )
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("Username") ?: "ilyus"

        val sectionsPagerAdapter = SectionsPagerAdapter(this, username)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f


        detailUser(username)
        Toast.makeText(this, username, Toast.LENGTH_SHORT).show()
    }

    private fun detailUser(name: String) {
        binding.progressBar.visibility = View.VISIBLE
        val client = ApiConfig.getApiService().detailUser(name)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                val data = response.body()
                binding.tvUsername.text = data?.login
                binding.tvNamaDetailuser.text = data?.name
                binding.tvCompanyDetailuser.text = data?.company
                binding.tvAddressDetailuser.text = data?.location
                binding.textView2.text = data?.blog
                binding.tvFollowers.text = data?.followers.toString()
                binding.tvFollowing.text = data?.following.toString()
                binding.tvRepository.text = data?.publicRepos.toString()
                Glide.with(binding.imgDetailUser)
                    .load(data?.avatarUrl)
                    .circleCrop()
                    .into(binding.imgDetailUser)
                binding.progressBar.visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                binding.progressBar.visibility = View.INVISIBLE
                Log.d("DetailUserActivity", "Error: ${t.message}")
            }
        })
    }
}