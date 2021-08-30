package com.android.pokedex.utils

import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Spanned
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import androidx.core.text.HtmlCompat
import androidx.navigation.NavController
import androidx.palette.graphics.Palette
import com.android.pokedex.R
import com.android.pokedex.utils.Constants.pokemon_name_url
import com.bumptech.glide.Glide

object Utils {

    fun String.getPokemonImgFromNameOrId(): String {
        return "${pokemon_name_url}$this.png"
    }

    fun String?.fromHtml(): Spanned {
        return HtmlCompat.fromHtml(this ?: "", HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    fun NavController.killAppIfHome(activity: Activity) {

    }

    fun ImageView.url(url: String) {
        try {
            Glide.with(this.context).load(url).into(this)
        } catch (e: Exception) {
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun View.getDominantColor(resource: Drawable?): Int {
        val palette = Palette.Builder(resource?.toBitmap()!!).generate()
        return palette.getMutedColor(
            palette.getVibrantColor(
                this.context.getColor(
                    R.color.colorSecondary
                )
            )
        )
    }

    fun View.hideShowView(show: Boolean) {
        if (show) {
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.GONE
        }
    }

    fun EditText.onSearch(func: () -> Unit) {
        setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                func()
            }
            true
        }
    }

    fun Activity.hideKeyboard() {
        val imm: InputMethodManager =
            this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = this.currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}