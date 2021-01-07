package com.example.introtuceapp.models

data class User(
   val nameValue:String = "",
   val emailValue:String = "",
   val addressValue:String = "",
   val phoneValue:String= "",
   val profilePic:String= "",
   val created:Long= -1,
)