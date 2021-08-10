package com.example.waybillmanagement.logic.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name="waybillRecord",strict = false)
@Entity(tableName = "t_waybill")
@Parcelize
data class Waybill constructor (

        @SerializedName("waybillNo")
        @field:Element(name = "waybillNo", required = false)
        @ColumnInfo(name = "waybillNo")
        @PrimaryKey
        var waybillno: String ="",

        @SerializedName("consignor")
        @field:Element(name = "consignor", required = false)
        @ColumnInfo(name = "consignor")
        var consignor: String ="",

        @SerializedName("consignorPhoneNumber")
        @field:Element(name = "consignorPhoneNumber", required = false)
        @ColumnInfo(name = "consignorPhoneNumber")
        var cerphone: String ="",

        @SerializedName("consignee")
        @field:Element(name = "consignee", required = false)
        @ColumnInfo(name = "consignee")
        var consignee: String ="",

        @SerializedName("consigneePhoneNumber")
        @field:Element(name = "consigneePhoneNumber", required = false)
        @ColumnInfo(name = "consigneePhoneNumber")
        var ceephone: String ="",

        @SerializedName("transportationDepartureStation")
        @field:Element(name = "transportationDepartureStation", required = false)
        @ColumnInfo(name = "transportationDepartureStation")
        var departure: String ="",

        @SerializedName("transportationArrivalStation")
        @field:Element(name = "transportationArrivalStation", required = false)
        @ColumnInfo(name = "transportationArrivalStation")
        var arrival: String ="",

        @SerializedName("goodsDistributionAddress")
        @field:Element(name = "goodsDistributionAddress", required = false)
        @ColumnInfo(name = "goodsDistributionAddress")
        var address: String ="",

        @SerializedName("goodsName")
        @field:Element(name = "goodsName", required = false)
        @ColumnInfo(name = "goodsName")
        var goodsname: String ="",

        @SerializedName("numberOfPackages")
        @field:Element(name = "numberOfPackages", required = false)
        @ColumnInfo(name = "numberOfPackages")
        var numberofPackages: String ="",

        @SerializedName("freightPaidByTheReceivingParty")
        @field:Element(name = "freightPaidByTheReceivingParty", required = false)
        @ColumnInfo(name = "freightPaidByTheReceivingParty")
        var rpaid: String ="",

        @SerializedName("freightPaidByConsignor")
        @field:Element(name = "freightPaidByConsignor", required = false)
        @ColumnInfo(name = "freightPaidByConsignor")
        var cpaid: String =""): Parcelable {


}