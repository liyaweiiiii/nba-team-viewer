package com.liyawei.nbateamviewer.extensions

import android.databinding.BindingAdapter
import android.view.View

@BindingAdapter("setVisibleOrGone")
fun View.setVisibleOrGone(condition: Boolean?) {
    condition?.let {
        visibility = if (condition) View.VISIBLE else View.GONE
    }
}