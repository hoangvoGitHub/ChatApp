package com.hoangkotlin.chatapp.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.hoangkotlin.chatapp.ChatApplication
import com.hoangkotlin.chatapp.R
import com.hoangkotlin.chatapp.databinding.ActivityMainBinding
import com.hoangkotlin.chatapp.firebase.utils.StorageReference
import com.hoangkotlin.chatapp.logindata.model.LoggedInUser
import com.hoangkotlin.chatapp.ui.login.LoginActivity
import io.getstream.avatarview.AvatarView
import io.getstream.avatarview.coil.loadImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var binding: ActivityMainBinding
    private lateinit var headerImageView: AvatarView
    private lateinit var headerNameTextView: TextView
    private lateinit var headerEmailTextView: TextView
    private lateinit var logInPreference: SharedPreferences
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    lateinit var toolbar: MaterialToolbar
    private var username: String? = null
    private var password: String? = null
    private var displayName: String? = null
    private var uid: String? = null

    private var _currentUser: LoggedInUser? = null
    val currentUser: LoggedInUser?
        get() = _currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logInPreference =
            this@MainActivity.getSharedPreferences("logged_in_user", Context.MODE_PRIVATE)
        username = logInPreference.getString("username", null)
        password = logInPreference.getString("password", null)
        displayName = logInPreference.getString("displayName", null)
        uid = logInPreference.getString("uid", null)

        if (username == null || password == null) {
            logOut()
        } else {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            firebaseFirestore = FirebaseFirestore.getInstance()
            auth = FirebaseAuth.getInstance()
            firebaseStorage = FirebaseStorage.getInstance()

            _currentUser = LoggedInUser(username!!, password!!, displayName!!, uid!!)


            toolbar = binding.appBarMain.toolbar
            setSupportActionBar(toolbar)

            drawerLayout = binding.drawerLayout
            val navView: NavigationView = binding.navView
            navController = findNavController(R.id.nav_host_fragment_content_main)

            // Initialize header
            val header = navView.getHeaderView(0)
            // User name
            headerNameTextView = header.findViewById(R.id.nameTextView)
            headerNameTextView.text = displayName
            headerEmailTextView = header.findViewById(R.id.emailTextView)
            headerEmailTextView.text = username

            // Image view
            headerImageView = header.findViewById(R.id.profileImageView)
            loadHeaderImage(headerImageView)

            // Set up the app bar compose item

            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_home, R.id.nav_profile, R.id.nav_slideshow
                ), drawerLayout
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)



            binding.logOutView.setOnClickListener {
                logOut()
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return item.onNavDestinationSelected(navController) ||super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }



    // Clear focus for edit text when the user clicks outside the message input bar
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                val v: View? = currentFocus
                if (v is EditText) {
                    val parent = v.parent
                    parent as View
                    val outRect = Rect()
                    parent.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                        v.clearFocus()
                        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    // Load user's avatar to the header in the drawer
    private fun loadHeaderImage(avatarView: AvatarView) {
        val storageReference = Firebase.storage
        val ref = storageReference.reference
            .child(
                "${StorageReference.PROFILE_IMAGE}/$uid"
            )
        ref.downloadUrl.addOnCompleteListener {
            if (it.isSuccessful && it.result != null) {
                avatarView.loadImage(it.result)
            } else {
                avatarView.loadImage(R.drawable.avatar)
            }
        }
        avatarView.loadImage(R.drawable.avatar)
    }

    // log out the current user
    private fun logOut() {
        val intent = Intent(this, LoginActivity::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            (application as ChatApplication).database.clearAllTables()
        }
        intent.putExtra("logOutSignal", "loggedOut")
        startActivity(intent)
        finish()
    }


}

