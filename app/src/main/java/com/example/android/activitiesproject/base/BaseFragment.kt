package com.example.android.activitiesproject.base


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.Navigation

/**
 * A simple [Fragment] subclass.
 */
abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel> : Fragment() {
    lateinit var dataBinding: T
    lateinit var viewModel: V
    lateinit var navigation: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dataBinding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
        var v = dataBinding.root
        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dataBinding.setVariable(bindingVariable(), viewModel)
        dataBinding.lifecycleOwner = this
        navigation = Navigation.findNavController(view)
    }

    abstract fun layoutId(): Int
    abstract fun bindingVariable(): Int

}
