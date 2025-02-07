package com.example.gaseagua.util

import android.util.Patterns

fun validateEmail(email: String): RegisterValidation{
    if(email.isEmpty())
        return RegisterValidation.Failed("Email em branco")
    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return RegisterValidation.Failed("Email em formato incorreto")
    return RegisterValidation.Sucess
}

fun validatePassword(password: String): RegisterValidation{
    if(password.isEmpty())
        return RegisterValidation.Failed("Senha em branco")
    if(password.length < 8)
        return RegisterValidation.Failed("Senha mínima: 8 caracteres")
    return RegisterValidation.Sucess
}
fun validateNome(password: String): RegisterValidation{
    if(password.isEmpty())
        return RegisterValidation.Failed("Nome em branco")
    return RegisterValidation.Sucess
}
fun validateSobrenome(password: String): RegisterValidation{
    if(password.isEmpty())
        return RegisterValidation.Failed("Sobrenome em branco")
    return RegisterValidation.Sucess
}