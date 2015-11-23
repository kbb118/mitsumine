package me.kirimin.mitsumine.data

import android.content.Context
import android.preference.PreferenceManager
import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.data.database.FeedDAO
import me.kirimin.mitsumine.data.network.api.BookmarkCountApi
import me.kirimin.mitsumine.data.network.api.TagListApi
import me.kirimin.mitsumine.domain.model.Feed
import rx.Observable

abstract class AbstractFeedData(val context: Context) {

    val isUseBrowserSettingEnable: Boolean
        get() = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.key_use_browser_to_comment_list), false)

    val isShareWithTitleSettingEnable: Boolean
        get() = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.key_is_share_with_title), false)

    abstract fun requestFeed(): Observable<Feed>

    fun requestTagList(url: String): Observable<List<String>> = TagListApi.request(context.applicationContext, url)

    fun requestBookmarkCount(url: String): Observable<String> = BookmarkCountApi.request(context.applicationContext, url)

    fun saveFeed(feed: Feed) {
        FeedDAO.save(feed)
    }
}