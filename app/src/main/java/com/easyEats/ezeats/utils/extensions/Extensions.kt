package com.easyEats.ezeats.utils.extensions

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.smoothScrollToCenter(position: Int) {
    val layoutManager = this.layoutManager as? LinearLayoutManager ?: return
    val view = layoutManager.findViewByPosition(position)
    if (view != null) {
        val parentWidth = this.width
        val childWidth = view.width
        val childLeft = view.left
        val centerOffset = (parentWidth / 2) - (childWidth / 2)
        this.smoothScrollBy(childLeft - centerOffset, 0)
    } else {
        // If view is null (offscreen), scroll to it and center after layout
        this.scrollToPosition(position)
        this.post {
            val centeredView = layoutManager.findViewByPosition(position)
            centeredView?.let {
                val parentWidth = this.width
                val childWidth = it.width
                val childLeft = it.left
                val centerOffset = (parentWidth / 2) - (childWidth / 2)
                this.smoothScrollBy(childLeft - centerOffset, 0)
            }
        }
    }
}