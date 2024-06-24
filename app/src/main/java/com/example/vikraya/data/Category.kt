package com.example.vikraya.data

sealed class Category(val category: String){
    object Chair:Category("Chair")
    object Cupboard:Category("Cupboard")
    object Furniture:Category("Furniture")
    object Accessory:Category("Accessory")
    object Table:Category("Table")

}
