package com.example.gaseagua.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gaseagua.data.User
import com.example.gaseagua.util.RegisterFieldsState
import com.example.gaseagua.util.RegisterValidation
import com.example.gaseagua.util.Resource
import com.example.gaseagua.util.validateEmail
import com.example.gaseagua.util.validateNome
import com.example.gaseagua.util.validatePassword
import com.example.gaseagua.util.validateSobrenome
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): ViewModel() {


    private val _register = MutableStateFlow<Resource<FirebaseUser>>(Resource.Initial())
    val register: Flow<Resource<FirebaseUser>> = _register

    private val _validation = Channel<RegisterFieldsState>()
    val validation = _validation.receiveAsFlow()

    fun createAccountWithEmailAndPassword(user: User, password: String){

        if(checkValidation(user, password)){


        runBlocking {
            _register.emit(Resource.Loading())
        }
        firebaseAuth.createUserWithEmailAndPassword(user.email,password)
            .addOnSuccessListener {
                it.user?.let {
                    _register.value = Resource.Success(it)
                }
            } .addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())
            }
        }else{
            val registerFieldsState = RegisterFieldsState(
                validateEmail(user.email), validatePassword(password),
                validateNome(user.firstName), validateSobrenome(user.lastName)
            )
            runBlocking {
                _validation.send(registerFieldsState)
            }
        }
    }

    private fun checkValidation(user: User, password: String): Boolean {
        val nomeValidation = validateNome(user.firstName)
        val sobrenomeValidation = validateNome(user.lastName)
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)
        val register = emailValidation is RegisterValidation.Sucess &&
                passwordValidation is RegisterValidation.Sucess && nomeValidation is
                RegisterValidation.Sucess && sobrenomeValidation is RegisterValidation.Sucess
        return register
    }
}