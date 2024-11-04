package com.example.opsc7213_goalignite

import android.app.Activity
import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewException
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory


class InAppReviewHelper(private val context: Context) {

    private val reviewManager: ReviewManager = ReviewManagerFactory.create(context)

    fun requestReviewInfo(onReviewInfoReceived: (ReviewInfo?) -> Unit, onError: (Int?) -> Unit) {
        val request: Task<ReviewInfo> = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo: ReviewInfo? = task.result
                onReviewInfoReceived(reviewInfo)
            } else {
                val reviewErrorCode = (task.exception as? ReviewException)?.errorCode
                onError(reviewErrorCode)
            }
        }
    }

    fun launchReviewFlow(activity: Activity, reviewInfo: ReviewInfo) {
        val flow = reviewManager.launchReviewFlow(activity, reviewInfo)
        flow.addOnCompleteListener {
            // Flow has finished. Proceed with app flow.
        }
    }
}
