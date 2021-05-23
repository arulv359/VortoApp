package com.codingassessment.vortoarul.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Coordinates (

		@SerializedName("latitude") val latitude : Double,
		@SerializedName("longitude") val longitude : Double
): Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readDouble(),
			parcel.readDouble()) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeDouble(latitude)
		parcel.writeDouble(longitude)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Coordinates> {
		override fun createFromParcel(parcel: Parcel): Coordinates {
			return Coordinates(parcel)
		}

		override fun newArray(size: Int): Array<Coordinates?> {
			return arrayOfNulls(size)
		}
	}
}