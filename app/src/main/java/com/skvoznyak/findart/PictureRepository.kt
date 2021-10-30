package com.skvoznyak.findart

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

//template for the future
object PictureRepository {

    fun getThings(): Single<Unit> {
        return Single.fromCallable {
            getThignsFromSomewhere()
        }.subscribeOn(Schedulers.io())
    }

    private fun getThignsFromSomewhere() {
    }

}