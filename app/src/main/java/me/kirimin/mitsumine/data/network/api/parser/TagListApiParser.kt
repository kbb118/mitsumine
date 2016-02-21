package me.kirimin.mitsumine.data.network.api.parser

import me.kirimin.mitsumine.domain.extensions.toList
import org.json.JSONException
import org.json.JSONObject

object TagListApiParser {

    fun parseResponse(response: JSONObject): List<String> {
        try {
            val tags = response.getJSONArray("bookmarks").toList<JSONObject>()
                    .map { bookmark -> bookmark.getJSONArray("tags").toList<kotlin.String>() }
                    .flatMap { tags -> tags }
                    .groupBy { tags -> tags }
                    .map { tagMap -> tagMap.value }
                    .sortedByDescending { tags -> tags.size }
                    .take(4)
                    .map { tags -> tags.get(0) }
            return tags
        } catch (e: JSONException) {
            return emptyList()
        }
    }
}