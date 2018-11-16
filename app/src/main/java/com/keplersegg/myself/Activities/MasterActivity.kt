package com.keplersegg.myself.Activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.keplersegg.myself.Interfaces.IHttpProvider
import com.keplersegg.myself.Helper.TokenType
import com.keplersegg.myself.Interfaces.IErrorMessage
import com.keplersegg.myself.Models.User
import com.keplersegg.myself.MySelfApplication
import com.keplersegg.myself.R
import com.keplersegg.myself.Room.AppDatabase
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

@SuppressLint("Registered")
open class MasterActivity : AppCompatActivity(), IHttpProvider, IErrorMessage {

    var application: MySelfApplication? = null
    var master: MasterActivity? = null
    var mGoogleSigninClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        master = this
        application = getApplication() as MySelfApplication

        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = GetColor(R.color.colorPrimary)

        initializeGPlusSettings()
    }

    fun GetColor(id: Int): Int {
        return ContextCompat.getColor(this, id)
    }

    fun AppDB(): AppDatabase {
        return AppDatabase.getAppDatabase(this)!!
    }

    fun NavigateToActivity(activityName: String, clearTop: Boolean) {

        var i: Intent? = null

        when (activityName) {

            "Main" ->

                i = Intent(application, MainActivity::class.java)

            "Login" ->

                i = Intent(application, LoginActivity::class.java)

            else -> {
            }
        }

        if (i != null)
            NavigateToActivity(i, clearTop)
    }

    protected fun NavigateToActivity(i: Intent?, clearTop: Boolean) {

        if (clearTop)
        // ana sayfadan geri dön'e tıklandığında çıkış yapma diyaloğu gelmesi için.
            i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

        startActivity(i)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

        if (clearTop)
        // ana sayfadan geri dön'e tıklandığında çıkış yapma diyaloğu gelmesi için.
            finish()
    }

    private fun initializeGPlusSettings() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(getString(R.string.google_client_id))
                //.requestServerAuthCode(getString(R.string.google_client_id))
                .requestEmail()
                .build()

        mGoogleSigninClient = GoogleSignIn.getClient(this, gso)
    }

    protected fun handleGoogleSignInResult(account: GoogleSignInAccount): Boolean {

        setToken(TokenType.Google, account.idToken)

        application!!.user = User()
        application!!.user!!.Email = account.email
        application!!.user!!.FirstName = account.givenName
        application!!.user!!.LastName = account.familyName
        val pictureUri = account.photoUrl
        if (pictureUri != null)
            application!!.user!!.PictureUrl = pictureUri.toString()

        return true
    }

    open fun setToken(tokenType: TokenType, token: String?) {

        when (tokenType) {

            TokenType.MySelf -> application!!.dataStore!!.setAccessToken(token)

            TokenType.Facebook -> application!!.dataStore!!.setFacebookToken(token)

            TokenType.Google -> application!!.dataStore!!.setGoogleToken(token)

        }
    }

    override fun getConnectivityManager(): ConnectivityManager {
        return getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    override fun getAccessToken(): String? {
        return application!!.dataStore!!.getAccessToken()
    }

    override fun getDeviceId(): String? {
        return application!!.dataStore!!.getRegisterID()
    }

    override fun showErrorMessage(message: String) {

        runOnUiThread { Toast.makeText(this@MasterActivity, message, Toast.LENGTH_SHORT).show() }
    }

    override fun logException(exception: Exception, message: String) {

        //CrashLogger.AddExceptionLog(message, exc);
        showErrorMessage(message)
    }
}
