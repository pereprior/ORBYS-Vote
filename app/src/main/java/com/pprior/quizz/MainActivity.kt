package com.pprior.quizz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pprior.quizz.databinding.ActivityMainBinding
import com.pprior.quizz.ui.fragments.HeadFragment
import com.pprior.quizz.ui.fragments.ListFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val headFragment = HeadFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(binding.headerFragment.id, headFragment)
            .commit()

        val listFragment = ListFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(binding.fragmentListRecyclerView.id, listFragment)
            .commit()
    }
}