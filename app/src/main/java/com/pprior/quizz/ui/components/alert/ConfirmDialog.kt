package com.pprior.quizz.ui.components.alert

import android.app.AlertDialog
import android.content.Context
import com.pprior.quizz.R

/**
 * ConfirmDialog es una clase que se utiliza para mostrar un diálogo para confirmar alguna opcion que este a punto de realizar el usuario.
 *
 * @property context El contexto en el que se muestra el diálogo.
 * @property message El mensaje que se muestra en el diálogo.
 * @property confirmAction La acción que se realiza cuando se confirma el diálogo.
 */
class ConfirmDialog(
    private val context: Context,
    private val message: String,
    private val confirmAction: () -> Unit
) {

    private val builder = AlertDialog.Builder(context)

    init {
        show()
    }

    private fun show() {
        builder.setMessage(message)
            .setPositiveButton(context.getString(R.string.positive_button_message)) { _, _ ->
                confirmAction()
            }
            .setNegativeButton(context.getString(R.string.negative_button_message), null)
            .create()
            .show()
    }

}