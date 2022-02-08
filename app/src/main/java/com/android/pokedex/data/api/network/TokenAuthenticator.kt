package com.android.pokedex.data.api.network

/*import android.util.Log
import com.android.pokedex.data.constants.PLATFORM_ANDROID
import com.android.pokedex.data.data.models.request.aws.RefreshTokenRequestModel
import com.android.pokedex.data.data.models.response.aws.RefreshTokenResponse
import com.android.pokedex.data.utils.SingletonProvider
import com.android.pokedex.data.api.network.NetworkConstants.BaseUri.CONFIG_BASE_URL_COGNITO
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody*/

/*
class TokenAuthenticator : Authenticator {

    private val TAG = TokenAuthenticator::class.java.canonicalName
    private val HEADER_AUTHORIZATION = "Authorization"
    private val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()

    override fun authenticate(route: Route?, response: Response): Request? {
        Log.d(TAG, "authenticate response: $response")
        if (responseCount(response) < 2) {
            val updatedToken = getNewToken()

            return updatedToken?.let {
                SingletonProvider.getStorage()?.saveAuth(it)
                response.request
                    .newBuilder()
                    .removeHeader(HEADER_AUTHORIZATION)
                    .header(HEADER_AUTHORIZATION, "Bearer $it")
                    .build()
            }
        }
        return null
    }

    @Synchronized
    private fun getNewToken(): String? {
        val username = SingletonProvider.getStorage()?.getUserFcflId()
        val refreshToken = SingletonProvider.getStorage()?.getRefreshToken()
        LogUtil.debugLog(TAG, "getNewToken userName: $username, refreshToken: $refreshToken")
        if (!username.isNullOrEmpty() && !refreshToken.isNullOrEmpty()) {
//            val formBody: RequestBody = FormBody.Builder()
//                .add("refresh_token", refreshToken)
//                .add("username", username)
//                .build()

            val refreshTokenRequest =
                RefreshTokenRequestModel(username, refreshToken, PLATFORM_ANDROID)
            val toJson = Gson().toJson(refreshTokenRequest)
            LogUtil.debugLog(TAG, "getNewToken refreshTokenRequestToJson: $toJson")
            val requestBody = toJson.toRequestBody(JSON)
            val request = Request.Builder()
                .url(CONFIG_BASE_URL_COGNITO + "refresh-token")
                .post(requestBody)
                .build()

            SingleOkHttpClient.getOkHttpClient().newCall(request).execute().use {
                return try {
                    if (it.isSuccessful) {
                        val resStr = it.body?.charStream()
                        LogUtil.debugLog(TAG, "getNewToken resStr: $resStr")
                        Gson().fromJson(resStr, RefreshTokenResponse::class.java)?.auth_token
                    } else {
                        null
                    }
                } catch (exc: Exception) {
                    null
                }
            }
        } else {
            return null
        }
    }

    private fun responseCount(resp: Response): Int {
        var response = resp
        var result = 1
        while (response.priorResponse?.also { response = it } != null) {
            result++
        }
        return result
    }
}*/
