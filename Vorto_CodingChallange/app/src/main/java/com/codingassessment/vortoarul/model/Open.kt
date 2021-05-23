package com.codingassessment.vortoarul.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Open (

		@SerializedName("is_overnight") val is_overnight : Boolean,
		@SerializedName("start") val start : Int,
		@SerializedName("end") val end : Int,
		@SerializedName("day") val day : Int
): Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readByte() != 0.toByte(),
			parcel.readInt(),
			parcel.readInt(),
			parcel.readInt()) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeByte(if (is_overnight) 1 else 0)
		parcel.writeInt(start)
		parcel.writeInt(end)
		parcel.writeInt(day)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Open> {
		override fun createFromParcel(parcel: Parcel): Open {
			return Open(parcel)
		}

		override fun newArray(size: Int): Array<Open?> {
			return arrayOfNulls(size)
		}
	}
}