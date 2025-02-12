package com.example.gaseagua.data.order

sealed class OrderStatus(val status: String) {
    object Ordered: OrderStatus("Pedido Realizado")
    object Canceled: OrderStatus("Cancelado")
    object Confirmed: OrderStatus("Confirmado")
    object Delivered: OrderStatus("Enviado")
}

fun getOrderStatus(status: String): OrderStatus{
    return when(status){
        "Canceled" -> {
            OrderStatus.Canceled
        }
        "Ordered" -> {
            OrderStatus.Ordered
        }
        "Confirmed" ->{
            OrderStatus.Confirmed
        }
        "Delivered" ->{
            OrderStatus.Delivered
        }
        else -> OrderStatus.Ordered
    }
}