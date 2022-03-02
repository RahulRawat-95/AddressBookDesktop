package window

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.library.address.database.Address
import com.rawat.address.extensions.concatenate
import com.rawat.address.viewModel.MainViewModel
import util.AsyncImage
import util.getIconsFolderPath
import util.loadXmlImageVector
import java.io.File

@Composable
fun MainActivityContent(
    viewModel: MainViewModel,
    onDeleteClick: (Address) -> Unit,
    onUpdateClick: (Address) -> Unit,
    onAddClick: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val addresses by viewModel.addressFlow.collectAsState(initial = listOf())
    val showProgress by viewModel.showProgressBar.collectAsState(false)

    Surface(color = MaterialTheme.colors.background) {
        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Row {
                        Text(
                            text = "Addresses",
                            modifier = Modifier.padding(all = 10.dp)
                        )
                    }
                })
            },
            scaffoldState = scaffoldState,
            snackbarHost = { scaffoldState.snackbarHostState }) {
            Box(modifier = Modifier.fillMaxSize()) {
                Addresses(
                    addresses = addresses,
                    onDeleteClick = onDeleteClick,
                    onUpdateClick = onUpdateClick,
                    onAddClick = onAddClick
                )
                LoadingBar(showProgress = showProgress)
            }
        }
    }
}

@Composable
fun Addresses(
    addresses: List<Address>,
    onDeleteClick: (Address) -> Unit,
    onUpdateClick: (Address) -> Unit,
    onAddClick: () -> Unit
) {
    if (addresses.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Your address book is blank",
                fontSize = 20.sp,
                modifier = Modifier.padding(10.dp)
            )
            Text(
                text = "Kindly add shipping / billing address and enjoy faster checkout",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 80.dp, end = 80.dp, top = 10.dp)
            )
            AddFabComposable(modifier = Modifier, onAddClick = onAddClick)
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(addresses.size) {
                    AddressComposable(
                        address = addresses[it],
                        onDeleteClick = onDeleteClick,
                        onUpdateClick = onUpdateClick
                    )
                }
            }
            AddFabComposable(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 30.dp, end = 30.dp),
                onAddClick = onAddClick
            )
        }
    }
}

@Composable
fun AddFabComposable(modifier: Modifier, onAddClick: () -> Unit) {
    val density = LocalDensity.current
    FloatingActionButton(
        backgroundColor = Color(0xFFFF9800),
        modifier = modifier
            .padding(top = 20.dp)
            .size(40.dp),
        onClick = {
            onAddClick()
        }) {
        AsyncImage(
            load = { loadXmlImageVector(File("${getIconsFolderPath()}ic_plus.xml"), density) },
            painterFor = { rememberVectorPainter(it) },
            contentDescription = "Add Address",
            tintColor = Color.White,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun AddressComposable(
    address: Address,
    onDeleteClick: (Address) -> Unit = {},
    onUpdateClick: (Address) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    val density = LocalDensity.current

    Column {

        Row {
            Column(
                modifier = Modifier
                    .weight(1F)
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                OnlyNonBlankText(
                    address.firstname, address.lastname,
                    maxLines = 1
                )
                OnlyNonBlankText(
                    address.address1, address.address2,
                    maxLines = 2
                )
                OnlyNonBlankText(
                    address.city, address.zipcode,
                    maxLines = 1
                )
                OnlyNonBlankText(
                    address.phone,
                    maxLines = 1
                )
            }

            Column {
                Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                        onClick = {
                            expanded = true
                        },
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .size(width = 40.dp, height = 40.dp)
                    ) {
                        AsyncImage(
                            load = { loadXmlImageVector(File("${getIconsFolderPath()}ic_popup.xml"), density) },
                            painterFor = { rememberVectorPainter(it) },
                            contentDescription = "Menu",
                            tintColor = Color.Black,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        DropdownMenuItem(onClick = {
                            expanded = false
                            onDeleteClick(address)
                        }) {
                            Text("Delete")
                        }
                        DropdownMenuItem(onClick = {
                            expanded = false
                            onUpdateClick(address)
                        }) {
                            Text("Update")
                        }
                    }
                }

                Divider(modifier = Modifier.width(0.dp).height(20.dp))

                if (address.is_default == true) {
                    AsyncImage(
                        load = { loadXmlImageVector(File("${getIconsFolderPath()}ic_check.xml"), density) },
                        painterFor = { rememberVectorPainter(it) },
                        contentDescription = "Menu",
                        tintColor = Color(0xFFFF9800),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.align(Alignment.CenterHorizontally).size(20.dp)
                    )
                }
            }
        }

        Divider(
            modifier = Modifier
                .background(color = MaterialTheme.colors.onBackground)
                .fillMaxWidth()
                .height(1.dp)
        )
    }
}

@Composable
fun OnlyNonBlankText(vararg text: String?, maxLines: Int, modifier: Modifier = Modifier) {
    val textToDisplay = text.concatenate()
    if (textToDisplay.isNotBlank()) {
        Text(
            text = textToDisplay,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier,
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun LoadingBar(showProgress: Boolean) {
    if (showProgress) {
        val interactionSource = remember { MutableInteractionSource() }

        Box(modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { }) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
