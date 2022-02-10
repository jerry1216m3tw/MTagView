package com.example.mtagview.widget

import android.app.Activity
import android.graphics.Rect
import android.view.View

class KeyBoardListener(activity: Activity,keyBoardShow:((Int) -> Unit)? = null,keyBoardHide:((Int) -> Unit)? = null,keyBoardChange : (() -> Unit)? = null) {
    private var rootView: View? = null
    private var rootViewVisibleHeight = 0

    init {
        rootView = activity.window.decorView
        rootView?.viewTreeObserver?.addOnGlobalLayoutListener{
            val r = Rect()
            rootView?.getWindowVisibleDisplayFrame(r)
            val visibleHeight = r.height()
            if (rootViewVisibleHeight == 0) {
                rootViewVisibleHeight = visibleHeight
                return@addOnGlobalLayoutListener
            }
            //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
            if (rootViewVisibleHeight == visibleHeight) {
                return@addOnGlobalLayoutListener
            }

            //用來判斷鍵盤是否為切換語系時造成鍵盤的變動高度
            val keyHeight = visibleHeight - rootViewVisibleHeight

            if (keyHeight < 200) {
                if (keyHeight > -200) keyBoardChange?.invoke() else keyBoardShow?.invoke(
                        rootViewVisibleHeight - visibleHeight
                )
                rootViewVisibleHeight = visibleHeight
                return@addOnGlobalLayoutListener
            }

            //根视图显示高度变大超过200，可以看作软键盘隐藏了
            if (visibleHeight - rootViewVisibleHeight > 200) {
                keyBoardHide?.invoke(visibleHeight - rootViewVisibleHeight)
                rootViewVisibleHeight = visibleHeight
                return@addOnGlobalLayoutListener
            }
        }
    }

//    fun setListener(activity: Activity,keyBoardShow:((Int) -> Unit)? = null,keyBoardHide:((Int) -> Unit)? = null,keyBoardChange : (() -> Unit)? = null) {
//        rootView = activity.window.decorView
//        rootView?.viewTreeObserver?.addOnGlobalLayoutListener{
//            val r = Rect()
//            rootView?.getWindowVisibleDisplayFrame(r)
//            val visibleHeight = r.height()
//            if (rootViewVisibleHeight == 0) {
//                rootViewVisibleHeight = visibleHeight
//                return@addOnGlobalLayoutListener
//            }
//            //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
//            if (rootViewVisibleHeight == visibleHeight) {
//                return@addOnGlobalLayoutListener
//            }
//
//            //用來判斷鍵盤是否為切換語系時造成鍵盤的變動高度
//            val keyHeight = visibleHeight - rootViewVisibleHeight
//
//            if (keyHeight < 200) {
//                if (keyHeight > -200) keyBoardChange?.invoke() else keyBoardShow?.invoke(
//                    rootViewVisibleHeight - visibleHeight
//                )
//                rootViewVisibleHeight = visibleHeight
//                return@addOnGlobalLayoutListener
//            }
//
//            //根视图显示高度变大超过200，可以看作软键盘隐藏了
//            if (visibleHeight - rootViewVisibleHeight > 200) {
//                keyBoardHide?.invoke(visibleHeight - rootViewVisibleHeight)
//                rootViewVisibleHeight = visibleHeight
//                return@addOnGlobalLayoutListener
//            }
//        }
//    }
}