package com.orbys.quizz.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.orbys.quizz.databinding.ActivityMainBinding
import com.orbys.quizz.ui.components.cards.OtherCard
import com.orbys.quizz.ui.components.cards.SliderCard
import com.orbys.quizz.ui.components.cards.StarsCard
import com.orbys.quizz.ui.components.cards.YesNoCard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getPermission()

        printFragments()
    }

    private fun printFragments() {
        supportFragmentManager.beginTransaction().apply {
            // Lista de preguntas
            //replace(binding.fragmentListRecyclerView.id, ListFragment())
            commit()
        }
    }

    private fun getPermission() {

        if(!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
        }

    }

}