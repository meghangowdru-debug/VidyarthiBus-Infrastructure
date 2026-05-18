package com.vidyarthi.bus.tracker

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Announcement
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.Date
import java.util.concurrent.TimeUnit

private val DeepGreen = Color(0xFF065F46)
private val FreshGreen = Color(0xFF10B981)
private val AppBg = Color(0xFFF8F9FA)
private val TextDark = Color(0xFF111827)
private val Muted = Color(0xFF9CA3AF)
private const val REPORT_EXPIRY_MINUTES = 30L

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeFirebase(this)

        setContent {
            VidyarthiTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                    VidyarthiApp()
                }
            }
        }
    }
}

fun initializeFirebase(context: Context) {
    if (FirebaseApp.getApps(context).isNotEmpty()) return

    val options = FirebaseOptions.Builder()
        .setProjectId("vidyarthi-bus-fcfdd")
        .setApplicationId("1:417098586139:web:835e91ce9c04e56a2b40be")
        .setApiKey("AIzaSyCw-Zm3133DMlbkodC_z5wYuTCF9fWKxOA")
        .setStorageBucket("vidyarthi-bus-fcfdd.firebasestorage.app")
        .setDatabaseUrl("https://vidyarthi-bus-fcfdd-default-rtdb.firebaseio.com")
        .build()
    FirebaseApp.initializeApp(context, options)
}

@Composable
private fun VidyarthiTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = DeepGreen,
            secondary = FreshGreen,
            background = AppBg,
            surface = Color.White,
            onPrimary = Color.White,
            onSurface = TextDark
        ),
        typography = MaterialTheme.typography,
        content = content
    )
}

data class Route(
    val id: String = "",
    val busNumber: String = "",
    val origin: String = "",
    val destination: String = "",
    val sharedAutoContacts: List<SharedAutoContact> = emptyList(),
    val crowdStatus: String = "empty",
    val etaMinutes: Int = 8,
    val seatsLeft: Int = 12
)

data class SharedAutoContact(val name: String = "", val phone: String = "")
data class TransportAlternative(
    val service: String,
    val type: String,
    val etaMinutes: Int,
    val seats: Int,
    val status: String,
    val route: String,
    val price: String,
    val color: Color,
    val icon: ImageVector
)
data class UserProfile(
    val name: String,
    val phone: String,
    val photoUri: String?
)
data class Report(
    val id: String = "",
    val routeId: String = "",
    val status: String = "empty",
    val reporterId: String = "",
    val createdAt: Date = Date(),
    val lat: Double? = null,
    val lng: Double? = null
)

data class Announcement(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val timestamp: Date,
    var isRead: Boolean
)

private val campusSources = listOf(
    "Kolar",
    "Mandya",
    "Mysore",
    "Whitefield",
    "Electronic City",
    "Silk Board",
    "Tumkur"
)

private val campusDestinations = listOf(
    "PES University",
    "RR Campus",
    "Main Block",
    "South Campus",
    "Engineering Block",
    "Hostel Gate",
    "Library Block"
)

private val curatedRoutes = listOf(
    Route(
        id = "kolar-pes-1",
        busNumber = "B21",
        origin = "Kolar",
        destination = "PES University",
        sharedAutoContacts = listOf(
            SharedAutoContact("Ravi Shared Auto", "9845012345"),
            SharedAutoContact("Suresh Van", "9731211223")
        ),
        crowdStatus = "seated",
        etaMinutes = 11,
        seatsLeft = 9
    ),
    Route(
        id = "mandya-rr-1",
        busNumber = "B45",
        origin = "Mandya",
        destination = "RR Campus",
        sharedAutoContacts = listOf(
            SharedAutoContact("Ganesh Travels", "9900112233"),
            SharedAutoContact("College Pickup Jeep", "9739811220")
        ),
        crowdStatus = "empty",
        etaMinutes = 14,
        seatsLeft = 18
    ),
    Route(
        id = "mysore-south-1",
        busNumber = "B12",
        origin = "Mysore",
        destination = "South Campus",
        sharedAutoContacts = listOf(
            SharedAutoContact("KSRTC Express", "9880123456"),
            SharedAutoContact("Railway Pickup Van", "9880456123")
        ),
        crowdStatus = "seated",
        etaMinutes = 18,
        seatsLeft = 7
    ),
    Route(
        id = "whitefield-main-1",
        busNumber = "B33",
        origin = "Whitefield",
        destination = "Main Block",
        sharedAutoContacts = listOf(
            SharedAutoContact("Metro Feeder Cab", "9972123456"),
            SharedAutoContact("Shuttle Mini Bus", "9845781200")
        ),
        crowdStatus = "empty",
        etaMinutes = 9,
        seatsLeft = 15
    ),
    Route(
        id = "silkboard-engg-1",
        busNumber = "B18",
        origin = "Silk Board",
        destination = "Engineering Block",
        sharedAutoContacts = listOf(
            SharedAutoContact("Naveen Pickup Point", "9740011223"),
            SharedAutoContact("Silk Board Mini Bus", "9740098765")
        ),
        crowdStatus = "seated",
        etaMinutes = 7,
        seatsLeft = 6
    ),
    Route(
        id = "ecity-pes-1",
        busNumber = "B77",
        origin = "Electronic City",
        destination = "PES University",
        sharedAutoContacts = listOf(
            SharedAutoContact("Tech Park Shuttle", "9845123490"),
            SharedAutoContact("E-City Shared Van", "9845019876")
        ),
        crowdStatus = "full",
        etaMinutes = 16,
        seatsLeft = 2
    ),
    Route(
        id = "tumkur-rr-1",
        busNumber = "B29",
        origin = "Tumkur",
        destination = "RR Campus",
        sharedAutoContacts = listOf(
            SharedAutoContact("Tumkur Express Van", "9632012345"),
            SharedAutoContact("Highway Shared Auto", "9632098765")
        ),
        crowdStatus = "empty",
        etaMinutes = 22,
        seatsLeft = 20
    ),
    Route(
        id = "kolar-rr-2",
        busNumber = "B22",
        origin = "Kolar",
        destination = "RR Campus",
        sharedAutoContacts = listOf(
            SharedAutoContact("Kolar Morning Shuttle", "9845098761"),
            SharedAutoContact("NH75 Pool Cab", "9845098762")
        ),
        crowdStatus = "empty",
        etaMinutes = 17,
        seatsLeft = 14
    ),
    Route(
        id = "mandya-pes-2",
        busNumber = "B46",
        origin = "Mandya",
        destination = "PES University",
        sharedAutoContacts = listOf(
            SharedAutoContact("Mandya Student Cab", "9900112244"),
            SharedAutoContact("Sugar City Shuttle", "9900112255")
        ),
        crowdStatus = "seated",
        etaMinutes = 19,
        seatsLeft = 11
    ),
    Route(
        id = "mysore-rr-2",
        busNumber = "B13",
        origin = "Mysore",
        destination = "RR Campus",
        sharedAutoContacts = listOf(
            SharedAutoContact("Mysore Road Shuttle", "9880456124"),
            SharedAutoContact("Ramanagara Link Van", "9880456125")
        ),
        crowdStatus = "empty",
        etaMinutes = 24,
        seatsLeft = 16
    ),
    Route(
        id = "whitefield-engg-2",
        busNumber = "B34",
        origin = "Whitefield",
        destination = "Engineering Block",
        sharedAutoContacts = listOf(
            SharedAutoContact("ITPL Campus Cab", "9972123460"),
            SharedAutoContact("Kadugodi Feeder", "9972123461")
        ),
        crowdStatus = "seated",
        etaMinutes = 13,
        seatsLeft = 10
    ),
    Route(
        id = "silkboard-pes-2",
        busNumber = "B19",
        origin = "Silk Board",
        destination = "PES University",
        sharedAutoContacts = listOf(
            SharedAutoContact("BTM Shared Auto", "9740098766"),
            SharedAutoContact("Forum Shuttle", "9740098767")
        ),
        crowdStatus = "empty",
        etaMinutes = 10,
        seatsLeft = 13
    ),
    Route(
        id = "ecity-south-2",
        busNumber = "B78",
        origin = "Electronic City",
        destination = "South Campus",
        sharedAutoContacts = listOf(
            SharedAutoContact("Phase 1 Pickup", "9845019877"),
            SharedAutoContact("Hosur Road Cab", "9845019878")
        ),
        crowdStatus = "seated",
        etaMinutes = 15,
        seatsLeft = 8
    ),
    Route(
        id = "tumkur-main-2",
        busNumber = "B30",
        origin = "Tumkur",
        destination = "Main Block",
        sharedAutoContacts = listOf(
            SharedAutoContact("Tumkur Toll Shuttle", "9632098766"),
            SharedAutoContact("Nelamangala Link Cab", "9632098767")
        ),
        crowdStatus = "full",
        etaMinutes = 26,
        seatsLeft = 3
    ),
    Route(
        id = "kengeri-rr-1",
        busNumber = "B52",
        origin = "Kengeri",
        destination = "RR Campus",
        sharedAutoContacts = listOf(
            SharedAutoContact("Ramesh Auto Stand", "9902012345"),
            SharedAutoContact("Kengeri Campus Cab", "9902087654")
        ),
        crowdStatus = "seated",
        etaMinutes = 10,
        seatsLeft = 8
    )
)

private val fallbackRoutes = buildCampusRoutes()

private fun buildCampusRoutes(): List<Route> {
    val curatedKeys = curatedRoutes.map { routeKey(it.origin, it.destination) }.toSet()
    val generatedRoutes = campusSources.flatMapIndexed { sourceIndex, source ->
        campusDestinations.mapIndexedNotNull { destinationIndex, destination ->
            val key = routeKey(source, destination)
            if (key in curatedKeys) {
                null
            } else {
                generatedRoute(source, destination, sourceIndex, destinationIndex)
            }
        }
    }
    return (curatedRoutes + generatedRoutes)
        .filter { it.origin in campusSources && it.destination in campusDestinations }
        .sortedWith(compareBy<Route> { campusSources.indexOf(it.origin) }.thenBy { campusDestinations.indexOf(it.destination) }.thenBy { it.etaMinutes })
}

private fun mergeCampusRoutes(firebaseRoutes: List<Route>): List<Route> {
    val cleanFirebaseRoutes = firebaseRoutes.filter { it.origin.isNotBlank() && it.destination.isNotBlank() }
    val firebaseKeys = cleanFirebaseRoutes.map { routeKey(it.origin, it.destination) }.toSet()
    return (cleanFirebaseRoutes + fallbackRoutes.filter { routeKey(it.origin, it.destination) !in firebaseKeys })
        .sortedWith(compareBy<Route> { campusSources.indexOf(it.origin).takeIf { index -> index >= 0 } ?: Int.MAX_VALUE }.thenBy { campusDestinations.indexOf(it.destination).takeIf { index -> index >= 0 } ?: Int.MAX_VALUE }.thenBy { it.etaMinutes })
}

private fun generatedRoute(source: String, destination: String, sourceIndex: Int, destinationIndex: Int): Route {
    val number = 40 + (sourceIndex * 7) + destinationIndex
    val eta = 8 + ((sourceIndex * 3 + destinationIndex * 2) % 19)
    val status = when ((sourceIndex + destinationIndex) % 5) {
        0 -> "full"
        1, 3 -> "seated"
        else -> "empty"
    }
    val seats = when (status) {
        "full" -> 1 + ((sourceIndex + destinationIndex) % 2)
        "seated" -> 5 + ((sourceIndex * 2 + destinationIndex) % 6)
        else -> 13 + ((sourceIndex * 3 + destinationIndex * 2) % 12)
    }
    return Route(
        id = "local-${source.slug()}-${destination.slug()}",
        busNumber = "B$number",
        origin = source,
        destination = destination,
        sharedAutoContacts = listOf(
            SharedAutoContact("$source Shared Auto", "98${sourceIndex + 3}${destinationIndex + 4}50123${destinationIndex}"),
            SharedAutoContact("$destination Mini Van", "97${destinationIndex + 2}${sourceIndex + 5}81234${sourceIndex}")
        ),
        crowdStatus = status,
        etaMinutes = eta,
        seatsLeft = seats
    )
}

private fun routeKey(origin: String, destination: String): String = "${origin.normalizedLocation()}|${destination.normalizedLocation()}"

@Composable
private fun VidyarthiApp() {
    val context = LocalContext.current
    val auth = remember { FirebaseAuth.getInstance() }
    var loading by remember { mutableStateOf(true) }
    var user by remember { mutableStateOf(auth.currentUser) }
    var showSplash by remember { mutableStateOf(true) }

    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener {
            user = it.currentUser
            loading = false
        }
        auth.addAuthStateListener(listener)
        onDispose { auth.removeAuthStateListener(listener) }
    }

    when {
        loading -> FullscreenLoader()
        showSplash -> SplashScreen { showSplash = false }
        user == null -> LoginScreen(auth = auth)
        else -> DashboardScreen(user = user!!, onSignOut = { auth.signOut() })
    }
}

@Composable
private fun FullscreenLoader() {
    Box(Modifier.fillMaxSize().background(Color.White), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = FreshGreen)
    }
}

@Composable
private fun SplashScreen(onComplete: () -> Unit) {
    var progress by remember { mutableStateOf(0f) }
    val rotation = rememberInfiniteTransition(label = "logoRotation").animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(4500, easing = LinearEasing), RepeatMode.Restart),
        label = "rotation"
    )

    LaunchedEffect(Unit) {
        repeat(100) {
            kotlinx.coroutines.delay(20)
            progress = it + 1f
        }
        kotlinx.coroutines.delay(450)
        onComplete()
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White).padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .clip(CircleShape)
                    .background(Brush.radialGradient(listOf(Color(0xFFD1FAE5), Color.Transparent)))
            )
            Box(
                modifier = Modifier
                    .size(190.dp)
                    .rotate(rotation.value)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(DeepGreen, FreshGreen, Color(0xFFE5E7EB))))
                    .border(8.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.DirectionsBus, contentDescription = null, tint = Color.White, modifier = Modifier.size(82.dp))
            }
        }

        Spacer(Modifier.height(44.dp))
        ProgressBar(progress / 100f, Modifier.width(190.dp))
        Spacer(Modifier.height(12.dp))
        Text("Establishing Secure Link...", color = Muted, fontSize = 9.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
        Spacer(Modifier.height(42.dp))
        Text("Vidyarthi-Bus", color = DeepGreen, fontSize = 38.sp, fontWeight = FontWeight.Black)
        Text("Smart Crowdsourced Bus Alert", color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
    }
}

@Composable
private fun LoginScreen(auth: FirebaseAuth) {
    val context = LocalContext.current
    val activity = context as Activity
    var phone by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var verificationId by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    var emailLoading by remember { mutableStateOf(false) }
    var emailAuthAction by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    fun formattedPhone(): String {
        val clean = phone.trim()
        return if (clean.startsWith("+")) clean else "+91$clean"
    }

    fun signInWithCredential(credential: PhoneAuthCredential) {
        loading = true
        auth.signInWithCredential(credential)
            .addOnSuccessListener { Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener {
                error = "Invalid code. Please try again."
                loading = false
            }
    }

    fun sendOtp() {
        val normalized = formattedPhone()
        if (!normalized.startsWith("+") || normalized.length < 12) {
            error = "Please enter a valid phone number."
            successMessage = null
            return
        }
        loading = true
        error = null
        successMessage = null
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithCredential(credential)
            }

            override fun onVerificationFailed(e: com.google.firebase.FirebaseException) {
                loading = false
                error = e.localizedMessage ?: "Failed to send code."
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                verificationId = id
                otp = ""
                loading = false
                Toast.makeText(context, "Verification code sent", Toast.LENGTH_SHORT).show()
            }
        }

        PhoneAuthProvider.verifyPhoneNumber(
            PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(normalized)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callbacks)
                .build()
        )
    }

    fun validateEmailPassword(requirePassword: Boolean = true): Boolean {
        val trimmedEmail = email.trim()
        error = when {
            trimmedEmail.isBlank() -> "Please enter your email address."
            !android.util.Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches() -> "Please enter a valid email address."
            requirePassword && password.length < 6 -> "Password must be at least 6 characters."
            else -> null
        }
        if (error != null) successMessage = null
        return error == null
    }

    fun loginWithEmail() {
        if (!validateEmailPassword()) return
        emailLoading = true
        emailAuthAction = "login"
        error = null
        successMessage = null
        auth.signInWithEmailAndPassword(email.trim(), password)
            .addOnSuccessListener {
                emailLoading = false
                emailAuthAction = null
                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                error = it.localizedMessage ?: "Email login failed. Please try again."
                emailLoading = false
                emailAuthAction = null
            }
    }

    fun createEmailAccount() {
        if (!validateEmailPassword()) return
        emailLoading = true
        emailAuthAction = "signup"
        error = null
        successMessage = null
        auth.createUserWithEmailAndPassword(email.trim(), password)
            .addOnSuccessListener {
                emailLoading = false
                emailAuthAction = null
                Toast.makeText(context, "Account created", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                error = it.localizedMessage ?: "Could not create account. Please try again."
                emailLoading = false
                emailAuthAction = null
            }
    }

    fun sendPasswordReset() {
        if (!validateEmailPassword(requirePassword = false)) return
        emailLoading = true
        emailAuthAction = "reset"
        error = null
        successMessage = null
        auth.sendPasswordResetEmail(email.trim())
            .addOnSuccessListener {
                emailLoading = false
                emailAuthAction = null
                successMessage = "Password reset email sent to ${email.trim()}."
                Toast.makeText(context, "Password reset email sent", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                error = it.localizedMessage ?: "Could not send password reset email."
                emailLoading = false
                emailAuthAction = null
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7FBF9))
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(34.dp),
            colors = CardDefaults.cardColors(Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    if (verificationId != null) {
                        IconButton(onClick = { verificationId = null }, modifier = Modifier.align(Alignment.CenterStart).size(38.dp)) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Muted)
                        }
                    }
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Box(Modifier.size(70.dp).clip(CircleShape).background(Color(0xFFECFDF5)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.DirectionsBus, null, tint = DeepGreen, modifier = Modifier.size(36.dp))
                        }
                        Box(Modifier.size(22.dp).clip(CircleShape).background(FreshGreen).border(2.dp, Color.White, CircleShape), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.CheckCircle, null, tint = Color.White, modifier = Modifier.size(12.dp))
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))
                Text("Vidyarthi-Bus", fontSize = 27.sp, fontWeight = FontWeight.Black, color = DeepGreen)
                Text(
                    if (verificationId == null) "Smart campus transport access" else "Enter the code sent to ${formattedPhone()}",
                    fontSize = 11.sp,
                    color = Muted,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(14.dp))

                if (verificationId == null) {
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it.filter { c -> c.isDigit() || c == '+' }.take(14) },
                        label = { Text("Phone Number") },
                        leadingIcon = { Icon(Icons.Default.Phone, null, tint = Muted) },
                        prefix = { if (!phone.startsWith("+")) Text("+91", fontWeight = FontWeight.Bold, color = Muted) },
                        placeholder = { Text("Enter 10 Digits") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    CompactAuthButton("GET LOGIN CODE", loading = loading, icon = Icons.Default.Smartphone) { sendOtp() }
                } else {
                    OutlinedTextField(
                        value = otp,
                        onValueChange = {
                            otp = it.filter(Char::isDigit).take(6)
                            if (otp.length == 6) verificationId?.let { id -> signInWithCredential(PhoneAuthProvider.getCredential(id, otp)) }
                        },
                        label = { Text("One Time Password") },
                        placeholder = { Text("6-digit code") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    CompactAuthButton("VERIFY IDENTITY", loading = loading, icon = Icons.Default.CheckCircle) {
                        verificationId?.let { signInWithCredential(PhoneAuthProvider.getCredential(it, otp)) }
                    }
                }

                error?.let {
                    Spacer(Modifier.height(6.dp))
                    Text(it, color = Color(0xFFDC2626), fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = Modifier.fillMaxWidth())
                }
                successMessage?.let {
                    Spacer(Modifier.height(6.dp))
                    Text(it, color = DeepGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = Modifier.fillMaxWidth())
                }

                Spacer(Modifier.height(11.dp))
                AuthDivider()
                Spacer(Modifier.height(11.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it.trim()
                        error = null
                        successMessage = null
                    },
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Default.Mail, null, tint = Muted) },
                    placeholder = { Text("student@example.com") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                )
                Spacer(Modifier.height(9.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        error = null
                        successMessage = null
                    },
                    label = { Text("Password") },
                    placeholder = { Text("Minimum 6 characters") },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = Muted
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                )
                Spacer(Modifier.height(8.dp))
                CompactAuthButton(
                    "LOGIN WITH EMAIL",
                    loading = emailAuthAction == "login",
                    enabled = !emailLoading && !loading,
                    icon = Icons.Default.Mail
                ) {
                    loginWithEmail()
                }

                Row(Modifier.fillMaxWidth().height(34.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    if (verificationId != null) {
                        TextButton(onClick = { sendOtp() }, enabled = !loading, modifier = Modifier.height(34.dp)) {
                            Text("RESEND OTP", color = FreshGreen, fontSize = 10.sp, fontWeight = FontWeight.Black)
                        }
                    } else {
                        TextButton(onClick = { createEmailAccount() }, enabled = !emailLoading && !loading, modifier = Modifier.height(34.dp)) {
                            Text(if (emailAuthAction == "signup") "CREATING..." else "CREATE ACCOUNT", color = FreshGreen, fontSize = 10.sp, fontWeight = FontWeight.Black)
                        }
                    }
                    TextButton(onClick = { sendPasswordReset() }, enabled = !emailLoading && !loading, modifier = Modifier.height(34.dp)) {
                        Text(if (emailAuthAction == "reset") "SENDING..." else "FORGOT PASSWORD?", color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Black)
                    }
                }

                Text("MindMatrix Internship - VTU 2026", color = Color(0xFFD1D5DB), fontSize = 7.sp, fontWeight = FontWeight.Black, letterSpacing = 1.4.sp)
            }
        }
    }
}

@Composable
private fun AuthDivider() {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.height(1.dp).weight(1f).background(Color(0xFFA7F3D0)))
        Box(
            Modifier
                .padding(horizontal = 10.dp)
                .clip(RoundedCornerShape(99.dp))
                .background(Color(0xFFECFDF5))
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text("OR", color = DeepGreen, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
        }
        Box(Modifier.height(1.dp).weight(1f).background(Color(0xFFA7F3D0)))
    }
}

@Composable
private fun CompactAuthButton(text: String, loading: Boolean, icon: ImageVector, enabled: Boolean = true, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled && !loading,
        modifier = Modifier.fillMaxWidth().height(44.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(containerColor = DeepGreen)
    ) {
        if (loading) {
            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
        } else {
            Icon(icon, null, tint = Color.White, modifier = Modifier.size(16.dp))
        }
        Spacer(Modifier.width(8.dp))
        Text(text, fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardScreen(user: FirebaseUser, onSignOut: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = remember { FirebaseFirestore.getInstance(FirebaseApp.getInstance(), "ai-studio-ff2ffe61-7475-46c6-a311-1c42c9ca5354") }
    var activeTab by remember { mutableStateOf("home") }
    var routes by remember { mutableStateOf(fallbackRoutes) }
    var selectedRouteId by remember { mutableStateOf(fallbackRoutes.first().id) }
    var reports by remember { mutableStateOf<List<Report>>(emptyList()) }
    var isReporting by remember { mutableStateOf(false) }
    var liveLocation by remember { mutableStateOf<BusLocation?>(null) }
    var isTracking by remember { mutableStateOf(false) }
    var profile by remember(user.uid) { mutableStateOf(loadUserProfile(context, user)) }
    val announcements = remember { mutableStateListOf(*seedAnnouncements().toTypedArray()) }

    val locationLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { }

    DisposableEffect(Unit) {
        val routeListener = db.collection("routes").addSnapshotListener { snap, _ ->
            val fetched = snap?.documents?.mapNotNull { doc ->
                val contacts = (doc.get("sharedAutoContacts") as? List<*>)?.mapNotNull { item ->
                    (item as? Map<*, *>)?.let { SharedAutoContact(it["name"].toString(), it["phone"].toString()) }
                }.orEmpty()
                Route(
                    id = doc.id,
                    busNumber = doc.getString("busNumber").orEmpty(),
                    origin = doc.getString("origin").orEmpty(),
                    destination = doc.getString("destination").orEmpty(),
                    sharedAutoContacts = contacts,
                    crowdStatus = doc.getString("crowdStatus") ?: "empty",
                    etaMinutes = doc.getLong("etaMinutes")?.toInt() ?: 8,
                    seatsLeft = doc.getLong("seatsLeft")?.toInt() ?: 12
                )
            }.orEmpty()
            routes = mergeCampusRoutes(fetched)
        }
        onDispose { routeListener.remove() }
    }

    DisposableEffect(selectedRouteId) {
        val reportListener = db.collection("reports")
            .whereEqualTo("routeId", selectedRouteId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(20)
            .addSnapshotListener { snap, _ ->
                val expiry = Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(REPORT_EXPIRY_MINUTES))
                reports = snap?.documents?.mapNotNull { doc ->
                    val ts = doc.getTimestamp("createdAt") ?: Timestamp.now()
                    if (ts.toDate().before(expiry)) null else Report(
                        id = doc.id,
                        routeId = doc.getString("routeId").orEmpty(),
                        status = doc.getString("status") ?: "empty",
                        reporterId = doc.getString("reporterId").orEmpty(),
                        createdAt = ts.toDate(),
                        lat = doc.getDouble("location.lat"),
                        lng = doc.getDouble("location.lng")
                    )
                }.orEmpty()
            }
        onDispose { reportListener.remove() }
    }

    DisposableEffect(Unit) {
        val ref = FirebaseDatabase.getInstance().getReference("busLocation")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                liveLocation = snapshot.getValue(BusLocation::class.java)
            }
            override fun onCancelled(error: DatabaseError) = Unit
        }
        ref.addValueEventListener(listener)
        onDispose { ref.removeEventListener(listener) }
    }

    fun report(status: String) {
        scope.launch {
            isReporting = true
            try {
                val payload = mutableMapOf<String, Any>(
                    "routeId" to selectedRouteId,
                    "status" to status,
                    "reporterId" to user.uid,
                    "createdAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
                )
                liveLocation?.let {
                    payload["location"] = mapOf("lat" to it.latitude, "lng" to it.longitude)
                }
                db.collection("reports").add(payload).await()
                Toast.makeText(context, "Alert submitted successfully!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Submission failed. Try again later.", Toast.LENGTH_LONG).show()
            } finally {
                isReporting = false
            }
        }
    }

    fun startTracking() {
        val permissions = buildList {
            add(Manifest.permission.ACCESS_FINE_LOCATION)
            add(Manifest.permission.ACCESS_COARSE_LOCATION)
            if (Build.VERSION.SDK_INT >= 33) add(Manifest.permission.POST_NOTIFICATIONS)
        }.toTypedArray()
        val missing = permissions.any { ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED }
        if (missing) {
            locationLauncher.launch(permissions)
            return
        }

        val intent = Intent(context, LocationTrackingService::class.java)
        ContextCompat.startForegroundService(context, intent)
        isTracking = true
    }

    val currentRoute = routes.firstOrNull { it.id == selectedRouteId } ?: routes.first()
    val currentStatus = reports.firstOrNull()?.status ?: currentRoute.crowdStatus

    Scaffold(
        containerColor = AppBg,
        topBar = {
            Header(profile = profile, unread = announcements.any { !it.isRead }, onProfile = { activeTab = "settings" }, onAnnouncements = { activeTab = "announcements" })
        },
        bottomBar = {
            BottomNav(activeTab = activeTab, onTab = { activeTab = it })
        }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when (activeTab) {
                "home" -> HomeTab(routes, selectedRouteId, { selectedRouteId = it }, currentRoute, currentStatus, reports, isReporting, ::report, announcements.firstOrNull())
                "crowd" -> CrowdTab(reports, isReporting, ::report)
                "alternatives" -> AlternativesTab(currentRoute, liveLocation, isTracking, ::startTracking) {
                    context.stopService(Intent(context, LocationTrackingService::class.java))
                    isTracking = false
                }
                "settings" -> SettingsTab(user, profile, { profile = it }, isTracking, ::startTracking, onSignOut)
                "announcements" -> AnnouncementsTab(announcements)
            }
        }
    }
}

@Composable
private fun Header(profile: UserProfile, unread: Boolean, onProfile: () -> Unit, onAnnouncements: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().background(Color.White).statusBarsPadding().padding(horizontal = 22.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable(onClick = onProfile)) {
            Avatar(profile.name, photoUri = profile.photoUri)
            Spacer(Modifier.width(14.dp))
            Column {
                Text("Vidyarthi", fontSize = 18.sp, fontWeight = FontWeight.Black, color = DeepGreen)
                Text("BUS TRANSPORT", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Muted, letterSpacing = 1.6.sp)
            }
        }
        Box {
            IconButton(onClick = onAnnouncements, modifier = Modifier.clip(RoundedCornerShape(18.dp)).background(Color(0xFFF9FAFB))) {
                Icon(Icons.Default.Notifications, contentDescription = "Announcements", tint = Muted)
            }
            if (unread) Box(Modifier.align(Alignment.TopEnd).padding(13.dp).size(9.dp).clip(CircleShape).background(Color(0xFFEF4444)))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTab(
    routes: List<Route>,
    selectedRouteId: String,
    onRoute: (String) -> Unit,
    currentRoute: Route,
    currentStatus: String?,
    reports: List<Report>,
    isReporting: Boolean,
    onReport: (String) -> Unit,
    announcement: Announcement?
) {
    val fromLocations = campusSources
    var selectedFrom by remember { mutableStateOf(currentRoute.origin.takeIf { it.isNotBlank() } ?: fromLocations.firstOrNull().orEmpty()) }
    val destinations = campusDestinations
    var selectedTo by remember { mutableStateOf(currentRoute.destination.takeIf { it.isNotBlank() } ?: destinations.firstOrNull().orEmpty()) }
    val matchingRoutes = routes.filter { route ->
        route.origin.matchesLocation(selectedFrom) && route.destination.matchesLocation(selectedTo)
    }.sortedWith(compareBy<Route> { it.etaMinutes }.thenByDescending { it.seatsLeft })

    LaunchedEffect(currentRoute.id) {
        if (currentRoute.origin.isNotBlank()) selectedFrom = currentRoute.origin
        if (currentRoute.destination.isNotBlank()) selectedTo = currentRoute.destination
    }

    LaunchedEffect(selectedFrom, routes) {
        if (selectedFrom !in campusSources) {
            selectedFrom = campusSources.first()
        }
        if (selectedTo !in campusDestinations) {
            selectedTo = campusDestinations.first()
        }
    }

    LaunchedEffect(selectedFrom, selectedTo, routes) {
        val firstMatch = matchingRoutes.firstOrNull()
        if (firstMatch != null && matchingRoutes.none { it.id == selectedRouteId }) {
            onRoute(firstMatch.id)
        }
    }

    LazyColumn(Modifier.fillMaxSize().padding(22.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        item {
            Card(
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                modifier = Modifier.fillMaxWidth().animateContentSize()
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text("PLAN YOUR RIDE", color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                    Spacer(Modifier.height(12.dp))
                    TransportDropdown(
                        label = "From Location",
                        value = selectedFrom,
                        options = fromLocations,
                        onValue = { selectedFrom = it }
                    )
                    Spacer(Modifier.height(12.dp))
                    TransportDropdown(
                        label = "Destination",
                        value = selectedTo,
                        options = destinations,
                        onValue = { selectedTo = it }
                    )
                    Spacer(Modifier.height(18.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Matching buses", color = TextDark, fontSize = 18.sp, fontWeight = FontWeight.Black)
                        Text("${matchingRoutes.size} MATCHING BUSES FOUND", color = FreshGreen, fontSize = 10.sp, fontWeight = FontWeight.Black)
                    }
                    Spacer(Modifier.height(10.dp))
                    if (matchingRoutes.isEmpty()) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(22.dp))
                                .background(Color(0xFFF8FAFC))
                                .padding(18.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Select a destination to view matching buses.", color = Muted, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        matchingRoutes.forEach { route ->
                            MatchingRouteRow(route, selected = route.id == selectedRouteId, onClick = { onRoute(route.id) })
                        }
                    }
                }
            }
        }
        item { CrowdStatusCard(currentStatus, reports.size) }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.fillMaxWidth()) {
                ReportQuickButton("Verify Seat", "I'm On Bus", Icons.Default.CheckCircle, FreshGreen, Modifier.weight(1f)) { onReport("empty") }
                ReportQuickButton("Alert Students", "No More Seats", Icons.Default.Warning, Color(0xFFEF4444), Modifier.weight(1f)) { onReport("full") }
            }
        }
        item {
            AppCard {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Announcements", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    Text("VIEW ALL", fontSize = 10.sp, fontWeight = FontWeight.Black, color = FreshGreen)
                }
                Spacer(Modifier.height(14.dp))
                announcement?.let { AnnouncementPreview(it) }
            }
        }
        item { Spacer(Modifier.height(70.dp)) }
    }
}

@Composable
private fun CrowdStatusCard(status: String?, reportsCount: Int) {
    val capacity = when (status) {
        "full" -> 1f
        "seated" -> .65f
        else -> .2f
    }
    val animated by animateFloatAsState(capacity, label = "capacity")
    Card(shape = RoundedCornerShape(32.dp), colors = CardDefaults.cardColors(DeepGreen), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(24.dp)) {
            Text("REAL-TIME CROWD STATUS", color = Color(0xFF86EFAC), fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 1.5.sp)
            Spacer(Modifier.height(8.dp))
            Text(crowdLabel(status), color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
            Spacer(Modifier.height(22.dp))
            Box(Modifier.fillMaxWidth().height(16.dp).clip(RoundedCornerShape(99.dp)).background(Color.White.copy(alpha = .12f))) {
                Box(
                    Modifier.fillMaxWidth(animated).height(16.dp).clip(RoundedCornerShape(99.dp)).background(
                        crowdColor(status)
                    )
                )
            }
            Spacer(Modifier.height(22.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("+${if (reportsCount > 5) reportsCount else 12}", color = Color.White, fontWeight = FontWeight.Black)
                Column(horizontalAlignment = Alignment.End) {
                    Text("CONFIDENCE", color = Color(0xFF86EFAC), fontSize = 9.sp, fontWeight = FontWeight.Black)
                    Text("COMMUNITY VERIFIED", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
private fun CrowdTab(reports: List<Report>, isReporting: Boolean, onReport: (String) -> Unit) {
    var now by remember { mutableStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(5000)
            now = System.currentTimeMillis()
        }
    }

    LazyColumn(Modifier.fillMaxSize().padding(22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Crowd Feed", fontSize = 26.sp, fontWeight = FontWeight.Black, color = TextDark)
                    Text("${reports.size} ROUTE-SPECIFIC LIVE REPORTS", color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                }
                Row(
                    Modifier.clip(RoundedCornerShape(99.dp)).background(Color(0xFFECFDF5)).padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.size(7.dp).clip(CircleShape).background(FreshGreen))
                    Spacer(Modifier.width(6.dp))
                    Text("LIVE", color = DeepGreen, fontSize = 10.sp, fontWeight = FontWeight.Black)
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                listOf(
                    Triple("empty", crowdLabel("empty"), crowdColor("empty")),
                    Triple("seated", crowdLabel("seated"), crowdColor("seated")),
                    Triple("full", crowdLabel("full"), crowdColor("full"))
                ).forEach { (status, label, color) ->
                    CrowdChip(label, color, enabled = !isReporting, modifier = Modifier.weight(1f)) { onReport(status) }
                }
            }
        }
        if (reports.isEmpty()) {
            item { EmptyState("No recent reports yet", "Be the first student to verify this route.") }
        } else {
            items(reports, key = { it.id }) { report -> ReportCard(report, now) }
        }
        item { Spacer(Modifier.height(70.dp)) }
    }
}

@Composable
private fun AlternativesTab(route: Route, location: BusLocation?, isTracking: Boolean, onStart: () -> Unit, onStop: () -> Unit) {
    val primaryContact = route.sharedAutoContacts.firstOrNull()
    val driverName = primaryContact?.name ?: "Campus Auto Partner"
    val driverPhone = primaryContact?.phone ?: "Transport Desk"
    val seatsLeft = route.seatsLeft.toString()
    val eta = "${route.etaMinutes} min"
    val alternateOptions = remember(route.id) { buildAlternatives(route) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBg),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text("Alternatives", fontSize = 30.sp, fontWeight = FontWeight.Black, color = TextDark)
                Text("FASTEST VERIFIED OPTIONS", color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
            }
        }
        item {
            Card(
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(22.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Box(
                            Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE7F8EF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.DirectionsBus, null, tint = DeepGreen, modifier = Modifier.size(34.dp))
                        }
                        Spacer(Modifier.width(16.dp))
                        Column(Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("AUTO DRIVER", color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                                Spacer(Modifier.width(8.dp))
                                VerifiedBadge()
                            }
                            Spacer(Modifier.height(5.dp))
                            Text(driverName, color = TextDark, fontSize = 20.sp, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text(driverPhone, color = Color(0xFF4B5563), fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text("${route.busNumber} - ${crowdLabel(route.crowdStatus)}", color = crowdColor(route.crowdStatus), fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = 0.8.sp)
                        }
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .clickable { }
                                .background(DeepGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = "Call driver", tint = Color.White, modifier = Modifier.size(24.dp))
                        }
                    }

                    Spacer(Modifier.height(22.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        TransportMetric("ETA", eta, Modifier.weight(1f))
                        TransportMetric("Seats left", seatsLeft, Modifier.weight(1f))
                    }

                    Spacer(Modifier.height(18.dp))

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(22.dp))
                            .background(Color(0xFFF1F8F4))
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.LocationOn, null, tint = DeepGreen, modifier = Modifier.size(22.dp))
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text("${route.origin} to ${route.destination}", color = TextDark, fontSize = 13.sp, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text("${crowdLabel(route.crowdStatus)} - ETA $eta", color = Muted, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.6.sp)
                        }
                    }
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                if (isTracking) {
                    OutlinedButton(onClick = onStop, modifier = Modifier.weight(1f).height(58.dp), shape = RoundedCornerShape(22.dp)) {
                        Text("STOP TRACKING", color = Color(0xFFDC2626), fontSize = 11.sp, fontWeight = FontWeight.Black)
                    }
                } else {
                    Button(
                        onClick = onStart,
                        modifier = Modifier.weight(1f).height(58.dp),
                        shape = RoundedCornerShape(22.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepGreen)
                    ) {
                        Icon(Icons.Default.LocationOn, null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("START TRACKING", fontSize = 11.sp, fontWeight = FontWeight.Black)
                    }
                }
                Card(
                    modifier = Modifier.weight(1f).height(58.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Row(Modifier.fillMaxSize().padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, null, tint = FreshGreen, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("VERIFIED ROUTE", color = TextDark, fontSize = 11.sp, fontWeight = FontWeight.Black)
                    }
                }
            }
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Other options", color = TextDark, fontSize = 18.sp, fontWeight = FontWeight.Black)
                Text("${alternateOptions.size} LIVE", color = FreshGreen, fontSize = 10.sp, fontWeight = FontWeight.Black)
            }
        }
        items(alternateOptions, key = { "${it.service}-${it.type}-${it.route}" }) { option ->
            TransportOptionCard(option)
        }
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text("Alternate Contacts", color = TextDark, fontSize = 16.sp, fontWeight = FontWeight.Black)
                    Spacer(Modifier.height(8.dp))
                    if (route.sharedAutoContacts.isEmpty()) {
                        Text("No shared auto contacts for this route.", color = Muted, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    } else {
                        route.sharedAutoContacts.drop(1).ifEmpty { route.sharedAutoContacts }.forEach {
                            InfoLine(it.name, it.phone)
                        }
                    }
                }
            }
        }
        item { Spacer(Modifier.height(70.dp)) }
    }
}

@Composable
private fun VerifiedBadge() {
    Row(
        Modifier
            .clip(RoundedCornerShape(99.dp))
            .background(Color(0xFFE7F8EF))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.CheckCircle, null, tint = FreshGreen, modifier = Modifier.size(12.dp))
        Spacer(Modifier.width(4.dp))
        Text("VERIFIED", color = DeepGreen, fontSize = 8.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun TransportMetric(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier
            .clip(RoundedCornerShape(22.dp))
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Text(label.uppercase(), color = Muted, fontSize = 9.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
        Spacer(Modifier.height(5.dp))
        Text(value, color = TextDark, fontSize = 22.sp, fontWeight = FontWeight.Black)
    }
}

private fun crowdLabel(status: String?): String = when (status) {
    "full" -> "FULL"
    "seated" -> "SEATED"
    else -> "EMPTY"
}

private fun crowdColor(status: String?): Color = when (status) {
    "full" -> Color(0xFFEF4444)
    "seated" -> Color(0xFFF59E0B)
    else -> FreshGreen
}

private fun buildAlternatives(route: Route): List<TransportAlternative> {
    val base = route.etaMinutes
    val seats = route.seatsLeft.coerceAtLeast(1)
    val routeLabel = "${route.origin} -> ${route.destination}"
    return listOf(
        TransportAlternative("College Shuttle", "Campus shuttle", (base - 4).coerceAtLeast(4), (seats / 2 + 1).coerceIn(3, 14), "FASTEST", routeLabel, "Free", Color(0xFFF59E0B), Icons.Default.CheckCircle),
        TransportAlternative("Shared Auto", "Shared auto", (base - 2).coerceAtLeast(5), (seats / 3 + 1).coerceIn(2, 8), "AVAILABLE", routeLabel, "Rs 35", Color(0xFF10B981), Icons.Default.Phone),
        TransportAlternative("Metro Feeder", "Metro feeder", base + 3, (seats / 2 + 5).coerceIn(5, 18), "ON TIME", "${route.origin} -> Metro -> ${route.destination}", "Rs 45", Color(0xFF7C3AED), Icons.Default.LocationOn),
        TransportAlternative("KSRTC Express", "Intercity bus", base + 5, (seats + 9).coerceIn(10, 32), "LIMITED", routeLabel, "Rs 55", Color(0xFF2563EB), Icons.Default.DirectionsBus),
        TransportAlternative("Campus Cab", "Verified cab", base + 8, (seats / 2 + 2).coerceIn(3, 12), "PREMIUM", routeLabel, "Rs 120", Color(0xFF065F46), Icons.Default.People),
        TransportAlternative("Mini Van", "Student van", base + 12, (seats / 4 + 2).coerceIn(2, 10), "FILLING", "${route.origin} pickup -> ${route.destination}", "Rs 80", Color(0xFF0F766E), Icons.Default.DirectionsBus)
    ).sortedBy { it.etaMinutes }
}

@Composable
private fun TransportOptionCard(option: TransportAlternative) {
    Card(
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(option.color.copy(alpha = .12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(option.icon, null, tint = option.color, modifier = Modifier.size(25.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(option.service, color = TextDark, fontSize = 16.sp, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                    Spacer(Modifier.width(10.dp))
                    LiveBadge(option.status, option.color)
                }
                Spacer(Modifier.height(5.dp))
                Text(option.route, color = Color(0xFF4B5563), fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    TransportPill("ETA ${option.etaMinutes} min", option.color, Modifier.weight(1f))
                    TransportPill("${option.seats} seats", option.color, Modifier.weight(1f))
                    TransportPill(option.price, option.color, Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun TransportPill(text: String, color: Color, modifier: Modifier = Modifier) {
    Text(
        text,
        color = color,
        fontSize = 9.sp,
        fontWeight = FontWeight.Black,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .clip(RoundedCornerShape(99.dp))
            .background(color.copy(alpha = .09f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Composable
private fun LiveBadge(label: String, color: Color) {
    Text(
        label,
        color = color,
        fontSize = 8.sp,
        fontWeight = FontWeight.Black,
        maxLines = 1,
        modifier = Modifier
            .clip(RoundedCornerShape(99.dp))
            .background(color.copy(alpha = .12f))
            .padding(horizontal = 7.dp, vertical = 4.dp)
    )
}

@Composable
private fun SettingsTab(
    user: FirebaseUser,
    profile: UserProfile,
    onProfileChanged: (UserProfile) -> Unit,
    isTracking: Boolean,
    onGps: () -> Unit,
    onSignOut: () -> Unit
) {
    var editing by remember { mutableStateOf(false) }
    if (editing) {
        EditProfileScreen(
            user = user,
            profile = profile,
            onBack = { editing = false },
            onSaved = {
                onProfileChanged(it)
                editing = false
            }
        )
        return
    }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(12.dp))
        Avatar(profile.name, size = 112, photoUri = profile.photoUri)
        Spacer(Modifier.height(18.dp))
        Text(profile.name, fontSize = 24.sp, fontWeight = FontWeight.Black, color = TextDark, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(profile.phone.ifBlank { user.email ?: "Verified Student" }, fontSize = 11.sp, color = Muted, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Spacer(Modifier.height(28.dp))
        AppCard {
            InfoLine("Profile", "Local + Firebase")
            Spacer(Modifier.height(12.dp))
            InfoLine("GPS Permissions", if (isTracking) "Location Access Granted" else "Request Access")
            Spacer(Modifier.height(12.dp))
            PrimaryButton("EDIT PROFILE", false, Icons.Default.Settings) {
                editing = true
            }
        }
        Spacer(Modifier.height(14.dp))
        OutlinedButton(onClick = onGps, modifier = Modifier.fillMaxWidth().height(58.dp), shape = RoundedCornerShape(24.dp)) {
            Icon(Icons.Default.LocationOn, null, tint = DeepGreen)
            Spacer(Modifier.width(8.dp))
            Text("GPS Permissions", color = TextDark, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(12.dp))
        OutlinedButton(onClick = onSignOut, modifier = Modifier.fillMaxWidth().height(58.dp), shape = RoundedCornerShape(24.dp), border = BorderStroke(1.dp, Color(0xFFFEE2E2))) {
            Icon(Icons.Default.Logout, null, tint = Color(0xFFDC2626))
            Spacer(Modifier.width(8.dp))
            Text("Sign Out Account", color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun EditProfileScreen(
    user: FirebaseUser,
    profile: UserProfile,
    onBack: () -> Unit,
    onSaved: (UserProfile) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var username by remember(profile.name) { mutableStateOf(profile.name) }
    var phone by remember(profile.phone) { mutableStateOf(profile.phone) }
    var photoUri by remember(profile.photoUri) { mutableStateOf(profile.photoUri) }
    var saving by remember { mutableStateOf(false) }

    val photoPicker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri ?: return@rememberLauncherForActivityResult
        val localUri = copyProfileImageToLocal(context, user.uid, uri)
        if (localUri == null) {
            Toast.makeText(context, "Could not load selected image.", Toast.LENGTH_SHORT).show()
        } else {
            photoUri = localUri
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(AppBg).padding(22.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack, modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(Color.White)) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = DeepGreen)
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Edit Profile", fontSize = 26.sp, fontWeight = FontWeight.Black, color = TextDark)
                    Text("PROFILE PHOTO AND CONTACT", color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                }
            }
        }
        item {
            AppCard {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Avatar(username, size = 118, photoUri = photoUri)
                    Spacer(Modifier.height(14.dp))
                    OutlinedButton(
                        onClick = { photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        shape = RoundedCornerShape(22.dp)
                    ) {
                        Icon(Icons.Default.Refresh, null, tint = DeepGreen)
                        Spacer(Modifier.width(8.dp))
                        Text("Choose Profile Photo", color = TextDark, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        item {
            AppCard {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    singleLine = true,
                    shape = RoundedCornerShape(22.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it.filter { ch -> ch.isDigit() || ch == '+' || ch == ' ' || ch == '-' }.take(18) },
                    label = { Text("Phone number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    shape = RoundedCornerShape(22.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        item {
            PrimaryButton("SAVE PROFILE", saving, Icons.Default.CheckCircle) {
                val trimmedName = username.trim().ifBlank { "Bus Student" }
                val trimmedPhone = phone.trim()
                val updated = UserProfile(trimmedName, trimmedPhone, photoUri)
                scope.launch {
                    saving = true
                    try {
                        saveUserProfile(context, user.uid, updated)
                        user.updateProfile(userProfileChangeRequest { displayName = trimmedName }).await()
                        onSaved(updated)
                        Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        saveUserProfile(context, user.uid, updated)
                        onSaved(updated)
                        Toast.makeText(context, "Saved locally. Firebase profile will sync later.", Toast.LENGTH_LONG).show()
                    } finally {
                        saving = false
                    }
                }
            }
        }
        item { Spacer(Modifier.height(70.dp)) }
    }
}

@Composable
private fun AnnouncementsTab(announcements: MutableList<Announcement>) {
    var query by remember { mutableStateOf("") }
    val filtered = announcements.filter {
        it.title.contains(query, true) || it.description.contains(query, true) || it.category.contains(query, true)
    }
    LazyColumn(Modifier.fillMaxSize().padding(22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Text("Announcements", fontSize = 28.sp, fontWeight = FontWeight.Black, color = TextDark)
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = Muted) },
                placeholder = { Text("Search updates") },
                singleLine = true,
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }
        items(filtered) { announcement ->
            AnnouncementCard(announcement) { announcement.isRead = true }
        }
        item { Spacer(Modifier.height(70.dp)) }
    }
}

@Composable
private fun BottomNav(activeTab: String, onTab: (String) -> Unit) {
    Row(
        Modifier.fillMaxWidth().background(Color.White).navigationBarsPadding().padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavItem("home", "Home", Icons.Default.Home, activeTab, onTab)
        NavItem("crowd", "Crowd", Icons.Default.People, activeTab, onTab)
        NavItem("alternatives", "Alt", Icons.Default.DirectionsBus, activeTab, onTab)
        NavItem("settings", "Config", Icons.Default.Settings, activeTab, onTab)
    }
}

@Composable
private fun NavItem(id: String, label: String, icon: ImageVector, activeTab: String, onTab: (String) -> Unit) {
    val active = id == activeTab
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onTab(id) }.padding(8.dp)) {
        Box(Modifier.size(42.dp).clip(RoundedCornerShape(16.dp)).background(if (active) Color(0xFFECFDF5) else Color.Transparent), contentAlignment = Alignment.Center) {
            Icon(icon, label, tint = if (active) FreshGreen else Muted)
        }
        Text(label.uppercase(), color = if (active) FreshGreen else Muted, fontSize = 9.sp, fontWeight = FontWeight.Black)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransportDropdown(label: String, value: String, options: List<String>, onValue: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = RoundedCornerShape(22.dp),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .exposedDropdownSize()
                .heightIn(max = 320.dp)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            option,
                            color = if (option == value) DeepGreen else TextDark,
                            fontWeight = if (option == value) FontWeight.Black else FontWeight.Bold
                        )
                    },
                    onClick = {
                        onValue(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun AppCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(20.dp)) {
            content()
        }
    }
}

@Composable
private fun MatchingRouteRow(route: Route, selected: Boolean, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .clickable(onClick = onClick)
            .background(if (selected) Color(0xFFECFDF5) else Color(0xFFF8FAFC))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(17.dp))
                .background(if (selected) DeepGreen else Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.DirectionsBus, null, tint = if (selected) Color.White else DeepGreen)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(route.busNumber, color = TextDark, fontSize = 16.sp, fontWeight = FontWeight.Black)
            Text("${route.origin} -> ${route.destination}", color = Muted, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(crowdLabel(route.crowdStatus), color = crowdColor(route.crowdStatus), fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 0.8.sp)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(if (selected) "ACTIVE" else "SELECT", color = if (selected) FreshGreen else Muted, fontSize = 9.sp, fontWeight = FontWeight.Black)
            Text("ETA ${route.etaMinutes} min", color = TextDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text("${route.seatsLeft} seats", color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun CrowdChip(label: String, color: Color, enabled: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.height(48.dp).clickable(enabled = enabled, onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(color.copy(alpha = .10f)),
        border = BorderStroke(1.dp, color.copy(alpha = .22f))
    ) {
        Row(Modifier.fillMaxSize().padding(horizontal = 10.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(8.dp).clip(CircleShape).background(color))
            Spacer(Modifier.width(8.dp))
            Text(label.uppercase(), color = color, fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp, maxLines = 1)
        }
    }
}

@Composable
private fun RouteRow(route: Route, selected: Boolean, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).clickable(onClick = onClick).background(if (selected) Color(0xFFECFDF5) else Color.Transparent).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.DirectionsBus, null, tint = DeepGreen)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text("${route.busNumber} - ${route.origin}", fontWeight = FontWeight.Black, color = TextDark)
            Text(route.destination, color = Muted, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        if (selected) Icon(Icons.Default.CheckCircle, null, tint = FreshGreen)
    }
}

@Composable
private fun ReportQuickButton(label: String, value: String, icon: ImageVector, color: Color, modifier: Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(Color.White),
        border = BorderStroke(1.dp, color.copy(alpha = .12f))
    ) {
        Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.size(54.dp).clip(RoundedCornerShape(18.dp)).background(color.copy(alpha = .10f)), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = color, modifier = Modifier.size(28.dp))
            }
            Spacer(Modifier.height(10.dp))
            Text(label.uppercase(), color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Black, maxLines = 1)
            Text(value, color = TextDark, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun AnnouncementPreview(a: Announcement) {
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(AppBg).padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(if (a.category == "Emergency") Icons.Default.Warning else Icons.Default.Announcement, null, tint = if (a.category == "Emergency") Color(0xFFDC2626) else FreshGreen)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(a.title, fontWeight = FontWeight.Bold, color = TextDark, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(a.description, color = Muted, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        if (!a.isRead) Box(Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFEF4444)))
    }
}

@Composable
private fun ReportCard(report: Report, now: Long) {
    val color = crowdColor(report.status)
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = BorderStroke(1.dp, color.copy(alpha = .12f)),
        modifier = Modifier.fillMaxWidth().animateContentSize()
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(46.dp).clip(RoundedCornerShape(16.dp)).background(color.copy(alpha = .12f)), contentAlignment = Alignment.Center) {
                Icon(if (report.status == "full") Icons.Default.Warning else Icons.Default.CheckCircle, null, tint = color)
            }
            Spacer(Modifier.width(13.dp))
            Column(Modifier.weight(1f)) {
                Text(crowdLabel(report.status), fontWeight = FontWeight.Black, color = TextDark, fontSize = 15.sp, letterSpacing = 0.8.sp)
                Text("${relativeTime(report.createdAt, now)} - Route verified", color = Muted, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Text(
                crowdLabel(report.status),
                color = color,
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.8.sp,
                modifier = Modifier.clip(RoundedCornerShape(99.dp)).background(color.copy(alpha = .10f)).padding(horizontal = 10.dp, vertical = 6.dp)
            )
        }
    }
}

private fun relativeTime(date: Date, now: Long): String {
    val diffMinutes = ((now - date.time).coerceAtLeast(0L) / TimeUnit.MINUTES.toMillis(1)).toInt()
    return when {
        diffMinutes <= 0 -> "Just now"
        diffMinutes == 1 -> "1 min ago"
        diffMinutes < 60 -> "$diffMinutes mins ago"
        else -> {
            val hours = diffMinutes / 60
            if (hours == 1) "1 hour ago" else "$hours hours ago"
        }
    }
}

private fun String.normalizedLocation(): String = trim().lowercase().replace(Regex("\\s+"), " ")

private fun String.slug(): String = normalizedLocation().replace(Regex("[^a-z0-9]+"), "-").trim('-')

private fun String.matchesLocation(other: String): Boolean = normalizedLocation() == other.normalizedLocation()

private fun loadUserProfile(context: Context, user: FirebaseUser): UserProfile {
    val prefs = context.getSharedPreferences("vidyarthi_profile_${user.uid}", Context.MODE_PRIVATE)
    return UserProfile(
        name = prefs.getString("name", null) ?: user.displayName ?: "Bus Student",
        phone = prefs.getString("phone", null) ?: user.phoneNumber.orEmpty(),
        photoUri = prefs.getString("photoUri", null)
    )
}

private fun saveUserProfile(context: Context, uid: String, profile: UserProfile) {
    context.getSharedPreferences("vidyarthi_profile_$uid", Context.MODE_PRIVATE)
        .edit()
        .putString("name", profile.name)
        .putString("phone", profile.phone)
        .putString("photoUri", profile.photoUri)
        .apply()
}

private fun copyProfileImageToLocal(context: Context, uid: String, source: Uri): String? {
    return try {
        val fileName = "profile_avatar_$uid.jpg"
        context.contentResolver.openInputStream(source)?.use { input ->
            context.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
                input.copyTo(output)
            }
        } ?: return null
        Uri.fromFile(File(context.filesDir, fileName)).toString()
    } catch (e: Exception) {
        null
    }
}

@Composable
private fun AnnouncementCard(a: Announcement, onRead: () -> Unit) {
    AppCard {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(a.category.uppercase(), color = if (a.category == "Emergency") Color(0xFFDC2626) else FreshGreen, fontSize = 10.sp, fontWeight = FontWeight.Black)
            if (!a.isRead) Box(Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFEF4444)))
        }
        Spacer(Modifier.height(8.dp))
        Text(a.title, fontSize = 17.sp, fontWeight = FontWeight.Black, color = TextDark)
        Text(a.description, color = Color(0xFF4B5563), fontSize = 13.sp, lineHeight = 19.sp, modifier = Modifier.padding(top = 8.dp))
        TextButton(onClick = onRead, modifier = Modifier.align(Alignment.End)) {
            Text("MARK READ", color = FreshGreen, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    AppCard(modifier = modifier) {
        Text(label.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Black, color = Muted, letterSpacing = 1.sp)
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextDark, modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
private fun InfoLine(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = Color(0xFF4B5563), fontWeight = FontWeight.Bold)
        Text(value, color = TextDark, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun Avatar(name: String?, size: Int = 44, photoUri: String? = null) {
    Box(
        Modifier
            .size(size.dp)
            .clip(if (size > 80) CircleShape else RoundedCornerShape(16.dp))
            .background(DeepGreen),
        contentAlignment = Alignment.Center
    ) {
        if (!photoUri.isNullOrBlank()) {
            AndroidView(
                factory = { context ->
                    ImageView(context).apply {
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }
                },
                update = { imageView ->
                    imageView.setImageURI(Uri.parse(photoUri))
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text((name?.firstOrNull()?.uppercaseChar() ?: 'U').toString(), color = Color.White, fontWeight = FontWeight.Black, fontSize = (size / 3).sp)
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(text.uppercase(), color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp, modifier = Modifier.fillMaxWidth().padding(start = 4.dp, bottom = 8.dp))
}

@Composable
private fun PrimaryButton(text: String, loading: Boolean, icon: ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = !loading,
        modifier = Modifier.fillMaxWidth().height(58.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(containerColor = DeepGreen)
    ) {
        if (loading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp) else Icon(icon, null, tint = Color.White, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(10.dp))
        Text(text, fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 1.5.sp)
    }
}

@Composable
private fun StatusCard(text: String, color: Color, icon: ImageVector) {
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(color.copy(alpha = .10f)).padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = color)
        Spacer(Modifier.width(10.dp))
        Text(text, color = color, fontSize = 11.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun ProgressBar(progress: Float, modifier: Modifier = Modifier) {
    Box(modifier.height(4.dp).clip(RoundedCornerShape(99.dp)).background(Color(0xFFE5E7EB))) {
        Box(Modifier.fillMaxWidth(progress.coerceIn(0f, 1f)).height(4.dp).clip(RoundedCornerShape(99.dp)).background(DeepGreen))
    }
}

@Composable
private fun EmptyState(title: String, subtitle: String) {
    AppCard {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            Icon(Icons.Default.DirectionsBus, null, tint = Muted, modifier = Modifier.size(42.dp))
            Spacer(Modifier.height(8.dp))
            Text(title, color = TextDark, fontWeight = FontWeight.Black)
            Text(subtitle, color = Muted, fontSize = 12.sp)
        }
    }
}

private fun seedAnnouncements() = listOf(
    Announcement("1", "Bus Delay - Route B21", "The bus scheduled for 8:30 AM is delayed by 15 minutes due to heavy traffic at Silk Board junction.", "Bus Delay", Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(30)), false),
    Announcement("2", "Holiday Notice", "The college will remain closed tomorrow on account of State Festival. Bus services will be suspended for the day.", "Holiday", Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(5)), true),
    Announcement("3", "Emergency: Route Change", "Route B21 will take a detour via Nice Road today. Stops at Electronic City Phase 1 shift to the main junction.", "Emergency", Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(24)), false),
    Announcement("4", "New Student ID Card", "Please collect your updated smart bus ID cards from the transport office before Friday.", "Important", Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(48)), true),
    Announcement("5", "Weather Alert", "Heavy rainfall is expected in the evening. Drivers have been instructed to maintain safe speeds.", "General", Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(72)), true)
)
