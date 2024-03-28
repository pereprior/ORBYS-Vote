package com.pprior.quizz.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pprior.quizz.databinding.ActivityQuestionTypeBinding

class QuestionTypeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}