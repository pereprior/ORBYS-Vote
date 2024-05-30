package com.orbys.vote.ui.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.orbys.vote.R
import com.orbys.vote.core.extensions.isNetworkAvailable
import com.orbys.vote.core.extensions.showToastWithCustomView
import com.orbys.vote.core.managers.PermissionManager
import com.orbys.vote.databinding.ActivityMainBinding
import com.orbys.vote.ui.view.fragments.DownloadFragment
import com.orbys.vote.ui.view.fragments.TypesQuestionFragment
import com.orbys.vote.ui.viewmodels.QuestionViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Actividad principal de la aplicación
 *
 * @property viewModel Contiene los datos de las preguntas las funciones para poder gestionarlas
 * @property binding Referencia a la vista de la actividad
 * @property permissionManager Gestor de los permisos necesarios para la aplicación
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
        if(!isNetworkAvailable())
            showToastWithCustomView(getString(R.string.no_network_error), Toast.LENGTH_LONG)

        // Verificar si tenemos los permisos necesarios
        permissionManager.checkAndRequestPermissions()

        // Mostrar la vista
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        printFragments()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Si se han concedido los permisos necesarios, mostrar los fragmentos
        permissionManager
            .onRequestPermissionsResult(requestCode, grantResults) {
                printFragments()
            }
    }

    /**
     * Imprime los fragmentos en la actividad
     *
     * Si se ha pasado un extra en el intent, se mostrará el fragmento de descarga del fichero
     * en caso contrario, se mostrará el fragmento de selección de tipos de preguntas
     */
    private fun printFragments() {

        supportFragmentManager.beginTransaction().apply {
            val fragment = if (intent.getBooleanExtra("SHOW_DOWNLOAD_FRAGMENT", false)) {
                // Mostrar el fragmento de descarga del fichero
                DownloadFragment(viewModel)
            } else {
                // Mostrar el fragmento de selección de tipos de preguntas
                TypesQuestionFragment(viewModel)
            }
            replace(binding.fragmentContainer.id, fragment)
            commit()
        }
    }

}