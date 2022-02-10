package com.example.mtagview.widget

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.*
import android.widget.AdapterView
import android.widget.BaseAdapter
import androidx.core.widget.NestedScrollView
import com.example.mtagview.R

@SuppressLint("ClickableViewAccessibility")
class TagView(context: Context, attrs: AttributeSet?) : NestedScrollView(context, attrs) {
    private var view : View? = null
    private var touchEvent = true
    private var keyBoard = 0
    private var editHeight = 0
    private var emptyView : View? = null
    private var listView : FullListview? = null
    private val displayMetrics : DisplayMetrics by lazy { DisplayMetrics() }
    private var mWindowManager: WindowManager? = null
    private var cellHeight = 0
    private var scrollHeight = 0
    private var defaultCount = 3

    init {
        mWindowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        mWindowManager?.defaultDisplay?.getMetrics(displayMetrics)

        view = LayoutInflater.from(context).inflate(R.layout.view_m_tagview, this)

        emptyView = view?.findViewById(R.id.view)
        emptyView?.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                touchEvent = false
            }
            return@setOnTouchListener false
        }

        listView = view?.findViewById(R.id.lv_tab)
        listView?.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                touchEvent = true
            }
            return@setOnTouchListener false
        }

        KeyBoardListener(context as Activity,{
            keyBoard += it
            reSetData()
        },{
            keyBoard -= it
            reSetData()
        })
    }

    fun setEditHeight(height : Int) {
        editHeight = height
    }

    fun setDefaultCount(count : Int) {
        defaultCount = count
    }

    fun reSetData() {
        val p = emptyView?.layoutParams as ViewGroup.LayoutParams
        p.height = displayMetrics.heightPixels - keyBoard - editHeight - cellHeight
        emptyView?.layoutParams = p
        this.postDelayed({this.scrollTo(0,scrollHeight) },30)
    }

    fun setData(adapter: BaseAdapter,callback:((Int) -> Unit)? = null) {
        listView?.adapter = adapter
        listView?.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            callback?.invoke(position)
        }
        adapter.notifyDataSetChanged()
        val item = listView?.adapter?.getView(0,null,listView)
        item?.apply {
            measure(0,0)
            cellHeight = measuredHeight * setCount(adapter.count)
        }
        reSetData()
    }

    fun setTagViewVisibility(isShow: Boolean) {
        if(isShow){
            this.visibility = View.VISIBLE
        }else {
            scrollHeight = 0
            this.postDelayed({this.scrollTo(0,10) },30)
            this.postDelayed({this.scrollTo(0,0) },30)
            this.visibility = View.GONE
        }
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (oldt != 0)
            scrollHeight = oldt
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        super.dispatchTouchEvent(ev)
        return touchEvent
    }

    private fun setCount(dataSize: Int) : Int =
        when (dataSize) {
            1 ->  1
            2 ->  2
            else -> defaultCount
        }
}