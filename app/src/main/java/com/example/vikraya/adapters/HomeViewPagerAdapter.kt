package com.example.vikraya.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.play.integrity.internal.f

class HomeViewPagerAdapter(
    private val fragment:List<Fragment>,
    fm:FragmentManager,
    lifecycle: Lifecycle,

):FragmentStateAdapter(fm,lifecycle) {
    override fun getItemCount(): Int {
        return fragment.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragment[position]
    }


}