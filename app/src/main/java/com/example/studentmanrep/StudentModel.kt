package com.example.studentmanrep

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StudentModel(
    val name: String,
    val id: String
) : Parcelable {
    override fun toString(): String = "$name ($id)"
}
