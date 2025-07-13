package com.easyEats.ezeats.domain.main.model.request.cart.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
@Parcelize
data class UpdateCartResponse(
    @SerializedName("result")
    var result: Result? = Result()
) : Parcelable

//isAddress , isPickup , isDelivery , isUmberlla
/*
if(isPickUp == true)
//call api from is ////PickUp


api > savePickUpStation
pickup ->
{
   "orderId": "4QxXfNMnVE",
   "pickUpStation":"Restaurant",
   "compoundName" : " from updateCart -> compoundObj -> name "
}

api > savePickUpStation
{
   "orderId": "4QxXfNMnVE",
   "pickUpStation":"umberall-444",
  "compoundName" : "marassi"

}

saveUserAddressIdOnOrderTable
{
   "orderId": "VSbH9UTHFL",
   "userAddressId": "boTkzowIOL"
}

 */