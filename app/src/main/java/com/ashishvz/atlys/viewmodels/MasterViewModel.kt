package com.ashishvz.atlys.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MasterViewModel: ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
}