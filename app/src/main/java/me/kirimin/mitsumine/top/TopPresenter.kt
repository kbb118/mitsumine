package me.kirimin.mitsumine.top

import android.view.View
import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.top.TopUseCase
import me.kirimin.mitsumine.common.domain.enums.Category
import me.kirimin.mitsumine.common.domain.enums.Type
import me.kirimin.mitsumine.top.TopView
import me.kirimin.mitsumine.login.LoginActivity
import me.kirimin.mitsumine.feed.read.ReadActivity
import me.kirimin.mitsumine.setting.SettingActivity
import me.kirimin.mitsumine.search.KeywordSearchActivity
import me.kirimin.mitsumine.search.MyBookmarksActivity
import me.kirimin.mitsumine.search.SearchActivity
import me.kirimin.mitsumine.search.UserSearchActivity

class TopPresenter {

    private var view: TopView? = null
    private lateinit var useCase: TopUseCase

    private var mSelectedType = Type.HOT
    private var mSelectedCategory = Category.MAIN

    fun onCreate(topView: TopView, topUseCase: TopUseCase, category: Category = Category.MAIN, type: Type = Type.HOT) {
        view = topView
        useCase = topUseCase
        mSelectedCategory = category
        mSelectedType = type

        topView.initViews()

        topView.addNavigationCategoryButton(Category.MAIN)
        topView.addNavigationCategoryButton(Category.SOCIAL)
        topView.addNavigationCategoryButton(Category.ECONOMICS)
        topView.addNavigationCategoryButton(Category.LIFE)
        topView.addNavigationCategoryButton(Category.KNOWLEDGE)
        topView.addNavigationCategoryButton(Category.IT)
        topView.addNavigationCategoryButton(Category.FUN)
        topView.addNavigationCategoryButton(Category.ENTERTAINMENT)
        topView.addNavigationCategoryButton(Category.GAME)

        // 古い既読を削除
        topUseCase.deleteOldFeedData()
        topView.refreshShowCategoryAndType(mSelectedCategory, mSelectedType, topUseCase.getTypeInt(mSelectedType))
    }

    fun onStart() {
        val view = view ?: return
        view.removeNavigationAdditions()
        useCase.additionKeywords.forEach { keyword -> view.addAdditionKeyword(keyword) }
        useCase.additionUsers.forEach { userId -> view.addAdditionUser(userId) }

        val account = useCase.account
        if (account != null) {
            view.enableUserInfo(account.displayName, account.imageUrl)
        } else {
            view.disableUserInfo()
        }
    }

    fun onDestroy() {
        view = null
    }

    fun onNavigationClick(position: Int): Boolean {
        val view = view ?: return true
        view.closeNavigation()
        mSelectedType = if (position == 0) Type.HOT else Type.NEW
        view.refreshShowCategoryAndType(mSelectedCategory, mSelectedType, useCase.getTypeInt(mSelectedType))
        return true
    }

    fun onToolbarClick() {
        val view = view ?: return
        if (view.isOpenNavigation()) {
            view.closeNavigation()
        } else {
            view.openNavigation()
        }
    }

    fun onBackKeyClick() {
        val view = view ?: return
        if (view.isOpenNavigation()) {
            view.closeNavigation()
        } else {
            view.backPress()
        }
    }

    fun onViewClick(id: Int) {
        val view = view ?: return
        when (id) {
            R.id.navigationReadTextView -> {
                view.startActivity(ReadActivity::class.java)
                view.closeNavigation()
            }
            R.id.navigationSettingsTextView -> {
                view.startActivity(SettingActivity::class.java)
                view.closeNavigation()
            }
            R.id.navigationKeywordSearchTextView -> {
                view.startActivity(KeywordSearchActivity::class.java)
                view.closeNavigation()
            }
            R.id.navigationUserSearchTextView -> {
                view.startActivity(UserSearchActivity::class.java)
                view.closeNavigation()
            }
            R.id.navigationUserInfoLayout -> {
                view.startMyBookmarksActivity()
                view.closeNavigation()
            }
            R.id.navigationLoginButton -> {
                view.startActivity(LoginActivity::class.java)
            }
        }
    }

    fun onCategoryClick(category: Category) {
        val view = view ?: return
        view.closeNavigation()
        view.refreshShowCategoryAndType(category, mSelectedType, useCase.getTypeInt(mSelectedType))
        mSelectedCategory = category
    }

    fun onAdditionUserClick(userId: String) {
        val view = view ?: return
        view.closeNavigation()
        view.startUserSearchActivity(userId)
    }

    fun onAdditionUserLongClick(userId: String, target: View): Boolean {
        val view = view ?: return false
        view.showDeleteUserDialog(userId, target)
        return false
    }

    fun onAdditionKeywordClick(keyword: String) {
        val view = view ?: return
        view.closeNavigation()
        view.startKeywordSearchActivity(keyword)
    }

    fun onAdditionKeywordLongClick(keyword: String, target: View): Boolean {
        val view = view ?: return false
        view.showDeleteKeywordDialog(keyword, target)
        return false
    }

    fun onDeleteUserIdDialogClick(word: String, target: View) {
        useCase.deleteAdditionUser(word)
        target.visibility = View.GONE
    }

    fun onDeleteKeywordDialogClick(word: String, target: View) {
        useCase.deleteAdditionKeyword(word)
        target.visibility = View.GONE
    }

    fun getCurrentType(): Type {
        return mSelectedType
    }

    fun getCurrentCategory(): Category {
        return mSelectedCategory
    }
}
