package com.codingassessment.vortoarul.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Hours (

		@SerializedName("open") val open : List<Open>,
		@SerializedName("hours_type") val hours_type : String,
		@SerializedName("is_open_now") val is_open_now : Boolean
): Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.createTypedArrayList(Open.CREATOR) ?: arrayListOf(),
			parcel.readString() ?: "",
			parcel.readByte() != 0.toByte()) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeTypedList(open)
		parcel.writeString(hours_type)
		parcel.writeByte(if (is_open_now) 1 else 0)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Hours> {
		override fun createFromParcel(parcel: Parcel): Hours {
			return Hours(parcel)
		}

		override fun newArray(size: Int): Array<Hours?> {
			return arrayOfNulls(size)
		}
	}
}