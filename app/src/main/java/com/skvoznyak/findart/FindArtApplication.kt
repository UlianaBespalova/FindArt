package com.skvoznyak.findart

import android.app.Application
import com.pacoworks.rxpaper2.RxPaperBook

class FindArtApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        RxPaperBook.init(this)
    }
}