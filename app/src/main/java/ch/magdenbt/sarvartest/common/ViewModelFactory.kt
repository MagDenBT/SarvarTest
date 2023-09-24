package ch.magdenbt.sarvartest.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

typealias ViewModelCreator<VM> = () -> VM

@Suppress("UNCHECKED_CAST")
class ViewModelFactory<VM : ViewModel> (private val creator: ViewModelCreator<VM>) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return creator() as T
    }
}

inline fun <reified VM : ViewModel> Fragment.viewModelCreator(noinline creator: ViewModelCreator<VM>): Lazy<VM> {
    return viewModels { ViewModelFactory(creator) }
}
