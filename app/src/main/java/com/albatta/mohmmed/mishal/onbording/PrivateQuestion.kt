package com.albatta.mohmmed.mishal.onbording

data class PrivateQuestion(val id:Int, val question:String, val answer:String, val option1:String, val option2:String, val option3:String, val time: Long, val hintOfAnswer:String,val userId:Int, var image:ByteArray?=null)

