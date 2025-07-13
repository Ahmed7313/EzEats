package com.easyEats.ezeats.domain.main.model.request.cart.response


import android.os.Parcelable
import com.easyEats.ezeats.domain.main.model.response.compound.CompoundResult
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
@Parcelize
data class Restaurant(

    @SerializedName("compound")
    var compound: CompoundResult? = null,
    @SerializedName("country")
    var country: String? = "",
    @SerializedName("kashierAPIkey")
    var kashierAPIkey: String? = "",
    @SerializedName("kashierMID")
    var kashierMID: String? = "",
    @SerializedName("kashierSecretKey")
    var kashierSecretKey: String? = "",
    @SerializedName("objectId")
    var objectId: String? = "",
    @SerializedName("paymentSystem")
    var paymentSystem: String? = "",
    @SerializedName("isUmbrellaDelivery")
    var isUmbrellaDelivery: Boolean? = false,
    @SerializedName("isPickUp")
    var isPickUp: Boolean? = false,
    @SerializedName("isDelivery")
    var isDelivery: Boolean? = false,
) : Parcelable
