package com.android.pokedex.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.pokedex.data.model.EvolutionChain
import com.android.pokedex.data.model.evolution.EvolutionResponse
import com.android.pokedex.data.model.repository.EvolutionRepository
import com.android.pokedex.utils.Resource
import kotlinx.coroutines.launch

class EvolutionViewModel(private val evolutionViewModel: EvolutionRepository) : ViewModel() {

    private val mSpeciesResponse = MutableLiveData<Resource<EvolutionChain>>()
    private val mEvolutionResponse = MutableLiveData<Resource<EvolutionResponse>>()

    fun getSpecies(id: Int) {
        mEvolutionResponse.postValue(Resource.loading(null))
        viewModelScope.launch {
            try {
                val response = evolutionViewModel.getSpecies(id)
                mSpeciesResponse.postValue(Resource.success(response))
            } catch (e: Exception) {
                mSpeciesResponse.postValue(Resource.error(e.message.toString(), null))
            }
        }
    }

    fun observeSpecies() = mSpeciesResponse

    fun observeEvolution() = mEvolutionResponse

    fun getEvolutions(id: Int) {
        mEvolutionResponse.postValue(Resource.loading(null))
        viewModelScope.launch {
            try {
                val response = evolutionViewModel.getEvolution(id)
                mEvolutionResponse.postValue(Resource.success(response))
            } catch (e: Exception) {
                mEvolutionResponse.postValue(Resource.error(e.message!!, null))
            }
        }
    }

}