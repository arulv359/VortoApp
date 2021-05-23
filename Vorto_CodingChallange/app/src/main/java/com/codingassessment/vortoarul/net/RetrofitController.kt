package com.codingassessment.vortoarul.net

import com.codingassessment.vortoarul.model.BusinessesList
import com.codingassessment.vortoarul.utils.Constants.BASE_URL
import com.codingassessment.vortoarul.utils.Constants.API_AUTHORIZATION
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*
import javax.security.cert.CertificateException

class RetrofitController {
    companion object {
        private val mRetrofit: Retrofit = getInstance()
        private val webserviceApi = mRetrofit.create<Api>(Api::class.java)

        private fun getInstance() : Retrofit {
            if (mRetrofit == null) {
                val gson = GsonBuilder()
                        .setLenient()
                        .create()

                return Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .client(getOkHttpClientBuilder().build())
                        .build()
            }

            return mRetrofit
        }

        fun loadBusiness(key: String, latitude: String, longtitude: String, loadShopsCallBack: Callback<BusinessesList>) {
            val call = webserviceApi.loadBusiness(key, latitude, longtitude)
            call.enqueue(loadShopsCallBack)
        }
    }
}

fun getOkHttpClientBuilder() : OkHttpClient.Builder {
    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }

        @Throws(CertificateException::class)
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {

        }
    })

    // Install the all-trusting trust manager

    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    val client = OkHttpClient.Builder()
    client.interceptors().add(httpLoggingInterceptor)
    client.readTimeout(180, TimeUnit.SECONDS)
    client.connectTimeout(180, TimeUnit.SECONDS)
    client.addInterceptor { chain ->
        val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", API_AUTHORIZATION)
                .build()
        chain.proceed(newRequest)}

    val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
    keyStore.load(null, null)

    val sslContext = SSLContext.getInstance("TLS")

    val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
    trustManagerFactory.init(keyStore)
    val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
    keyManagerFactory.init(keyStore, "keystore_pass".toCharArray())
    sslContext.init(null, trustAllCerts, SecureRandom())
    val sslSocketFactory = sslContext.socketFactory
    client.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
    return client
}