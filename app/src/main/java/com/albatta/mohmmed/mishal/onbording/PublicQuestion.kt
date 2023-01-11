package com.albatta.mohmmed.mishal.onbording

data class PublicQuestion(val id:Int, val question:String, val answer:String, val option1:String
                          , val option2:String, val option3:String, val time: Long, var hintOfAnswer:String, var image:ByteArray?=null)

