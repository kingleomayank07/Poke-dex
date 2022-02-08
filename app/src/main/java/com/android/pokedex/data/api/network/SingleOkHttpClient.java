package com.android.pokedex.data.api.network;

import android.content.Context;

import com.android.pokedex.BuildConfig;
import com.android.pokedex.utils.Device;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class SingleOkHttpClient {

    private static final int CACHE_SIZE = 50 * 1024 * 1024; // 50 MiB
    private static OkHttpClient okHttpClient;
    private static String deviceId;
    private static final String HEADER_CLIENT_ID = "x-client-id";
    private static final String HEADER_REQUEST_ID = "x-request-id";
    private static final String HEADER_PLATFORM_INFO = "X-Platform-Info";
    private static final boolean USE_UNSAFE_OKHTTP = false;
    private static final long DEFAULT_TIMEOUT = TimeUnit.SECONDS.toMillis(20);

    synchronized public static final void init(Context context) {
        HttpLoggingInterceptor logging = null;
        if (BuildConfig.DEBUG) {
            /** If app is debuggable then allow logging */
            logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        if (deviceId == null) {
            deviceId = Device.getInstallationId(context);
        }
        long timeOut = DEFAULT_TIMEOUT;
   /* try {
      //timeOut = LogSingletonProvider.Companion.getConfig().getREQUEST_TIMEOUT();
    } catch (Config.ConfigNotInitialized e) {
    }
    if (timeOut == 0L || timeOut < NetworkConstants.Companion.DEFAULT_TIMEOUT) {
      timeOut = NetworkConstants.Companion.DEFAULT_TIMEOUT;
    }*/
        Interceptor forServerLoggingRequestHeader = chain -> {
            Request request = chain.request();
            Request newRequest;
            CacheControl.Builder cacheControl = new CacheControl.Builder().noCache().noStore().maxAge(1000, TimeUnit.MILLISECONDS);
            newRequest = request.newBuilder().addHeader(HEADER_CLIENT_ID, deviceId) // for logging
                    .addHeader(HEADER_REQUEST_ID, "" + System.currentTimeMillis())
                    .addHeader(HEADER_PLATFORM_INFO, "android")
                    .cacheControl(cacheControl.build()).build(); // for logging
            return chain.proceed(newRequest);
        };
        Interceptor emptyResponseInterceptor = chain -> {
            Response originalResponse = chain.proceed(chain.request());
            ResponseBody firstByteOfResponse = originalResponse.peekBody(1);
            if (firstByteOfResponse.contentLength() == 0) {
                ResponseBody responseBody = ResponseBody.create(MediaType.parse("application/json"), "{}");
                Response.Builder builder = originalResponse.newBuilder();
                return builder.body(responseBody).build();
            } else if (originalResponse.code() > 401) {
                /*String message = !TextUtils.isEmpty(originalResponse.message()) ? originalResponse.message()
                        : "Something went wrong";
                Error error = new Error(message, originalResponse.code());
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setOkHttpErrorObject(error);
                ResponseBody responseBody = ResponseBody.create(MediaType.parse("application/json"),
                        new Gson().toJson(errorResponse));
                Response.Builder builder = originalResponse.newBuilder();
                return builder.body(responseBody).build();*/
            }
            return originalResponse;
        };
        OkHttpClient.Builder builder;
        if (USE_UNSAFE_OKHTTP) {
            builder = getUnsafeOkHttpClientBuilder();
        } else {
            builder = new OkHttpClient.Builder();
        }
        if (logging != null) {
            builder = builder.addInterceptor(logging);
        }
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
        okHttpClient = builder.addInterceptor(forServerLoggingRequestHeader)
                .connectTimeout(timeOut, TimeUnit.MILLISECONDS)
                .readTimeout(timeOut, TimeUnit.MILLISECONDS)
                .writeTimeout(timeOut, TimeUnit.MILLISECONDS)
                .addInterceptor(emptyResponseInterceptor)
//                .addNetworkInterceptor(new StethoInterceptor())
//                .authenticator(new TokenAuthenticator())
                .cache(null)
                .cookieJar(cookieJar)
                .build();
        //okHttpClient.interceptors().add(new DeleteWithBodyInterceptor());
    }

    private static OkHttpClient.Builder getUnsafeOkHttpClientBuilder() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain,
                                                       String authType) throws
                                CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain,
                                                       String authType) throws
                                CertificateException {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws
                        CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws
                        CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            });
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
