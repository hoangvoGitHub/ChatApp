package com.hoangkotlin.chatapp.ui.register

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hoangkotlin.chatapp.databinding.ActivityRegisterBinding
import com.hoangkotlin.chatapp.ui.login.LoginActivity
import com.hoangkotlin.chatapp.ui.login.afterTextChanged


class RegisterActivity : AppCompatActivity() {
    private val registerViewModel: RegisterViewModel by viewModels()
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
        database = Firebase.database

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this);
        val username = binding.username
        val password = binding.password
        val confirmPassword = binding.confirmPassword
        val register = binding.register
        val name = binding.name
        val loading = binding.loading
        val addProfileImage = binding.addProfileImage

        registerViewModel.registerFormState.observe(this@RegisterActivity, Observer {
            val registerFormState = it ?: return@Observer

            // disable login button unless both username / password is valid
            register.isEnabled = registerFormState.isDataValid
            if (registerFormState.nameError != null) {
                name.error = getString(registerFormState.nameError)
            }
            if (registerFormState.usernameError != null) {
                username.error = getString(registerFormState.usernameError)
            }
            if (registerFormState.passwordError != null) {
                password.error = getString(registerFormState.passwordError)
            }
            if (registerFormState.confirmPasswordError != null) {
                confirmPassword.error = getString(registerFormState.confirmPasswordError)
            }
        })

        registerViewModel.registerResult.observe(this@RegisterActivity, Observer {
            val registerResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (!registerResult) {
                showRegisterFailed()
            }
            if (registerResult) {
                val successfulString = "User ${name.text} Created"
                Toast.makeText(applicationContext, successfulString, Toast.LENGTH_SHORT).show()
                updateUiWithUser()
            }
            setResult(RESULT_OK)
            finish()
        })
        name.afterTextChanged {
            registerViewModel.registerDataChanged(
                username.text.toString(),
                password.text.toString(),
                confirmPassword.text.toString(),
                name.text.toString()
            )
        }

        username.afterTextChanged {
            registerViewModel.registerDataChanged(
                username.text.toString(),
                password.text.toString(),
                confirmPassword.text.toString(),
                name.text.toString()
            )
        }

        password.afterTextChanged {
            registerViewModel.registerDataChanged(
                username.text.toString(),
                password.text.toString(),
                confirmPassword.text.toString(),
                name.text.toString()
            )
        }


        confirmPassword.apply {
            afterTextChanged {
                registerViewModel.registerDataChanged(
                    username.text.toString(),
                    password.text.toString(),
                    confirmPassword.text.toString(),
                    name.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        registerViewModel.register(
                            username.text.toString(),
                            password.text.toString(),
                            name.text.toString()
                        )
                }
                false
            }

            register.setOnClickListener {
                loading.visibility = View.VISIBLE
                registerViewModel.register(
                    username.text.toString(),
                    password.text.toString(),
                    name.text.toString()
                )
            }

            addProfileImage.setOnClickListener {
                addProfileImage()
            }


        }
    }

    private fun updateUiWithUser() {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun showRegisterFailed() {
        val errorString = "Register Error"
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    private fun openCamera() {

    }

    @SuppressLint("RtlHardcoded")
    private fun addProfileImage() {
        val options = arrayOf("Camera", "Gallery", "Cancel")
        val alertBuilder = AlertDialog.Builder(this@RegisterActivity)

        with(alertBuilder) {
            setTitle("Add Profile Image Options")
            setMessage("Add Profile Image From?")
            setPositiveButton(options[0]) { _, _ ->
                openCamera()
            }
            setNegativeButton(options[1]) {  _, _ ->
                openCamera()
            }
            setNeutralButton(options[2]) { dialog, _ ->
                dialog.dismiss()
            }

        }
        val dialog = alertBuilder.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val wmp = dialog.window!!.attributes
        wmp.gravity = Gravity.TOP or Gravity.LEFT
        wmp.y = 200 //y position

        dialog.show()
    }

    private fun openGallery() {
        TODO("Not yet implemented")
    }
}
