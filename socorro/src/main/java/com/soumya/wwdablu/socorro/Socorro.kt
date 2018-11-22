package com.soumya.wwdablu.socorro

import android.content.Context
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.concurrent.TimeUnit
import kotlin.text.StringBuilder

class Socorro<T> {

    fun mockWith(config:       SocorroConfig,
                 context:      Context,
                 castClass:    Class<T>,
                 deserializer: JsonDeserializer<T>? = null) : Observable<Response<T>> {

        return Observable.create(ObservableOnSubscribe<Response<T>> { emitter ->

            val iStream: InputStream? = when(config.getFileContainer()) {

                SocorroConfig.SourceFileFrom.Raw -> {
                    FileStream.fromRaw(context, getApplicableFilename(config))
                }

                SocorroConfig.SourceFileFrom.Assets -> {
                    FileStream.fromAssets(context, getApplicableFilename(config))
                }

                SocorroConfig.SourceFileFrom.Resources -> {
                    FileStream.fromResources(getApplicableFilename(config))
                }

            }

            if(iStream == null && config.isSuccess()) {
                emitter.onError(FileNotFoundException("File not found: " + getApplicableFilename(config)))
                return@ObservableOnSubscribe
            }

            if(iStream != null && config.isSuccess()) {
                val fileContent: java.lang.StringBuilder = FileStream.read(iStream)
                emitter.onNext(createSuccessResponse(castClass, fileContent,
                    createGson(castClass, deserializer), config))
            } else {
                emitter.onNext(createErrorResponse(config))
            }

            emitter.onComplete()

        }).delay(config.getDelay().toLong(), TimeUnit.MILLISECONDS)
    }

    /*
     *
     * PRIVATE METHODS WHICH ARE USED INTERNALLY BY THIS LIBRARY
     *
     */

    private fun getApplicableFilename(config: SocorroConfig) : String {

        var filename: String? = config.getCodeResponseMap(config.getResponseCode())

        //If there is no explicit maping, then try to generate it
        if(filename == null || TextUtils.isEmpty(filename)) {

            filename = config.getEndPoint().replace("/", "_", true).plus("_")
                .plus(config.getResponseCode().toString()).plus(".json")
        }

        if(!filename.endsWith(".json", true)) {
            throw IllegalArgumentException("Mock files must end with .json extension")
        }

        filename = when(config.getFileContainer()) {
            SocorroConfig.SourceFileFrom.Raw -> {
                filename.substring(0, filename.length - 5)
            }

            else -> filename
        }

        return filename
    }

    private fun createGson(cc: Class<T>, ds: JsonDeserializer<T>?) : Gson {

        if(ds == null) {
            return Gson()
        }

        return GsonBuilder().registerTypeAdapter(cc, ds).create()
    }

    private fun createSuccessResponse(cc: Class<T>, sb: StringBuilder, gson: Gson, config: SocorroConfig) : Response<T> {

        val succresp = if(cc.isInstance("String")) {
            sb.toString() as T
        } else {
            gson.fromJson(sb.toString(), cc)
        }

        return Response.success(succresp,
            okhttp3.Response.Builder()
                .request(Request.Builder().url("https://socorro.mock/".plus(config.getEndPoint())).build())
                .protocol(Protocol.HTTP_2)
                .code(config.getResponseCode())
                .message("")
                .build())
    }

    private fun createErrorResponse(config: SocorroConfig) : Response<T> {
        return Response.error(config.getResponseCode(), ResponseBody.create(null, ""))
    }
}