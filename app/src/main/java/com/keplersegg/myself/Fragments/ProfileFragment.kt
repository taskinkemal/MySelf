package com.keplersegg.myself.Fragments


import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.keplersegg.myself.Activities.MasterActivity
import kotlinx.android.synthetic.main.fragment_profile.*

import com.keplersegg.myself.Interfaces.ISignOut
import com.keplersegg.myself.Async.SignOut
import com.keplersegg.myself.Models.User
import com.keplersegg.myself.R
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class ProfileFragment : MasterFragment(), ISignOut {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.layout = R.layout.fragment_profile
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtLogin.setOnClickListener { activity!!.NavigateToActivity("Login", true) }
        txtLogout.setOnClickListener { SignOut().Run(this@ProfileFragment) }

        txtLogin.visibility = if (activity!!.application!!.user == null) View.VISIBLE else View.GONE
        txtLogout.visibility = if (activity!!.application!!.user != null) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        SetTitle(R.string.lbl_profile)

        lblUserName!!.text = if (activity!!.application!!.user != null) activity!!.application!!.user!!.FirstName + " " + activity!!.application!!.user!!.LastName else "Guest"

        if (!activity!!.application!!.user?.PictureUrl.isNullOrBlank()) {

            Glide.with(activity!!)
                    .load(activity!!.application!!.user!!.PictureUrl)
                    .apply(RequestOptions()
                            .placeholder(R.drawable.ic_baseline_account_circle_24px)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .apply(RequestOptions.circleCropTransform())
                    .into(imgUserPicture)
        }
        else {

            Glide.with(activity!!)
                    .load(R.drawable.ic_baseline_account_circle_24px)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imgUserPicture)
        }
    }

    override fun onSignOut() {

        doAsync {

            activity!!.AppDB().taskDao().deleteAll() // this will also delete the entries.

            uiThread {
                activity!!.application!!.user = null
                activity!!.application!!.dataStore!!.setAccessToken(null)
                activity!!.application!!.dataStore!!.setGoogleToken(null)
                activity!!.application!!.dataStore!!.setFacebookToken(null)
                activity!!.NavigateToActivity("Login", true)
            }
        }
    }

    override fun GetUser(): User? {
        return activity!!.application!!.user
    }

    override fun GetGoogleSignInClient(): GoogleSignInClient {
        return activity!!.mGoogleSigninClient!!
    }

    override fun GetMasterActivity() : MasterActivity {
        return activity!!
    }

    companion object {

        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }
}
