package com.rawat.address.viewModel

import com.library.address.repository.Repo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.kodein.di.DI
import org.kodein.di.instance

open class BaseViewModel(di: DI) {
    protected val repo: Repo by di.instance()
    protected val viewModelScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun clear() {
        viewModelScope.cancel()
    }
}