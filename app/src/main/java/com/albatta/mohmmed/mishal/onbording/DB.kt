package com.albatta.mohmmed.mishal.onbording

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.core.database.getBlobOrNull

class DB(val context: Context): SQLiteOpenHelper(context,"Home",null,3) {

        override fun onCreate(x: SQLiteDatabase?) {
            x?.execSQL("create table PublicQuestion(id integer primary key autoincrement,question text,answer text,option1 text,option2 text,option3 text,time real,hintOfAnswer text,image BlobOrNull)")
            x?.execSQL("create table PrivateQuestion(id integer primary key autoincrement,question text,answer text,option1 text,option2 text,option3 text,time real,hintOfAnswer text,userId integer,image BlobOrNull)")
            x?.execSQL("create table User(id integer primary key autoincrement,Email text,password text,inout text,image BlobOrNull)")
        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
            p0?.execSQL("DROP TABLE IF EXISTS " + "PublicQuestion")
            p0?.execSQL("DROP TABLE IF EXISTS " + "PrivateQuestion")
            p0?.execSQL("DROP TABLE IF EXISTS " + "User")
            onCreate(p0)
        }


        fun addPublicQuestion(question:String, answer:String, option1:String, option2:String, option3:String, time:Long, hintOfAnswer:String, image: ByteArray?):Boolean{
            val db=this.writableDatabase
            val cv=ContentValues()
            cv.put("question",question)
            cv.put("answer",answer)
            cv.put("option1",option1)
            cv.put("option2",option2)
            cv.put("option3",option3)
            cv.put("time",time)
            cv.put("hintOfAnswer",hintOfAnswer)
            cv.put("image",image)
            val result=db.insert("PublicQuestion",null,cv)
            return result !=-1L
        }
        fun getPublicQuestion():ArrayList<PublicQuestion> {
            val myArray = ArrayList<PublicQuestion>()
            val db = this.readableDatabase
            val cursor = db.rawQuery("select * from PublicQuestion", null)
            while (cursor.moveToNext()){
                val id=cursor.getInt(0)
                val question=cursor.getString(1)
                val answer=cursor.getString(2)
                val option1=cursor.getString(3)
                val option2=cursor.getString(4)
                val option3=cursor.getString(5)
                val time=cursor.getLong(6)
                val hintOfAnswer=cursor.getString(7)
                val image=cursor.getBlobOrNull(8)
                val p=PublicQuestion(id,question,answer,option1,option2,option3,time,hintOfAnswer,image)
                myArray.add(p)
            }
            cursor.close()
            db.close()
            return myArray
        }

        fun deletePublicQuestion(id: Int):Boolean{
            val s="Delete From PublicQuestion where id =\"$id\";"
            val db=this.readableDatabase
            var re=false
            try {
                db.execSQL(s)
                re=true
            }catch (ex:Exception){
                Log.e("asd","Error,delete")
            }
            db.close()
            return re
        }

        fun updatePublicQuestion(id: String, question:String, answer:String, option1:String, option2:String, option3:String, time:Long, hintOfAnswer:String, image: ByteArray?):Boolean{
            val db=this.writableDatabase
            val cv=ContentValues()
            cv.put("question",question)
            cv.put("answer",answer)
            cv.put("option1",option1)
            cv.put("option2",option2)
            cv.put("option3",option3)
            cv.put("time",time)
            cv.put("hintOfAnswer",hintOfAnswer)
            cv.put("image",image)
            val re: Boolean = try {
                db.update("PublicQuestion",cv,"id = ?", arrayOf(id))
                true
            }catch (rx:Exception){
                false
            }
            return re
        }

        fun updatePublicQuestionImage(id: String, image: ByteArray?):Boolean{
            val db=this.writableDatabase
            val cv=ContentValues()
            cv.put("image",image)
            val re: Boolean = try {
                db.update("PublicQuestion",cv,"id = ?", arrayOf(id))
                true
            }catch (rx:Exception){
                false
            }
            return re
        }
    fun deleteAllPublicQuestions(){
        val write=this.writableDatabase
        write.delete("PublicQuestion", null, null)
    }


    fun addPrivateQuestion(question:String, answer:String, option1:String, option2:String, option3:String, time:Long, hintOfAnswer:String,userId:Int, image: ByteArray?):Boolean{
        val db=this.writableDatabase
        val cv=ContentValues()
        cv.put("question",question)
        cv.put("answer",answer)
        cv.put("option1",option1)
        cv.put("option2",option2)
        cv.put("option3",option3)
        cv.put("time",time)
        cv.put("hintOfAnswer",hintOfAnswer)
        cv.put("userId",userId)
        cv.put("image",image)
        val result=db.insert("PrivateQuestion",null,cv)
        return result !=-1L
    }
    fun getPrivateQuestion(sid: Int):ArrayList<PrivateQuestion> {
        val myArray = ArrayList<PrivateQuestion>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("select * from PrivateQuestion where userId =\"$sid\";", null)
        while (cursor.moveToNext()){
            val id=cursor.getInt(0)
            val question=cursor.getString(1)
            val answer=cursor.getString(2)
            val option1=cursor.getString(3)
            val option2=cursor.getString(4)
            val option3=cursor.getString(5)
            val time=cursor.getLong(6)
            val hintOfAnswer=cursor.getString(7)
            val userId=cursor.getInt(8)
            val image=cursor.getBlobOrNull(9)
            val p=PrivateQuestion(id,question,answer,option1,option2,option3,time,hintOfAnswer,userId,image)
            myArray.add(p)
        }
        cursor.close()
        db.close()
        return myArray
    }

    fun deletePrivateQuestion(id: Int):Boolean{
        val s="Delete From PrivateQuestion where id =\"$id\";"
        val db=this.readableDatabase
        var re=false
        try {
            db.execSQL(s)
            re=true
        }catch (ex:Exception){
            Log.e("asd","Error,delete")
        }
        db.close()
        return re
    }

    fun updatePrivateQuestion(id: String, question:String, answer:String, option1:String, option2:String, option3:String, time:Long, hintOfAnswer:String, image: ByteArray?):Boolean{
        val db=this.writableDatabase
        val cv=ContentValues()
        cv.put("question",question)
        cv.put("answer",answer)
        cv.put("option1",option1)
        cv.put("option2",option2)
        cv.put("option3",option3)
        cv.put("time",time)
        cv.put("hintOfAnswer",hintOfAnswer)
        cv.put("image",image)
        val re: Boolean = try {
            db.update("PrivateQuestion",cv,"id = ?", arrayOf(id))
            true
        }catch (rx:Exception){
            false
        }
        return re
    }

    fun updatePrivateQuestionImage(id: String, image: ByteArray?):Boolean{
        val db=this.writableDatabase
        val cv=ContentValues()
        cv.put("image",image)
        val re: Boolean = try {
            db.update("PrivateQuestion",cv,"id = ?", arrayOf(id))
            true
        }catch (rx:Exception){
            false
        }
        return re
    }
    fun deleteAllPrivateQuestions(){
        val write=this.writableDatabase
        write.delete("PrivateQuestion", null, null)
    }



    fun addUser(Email:String,password:String,inout:String, image: ByteArray?):Boolean{
        val db=this.writableDatabase
        val cv=ContentValues()
        cv.put("Email",Email)
        cv.put("password",password)
        cv.put("inout",inout)
        cv.put("image",image)
        val result=db.insert("User",null,cv)
        return result !=-1L
    }
    fun getUser():ArrayList<User> {
        val myArray = ArrayList<User>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("select * from User", null)
        while (cursor.moveToNext()){
            val id=cursor.getInt(0)
            val Email=cursor.getString(1)
            val password=cursor.getString(2)
            val inout=cursor.getString(3)
            val image=cursor.getBlobOrNull(4)
            val p=User(id,Email,password,inout,image)
            myArray.add(p)
        }
        cursor.close()
        db.close()
        return myArray
    }

    fun deleteUser(id: Int):Boolean{
        val s="Delete From User where id =\"$id\";"
        val db=this.readableDatabase
        var re=false
        try {
            db.execSQL(s)
            re=true
        }catch (ex:Exception){
            Log.e("asd","Error,delete")
        }
        db.close()
        return re
    }

    fun updateUser(id: String, Email:String, password:String, inout: String):Boolean{
        val db=this.writableDatabase
        val cv=ContentValues()
        cv.put("Email",Email)
        cv.put("password",password)
        cv.put("inout",inout)
        val re: Boolean = try {
            db.update("User",cv,"id = ?", arrayOf(id))
            true
        }catch (rx:Exception){
            false
        }
        return re
    }

    fun updateUserImage(id: String, image: ByteArray?):Boolean{
        val db=this.writableDatabase
        val cv=ContentValues()
        cv.put("image",image)
        val re: Boolean = try {
            db.update("User",cv,"id = ?", arrayOf(id))
            true
        }catch (rx:Exception){
            false
        }
        return re
    }

    fun updateUserinout(id: String, inout: String):Boolean{
        val db=this.writableDatabase
        val cv=ContentValues()
        cv.put("inout",inout)
        val re: Boolean = try {
            db.update("User",cv,"id = ?", arrayOf(id))
            true
        }catch (rx:Exception){
            false
        }
        return re
    }




    }