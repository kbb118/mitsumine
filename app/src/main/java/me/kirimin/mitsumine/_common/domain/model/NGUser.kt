package me.kirimin.mitsumine._common.domain.model

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table

@Table(name = "nguser")
class NGUser : Model() {

    @Column(name = "user", unique = true)
    var user: String? = null
}
