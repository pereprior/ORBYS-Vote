package com.orbys.quizz.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.orbys.quizz.core.network.NetworkManager
import com.orbys.quizz.core.permissions.PermissionManager
import com.orbys.quizz.databinding.ActivityMainBinding
import com.orbys.quizz.ui.view.fragments.DownloadFragment
import com.orbys.quizz.ui.view.fragments.TypesQuestionFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

/**
 * Actividad principal de la aplicación
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var networkManager: NetworkManager
    private lateinit var permissionManager: PermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionManager = PermissionManager(this)
        networkManager = NetworkManager()

        // Comprobar si hay conexión a Internet
        networkManager.checkNetwork(this)

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

            closeButton.setOnClickListener {
                finish()
                exitProcess(0)
            }

            supportFragmentManager.beginTransaction().apply {
                val fragment = if (intent.getBooleanExtra("SHOW_DOWNLOAD_FRAGMENT", false)) {
                    DownloadFragment()
                } else {
                    TypesQuestionFragment()
                }
                replace(fragmentContainer.id, fragment)
                commit()
            }

        }

    }

}