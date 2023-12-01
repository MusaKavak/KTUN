package dev.musakavak.ktun

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.Method
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.html5.table
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScraperViewModel : ViewModel() {
    var selectedTabId by mutableIntStateOf(0)
    var announcementList by mutableStateOf<List<Announcement>?>(null)

    init {
        selectTab(0)
    }

    fun selectTab(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            announcementList = getAnnouncements(
                when (id) {
                    0 -> "https://www.ktun.edu.tr/tr/Universite/TumDuyurular"
                    1 -> "https://www.ktun.edu.tr/tr/Birim/Duyurular/?brm=ZPQ822/69hQMsWh8CLU2+w=="
                    2 -> "https://www.ktun.edu.tr/tr/Birim/Duyurular/?brm=mx6As1e+K7FQMKHX5CX6jw=="
                    else -> null
                }
            )
            selectedTabId = id
        }
    }

    suspend fun getAnnouncements(url: String?): List<Announcement>? {
        if (url == null) return null
        val announcements = mutableListOf<Announcement>()

        skrape(HttpFetcher) {
            request {
                this.url = url
                method = Method.GET
            }
            response {
                htmlDocument(this.responseBody) {
                    relaxed = true
                    table {
                        (1..6).forEach {

                            findByIndex(it, "tr") {
                                val title = findFirst("a").text
                                val link =
                                    "https://www.ktun.edu.tr" + findFirst("a").attribute("href")
                                val date = findSecond("td").text
                                announcements.add(
                                    Announcement(
                                        title,
                                        date,
                                        link
                                    )
                                )
                            }

                        }
                    }
                }
            }
        }

        return announcements
    }
}

data class Announcement(
    val title: String,
    val date: String,
    val link: String
)