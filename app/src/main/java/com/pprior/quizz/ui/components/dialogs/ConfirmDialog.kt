package com.pprior.quizz.ui.components.dialogs

import android.app.AlertDialog
import android.content.Context
import com.pprior.quizz.R

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