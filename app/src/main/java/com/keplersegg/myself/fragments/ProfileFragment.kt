package com.keplersegg.myself.fragments


import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.keplersegg.myself.activities.MasterActivity
import kotlinx.android.synthetic.main.fragment_profile.*

import com.keplersegg.myself.interfaces.ISignOut
import com.keplersegg.myself.async.SignOut
import com.keplersegg.myself.models.User
import com.keplersegg.myself.R
import com.keplersegg.myself.helper.ServiceMethods
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

        if (activity?.application?.user != null) {
            lblUserName!!.text = activity!!.application!!.user!!.FirstName + " " + activity!!.application!!.user!!.LastName

            doAsync {

                val allBadges = activity!!.AppDB().userBadgeDao().all
                var user = ServiceMethods.getUser(activity!!)
                activity!!.application!!.user!!.Score = user!!.Score

                uiThread {

                    lblScore!!.text = activity!!.application!!.user!!.Score.toString()

                    setTint(imgBadge1!!, false)
                    setTint(imgBadge2!!, false)
                    setTint(imgBadge3!!, false)

                    for (b in allBadges) {

                        if (b.BadgeId == 1) {

                            setTint(imgBadge1!!, true)
                        } else if (b.BadgeId == 2) {

                            setTint(imgBadge2!!, true)
                        } else if (b.BadgeId == 3) {

                            setTint(imgBadge3!!, true)
                        }
                    }
                }
            }
        }
        else {
            lblUserName!!.text = "Guest"
            lblScore!!.text = "0"
            setTint(imgBadge1!!, false)
            setTint(imgBadge2!!, false)
            setTint(imgBadge3!!, false)
        }
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

    private fun setTint(img: ImageView, isEnabled: Boolean) {

        if (isEnabled) {
            img.clearColorFilter();
        }
        else {
            val semiTransparentGrey = Color.argb(155, 185, 185, 185);
            img.setColorFilter(semiTransparentGrey,
                    android.graphics.PorterDuff.Mode.SRC_ATOP)
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
