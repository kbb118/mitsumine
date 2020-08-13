package me.kirimin.mitsumine.setting

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceFragment
import android.widget.EditText
import android.widget.Toast
import me.kirimin.mitsumine.R
import me.kirimin.mitsumine._common.database.AccountDAO
import me.kirimin.mitsumine._common.database.NGWordDAO
import me.kirimin.mitsumine._common.database.NGUserDAO
import me.kirimin.mitsumine.about.AboutActivity

class SettingFragment : PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.app_preferences)
        findPreference("about").setOnPreferenceClickListener {
            startActivity(Intent(activity, AboutActivity::class.java))
            false
        }

        findPreference("ngword").setOnPreferenceClickListener {
            createEditNGWordDialog().show()
            false
        }

        findPreference("nguser").setOnPreferenceClickListener {
            createEditNGUserDialog().show()
            false
        }

        val preference = findPreference("logout")
        preference.isEnabled = AccountDAO.get() != null
        preference.setOnPreferenceClickListener { preference ->
            AccountDAO.delete()
            Toast.makeText(activity, getString(R.string.settings_logout_toast), Toast.LENGTH_SHORT).show()
            preference.isEnabled = false
            false
        }
    }

    private fun createEditNGWordDialog(): AlertDialog {
        val ngWordList = NGWordDAO.findAll().plus(getString(R.string.settings_ngword_add))
        return AlertDialog.Builder(activity)
                .setTitle(R.string.settings_ngword)
                .setItems(ngWordList.toTypedArray(), { dialog, which ->
                    if (which == ngWordList.size - 1) {
                        createAddDialog().show()
                    } else {
                        createDeleteDialog(which).show()
                    }
                })
                .create()
    }

    private fun createAddDialog(): AlertDialog {
        val editText = EditText(activity)
        return AlertDialog.Builder(activity)
                .setTitle(R.string.settings_ngword_add)
                .setPositiveButton(android.R.string.ok, { dialog, which ->
                    if (editText.text.length != 0) NGWordDAO.save(editText.text.toString())
                })
                .setView(editText)
                .create()
    }

    private fun createDeleteDialog(index: Int): AlertDialog {
        return AlertDialog.Builder(activity)
                .setTitle(R.string.settings_ngword_delete)
                .setPositiveButton(android.R.string.ok, { dialog, which ->
                    NGWordDAO.delete(NGWordDAO.findAll().get(index))
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create()
    }

    private fun createEditNGUserDialog(): AlertDialog {
        val ngUserList = NGUserDAO.findAll().plus(getString(R.string.settings_nguser_add))
        return AlertDialog.Builder(activity)
                .setTitle(R.string.settings_nguser)
                .setItems(ngUserList.toTypedArray(), { dialog, which ->
                    if (which == ngUserList.size - 1) {
                        createAddUserDialog().show()
                    } else {
                        createDeleteUserDialog(which).show()
                    }
                })
                .create()
    }

    private fun createAddUserDialog(): AlertDialog {
        val editText = EditText(activity)
        return AlertDialog.Builder(activity)
                .setTitle(R.string.settings_nguser_add)
                .setPositiveButton(android.R.string.ok, { dialog, which ->
                    if (editText.text.length != 0) NGUserDAO.save(editText.text.toString())
                })
                .setView(editText)
                .create()
    }

    private fun createDeleteUserDialog(index: Int): AlertDialog {
        return AlertDialog.Builder(activity)
                .setTitle(R.string.settings_nguser_delete)
                .setPositiveButton(android.R.string.ok, { dialog, which ->
                    NGUserDAO.delete(NGUserDAO.findAll().get(index))
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create()
    }
}