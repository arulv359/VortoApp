package com.codingassessment.vortoarul.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class BusinessDetails(
		@SerializedName("id") val id : String,
		@SerializedName("alias") val alias : String,
		@SerializedName("name") val name : String,
		@SerializedName("image_url") val image_url : String,
		@SerializedName("is_claimed") val is_claimed : Boolean,
		@SerializedName("is_closed") val is_closed : Boolean,
		@SerializedName("url") val url : String,
		@SerializedName("phone") val phone : String,
		@SerializedName("display_phone") val display_phone : String,
		@SerializedName("review_count") val review_count : String,
		@SerializedName("categories") val categories : List<Categories>,
		@SerializedName("rating") val rating : String,
		@SerializedName("location") val location : Location,
		@SerializedName("coordinates") val coordinates : Coordinates,
		@SerializedName("photos") val photos : List<String>,
		@SerializedName("price") val price : String,
		@SerializedName("hours") val hours : List<Hours>,
		@SerializedName("transactions") val transactions : List<String>
): Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readString() ?: "",
			parcel.readString()?: "",
			parcel.readString()?: "",
			parcel.readString()?: "",
			parcel.readByte() != 0.toByte(),
			parcel.readByte() != 0.toByte(),
			parcel.readString()?: "",
			parcel.readString()?: "",
			parcel.readString()?: "",
			parcel.readString()?: "",
			parcel.createTypedArrayList(Categories.CREATOR) ?: arrayListOf(),
			parcel.readString()?: "",
			parcel.readParcelable(Location::class.java.classLoader)!!,
			parcel.readParcelable(Coordinates::class.java.classLoader)!!,
			parcel.createStringArrayList() ?: arrayListOf(),
			parcel.readString()?: "",
			parcel.createTypedArrayList(Hours.CREATOR) ?: arrayListOf(),
			parcel.createStringArrayList() ?: arrayListOf()) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(id)
		parcel.writeString(alias)
		parcel.writeString(name)
		parcel.writeString(image_url)
		parcel.writeByte(if (is_claimed) 1 else 0)
		parcel.writeByte(if (is_closed) 1 else 0)
		parcel.writeString(url)
		parcel.writeString(phone)
		parcel.writeString(display_phone)
		parcel.writeString(review_count)
		parcel.writeTypedList(categories)
		parcel.writeString(rating)
		parcel.writeParcelable(location, flags)
		parcel.writeParcelable(coordinates, flags)
		parcel.writeStringList(photos)
		parcel.writeString(price)
		parcel.writeTypedList(hours)
		parcel.writeStringList(transactions)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<BusinessDetails> {
		override fun createFromParcel(parcel: Parcel): BusinessDetails {
			return BusinessDetails(parcel)
		}

		override fun newArray(size: Int): Array<BusinessDetails?> {
			return arrayOfNulls(size)
		}
	}
}