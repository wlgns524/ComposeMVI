package com.lulumedic.common.android.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.lulumedic.common.android.BR


abstract class ViewModelFragment<VD : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes
    protected val layoutResId: Int
) : Fragment() {

    protected var _viewDataBinding: VD? = null
    protected val viewDataBinding: VD
        get() = _viewDataBinding ?: throw IllegalStateException("viewDataBinding can not be null")

    @StyleRes
    protected open val themeResId: Int = -1
    protected abstract val viewModel: VM

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

    fun bindEvent(action: VD.() -> Unit) = action(viewDataBinding)
    abstract fun handleCommonError()
    abstract fun collectState()
    abstract fun collectSideEffect()

    override fun onDestroyView() {
        _viewDataBinding = null
        super.onDestroyView()
    }
}