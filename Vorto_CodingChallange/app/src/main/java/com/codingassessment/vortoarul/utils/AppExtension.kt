package com.codingassessment.vortoarul.utils

import android.Manifest
import android.Manifest.permission
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.codingassessment.vortoarul.R

object AppExtension {
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.checkLocationPermission() : Boolean {
    if (ContextCompat.checkSelfPermission(this,
                    permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        permission.ACCESS_FINE_LOCATION)){
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1);
        }else{
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1);
        }
        return false
    }
    return true
}

fun Activity.getProgressDialog(): ProgressDialog {
    val progressDialog = ProgressDialog(this,
            R.style.AppTheme_Dark_Dialog)
    progressDialog.isIndeterminate = true
    progressDialog.setMessage("Loading...")
    return progressDialog;
}

fun Context.showToast(value: String) = Toast.makeText(this, value, Toast.LENGTH_LONG).show()