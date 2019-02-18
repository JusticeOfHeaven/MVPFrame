package com.my.mvpframe.widget

import android.graphics.Canvas
import android.graphics.Rect
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.View

/**
 * Create by jzhan on 2019/2/15
 */
class GridItemDecoration : RecyclerView.ItemDecoration() {
    private val TAG = "GridItemDecoration"
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        Log.d(TAG, "onDrawOver")
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        Log.d(TAG, "onDraw")
    }


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        Log.d(TAG, "getItemOffsets")
        var spanCount = getSpanCount(parent)
        var childCount = parent.adapter?.itemCount
        childCount = childCount ?: 0
        if (isLastRaw(parent, (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition, spanCount, childCount)) {
            // 如果是最后一行，则不需要绘制底部
//            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0)
        } else if (isLastColumn(parent, (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition, spanCount, childCount)) {
            // 如果是最后一列，则不需要绘制右边
        } else {

        }

    }

    private fun isLastColumn(parent: RecyclerView, itemPosition: Int, spanCount: Int, childCount: Int): Boolean {
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            // 如果是最后一列，则不需要绘制右边
            if ((itemPosition + 1) % spanCount == 0) {
                return true
            }
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val orientation = layoutManager
                    .orientation
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                // 如果是最后一列，则不需要绘制右边
                if ((itemPosition + 1) % spanCount == 0) {
                    return true
                }
            } else {
                var pos = childCount - childCount % spanCount
                // 如果是最后一列，则不需要绘制右边
                if (itemPosition >= pos)
                    return true
            }
        }
        return false
    }

    private fun isLastRaw(parent: RecyclerView, itemPosition: Int, spanCount: Int, childCount: Int): Boolean {
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            var pos = childCount - childCount % spanCount
            if (itemPosition >= pos)
            // 如果是最后一行，则不需要绘制底部
                return true
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val orientation = layoutManager.orientation
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                var pos = childCount - childCount % spanCount
                // 如果是最后一行，则不需要绘制底部
                if (itemPosition >= pos)
                    return true
            } else {
                // StaggeredGridLayoutManager 且横向滚动
                // 如果是最后一行，则不需要绘制底部
                if ((itemPosition + 1) % spanCount == 0) {
                    return true
                }
            }
        }
        return false
    }

    private fun getSpanCount(parent: RecyclerView): Int {
        // 列数
        var spanCount = -1
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            spanCount = layoutManager.spanCount
        } else if (layoutManager is StaggeredGridLayoutManager) {
            spanCount = layoutManager.spanCount
        }
        return spanCount
    }
}