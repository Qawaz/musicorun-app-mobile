package com.musicorumapp.mobile.api.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

class PagingController<T: PageableItem>(
    val entity: LastfmEntity,
    val perPage: Int = 20,
    var totalResults: MutableState<Int> = mutableStateOf(0),
    var totalPages: Int = 0,
    val pages: MutableMap<Int, List<T>> = mutableMapOf(),
    val requester: suspend (page: Int) -> List<T>
) {
    val itemsAsState = mutableStateListOf<T>()
    val canRetrieveMore: Boolean
        get() = pages.size < totalPages

    private fun addPageContent(page: Int, items: List<T>): PagingController<T> {
        pages[page] = items
        itemsAsState.addAll(items)
        return this
    }

    fun getPageContent(page: Int): List<T>? {
        return pages[page]
    }

    fun getAllItems(): List<T> {
        val items: MutableList<T> = mutableListOf()

        pages.forEach { items.addAll(it.value) }

        return items
    }

    suspend fun fetchPage(page: Int): List<T> {
        val results = requester(page)
        addPageContent(page, results)
        return results
    }

    suspend fun fetchNextPage (): List<T>? {
        if (!canRetrieveMore) return null

        return fetchPage(pages.size + 1)
    }

    override fun toString(): String {
        return "PagingController<$entity>(perPage = $perPage, pages = ${pages.size}, items = ${getAllItems().size}, totalResults = ${totalResults})"
    }
}
