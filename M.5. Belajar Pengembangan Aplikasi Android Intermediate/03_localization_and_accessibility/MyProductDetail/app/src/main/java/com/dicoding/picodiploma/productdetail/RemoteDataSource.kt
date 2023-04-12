package com.dicoding.picodiploma.productdetail

import android.content.Context

class RemoteDataSource(private val context: Context) {
    fun getDetailProduct(): ProductModel {
        val gs = { name: Int -> context.getString(name) }

        return ProductModel(
            name = gs(R.string.name),
            price = gs(R.string.price),
            store = gs(R.string.store),
            date = gs(R.string.date),
            rating = gs(R.string.rating),
            countRating = gs(R.string.countRating),
            size = gs(R.string.size),
            color = gs(R.string.color),
            desc = gs(R.string.description),
            image = R.drawable.shoes,
        )
    }
}