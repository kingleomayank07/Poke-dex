package com.android.pokedex.ui.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.android.pokedex.R
import com.android.pokedex.utils.Utils.hideShowView
import kotlinx.android.synthetic.main.fragment_stats.*

class StatsFragment : Fragment(R.layout.fragment_stats) {
    private val _fragArgs: StatsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }

    private fun setUpUi() {
        val pokemonData = _fragArgs.pokemonDetails
        if (pokemonData != null && !pokemonData.stats.isNullOrEmpty()) {
            for (i in pokemonData.stats.indices) {
                setViewImage(
                    pokemonData.stats[i].stat.name,
                    pokemonData.stats[i].base_stat
                )
            }
        } else {
            showEmptyMessage()
        }
    }

    private fun showEmptyMessage() {
        skill_empty_tv.text = getString(R.string.no_data)
        skill_empty_tv.hideShowView(true)
    }

    private fun setViewImage(skillType: String?, skillValue: Int?) {
        if (skillType == null || skillValue == null || skillValue == 0)
            return

        val skillView: View =
            LayoutInflater.from(skill_list_container.context)
                .inflate(R.layout.skill_item_for_pokemon_profile, null, false)
        val skillTitleTextView = skillView.findViewById<TextView>(R.id.skill_title_txt)
        val skill_title_valueView = skillView.findViewById<TextView>(R.id.skill_title_value)
        val skillValueImgView = skillView.findViewById<ProgressBar>(R.id.skill_value_iv)
        val skillValueEmptyView = skillView.findViewById<View>(R.id.skill_value_view_empty)

        skillTitleTextView.text = skillType
        if (skillValue != 0) {
            skillValueEmptyView.hideShowView(false)
            skillValueImgView.max = 100
            skill_title_valueView.text = skillValue.toString().plus("pt")
            skillValueImgView.progress = skillValue
            skillValueImgView.progressTintList =
                ColorStateList.valueOf(_fragArgs.color)
        } else {
            skillValueImgView.hideShowView(false)
            skillValueEmptyView.hideShowView(true)
        }
        skill_list_container.addView(skillView)
    }


}