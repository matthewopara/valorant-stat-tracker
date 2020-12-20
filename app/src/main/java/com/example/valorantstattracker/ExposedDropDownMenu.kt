package com.example.valorantstattracker

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class ExposedDropDownMenu(context: Context, attributeSet: AttributeSet) :
    MaterialAutoCompleteTextView(context, attributeSet) {

    override fun getFreezesText(): Boolean {
        return false
    }
}