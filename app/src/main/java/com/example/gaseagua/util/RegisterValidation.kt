package com.example.gaseagua.util

sealed class RegisterValidation() {
    object Sucess: RegisterValidation()
    data class Failed(val message: String): RegisterValidation()
}


data class RegisterFieldsState(
    val email: RegisterValidation,
    val password: RegisterValidation,
    val nome: RegisterValidation,
    val sobrenome: RegisterValidation
)