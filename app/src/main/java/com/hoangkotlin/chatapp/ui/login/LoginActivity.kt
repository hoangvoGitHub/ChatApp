package com.hoangkotlin.chatapp.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.hoangkotlin.chatapp.ui.MainActivity
import com.hoangkotlin.chatapp.R
import com.hoangkotlin.chatapp.databinding.ActivityLoginBinding
import com.hoangkotlin.chatapp.ui.register.RegisterActivity


//import com.hoangkotlin.chatapp.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseMessaging: FirebaseMessaging


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        loginViewModel = ViewModelProvider(
            this,
            LoginViewModelFactory(applicationContext)
        )[LoginViewModel::class.java]

        auth = FirebaseAuth.getInstance()
        firebaseMessaging = FirebaseMessaging.getInstance()
        binding = ActivityLoginBinding.inflate(layoutInflater)

        val logOutSignal = intent.extras?.getString("logOutSignal")
        if (logOutSignal != null && logOutSignal == "loggedOut") {
            loginViewModel.loginRepository.logout()
        }

        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val login = binding.login
        val register = binding.register
        val loading = binding.loading

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
                setResult(Activity.RESULT_OK)

                //Complete and destroy login activity once successful
                finish()
            }

        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
        }

        register.setOnClickListener {
            goToRegisterScreen()
        }

    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        // TODO : initiate successful logged in experience

    }

    private fun goToRegisterScreen() {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)
    }


    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}