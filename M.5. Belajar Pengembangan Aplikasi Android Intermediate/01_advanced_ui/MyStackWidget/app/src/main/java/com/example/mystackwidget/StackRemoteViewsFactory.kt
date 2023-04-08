package com.example.mystackwidget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf

internal class StackRemoteViewsFactory(
    private val context: Context,
) : RemoteViewsService.RemoteViewsFactory {

    private val widgetItems = ArrayList<Bitmap>()

    override fun onCreate() {
    }

    override fun onDataSetChanged() {
        widgetItems.addAll(listOf(
            R.drawable.darth_vader,
            R.drawable.star_wars_logo,
            R.drawable.storm_trooper,
            R.drawable.starwars,
            R.drawable.falcon,
        ).map {
            (BitmapFactory.decodeResource(context.resources, it))
        })
    }

    override fun onDestroy() {
    }

    override fun getCount(): Int {
        return widgetItems.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.imageView, widgetItems[position])

        val extras = bundleOf(ImageBannerWidget.EXTRA_ITEM to position)
        val fillInIntent = Intent().putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun hasStableIds(): Boolean {
        return false
    }
}