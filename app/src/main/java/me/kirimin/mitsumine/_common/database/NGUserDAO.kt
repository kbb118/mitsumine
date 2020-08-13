package me.kirimin.mitsumine._common.database

import com.activeandroid.Model
import java.util.ArrayList

import com.activeandroid.query.Delete
import com.activeandroid.query.Select
import me.kirimin.mitsumine._common.domain.model.NGUser

class NGUserDAO private constructor() {
    companion object {

        fun save(user: String) {
            val ngUser = NGUser()
            ngUser.user = user
            ngUser.save()
        }

        fun findAll(): List<String> {
            val list = Select().from(NGUser::class.java).execute<NGUser>()
            val stringList = ArrayList<String>()
            for (w in list) {
                stringList.add(w.user!!)
            }
            return stringList
        }

        fun delete(user: String) {
            Delete().from(NGUser::class.java).where("user = ?", user).execute<Model>()
        }
    }
}
