package com.example.android.activitiesproject.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.activitiesproject.R

class CustomImage {
    companion object {
        @BindingAdapter("app:imagePath")
        @JvmStatic
        fun setImagePath(image: ImageView, path: String?) {
            val option=RequestOptions()

                .error(R.drawable.user_default_grey)
            Glide.with(image.context).load(path).apply(option).into(image)
        }
    }
}