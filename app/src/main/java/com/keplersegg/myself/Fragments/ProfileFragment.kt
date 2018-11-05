package com.keplersegg.myself.Fragments


import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_profile.*

import com.google.android.gms.common.api.GoogleApiClient
import com.keplersegg.myself.Async.ISignOut
import com.keplersegg.myself.Async.SignOut
import com.keplersegg.myself.Models.User
import com.keplersegg.myself.R


class ProfileFragment : MasterFragment(), ISignOut {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.layout = R.layout.fragment_profile
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtLogout.setOnClickListener { SignOut().Run(this@ProfileFragment) }
    }

    override fun onResume() {
        super.onResume()
        SetTitle(R.string.lbl_profile)

        lblUserName!!.text = if (activity!!.application.user != null) activity!!.application.user.FirstName + " " + activity!!.application.user.LastName else "Guest"

        if (!activity!!.application.user.PictureUrl.isNullOrBlank()) {

            Glide.with(this)
                    .load(activity!!.application.user.PictureUrl)
                    .apply(RequestOptions()
                            .placeholder(R.drawable.ic_baseline_account_circle_24px)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .apply(RequestOptions.circleCropTransform())
                    .into(imgUserPicture)
        }
    }

    override fun onSignOut() {
        activity!!.NavigateToActivity("Login", true)
    }

    override fun GetUser(): User {
        return activity!!.application.user
    }

    override fun GetGoogleApiClient(): GoogleApiClient {
        return activity!!.mGoogleApiClient
    }

    companion object {

        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }
}
