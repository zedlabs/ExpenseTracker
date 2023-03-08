package ml.zedlabs.expenseButler

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication: Application() {

    //private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate() {
        super.onCreate()
        //firebaseAnalytics = Firebase.analytics
    }
    fun logFirebase(eventName: String, vararg track: Pair<String, String>) {
//        firebaseAnalytics.logEvent(eventName) {
//            track.forEach {
//                param(it.first, it.second)
//            }
//        }
    }
}