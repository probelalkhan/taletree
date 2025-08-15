package dev.belalkhan.taletree.auth

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.belalkhan.taletree.auth.data.AuthRepository
import dev.belalkhan.taletree.auth.data.FirebaseAuthRepository

@InstallIn(ViewModelComponent::class)
@Module
abstract class AuthEventModule {
    @Binds
    abstract fun bindAuthRepository(
        firebaseAuthRepository: FirebaseAuthRepository
    ): AuthRepository
}