package com.kimoterru.jinttest.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kimoterru.jinttest.BinApplication
import com.kimoterru.jinttest.data.local.BinDatabase
import com.kimoterru.jinttest.data.local.model.BinEntity
import com.kimoterru.jinttest.data.remote.NetworkHelper
import com.kimoterru.jinttest.data.remote.model.BinResponse
import com.kimoterru.jinttest.ui.util.Resource
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val binRemoteMutableLiveData = MutableLiveData<Resource<BinResponse>>()
    val binRemoteLiveData: LiveData<Resource<BinResponse>> get() =  binRemoteMutableLiveData

    private val binLocalMutableLiveData = MutableLiveData<List<BinEntity>>()
    val binLocalLiveData: LiveData<List<BinEntity>> get() =  binLocalMutableLiveData

    private val networkApi = NetworkHelper().getService()
    private val db: BinDatabase = BinApplication.instance.db
    private val binDao = db.provideBinDao()

    val mutableBinNumber = MutableLiveData<Int>()
    private val defaultBin = 45717360

    init {
        getAllBinsFromDB()
        getBINFromBank(defaultBin)
    }

    fun getBINFromBank(binNumber: Int) = viewModelScope.launch {
        binRemoteMutableLiveData.postValue(Resource.loading())
        try {
            val remoteData = networkApi.getBINBankCard(binNumber)
            binRemoteMutableLiveData.postValue(Resource.success(remoteData))
        } catch (e: Exception) {
            binRemoteMutableLiveData.postValue(Resource.error(e.message.toString()))
        }
    }

    fun insertBinInDB(binNumber: Long) = viewModelScope.launch {
        binDao.insertBin(BinEntity(binNumber = binNumber))
        getAllBinsFromDB()
    }

    fun getBinFromDb(idBin: Int) = viewModelScope.launch {
        mutableBinNumber.postValue(binDao.getBinById(id = idBin).binNumber.toInt())
    }

    private fun getAllBinsFromDB() = viewModelScope.launch {
        val localData = binDao.getAllBins()
        binLocalMutableLiveData.postValue(localData)
    }

}