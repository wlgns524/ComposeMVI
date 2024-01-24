package com.lulumedic.common.android.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lulumedic.common.android.R
import com.lulumedic.common.android.BR


abstract class ViewModelBottomSheetDialogFragment<VD : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes
    protected val layoutResId: Int
) : BottomSheetDialogFragment() {

    protected var _viewDataBinding: VD? = null
    protected val viewDataBinding: VD
        get() = _viewDataBinding ?: throw IllegalStateException("viewDataBinding can not be null")

    @ColorRes
    protected open val backgroundDrawableResource = android.R.color.transparent

    @StyleRes
    protected open val themeId: Int = R.style.Dialog_Alert

    protected abstract val viewModel: VM

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NO_TITLE, if (themeId != -1) themeId else theme)
        return super.onCreateDialog(savedInstanceState).also { dialog ->
            dialog.window?.setBackgroundDrawableResource(backgroundDrawableResource)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<VD>(inflater, layoutResId, container, false)
            .also { viewDataBinding ->
                viewDataBinding.lifecycleOwner = viewLifecycleOwner
//                viewDataBinding.setVariable(BR.vm, viewModel)
                this._viewDataBinding = viewDataBinding
            }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleCommonError()
        collectState()
        collectSideEffect()
    }

    override fun onDestroyView() {
        _viewDataBinding = null
        super.onDestroyView()
    }

    fun bindEvent(action: VD.() -> Unit) = action(viewDataBinding)
    abstract fun handleCommonError()
    abstract fun collectState()
    abstract fun collectSideEffect()
}