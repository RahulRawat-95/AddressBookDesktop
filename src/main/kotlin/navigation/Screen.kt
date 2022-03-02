package navigation

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.library.address.database.Address

sealed class Screen: Parcelable {
    @Parcelize
    object Splash: Screen()
    @Parcelize
    object ListAddresses: Screen()
    @Parcelize
    data class AddUpdateAddress(val address: Address?): Screen()
}