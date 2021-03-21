package com.manuelblanco.core.model

import com.google.gson.annotations.SerializedName

/*
Copyright (c) 2021 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */


data class Products (

	@SerializedName("product_id") val product_id : Int,
	@SerializedName("owner") val owner : String,
	@SerializedName("created") val created : String,
	@SerializedName("likes") val likes : Int,
	@SerializedName("address") val address : String,
	@SerializedName("createdTime") val createdTime : String,
	@SerializedName("is_private") val is_private : Int,
	@SerializedName("price") val price : Int,
	@SerializedName("p_condition") val p_condition : String,
	@SerializedName("category") val category : String,
	@SerializedName("payment_method") val payment_method : String,
	@SerializedName("title") val title : String,
	@SerializedName("description") val description : String,
	@SerializedName("currency") val currency : String,
	@SerializedName("offer") val offer : Boolean,
	@SerializedName("story_img") val story_img : String,
	@SerializedName("story_url") val story_url : String,
	@SerializedName("rating_product") val rating_product : Int,
	@SerializedName("rating_amount") val rating_amount : Int,
	@SerializedName("distance") val distance : Int,
	@SerializedName("attachment") val attachment : Attachment,
	@SerializedName("gallery") val gallery : List<Gallery>,
	@SerializedName("like_user") val like_user : Boolean,
	@SerializedName("location") val location : Location
)