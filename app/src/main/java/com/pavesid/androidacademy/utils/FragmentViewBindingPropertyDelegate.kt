package com.pavesid.androidacademy.utils

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@MainThread
class FragmentViewBindingPropertyDelegate<T : ViewBinding>(
    private val fragment: Fragment,
    private val viewBinding: (View) -> T
) : ReadOnlyProperty<Fragment, T> {

    internal var binding: T? = null

    init {
        fragment.lifecycle.addObserver(BindingLifecycleObserver())
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        binding?.let { return it }
        check(fragment.viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            "Attempt to get view binding when fragment view is destroyed"
        }
        return viewBinding(thisRef.requireView()).also { binding = it }
    }

    private inner class BindingLifecycleObserver : DefaultLifecycleObserver {

        private val mainHandler = Handler(Looper.getMainLooper())

        override fun onCreate(owner: LifecycleOwner) {
            fragment.viewLifecycleOwnerLiveData.observe(
                fragment,
                Observer { viewLifecycleOwner: LifecycleOwner? ->
                    viewLifecycleOwner ?: return@Observer
                    val viewLifecycleObserver = object : DefaultLifecycleObserver {
                        override fun onDestroy(owner: LifecycleOwner) {
                            viewLifecycleOwner.lifecycle.removeObserver(this)
                            mainHandler.post { binding = null }
                        }
                    }

                    viewLifecycleOwner.lifecycle.addObserver(viewLifecycleObserver)
                }
            )
        }

        override fun onDestroy(owner: LifecycleOwner) {
            owner.lifecycle.removeObserver(this)
            mainHandler.post { binding = null }
        }
    }
}

@MainThread
fun <T : ViewBinding> Fragment.viewBinding(bind: (View) -> T): FragmentViewBindingPropertyDelegate<T> =
    FragmentViewBindingPropertyDelegate(this, bind)
