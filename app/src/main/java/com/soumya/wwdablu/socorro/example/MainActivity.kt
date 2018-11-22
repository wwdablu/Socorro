package com.soumya.wwdablu.socorro.example

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.soumya.wwdablu.socorro.Socorro
import com.soumya.wwdablu.socorro.SocorroConfig
import com.soumya.wwdablu.socorro.example.models.UserResponse
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
            .sourceFileFrom(SocorroConfig.SourceFileFrom.Resources)
            .responseCode(288)
            .delay(100)
            .success(true)

        //checkHowStringWorks(config)
        checkWithModelClasses(config)
    }

    @SuppressLint("CheckResult")
    private fun checkWithModelClasses(config: SocorroConfig) {

        Socorro<UserResponse>().mockWith(config, this, UserResponse::class.java)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeWith(object : DisposableObserver<Response<UserResponse>>() {
                override fun onComplete() {
                    //Nothing to do as of now
                }

                override fun onNext(t: Response<UserResponse>) {

                    if (t.isSuccessful) {
                        val response: UserResponse? = t.body()
                        Toast.makeText(applicationContext, response?.userInfo?.email, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(applicationContext, t.code().toString(), Toast.LENGTH_LONG).show()
                    }
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
                }
            })
    }

    @SuppressLint("CheckResult")
    private fun checkHowStringWorks(config: SocorroConfig) {

        Socorro<String>().mockWith(config, this, String::class.java, null)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeWith(object : DisposableObserver<Response<String>>() {
                override fun onComplete() {
                    //Nothing to do as of now
                }

                override fun onNext(t: Response<String>) {

                    val response: String? = t.body()
                    Toast.makeText(applicationContext, response, Toast.LENGTH_LONG).show()
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
                }
            })
    }
}
