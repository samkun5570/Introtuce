package com.example.introtuceapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.introtuceapp.adapter.ViewPagerAdapter
import com.example.introtuceapp.main_fragments.fragment_user_add
import com.example.introtuceapp.main_fragments.fragment_user_list
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
lateinit var tabLayoutmain: TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //creating fragment list
        val fragments:ArrayList<Fragment> = arrayListOf(
            fragment_user_list(),
            fragment_user_add(),
        )

        //connecting viewpager2 adapter
        val adapter = ViewPagerAdapter(fragments,this)
        viewPager_container.adapter = adapter
    }

    override fun onBackPressed() {
        if (viewPager_container.currentItem == 0){
            super.onBackPressed()
        }
        else{
            viewPager_container.currentItem = viewPager_container.currentItem - 1
        }

    }
}