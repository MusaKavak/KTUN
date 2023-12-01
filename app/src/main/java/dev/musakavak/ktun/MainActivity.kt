package dev.musakavak.ktun

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.musakavak.ktun.ui.theme.KTUNTheme

class MainActivity : ComponentActivity() {
    private lateinit var vm: ScraperViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KTUNTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    vm = viewModel(ScraperViewModel::class.java)

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            Navigation(vm.selectedTabId, vm::selectTab)
                        }
                    ) { inset ->
                        Surface(modifier = Modifier.padding(inset)) {
                            AnnouncementList(vm.announcementList)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnnouncementList(list: List<Announcement>?) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val modifier = Modifier.fillMaxWidth()
        list?.forEach {
            Card(modifier = modifier
                .padding(16.dp)
                .clickable {
                    uriHandler.openUri(it.link)
                }) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), text = it.title
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    text = it.date
                )
            }
        }
    }
}

@Composable
fun Navigation(selectedId: Int, onSelect: (Int) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            icon = {
                Icon(painter = painterResource(R.drawable.apartment), contentDescription = "")
            },
            onClick = { onSelect(0) },
            label = {
                Text("Genel")
            },
            selected = selectedId == 0
        )
        NavigationBarItem(
            icon = {
                Icon(painter = painterResource(R.drawable.home), contentDescription = "")
            },
            onClick = { onSelect(1) },
            label = {
                Text("TBMYO")
            },
            selected = selectedId == 1
        )
        NavigationBarItem(
            icon = {
                Icon(painter = painterResource(R.drawable.door), contentDescription = "")
            },
            onClick = { onSelect(2) },
            label = {
                Text("BP")
            },
            selected = selectedId == 2
        )
    }
}