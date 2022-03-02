package navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.arkivanov.decompose.router.Router
import com.arkivanov.decompose.router.router
import com.arkivanov.essenty.parcelable.Parcelable
import kotlin.reflect.KClass

@Composable
fun <C : Parcelable> rememberRouter(
    initialConfiguration: () -> C,
    configurationClass: KClass<out C>
): Router<C, Any> {
    val context = rememberComponentContext()

    return remember {
        context.router(
            initialConfiguration = initialConfiguration,
            configurationClass = configurationClass
        ) { configuration, _ -> configuration }
    }
}

@Composable
inline fun <reified C : Parcelable> rememberRouter(
    noinline initialConfiguration: () -> C
): Router<C, Any> =
    rememberRouter(
        initialConfiguration = initialConfiguration,
        configurationClass = C::class
    )