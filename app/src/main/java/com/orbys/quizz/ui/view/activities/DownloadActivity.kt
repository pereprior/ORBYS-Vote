package com.orbys.quizz.ui.view.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.orbys.quizz.databinding.ActivityMainBinding
import com.orbys.quizz.ui.view.fragments.DownloadFragment
import com.orbys.quizz.ui.services.FloatingViewService
import dagger.hilt.android.AndroidEntryPoint

class DownloadActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val STORAGE_PERMISSION_CODE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stopService(Intent(this, FloatingViewService::class.java))

        checkPermission()
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            storagePermissionGranted()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    storagePermissionGranted()
                } else {
                    storagePermissionDenied()
                }
            }
        }
    }

    private fun storagePermissionGranted() {
        Toast.makeText(this, "Storage permission granted", Toast.LENGTH_SHORT).show()

        // Inflar la vista y mostrar los fragmentos después de que se haya concedido el permiso
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().apply {
            // Fragmento con los tipos de preguntas
            replace(binding.fragmentContainer.id, DownloadFragment())
            commit()
        }
    }

    private fun storagePermissionDenied() {
        startService(Intent(this, FloatingViewService::class.java))

        Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show()
        finish()
    }

}