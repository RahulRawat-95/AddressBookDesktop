// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.push
import com.library.address.di.initKodein
import com.rawat.address.viewModel.MainViewModel
import navigation.Screen
import navigation.rememberRouter
import org.kodein.di.DI
import window.AddUpdateContent
import window.MainActivityContent
import window.SplashContent

fun main() = application {
    val di: DI = initKodein()

    MainWindow(di = di, appScope = this)
}

@Composable
@Preview
fun MainWindow(di: DI, appScope: ApplicationScope) {
    val viewModel = MainViewModel(di = di)
    viewModel.startFetching()
    viewModel.sync()

    Window(
        title = "Address Desktop",
        onCloseRequest = {
            viewModel.clear()
            appScope.exitApplication()
        }) {
        MaterialTheme {
            val router =
                rememberRouter<Screen>(
                    initialConfiguration = { Screen.Splash }
                )

            Children(
                routerState = router.state
            ) { screen ->
                when (val configuration = screen.configuration) {
                    is Screen.Splash -> {
                        SplashContent {
                            router.push(Screen.ListAddresses)
                        }
                    }
                    is Screen.ListAddresses -> {
                        viewModel.pauseSyncing = false
                        MainActivityContent(
                            viewModel = viewModel,
                            onDeleteClick = {
                                viewModel.deleteAddress(address = it)
                            }, onUpdateClick = {
                                router.push(Screen.AddUpdateAddress(address = it))
                            }, onAddClick = {
                                router.push(Screen.AddUpdateAddress(address = null))
                            }
                        )
                    }
                    is Screen.AddUpdateAddress -> {
                        viewModel.pauseSyncing = true
                        AddUpdateContent(
                            viewModel = viewModel,
                            address = configuration.address,
                            onSubmit = { map, isDefault ->
                                viewModel.addOrUpdateAddress(configuration.address, map, isDefault)
                                router.pop()
                            }
                        )
                    }
                }
            }
        }
    }
}