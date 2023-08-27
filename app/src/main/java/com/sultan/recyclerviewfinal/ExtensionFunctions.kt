package com.sultan.recyclerviewfinal

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// if u need just custom dialog

fun Activity.showCustomDialog(
    @StyleRes dialogStyle: Int,
    layoutRes: Int,
    positiveButtonId: Int,
    negativeButtonId: Int,
    positiveButtonClick: () -> Unit,
    negativeButtonClick: () -> Unit
) {
    val dialogView = layoutInflater.inflate(layoutRes, null)
    val width = (resources.displayMetrics.widthPixels * 0.90).toInt()

    val dialog = MaterialAlertDialogBuilder(this, dialogStyle)
        .setView(dialogView)
        .setCancelable(true)
        .create()

    // Set the width of the dialog window
    dialog.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)

    val positiveButtonView = dialogView.findViewById<Button>(positiveButtonId)
    val negativeButtonView = dialogView.findViewById<Button>(negativeButtonId)

    positiveButtonView.setOnClickListener {
        positiveButtonClick.invoke()
        dialog.dismiss()
    }

    negativeButtonView.setOnClickListener {
        negativeButtonClick.invoke()
        dialog.dismiss()
    }

    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.show()
}







//-------------------------------------------------------------------------------------------------
// if u need just formal non-custom dialog
fun Context.showAlertDialog(
    positiveButtonLabel: String ,
    negativeButtonLabel: String ,
    title: String ,
    message: String,
    actionOnPositiveButton: () -> Unit,
    actionOnNegativeButton: () -> Unit
) {
    val builder = AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
//        .setCancelable(false)
        .setPositiveButton(positiveButtonLabel) { dialog, _ ->
            dialog.cancel()
            actionOnPositiveButton()
        }
        .setNegativeButton(negativeButtonLabel) { dialog, _ ->
            dialog.cancel()
            actionOnNegativeButton()
        }

    val alert = builder.create()
    alert.show()
}
//--------------------------------------------------------------------------------------------------







fun Context.toast(msg:String, length:Int = Toast.LENGTH_SHORT){
    Toast.makeText(this,msg, length).show()
}
