package com.skvoznyak.findart.model

import android.util.Log
import com.pacoworks.rxpaper2.RxPaperBook
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


object Storage {

    val book = RxPaperBook.with("saved_pictures", Schedulers.newThread())

    fun write(key : String, value : Picture) {
            val write = book.write(key, value)
            write.subscribe(object : CompletableObserver {
                override fun onComplete() {
                    Log.d("ivan", "write: Ok")
                }
                override fun onError(e: Throwable) {
                    Log.d("ivan", "error while writing picture into storage")
                }
                override fun onSubscribe(d: Disposable) {}
            })
    }

    fun delete(key : String) {
        val delete = book.delete(key)
        delete.subscribe(object : CompletableObserver {
            override fun onComplete() {
                Log.d("ivan", "delete: Ok")
            }
            override fun onError(e: Throwable) {
                Log.d("ivan", "error while deleting picture from storage")
            }
            override fun onSubscribe(d: Disposable) {}
        })
    }

    fun getAll()  {
        Log.d("ivan", "trying get all")
        var res : List<Picture>
        val keys = book.keys()

        keys.subscribe(
            {r ->  Log.d("ivan", "keys: $r")},
            {e ->  Log.d("ivan", "error: $e")}
        )
    }
}