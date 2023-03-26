package ge.rogavactive.multipleselectionview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import ge.rogavactive.multipleselectionview.popup.MultipleSelectionListPopupWindow

class MultipleSelectionView<I> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(
    context,
    attrs,
    defStyleAttr
) {

    private var listener: OnClickListener? = null
    private val mModal = MultipleSelectionListPopupWindow(context, this, attrs, defStyleAttr)

    private var mAdapter: MultipleSelectionViewAdapter<I>? = null
        private set(value) {
            mModal.setAdapter(value)
            mModal.setOnDismissListener {
                value?.modalDismiss()
                // Display updated text after modal dismiss
                text = value?.getFormattedSelectedText()
            }
            value?.onShouldUpdateText = {
                // When new items are set recalculate displayed text
                text = value?.getFormattedSelectedText()
            }
            field = value
        }

    init {
        context.withStyledAttributes(attrs, R.styleable.MultipleSelectionView) {
            val modalBGColor = getDrawable(R.styleable.MultipleSelectionView_msv_modal_background)
            val modalTextColor =
                getColor(R.styleable.MultipleSelectionView_msv_modal_text_color, Color.BLACK)
            val modalTickColor = getColor(
                R.styleable.MultipleSelectionView_msv_modal_tick_color,
                ContextCompat.getColor(context, R.color.teal_700)
            )
            val modalTextSize = getDimensionPixelSize(
                R.styleable.MultipleSelectionView_msv_modal_text_size,
                resources.getDimensionPixelSize(R.dimen.modal_default_text_size)
            )

            val defaultAdapter = MultipleSelectionViewAdapterImpl<I>(
                modalTickColor = modalTickColor,
                modalTextColor = modalTextColor,
                modalTextSize = modalTextSize
            )
            mModal.setBackgroundDrawable(modalBGColor)

            mAdapter = defaultAdapter
        }
    }

    /**
     * Modify default adapter and retrieve it.
     */
    fun createAdapterWithItems(
        items: List<I>,
        selectedItems: List<I> = listOf(),
        itemTransformFun: ((I) -> String) = { it.toString() }
    ): MultipleSelectionViewAdapter<I> {
        if (mAdapter is MultipleSelectionViewAdapterImpl) {
            (mAdapter as MultipleSelectionViewAdapterImpl).setTransformFun(itemTransformFun)
        }
        mAdapter?.setItems(items, selectedItems)
        return mAdapter!!
    }

    /**
     * Set custom adapter if you want to have custom views and/or functionality
     */
    fun setAdapter(newAdapter: MultipleSelectionViewAdapter<I>) {
        mAdapter = newAdapter
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            listener?.onClick(this)
            mModal.show()
        }
        return super.dispatchTouchEvent(event)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN &&
            (event.keyCode == KeyEvent.KEYCODE_DPAD_CENTER || event.keyCode == KeyEvent.KEYCODE_ENTER)
        ) {
            listener?.onClick(this)
            mModal.show()
        }
        return super.dispatchKeyEvent(event)
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        this.listener = listener
    }

}