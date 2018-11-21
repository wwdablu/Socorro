package com.soumya.wwdablu.socorro

import android.content.Context
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception

internal class FileStream {

    companion object {

        internal fun fromAssets(context: Context,
                                fileName: String) : InputStream? {

            var iStream: InputStream?
            try {
                iStream = context.assets.open(fileName)
                return iStream
            } catch (e: Exception) {
                iStream = null
            }

            return iStream
        }

        internal fun fromRaw(context: Context,
                             fileName: String) : InputStream? {

            var iStream: InputStream?
            try {
                iStream = context.resources.openRawResource(
                    context.resources.getIdentifier(fileName, "raw", context.packageName))
                return iStream
            } catch (e: Exception) {
                iStream = null
            }

            return iStream
        }

        internal fun fromResources(fileName: String): InputStream? {

            var iStream: InputStream?
            try {
                iStream = FileStream::class.java.classLoader?.getResourceAsStream(fileName)
                return iStream
            } catch (e: Exception) {
                iStream = null
            }

            return iStream
        }

        internal fun read(iStream: InputStream) : StringBuilder {

            val fileContent = StringBuilder()
            var bReader: BufferedReader? = null

            try {
                bReader = BufferedReader(InputStreamReader(iStream))
                var line: String? = bReader.readLine()
                while (line != null) {
                    fileContent.append(line)
                    line = bReader.readLine()
                }

            } finally {
                bReader?.close()
                iStream.close()
            }

            return fileContent
        }
    }
}