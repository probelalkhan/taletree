package dev.belalkhan.taletree.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.belalkhan.taletree.auth.data.AuthRepository
import dev.belalkhan.taletree.utils.OneTimeEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val loginError: OneTimeEvent<String>? = null,
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    fun onEmailChanged(email: String) {
        _state.update { it.copy(email = email, emailError = null) }
    }

    fun onPasswordChanged(password: String) {
        _state.update { it.copy(password = password, passwordError = null) }
    }

    fun onTogglePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onLogin() {
        val current = _state.value

        if (current.email.isBlank()) {
            _state.update { it.copy(emailError = "Email cannot be empty") }
            return
        }

        if (current.password.length < 6) {
            _state.update { it.copy(passwordError = "Password must be at least 6 characters") }
            return
        }

        login()
    }

    private fun login() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }

        when (val result = authRepository.login(_state.value.email, _state.value.password)) {
            is AuthRepository.AuthResult.Error -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        loginError = OneTimeEvent(result.message)
                    )
                }
            }
            is AuthRepository.AuthResult.Success -> Unit
        }
    }
}

