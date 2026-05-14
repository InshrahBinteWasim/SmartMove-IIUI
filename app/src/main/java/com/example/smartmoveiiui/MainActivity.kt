package com.example.smartmoveiiui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.smartmoveiiui.databinding.ActivityMainBinding
import com.example.smartmoveiiui.model.AppRole
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

import java.util.Locale

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        
        if (currentUser == null) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            return
        }

        setSupportActionBar(binding.toolbar)

        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.nav_back, R.string.nav_back,
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)

        // Setup Dashboard data based on role
        val roleName = intent.getStringExtra(AppRole.EXTRA_NAME) ?: AppRole.COMMUTER.name
        val role = AppRole.valueOf(roleName)
        
        setupDashboard(role, currentUser.email ?: "")
    }

    private fun setupDashboard(role: AppRole, email: String) {
        binding.tvWelcome.text = getString(R.string.dash_welcome_back)
        val roleFormatted = role.name.replace("_", " ").lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        binding.tvSignedAs.text = getString(R.string.dash_signed_as, roleFormatted)
        
        // --- CREDIT SAVING STRATEGY: Role-based UI Filtering ---
        // Hide sections the user shouldn't see to prevent unnecessary navigation/reads
        val menu = binding.navView.menu
        menu.findItem(R.id.nav_group_staff).isVisible = (role == AppRole.TRANSPORT_STAFF || role == AppRole.SYSTEM_ADMINISTRATOR)
        menu.findItem(R.id.nav_group_admin).isVisible = (role == AppRole.SYSTEM_ADMINISTRATOR)

        // Update Nav Header
        val headerView = binding.navView.getHeaderView(0)
        headerView.findViewById<android.widget.TextView>(R.id.nav_user_email).text = email
        headerView.findViewById<android.widget.TextView>(R.id.nav_user_name).text = roleFormatted

        binding.btnGoToTracking.setOnClickListener {
            startActivity(Intent(this, TrackingActivity::class.java))
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_tracking -> {
                startActivity(Intent(this, TrackingActivity::class.java))
            }
            R.id.nav_queries -> {
                startActivity(Intent(this, SubmitQueryActivity::class.java))
            }
            R.id.nav_announcements -> {
                startActivity(Intent(this, AnnouncementsActivity::class.java))
            }
            R.id.nav_schedule -> {
                startActivity(Intent(this, ScheduleActivity::class.java))
            }
            R.id.nav_chatbot -> {
                startActivity(Intent(this, ChatbotActivity::class.java))
            }
            R.id.nav_post_announcement -> {
                startActivity(Intent(this, PostAnnouncementActivity::class.java))
            }
            R.id.nav_manage_buses -> {
                startActivity(Intent(this, ManageFleetActivity::class.java))
            }
            R.id.nav_manage_users -> {
                startActivity(Intent(this, ManageUsersActivity::class.java))
            }
            R.id.nav_logout -> {
                auth.signOut()
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
