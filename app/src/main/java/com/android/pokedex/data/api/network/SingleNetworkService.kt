package com.android.pokedex.data.api.network

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

interface SingleNetworkService {

    companion object {
        //var networkInterceptor: NetworkConnectionInterceptor?=null
        var baseUrl: String = ""

        operator fun <S> invoke(
            baseUrl: String,
            serviceClass: Class<S>/*networkInterceptor: NetworkConnectionInterceptor*/
        ): S {
            //this.networkInterceptor=networkInterceptor
            Companion.baseUrl = baseUrl

            return createService(baseUrl, serviceClass)
        }

        private fun getRetrofit(Url: String): Retrofit {
            return Retrofit.Builder()
                /*.addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create()
                )*/
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(SingleOkHttpClient.getOkHttpClient())
                .addConverterFactory(
                    GsonConverterFactory.create()
                )
                .baseUrl(Url)
                .build()
        }

        private fun getRetrofitBuilder(baseUrl: String): Retrofit {
            val retrofitBuilder = getRetrofit(baseUrl)
            return retrofitBuilder
        }

        /*fun createEndPoints(baseUrl:String): ApiEndPoint {
            val retrofitCall = getRetrofitBuilder(baseUrl)
            return retrofitCall.create(ApiEndPoint::class.java)
        }*/
        fun <S> createService(baseUrl: String, serviceClass: Class<S>): S {
            val retrofitCall = getRetrofitBuilder(baseUrl)
            return retrofitCall.create(serviceClass)
        }

        private fun createCertificate(trustedCertificateIS: InputStream): SSLContext {
            val cf: CertificateFactory = CertificateFactory.getInstance("X.509")
            val ca: Certificate
            ca = try {
                cf.generateCertificate(trustedCertificateIS)
            } finally {
                trustedCertificateIS.close()
            }
            // creating a KeyStore containing our trusted CAs
            val keyStoreType: String = KeyStore.getDefaultType()
            val keyStore: KeyStore = KeyStore.getInstance(keyStoreType)
            keyStore.load(null, null)
            keyStore.setCertificateEntry("ca", ca)
            // creating a TrustManager that trusts the CAs in our KeyStore
            val tmfAlgorithm: String = TrustManagerFactory.getDefaultAlgorithm()
            val tmf: TrustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm)
            tmf.init(keyStore)
            // creating an SSLSocketFactory that uses our TrustManager
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, tmf.trustManagers, null)
            return sslContext
        }
    }
}