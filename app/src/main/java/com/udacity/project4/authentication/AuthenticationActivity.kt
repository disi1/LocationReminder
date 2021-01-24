package com.udacity.project4.authentication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.udacity.project4.R
import com.udacity.project4.locationreminders.RemindersActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {

    companion object {
        val TAG = AuthenticationActivity::class.java.simpleName
        const val SIGN_IN_RESULT_CODE = 10001
    }

    private val viewModel by viewModels<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        supportActionBar?.hide()

        findViewById<Button>(R.id.login_button).setOnClickListener {
            launchSignInFlow()
        }

        viewModel.authenticationState.observe(this, { authenticationState ->
            when(authenticationState) {
                AuthenticationViewModel.AuthenticationState.AUTHENTICATED -> startActivity(Intent(this, RemindersActivity::class.java))
                AuthenticationViewModel.AuthenticationState.UNAUTHENTICATED -> Log.e(TAG, "Not authenticated!")
                else -> Log.e(TAG, "$authenticationState state that doesn't require any UI change.")
            }
        })

//          TODO: a bonus is to customize the sign in flow to look nice using :
        //https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md#custom-layout

    }

    private fun launchSignInFlow() {
        if(isNetworkAvailable()) {
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build()
            )

            startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers)
                    .setTheme(R.style.FirebaseAuthTheme)
                    .build(),
                SIGN_IN_RESULT_CODE
            )
        }
        else {
            Toast.makeText(this, R.string.network_unavailable, Toast.LENGTH_LONG).show()
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            return try {
                if(connectivityManager.activeNetworkInfo == null) {
                    false
                } else {
                    connectivityManager.activeNetworkInfo?.isConnected!!
                }
            } catch (e: Exception) {
                Log.e(TAG, "Connectivity error: $e")
                false
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == SIGN_IN_RESULT_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if(resultCode == Activity.RESULT_OK) {
                Log.i(
                    TAG,
                    "Successfully signed in user " +
                            "${FirebaseAuth.getInstance().currentUser?.displayName}!"
                )
            } else {
                Log.i(TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }
}
