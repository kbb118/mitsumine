package me.kirimin.mitsumine.mybookmark

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.entryinfo.EntryInfoActivity

import kotlinx.android.synthetic.main.fragment_my_bookmarks.view.*
import me.kirimin.mitsumine.MyApplication
import me.kirimin.mitsumine._common.domain.model.MyBookmark
import me.kirimin.mitsumine._common.ui.BaseFragment
import javax.inject.Inject

class MyBookmarkSearchFragment : BaseFragment(), MyBookmarkSearchView, SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {

    @Inject
    lateinit var presenter: MyBookmarkSearchPresenter

    private lateinit var adapter: MyBookmarkSearchAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_my_bookmarks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onCreate(this, arguments!!.getString("keyword"))
    }

    override fun onDestroyView() {
        presenter.onDestroy()
        super.onDestroyView()
    }

    override fun onRefresh() {
        presenter.onRefresh()
    }

    override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
    }

    override fun onScroll(absListView: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        val view = view ?: return
        presenter.onScroll(firstVisibleItem, visibleItemCount, totalItemCount, view.swipeLayout.isRefreshing)
    }

    override fun initViews() {
        val view = view ?: return
        view.swipeLayout.setColorSchemeResources(R.color.blue, R.color.orange)
        view.swipeLayout.setOnRefreshListener(this)
        view.swipeLayout.setProgressViewOffset(false, 0, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24f, resources.displayMetrics).toInt())
        adapter = MyBookmarkSearchAdapter(activity!!, { v, myBookmark ->
            presenter.onListItemClick(myBookmark)
        }, { v, myBookmark ->
            presenter.onListItemLongClick(myBookmark)
        })
        view.listView.adapter = adapter
        view.listView.setOnScrollListener(this)
    }

    override fun showRefreshing() {
        val view = view ?: return
        view.swipeLayout.isRefreshing = true
    }

    override fun dismissRefreshing() {
        val view = view ?: return
        view.swipeLayout.isRefreshing = false
    }

    override fun addListViewItem(myBookmarks: List<MyBookmark>) {
        adapter.addAll(myBookmarks)
    }

    override fun clearListViewItem() {
        adapter.clear()
    }

    override fun showErrorToast() {
        Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show()
    }

    override fun sendBrowserIntent(linkUrl: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl)))
    }

    override fun startEntryInfo(linkUrl: String) {
        val intent = Intent(activity, EntryInfoActivity::class.java)
        intent.putExtras(EntryInfoActivity.buildBundle(linkUrl))
        startActivity(intent)
    }

    override fun injection() {
        (activity!!.application as MyApplication).getApplicationComponent().inject(this)
    }

    companion object {
        fun newFragment(keyword: String): MyBookmarkSearchFragment {
            val fragment = MyBookmarkSearchFragment()
            val bundle = Bundle()
            bundle.putString("keyword", keyword)
            fragment.arguments = bundle
            return fragment
        }
    }
}