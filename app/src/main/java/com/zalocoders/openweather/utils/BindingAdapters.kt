package com.zalocoders.openweather.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


@BindingAdapter(value = ["setImageUrl"])
fun ImageView.bindImageUrl(url: String?) {
    if (url != null && url.isNotBlank()) {
        Picasso.get().load("https://openweathermap.org/img/wn/$url.png").into(this)

    }
}

@BindingAdapter(value = ["setAdapter"])
fun RecyclerView.bindRecyclerViewAdapter(adapter: RecyclerView.Adapter<*>) {
    this.run {
        this.setHasFixedSize(true)
        this.adapter = adapter
    }
}
