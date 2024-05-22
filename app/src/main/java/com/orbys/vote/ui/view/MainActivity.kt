package com.orbys.vote.ui.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.orbys.vote.core.managers.PermissionManager
import com.orbys.vote.databinding.ActivityMainBinding
import com.orbys.vote.ui.view.fragments.DownloadFragment
import com.orbys.vote.ui.view.fragments.TypesQuestionFragment
import com.orbys.vote.ui.viewmodels.QuestionViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Actividad principal de la aplicación
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<QuestionViewModel>()

    private lateinit var binding: ActivityMainBinding
    private lateinit var permissionManager: PermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionManager = PermissionManager(this)

        // Comprobar si hay conexión a Internet
        viewModel.checkNetworkOnActivity(this)
        // Verificar si tenemos los permisos necesarios
        permissionManager.checkAndRequestPermissions()

        // Mostrar la vista
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        printFragments()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        permissionManager
            .onRequestPermissionsResult(requestCode, grantResults) {
                printFragments()
            }
    }

    private fun printFragments() {

        with(binding) {

            supportFragmentManager.beginTransaction().apply {
                val fragment = if (intent.getBooleanExtra("SHOW_DOWNLOAD_FRAGMENT", false)) {
                    // Mostrar el fragmento de descarga del fichero
                    DownloadFragment(viewModel)
                } else {
                    // Mostrar el fragmento de selección de tipos de preguntas
                    TypesQuestionFragment(viewModel)
                }
                replace(fragmentContainer.id, fragment)
                commit()
            }

        }

    }

}