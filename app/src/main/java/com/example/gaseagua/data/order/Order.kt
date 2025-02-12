package com.example.gaseagua.data.order

import android.os.Parcelable
import com.example.gaseagua.data.Address
import com.example.gaseagua.data.CartProduct
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import kotlin.random.Random.Default.nextLong
import java.util.*
@Parcelize
data class Order(
    val orderStatus: String = "",
    val totalPrice: Float = 0f,
    val products: List<CartProduct> = emptyList(),
    val address: Address = Address(),
    val date: String = SimpleDateFormat("dd-MM-yyyy", Locale.US).format(Date()),
    val orderId: Long = nextLong(0, 100_000_000_000) + totalPrice.toLong()
): Parcelable