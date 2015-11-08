package me.kirimin.mitsumine.view.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import com.squareup.picasso.Picasso

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.view.adapter.EntryInfoPagerAdapter
import me.kirimin.mitsumine.model.EntryInfo
import me.kirimin.mitsumine.presenter.EntryInfoPresenter
import me.kirimin.mitsumine.view.EntryInfoView

import kotlinx.android.synthetic.activity_entry_info.*
import me.kirimin.mitsumine.data.EntryInfoData
import me.kirimin.mitsumine.domain.EntryInfoUseCase
import me.kirimin.mitsumine.model.Bookmark
import me.kirimin.mitsumine.view.fragment.BookmarkListFragment
import me.kirimin.mitsumine.view.fragment.RegisterBookmarkFragment

public class EntryInfoActivity : AppCompatActivity(), EntryInfoView {

    companion object {

        val KEY_URL = "url"

        public fun buildBundle(url: String): Bundle {
            val bundle = Bundle()
            bundle.putString(KEY_URL, url)
            return bundle
        }
    }

    private val presenter = EntryInfoPresenter()
    private val adapter = EntryInfoPagerAdapter(supportFragmentManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry_info)

        val url = intent.getStringExtra(KEY_URL)
        if (url == null) {
            finish()
            return
        }
        presenter.onCreate(this, EntryInfoUseCase(EntryInfoData()), url, applicationContext)
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (android.R.id.home == item.itemId) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initActionBar() {
        setSupportActionBar(toolBar as Toolbar)
        val actionBar = supportActionBar
        actionBar.setTitle(R.string.entry_info_title)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeButtonEnabled(true)
    }

    override fun setBookmarkFragments(allList: List<Bookmark>, hasCommentList: List<Bookmark>) {
        adapter.addPage(BookmarkListFragment.newFragment(allList), getString(R.string.entry_info_all_bookmarks))
        adapter.addPage(BookmarkListFragment.newFragment(hasCommentList), getString(R.string.entry_info_comments))
    }

    override fun setRegisterBookmarkFragment(url: String) {
        adapter.addPage(RegisterBookmarkFragment.newFragment(url), getString(R.string.entry_info_register_bookmark))
    }

    override fun setEntryInfo(entryInfo: EntryInfo) {
        countLayout.visibility = View.VISIBLE
        titleTextView.text = entryInfo.title
        bookmarkCountTextView.text = entryInfo.bookmarkCount.toString()
        Picasso.with(applicationContext).load(entryInfo.thumbnailUrl).fit().into(thumbnailImageView)
        tagsText.text = entryInfo.tagList.joinToString(", ")
    }

    override fun setViewPagerSettings(currentItem: Int, offscreenPageLimit: Int) {
        commentsViewPager.adapter = adapter
        commentsViewPager.currentItem = currentItem
        commentsViewPager.offscreenPageLimit = offscreenPageLimit
        tabs.setViewPager(commentsViewPager)
    }

    override fun setCommentCount(commentCount: String) {
        commentCountTextView.text = commentCount
    }

    override fun showNetworkErrorToast() {
        Toast.makeText(applicationContext, R.string.network_error, Toast.LENGTH_SHORT).show()
    }
}
