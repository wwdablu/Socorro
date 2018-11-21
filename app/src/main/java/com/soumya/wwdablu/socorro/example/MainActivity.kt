package com.soumya.wwdablu.socorro.example

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.soumya.wwdablu.socorro.Socorro
import com.soumya.wwdablu.socorro.SocorroConfig
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val config = SocorroConfig.createWith()
            .endPoint("simple/user/mock")
            .putCodeResponseMap(200, "simple_user_mock.json")
            .sourceFileFrom(SocorroConfig.SourceFileFrom.Assets)
            .responseCode(200)
            .delay(100)
            .success(true)

        Socorro<String>().mockWith(config, this, String::class.java, null)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeWith(object : DisposableObserver<Response<String>>() {
                override fun onComplete() {
                    //Nothing to do as of now
                }

                override fun onNext(t: Response<String>) {

                    val response: String? = t.body()
                    Toast.makeText(applicationContext, response, Toast.LENGTH_SHORT).show()
                }

                override fun onError(e: Throwable) {
                    Log.e("Socorro Example", e.message)
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
                }
            })
    }
}
