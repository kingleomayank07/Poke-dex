//package com.android.pokedex.data.api.network
//
//import com.android.fanchise.App
//import com.android.fanchise.BuildConfig
//import com.android.fanchise.data.localstorage.preferences.ConfigPreference
//import java.util.concurrent.TimeUnit
//
//interface NetworkConstants {
//    interface FcmStatus {
//        companion object {
//            const val PENDING = 1
//            const val DONE = 2
//        }
//    }
//
//    interface HttpStatusCodes {
//        companion object {
//            const val OK = 200
//            const val UNAUTHORIZED = 401
//            const val NOT_FOUND = 404
//            const val METHOD_NOT_ALLOWED = 405
//            const val FORBIDDEN = 403
//
//            /**
//             * The request could not be understood by the server due to malformed syntax.
//             * The client SHOULD NOT repeat the request without modifications.
//             */
//            const val BAD_REQUEST_BY_CLIENT = 400
//
//            /**
//             * GONE
//             */
//            const val GONE = 410
//
//            /**
//             * A generic error message, given when an unexpected condition was encountered
//             * and no more specific message is suitable.
//             */
//            const val INTERNAL_SERVER_ERROR = 500
//
//            /**
//             * The server is currently unavailable (because it is overloaded or down for maintenance).
//             * Generally, this is a temporary state
//             */
//            const val SERVICE_UNAVAILABLE = 503
//
//            /**
//             * The client needs to authenticate to gain network access.
//             * Intended for use by intercepting proxies used to control access to the network
//             * (e.g., "captive portals" used to require agreement to Terms of Service before
//             * granting full Internet access via a Wi-Fi hotspot)
//             */
//            const val CAPTIVE_PORTAL = 511
//
//            /**
//             * The server, while acting as a gateway or proxy, did not receive a timely response from
//             * the upstream server specified by the URI (e.g. HTTP, FTP, LDAP) or some other
//             * auxiliary server (e.g. DNS) it needed to access in attempting to complete the request.
//             */
//            const val GATEWAY_TIMEOUT = 504
//        }
//    }
//
//    object BaseUri {
//        var CONFIG_BASE_URL_TEAM: String = ""
//        var CONFIG_BASE_URL_UMGMT: String = ""
//        var CONFIG_BASE_URL_UMGMT_API: String = ""
//        var CONFIG_BASE_URL_VOTING: String = ""
//        var CONFIG_BASE_URL_COGNITO: String = ""
//        var CONFIG_BASE_URL_MEDIA: String = ""
//        var CONFIG_BASE_URL_SCHEDULER: String = ""
//        var CONFIG_BASE_URL_CMS: String = ""
//        var MEME_SHARE_BASE_URL: String = ""
//        var REFER_SHARE_BASE_URL: String = ""
//        var BASE_URI_WP_FORMS: String = ""
//        var CONFIG_BASE_URL_ADMIN: String = ""
//        var SOCIAL_SHAREABLE_BASE_URL: String = ""
//        var BASE_URI_COGNITO_REFRESH_TOKEN: String = ""
//        var BASE_URI_WP_SHORTCODES: String = ""
//
//        init {
//            val configResponseModel = ConfigPreference.getConfig(App.getInstance())
//            CONFIG_BASE_URL_TEAM = configResponseModel.configurations.BASE_URI_TEAM + "/"
//            CONFIG_BASE_URL_UMGMT = configResponseModel.configurations.BASE_URI_UMGMT + "/"
//            CONFIG_BASE_URL_UMGMT_API = "${BuildConfig.BASE_URL}/umgmt/api/v1/"
//            CONFIG_BASE_URL_VOTING = configResponseModel.configurations.BASE_URI_VOTING + "/"
//            CONFIG_BASE_URL_COGNITO = configResponseModel.configurations.BASE_URI_COGNITO + "/"
//            CONFIG_BASE_URL_MEDIA = configResponseModel.configurations.BASE_URI_MEDIA + "/"
//            CONFIG_BASE_URL_SCHEDULER = configResponseModel.configurations.BASE_URI_SCHEDULER + "/"
//            CONFIG_BASE_URL_CMS = configResponseModel.configurations.BASE_URI_CMS + "/"
//            MEME_SHARE_BASE_URL = "${BuildConfig.SHARE_BASE_URL}?c="
//            REFER_SHARE_BASE_URL = "${BuildConfig.BASE_URL_DEEPLINK}?refer="
//            BASE_URI_WP_FORMS = configResponseModel.configurations.BASE_URI_WP_FORMS + "/"
//            CONFIG_BASE_URL_ADMIN = configResponseModel.configurations.BASE_URI_ADMIN + "/"
//            SOCIAL_SHAREABLE_BASE_URL = "${BuildConfig.BASE_URL_DEEPLINK}?rtk="
//            BASE_URI_COGNITO_REFRESH_TOKEN =
//                configResponseModel.configurations.BASE_URI_COGNITO_REFRESH_TOKEN.plus("/")
//            BASE_URI_WP_SHORTCODES = configResponseModel.configurations.BASE_URI_WP_SHORTCODES + "/"
//        }
//    }
//
//    object ConfigTranslationWordpressBaseUri {
//        val CONFIG_BASE_URL_CONFIG_TRANSLATION = "${BuildConfig.BASE_URL}/umgmt/api/v1/umgmt/"
//    }
//
//    companion object {
//        val DEFAULT_TIMEOUT = TimeUnit.SECONDS.toMillis(20)
//
//    }
//}