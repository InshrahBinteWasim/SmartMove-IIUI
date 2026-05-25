package com.example.smartmoveiiui.ui.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.smartmoveiiui.R
import com.example.smartmoveiiui.ui.theme.AccentMint
import com.example.smartmoveiiui.ui.theme.CardDark
import com.example.smartmoveiiui.ui.theme.DangerRed
import com.example.smartmoveiiui.ui.theme.GlowGreen
import com.example.smartmoveiiui.ui.theme.PrimaryGreen
import com.example.smartmoveiiui.ui.theme.PrimaryGreenLight
import com.example.smartmoveiiui.ui.theme.TextSecondaryDark
import com.example.smartmoveiiui.ui.theme.TextSecondaryLight
import com.example.smartmoveiiui.ui.theme.WarningAmber
import kotlinx.coroutines.delay

private enum class LoginVisualState { Default, Validation, Locked, Success }
private enum class RecoveryStep { Email, Otp, NewPassword }

@Composable
fun SmartMoveLoginScreen(
    onLogin: (
        email: String,
        password: String,
        onLoading: (Boolean) -> Unit,
        onSuccess: () -> Unit,
        onError: (String, Boolean) -> Unit
    ) -> Unit,
    onGoogleSignIn: () -> Unit,
    onForgotPassword: () -> Unit,
    onDemoSuccess: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var visualState by rememberSaveable { mutableStateOf(LoginVisualState.Default) }
    var loading by rememberSaveable { mutableStateOf(false) }
    var statusText by rememberSaveable { mutableStateOf("System ready. IIUI transport hub online.") }
    var failedAttempts by rememberSaveable { mutableIntStateOf(0) }
    var demoSuccessPending by rememberSaveable { mutableStateOf(false) }

    val emailError = visualState == LoginVisualState.Validation &&
        email.isNotBlank() &&
        !email.endsWith("@iiu.edu.pk")
    val passwordError = visualState == LoginVisualState.Validation &&
        password.isNotBlank() &&
        password.length < 8

    AuthScaffold {
        AuthHero(
            title = "SmartMove IIUI",
            subtitle = "DIRECTORATE OF TRANSPORT",
            visualState = visualState
        )

        PremiumAuthCard {
            StateHeader(
                visualState = visualState,
                title = "Sign in to campus transport",
                subtitle = statusText
            )

            Spacer(Modifier.height(18.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it.trim()
                    if (visualState != LoginVisualState.Locked) visualState = LoginVisualState.Default
                },
                enabled = !loading && visualState != LoginVisualState.Locked,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("IIUI email") },
                placeholder = { Text("name.bsse4518@iiu.edu.pk") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                isError = emailError || (visualState == LoginVisualState.Validation && email.isBlank()),
                supportingText = {
                    if (visualState == LoginVisualState.Validation) {
                        Text(
                            when {
                                email.isBlank() -> "Email is required"
                                emailError -> "Use your official @iiu.edu.pk email"
                                else -> "Verified IIUI domain"
                            }
                        )
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(18.dp),
                colors = smartFieldColors()
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (visualState != LoginVisualState.Locked) visualState = LoginVisualState.Default
                },
                enabled = !loading && visualState != LoginVisualState.Locked,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Password, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = passwordError || (visualState == LoginVisualState.Validation && password.isBlank()),
                supportingText = {
                    if (visualState == LoginVisualState.Validation) {
                        Text(
                            when {
                                password.isBlank() -> "Password is required"
                                passwordError -> "Minimum 8 characters"
                                else -> "Strong enough for demo"
                            }
                        )
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(18.dp),
                colors = smartFieldColors()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HciChip("HCI #5 Error prevention")
                TextButton(onClick = onForgotPassword) {
                    Text("Forgot password?")
                }
            }

            AnimatedVisibility(
                visible = visualState == LoginVisualState.Locked,
                enter = fadeIn() + slideInVertically { it / 2 },
                exit = fadeOut() + slideOutVertically { it / 2 }
            ) {
                WarningPanel(
                    title = "Account temporarily locked",
                    body = "Too many failed attempts. Try reset password or use demo recovery."
                )
            }

            Button(
                onClick = {
                    val validationFailed = email.isBlank() ||
                        password.isBlank() ||
                        !email.endsWith("@iiu.edu.pk") ||
                        password.length < 8
                    if (validationFailed) {
                        visualState = LoginVisualState.Validation
                        statusText = "Check the highlighted fields before signing in."
                        return@Button
                    }
                    if (email.equals("demo@iiu.edu.pk", ignoreCase = true) && password == "SmartMove123") {
                        visualState = LoginVisualState.Success
                        statusText = "Welcome back. Opening commuter workspace."
                        loading = true
                        demoSuccessPending = true
                        return@Button
                    }
                    onLogin(
                        email,
                        password,
                        { loading = it },
                        {
                            visualState = LoginVisualState.Success
                            statusText = "Verified. Opening your workspace."
                            loading = false
                        },
                        { message, lockCandidate ->
                            failedAttempts += if (lockCandidate) 1 else 0
                            visualState = if (failedAttempts >= 2) LoginVisualState.Locked else LoginVisualState.Validation
                            statusText = message
                        }
                    )
                },
                enabled = !loading && visualState != LoginVisualState.Locked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                AnimatedContent(targetState = loading || visualState == LoginVisualState.Success, label = "loginButton") { busy ->
                    if (busy) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(Modifier.width(10.dp))
                            Text("Securing session")
                        }
                    } else {
                        Text("Sign in")
                    }
                }
            }

            LaunchedEffect(demoSuccessPending) {
                if (demoSuccessPending) {
                    delay(900)
                    demoSuccessPending = false
                    onDemoSuccess()
                }
            }

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = onGoogleSignIn,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("Continue with Google")
            }

            TextButton(
                onClick = {
                    failedAttempts = 0
                    visualState = LoginVisualState.Default
                    statusText = "System ready. IIUI transport hub online."
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Demo account: demo@iiu.edu.pk / SmartMove123")
            }
        }
    }
}

@Composable
fun ForgotPasswordFlowScreen(
    onBack: () -> Unit,
    onSendResetEmail: (String, () -> Unit) -> Unit,
    onFinished: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var step by rememberSaveable { mutableStateOf(RecoveryStep.Email) }
    var loading by rememberSaveable { mutableStateOf(false) }
    var error by rememberSaveable { mutableStateOf<String?>(null) }
    val otpDigits = remember { mutableStateListOf("", "", "", "", "", "") }
    var newPassword by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    AuthScaffold {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text("Password recovery", style = MaterialTheme.typography.titleLarge)
        }

        RecoveryStepper(step)

        PremiumAuthCard {
            AnimatedContent(targetState = step, label = "recoveryStep") { currentStep ->
                when (currentStep) {
                    RecoveryStep.Email -> EmailStep(
                        email = email,
                        error = error,
                        loading = loading,
                        onEmailChange = {
                            email = it.trim()
                            error = null
                        },
                        onContinue = {
                            if (!email.endsWith("@iiu.edu.pk")) {
                                error = "Use your official @iiu.edu.pk email"
                            } else {
                                loading = true
                                onSendResetEmail(email) {
                                    loading = false
                                    step = RecoveryStep.Otp
                                }
                            }
                        }
                    )

                    RecoveryStep.Otp -> OtpStep(
                        digits = otpDigits,
                        error = error,
                        onDigitChange = { index, value ->
                            otpDigits[index] = value.takeLast(1).filter(Char::isDigit)
                            error = null
                        },
                        onContinue = {
                            val code = otpDigits.joinToString("")
                            if (code.length != 6) {
                                error = "Enter all 6 digits"
                            } else if (code != "451826") {
                                error = "Demo OTP is 451826"
                            } else {
                                step = RecoveryStep.NewPassword
                                error = null
                            }
                        }
                    )

                    RecoveryStep.NewPassword -> NewPasswordStep(
                        password = newPassword,
                        confirmPassword = confirmPassword,
                        error = error,
                        onPasswordChange = {
                            newPassword = it
                            error = null
                        },
                        onConfirmPasswordChange = {
                            confirmPassword = it
                            error = null
                        },
                        onFinish = {
                            when {
                                newPassword.length < 8 -> error = "Minimum 8 characters required"
                                newPassword != confirmPassword -> error = "Passwords do not match"
                                else -> onFinished()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AuthScaffold(content: @Composable ColumnScope.() -> Unit) {
    val dark = isSystemInDarkTheme()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        if (dark) Color(0xFF071411) else Color(0xFFF7FFFE),
                        if (dark) Color(0xFF0D201C) else Color(0xFFE1F5EE)
                    )
                )
            )
    ) {
        PatternCanvas()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .navigationBarsPadding()
                .padding(horizontal = 22.dp, vertical = 20.dp),
            content = content
        )
    }
}

@Composable
private fun AuthHero(title: String, subtitle: String, visualState: LoginVisualState) {
    val transition = rememberInfiniteTransition(label = "hero")
    val busOffset by transition.animateFloat(
        initialValue = -18f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(tween(1800), RepeatMode.Reverse),
        label = "busOffset"
    )
    val blink by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(650), RepeatMode.Reverse),
        label = "blink"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .clip(RoundedCornerShape(0.dp, 0.dp, 32.dp, 32.dp))
                .background(
                    Brush.verticalGradient(listOf(PrimaryGreen, PrimaryGreenLight))
                )
                .padding(18.dp)
        ) {
            CampusCanvas(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(150.dp),
                busOffset = busOffset
            )
            Image(
                painter = painterResource(R.drawable.iiui_logo),
                contentDescription = "IIUI logo",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(62.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(5.dp)
            )
            StatusPill(
                text = when (visualState) {
                    LoginVisualState.Default -> "Live"
                    LoginVisualState.Validation -> "Review"
                    LoginVisualState.Locked -> "Locked"
                    LoginVisualState.Success -> "Verified"
                },
                alpha = blink,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }

        Spacer(Modifier.height(16.dp))
        Text(title, style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
        Text(subtitle, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            HciChip("HCI #1 Status")
            HciChip("HCI #4 Consistency")
        }
    }
}

@Composable
private fun PremiumAuthCard(content: @Composable ColumnScope.() -> Unit) {
    val dark = isSystemInDarkTheme()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = if (dark) CardDark else Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        content = {
            Column(
                modifier = Modifier.padding(20.dp),
                content = content
            )
        }
    )
}

@Composable
private fun StateHeader(visualState: LoginVisualState, title: String, subtitle: String) {
    val icon = when (visualState) {
        LoginVisualState.Default -> Icons.Default.DirectionsBus
        LoginVisualState.Validation -> Icons.Default.Route
        LoginVisualState.Locked -> Icons.Default.LockClock
        LoginVisualState.Success -> Icons.Default.CheckCircle
    }
    val color = when (visualState) {
        LoginVisualState.Default -> PrimaryGreen
        LoginVisualState.Validation -> WarningAmber
        LoginVisualState.Locked -> DangerRed
        LoginVisualState.Success -> AccentMint
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.16f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = color)
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(title, style = MaterialTheme.typography.titleLarge)
            Text(
                subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = secondaryText()
            )
        }
    }
}

@Composable
private fun EmailStep(
    email: String,
    error: String?,
    loading: Boolean,
    onEmailChange: (String) -> Unit,
    onContinue: () -> Unit
) {
    Column {
        StateHeader(
            visualState = LoginVisualState.Default,
            title = "Verify your IIUI email",
            subtitle = "We will show a 6-digit demo OTP for your presentation flow."
        )
        Spacer(Modifier.height(18.dp))
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Registered email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            isError = error != null,
            supportingText = { if (error != null) Text(error) else Text("Example: inshrah.bsse4518@iiu.edu.pk") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(18.dp),
            colors = smartFieldColors()
        )
        Spacer(Modifier.height(18.dp))
        Button(
            onClick = onContinue,
            enabled = !loading,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
        ) {
            if (loading) CircularProgressIndicator(Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
            else Text("Send verification code")
        }
    }
}

@Composable
private fun OtpStep(
    digits: List<String>,
    error: String?,
    onDigitChange: (Int, String) -> Unit,
    onContinue: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        StateHeader(
            visualState = LoginVisualState.Validation,
            title = "Enter OTP",
            subtitle = "Demo code: 451826. Six boxes reduce typing mistakes."
        )
        Spacer(Modifier.height(22.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            digits.forEachIndexed { index, digit ->
                OtpDigitBox(
                    value = digit,
                    isError = error != null,
                    onValueChange = { onDigitChange(index, it) }
                )
            }
        }
        if (error != null) {
            Spacer(Modifier.height(8.dp))
            Text(error, color = DangerRed, style = MaterialTheme.typography.labelLarge)
        }
        Spacer(Modifier.height(18.dp))
        HciChip("HCI #6 Recognition over recall")
        Spacer(Modifier.height(18.dp))
        Button(
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
        ) {
            Text("Verify OTP")
        }
    }
}

@Composable
private fun OtpDigitBox(
    value: String,
    isError: Boolean,
    onValueChange: (String) -> Unit
) {
    val borderColor = when {
        isError -> DangerRed
        value.isNotBlank() -> PrimaryGreen
        else -> secondaryText().copy(alpha = 0.28f)
    }
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        textStyle = MaterialTheme.typography.titleLarge.copy(
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        ),
        modifier = Modifier
            .size(width = 44.dp, height = 54.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.3.dp, borderColor, RoundedCornerShape(14.dp))
            .padding(top = 13.dp),
        decorationBox = { innerTextField ->
            Box(contentAlignment = Alignment.Center) {
                if (value.isBlank()) {
                    Text(
                        "-",
                        color = secondaryText().copy(alpha = 0.35f),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                innerTextField()
            }
        }
    )
}

@Composable
private fun NewPasswordStep(
    password: String,
    confirmPassword: String,
    error: String?,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onFinish: () -> Unit
) {
    Column {
        StateHeader(
            visualState = LoginVisualState.Success,
            title = "Create new password",
            subtitle = "Clear recovery feedback helps users finish confidently."
        )
        Spacer(Modifier.height(18.dp))
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("New password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            isError = error != null,
            shape = RoundedCornerShape(18.dp),
            colors = smartFieldColors()
        )
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Confirm password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            isError = error != null,
            supportingText = { if (error != null) Text(error) else Text("Use at least 8 characters") },
            shape = RoundedCornerShape(18.dp),
            colors = smartFieldColors()
        )
        Spacer(Modifier.height(18.dp))
        Button(
            onClick = onFinish,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
        ) {
            Text("Save new password")
        }
    }
}

@Composable
private fun RecoveryStepper(step: RecoveryStep) {
    val steps = listOf("Email", "OTP", "Password")
    val activeIndex = step.ordinal
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        steps.forEachIndexed { index, label ->
            val active = index <= activeIndex
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                color = if (active) PrimaryGreen.copy(alpha = 0.16f) else MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
                contentColor = if (active) PrimaryGreen else secondaryText()
            ) {
                Text(
                    text = "${index + 1}. $label",
                    modifier = Modifier.padding(vertical = 10.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
private fun WarningPanel(title: String, body: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(DangerRed.copy(alpha = 0.10f))
            .border(1.dp, DangerRed.copy(alpha = 0.22f), RoundedCornerShape(18.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.LockClock, contentDescription = null, tint = DangerRed)
        Spacer(Modifier.width(10.dp))
        Column {
            Text(title, style = MaterialTheme.typography.labelLarge, color = DangerRed)
            Text(body, style = MaterialTheme.typography.bodyMedium, color = secondaryText())
        }
    }
    Spacer(Modifier.height(12.dp))
}

@Composable
private fun StatusPill(text: String, alpha: Float, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.18f))
            .padding(horizontal = 12.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .alpha(alpha)
                .clip(CircleShape)
                .background(GlowGreen)
        )
        Spacer(Modifier.width(7.dp))
        Text(text, color = Color.White, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
private fun HciChip(text: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = AccentMint.copy(alpha = 0.16f),
        contentColor = PrimaryGreen
    ) {
        Text(text, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
private fun smartFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = PrimaryGreen,
    focusedLabelColor = PrimaryGreen,
    cursorColor = PrimaryGreen,
    errorBorderColor = DangerRed,
    errorLabelColor = DangerRed
)

@Composable
private fun secondaryText(): Color = if (isSystemInDarkTheme()) TextSecondaryDark else TextSecondaryLight

@Composable
private fun PatternCanvas() {
    Canvas(Modifier.fillMaxSize()) {
        val patternColor = Color(0xFF5DCAA5).copy(alpha = 0.055f)
        val step = 62f
        for (x in 0..(size.width / step).toInt() + 1) {
            for (y in 0..(size.height / step).toInt() + 1) {
                val cx = x * step
                val cy = y * step
                drawCircle(patternColor, radius = 5f, center = Offset(cx, cy))
                drawCircle(patternColor, radius = 13f, center = Offset(cx + step / 2, cy + step / 2), style = Stroke(width = 1.4f))
            }
        }
    }
}

@Composable
private fun CampusCanvas(modifier: Modifier, busOffset: Float) {
    Canvas(modifier = modifier) {
        val building = Path().apply {
            moveTo(size.width * 0.16f, size.height * 0.88f)
            lineTo(size.width * 0.16f, size.height * 0.36f)
            lineTo(size.width * 0.36f, size.height * 0.24f)
            lineTo(size.width * 0.50f, size.height * 0.10f)
            lineTo(size.width * 0.64f, size.height * 0.24f)
            lineTo(size.width * 0.84f, size.height * 0.36f)
            lineTo(size.width * 0.84f, size.height * 0.88f)
            close()
        }
        drawPath(building, color = Color(0xFFE29A62).copy(alpha = 0.95f))
        drawRect(Color(0xFF763F25).copy(alpha = 0.30f), topLeft = Offset(size.width * 0.21f, size.height * 0.46f), size = Size(size.width * 0.58f, 5f))
        drawRoundRect(
            color = Color(0xFF0D3D35),
            topLeft = Offset(size.width * 0.34f, size.height * 0.52f),
            size = Size(size.width * 0.32f, size.height * 0.36f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(80f, 80f)
        )
        drawLine(
            color = Color.White.copy(alpha = 0.55f),
            start = Offset(0f, size.height * 0.92f),
            end = Offset(size.width, size.height * 0.92f),
            strokeWidth = 4f
        )
        val bx = size.width * 0.26f + busOffset
        val by = size.height * 0.72f
        drawRoundRect(
            color = Color(0xFF073E34),
            topLeft = Offset(bx, by),
            size = Size(size.width * 0.46f, size.height * 0.18f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(18f, 18f)
        )
        repeat(5) {
            drawRoundRect(
                color = Color(0xFFBCEFE2),
                topLeft = Offset(bx + 18f + it * 35f, by + 13f),
                size = Size(24f, 20f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(5f, 5f)
            )
        }
        drawCircle(Color(0xFF101816), radius = 11f, center = Offset(bx + 44f, by + size.height * 0.18f))
        drawCircle(Color(0xFF101816), radius = 11f, center = Offset(bx + size.width * 0.40f, by + size.height * 0.18f))
    }
}
