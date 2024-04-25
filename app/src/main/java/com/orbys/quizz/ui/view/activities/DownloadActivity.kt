package com.orbys.quizz.ui.view.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.orbys.quizz.databinding.ActivityMainBinding
import com.orbys.quizz.ui.components.showToastWithCustomView
import com.orbys.quizz.ui.services.FloatingViewService
import com.orbys.quizz.ui.view.fragments.DownloadFragment

/**
 * Actividad que se encarga de proporcionar los permisos para descargar las preguntas del servidor
 *
 * @property binding Vista de la actividad
 */
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
            // Solicita el permiso de almacenamiento si no se ha concedido.
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
        // Inflar la vista y mostrar los fragmentos después de que se haya concedido el permiso
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().apply {
            replace(binding.fragmentContainer.id, DownloadFragment())
            commit()
        }
    }

    private fun storagePermissionDenied() {
        startService(Intent(this, FloatingViewService::class.java))

        // Muestra un mensaje indicando que se ha denegado el permiso de almacenamiento.
        this.showToastWithCustomView(getString(com.orbys.quizz.R.string.storage_permission_denied), Toast.LENGTH_LONG)
        finish()
    }

}