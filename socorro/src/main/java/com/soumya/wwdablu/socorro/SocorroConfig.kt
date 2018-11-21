package com.soumya.wwdablu.socorro

import android.text.TextUtils

class SocorroConfig {

    enum class SourceFileFrom {
        Assets,
        Raw,
        Resources
    }

    private var endPoint: String = ""
    private var codeResponseMap: HashMap<Int, String> = HashMap()
    private var delay: Int = 0
    private var fileContainer: SourceFileFrom = SourceFileFrom.Raw
    private var responseCode: Int = 200
    private var success: Boolean = true

    companion object {
        fun createWith() : SocorroConfig {
            return SocorroConfig()
        }
    }

    fun endPoint(endPoint: String) : SocorroConfig {

        if(TextUtils.isEmpty(endPoint)) {
            return this
        }

        this.endPoint = endPoint
        return this
    }

    internal fun getEndPoint() : String {
        return this.endPoint
    }

    fun putCodeResponseMap(code: Int, fileName: String) : SocorroConfig {
        codeResponseMap.put(code, fileName)
        return this
    }

    internal fun getCodeResponseMap(code: Int) : String? {

        if(codeResponseMap.containsKey(code)) {
            return codeResponseMap[code]
        }

        return null
    }

    fun delay(delay: Int) : SocorroConfig {
        this.delay = delay
        return this
    }

    internal fun getDelay() : Int {
        return this.delay
    }

    fun sourceFileFrom(from: SourceFileFrom) : SocorroConfig {
        this.fileContainer = from
        return this
    }

    internal fun getFileContainer() : SourceFileFrom {
        return this.fileContainer
    }

    fun responseCode(code: Int) : SocorroConfig {
        this.responseCode = code
        return this
    }

    internal fun getResponseCode() : Int {
        return this.responseCode
    }

    fun success(success: Boolean) : SocorroConfig {
        this.success = success
        return this
    }

    internal fun isSuccess() : Boolean {
        return this.success
    }

    private constructor()
}