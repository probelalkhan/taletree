package dev.belalkhan.taletree.auth.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dev.belalkhan.taletree.auth.data.AuthRepository.AuthResult
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun login(email: String, password: String): AuthResult {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let { AuthResult.Success(it) }
                ?: AuthResult.Error("Signup succeeded but user is null.")
        } catch (e: Exception) {
            if (e is FirebaseAuthUserCollisionException) {
                return try {
                    val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                    result.user?.let { AuthResult.Success(it) }
                        ?: AuthResult.Error("Login succeeded but user is null.")
                } catch (loginError: Exception) {
                    val message = when (loginError) {
                        is FirebaseAuthInvalidCredentialsException -> "Incorrect password."
                        else -> loginError.message ?: "Login failed."
                    }
                    AuthResult.Error(message)
                }
            } else {
                AuthResult.Error(e.message ?: "Signup failed.")
            }
        }
    }
}