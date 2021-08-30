package com.android.pokedex.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.pokedex.data.model.pokemondetailsmodels.PokemonDetails
import com.android.pokedex.ui.fragments.AboutFragment
import com.android.pokedex.ui.fragments.EvolutionFragment
import com.android.pokedex.ui.fragments.MovesFragment
import com.android.pokedex.ui.fragments.StatsFragment

class ViewPagerAdapter(
    fragment: Fragment,
    private val count: Int,
    private val pokemonDetails: PokemonDetails?,
    private val pokemonId: Int?,
    private val colorInt: Int,
) : FragmentStateAdapter(fragment) {

    private val mFragList: ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int = count

    override fun createFragment(position: Int): Fragment {
        return mFragList[position]
    }

    fun addFragment(fragment: Fragment) {
        when (fragment::class.simpleName) {
            AboutFragment::class.simpleName -> {
                fragment.arguments = Bundle().apply {
                    putParcelable("pokemon_details", pokemonDetails)
                    putInt("color", colorInt)
                }
            }
            EvolutionFragment::class.simpleName -> {
                fragment.arguments = Bundle().apply {
                    putInt("pokemon_id", pokemonId ?: 0)
                    putInt("color", colorInt)
                    putString("pokemon_name", pokemonDetails?.name)
                }
            }
            MovesFragment::class.java.simpleName -> {
                fragment.arguments = Bundle().apply {
                    putParcelable("pokemon_details", pokemonDetails)
                }
            }
            StatsFragment::class.java.simpleName -> {
                fragment.arguments = Bundle().apply {
                    putParcelable("pokemon_details", pokemonDetails)
                    putInt("color", colorInt)
                }
            }
        }
        mFragList.add(fragment)
    }

    /*fun getTabView(position: Int): View {
        val v: View =
            LayoutInflater.from(fragment.context).inflate(R.layout.layout_custom_tab, null)
        val tv = v.findViewById(R.id.text_tab) as TextView
        tv.text = TAB_LIST[position]
        if (position == 0) {
            tv.setTextColor(
                ContextCompat.getColor(
                    fragment.requireContext(),
                    R.color.white
                )
            )
        }
        return v
    }*/

}