package com.zalocoders.openweather.ui.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding


abstract class BindingActivity<T: ViewDataBinding>: AppCompatActivity() {
    abstract val layoutResId:Int
    lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,layoutResId)
    }

    override fun onDestroy() {
        if (::binding.isInitialized){
            binding.unbind()
        }
        super.onDestroy()
    }
}