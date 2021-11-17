package com.skvoznyak.findart.model

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.json.JSONObject




object PictureRepository {

    private const val knnBaseUrl = "https://dccf-178-132-97-188.ngrok.io"

    private val retrofit = Retrofit.Builder()
        .baseUrl(knnBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val knnApi = retrofit.create(KnnApi::class.java)


    fun getSimilarPictures(vector : FloatArray): Single<List<SimilarPicture>> {

        val kNeighbors = 6
        val paramObject = JSONObject()
        paramObject.put("vector", vector)
        paramObject.put("k_neighbors", kNeighbors)

        return Single.fromCallable {
            knnApi.knnGetSimilarPictures(paramObject).execute().body() ?: error("Empty response :(")
        }.subscribeOn(Schedulers.io())
    }

    private fun getThignsFromSomewhere() {
    }

}