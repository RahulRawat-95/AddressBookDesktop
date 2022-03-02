package window

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import com.library.address.database.Address
import com.rawat.address.viewModel.MainViewModel
import composable.Field
import composable.FormState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import util.Length
import util.Required

@Composable
fun AddUpdateContent(
    viewModel: MainViewModel,
    address: Address?,
    onSubmit: (Map<String, String>, Boolean) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val state by remember { mutableStateOf(FormState()) }
    val scope = rememberCoroutineScope()
    val checkedState = remember { mutableStateOf(address?.is_default ?: false) }
    val showProgress by viewModel.showProgressBar.collectAsState(false)

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Row {
                    Text(
                        text = if (address == null) "Create Address" else "Update Address",
                        modifier = Modifier.padding(all = 10.dp)
                    )
                }
            })
        },
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background)
        ) {
            FormContainer(
                addressSerialized = address,
                state = state,
                scope = scope,
                checkedState = checkedState,
                isUpdate = address != null,
                onCheckedChange = { checkedState.value = it },
                onSubmit = onSubmit
            )
            LoadingBar(showProgress = showProgress)
        }
    }
}

@Composable
fun FormContainer(
    addressSerialized: Address?,
    state: FormState,
    scope: CoroutineScope,
    checkedState: MutableState<Boolean>,
    isUpdate: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onSubmit: (Map<String, String>, Boolean) -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .onFocusChanged {

            }
    ) {
        val data = state.getData()
        Form(
            state = state,
            fields = listOf(
                Field(
                    name = MainViewModel.KEY_FIRSTNAME,
                    label = "First Name",
                    initialText = data[MainViewModel.KEY_FIRSTNAME] ?: addressSerialized?.firstname,
                    validators = listOf(Required())
                ),
                Field(
                    name = MainViewModel.KEY_LASTNAME,
                    label = "Last Name",
                    initialText = data[MainViewModel.KEY_LASTNAME] ?: addressSerialized?.lastname,
                    validators = listOf(Required())
                ),
                Field(
                    name = MainViewModel.KEY_ADDRESS_1,
                    label = "Address Line 1",
                    initialText = data[MainViewModel.KEY_ADDRESS_1] ?: addressSerialized?.address1,
                    validators = listOf(Required())
                ),
                Field(
                    name = MainViewModel.KEY_ADDRESS_2,
                    label = "Address Line 2",
                    initialText = data[MainViewModel.KEY_ADDRESS_2] ?: addressSerialized?.address2,
                    validators = listOf(Required())
                ),
                Field(
                    name = MainViewModel.KEY_CITY,
                    label = "City",
                    initialText = data[MainViewModel.KEY_CITY] ?: addressSerialized?.city,
                    validators = listOf(Required())
                ),
                Field(
                    name = MainViewModel.KEY_ZIPCODE,
                    label = "Zipcode",
                    initialText = data[MainViewModel.KEY_ZIPCODE] ?: addressSerialized?.zipcode,
                    validators = listOf(Required(), Length(5))
                ),
                Field(
                    name = MainViewModel.KEY_PHONE,
                    label = "Phone",
                    initialText = data[MainViewModel.KEY_PHONE] ?: addressSerialized?.phone,
                    validators = listOf(Required(), Length(10))
                ),
                Field(
                    name = MainViewModel.KEY_STATENAME,
                    label = "State Name",
                    initialText = data[MainViewModel.KEY_STATENAME] ?: addressSerialized?.state_name,
                    validators = listOf(Required())
                ),
                Field(
                    name = MainViewModel.KEY_ALT_PHONE,
                    label = "Alternative Phone",
                    initialText = data[MainViewModel.KEY_ALT_PHONE] ?: addressSerialized?.phone,
                    validators = listOf(Required())
                ),
                Field(
                    name = MainViewModel.KEY_COMPANY,
                    label = "Company",
                    initialText = data[MainViewModel.KEY_COMPANY] ?: addressSerialized?.company,
                    validators = listOf(Required())
                )
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            scope.launch {
                scrollState.animateScrollTo(it.toInt())
            }
        }
        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)) {
            Checkbox(
                checked = checkedState.value,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.padding(end = 10.dp)
            )
            Text(text = "Make this my default address")
        }
        Button(
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 5.dp)
                .align(Alignment.End),
            onClick = {
                if (state.validate())
                    onSubmit(state.getData(), checkedState.value)
            }
        ) {
            Text(
                if (isUpdate) "Update" else "Submit"
            )
        }
    }
}

@Composable
fun Form(state: FormState, fields: List<Field>, modifier: Modifier, scrollTo: (Float) -> Unit) {
    state.fields = fields

    Column(modifier = modifier) {
        fields.forEach {
            it.Content(modifier = modifier, scrollTo = scrollTo)
        }
    }
}