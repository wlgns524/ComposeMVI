package com.lulumedic.common.android.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.lulumedic.common.android.BR

abstract class ViewModelActivity<VD : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes
    protected val layoutResId: Int
) : AppCompatActivity() {

    protected abstract val viewModel: VM

    protected val viewDataBinding: VD by lazy(LazyThreadSafetyMode.NONE) {
        DataBindingUtil.setContentView(this, layoutResId)
    }

    @StyleRes
    protected open val themeResId: Int = -1

    init {
        addOnContextAvailableListener {
            viewDataBinding.notifyChange()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(themeResId)
        with(viewDataBinding) {
            lifecycleOwner = this@ViewModelActivity
//            setVariable(BR.vm, viewModel)
        }
        handleCommonError()
    }

    fun bindEvent(action: VD.() -> Unit) = action(viewDataBinding)
    abstract fun handleCommonError()
    override fun onDestroy() {
        viewDataBinding.unbind()
        super.onDestroy()
    }
}