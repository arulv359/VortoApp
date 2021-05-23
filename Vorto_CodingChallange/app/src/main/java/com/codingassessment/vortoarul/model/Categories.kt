package com.codingassessment.vortoarul.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Categories (

		@SerializedName("alias") val alias : String,
		@SerializedName("title") val title : String
): Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readString() ?: "",
			parcel.readString()?: "") {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(alias)
		parcel.writeString(title)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Categories> {
		override fun createFromParcel(parcel: Parcel): Categories {
			return Categories(parcel)
		}

		override fun newArray(size: Int): Array<Categories?> {
			return arrayOfNulls(size)
		}
	}
}