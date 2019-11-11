package com.example.date_picker

import android.annotation.SuppressLint
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import java.io.FileInputStream
import java.util.*
import android.widget.Toast
import android.widget.DatePicker.OnDateChangedListener
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileOutputStream
import java.lang.reflect.Array.get
import com.google.firebase.database.FirebaseDatabase
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T










class MainActivity : AppCompatActivity() {



    override protected fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        login.setOnClickListener{
            val Intent = Intent(this,com.example.date_picker.login::class.java)
            startActivity(Intent);
        }

        //val database = FirebaseDatabase.getInstance().getReference()
        //val myRef = database.child("text")

        //myRef.setValue("Hello, World!")

        val datePicker = findViewById(R.id.datePicker) as DatePicker
        val viewDatePick = findViewById(R.id.viewDatePick) as TextView
        val edtDiary = findViewById(R.id.edtDiary) as EditText
        val btnSave = findViewById(R.id.btnSave) as Button
        val login = findViewById(R.id.login) as Button

        // 오늘 날짜
        val c = Calendar.getInstance()
        val cYear = c.get(Calendar.YEAR)
        val cMonth = c.get(Calendar.MONTH)
        val cDay = c.get(Calendar.DAY_OF_MONTH)

        checkedDay(cYear, cMonth, cDay)

        // datePick 기능 만들기
        // datePicker.init(연도,달,일)
        datePicker.init(
            datePicker.year,
            datePicker.month,
            datePicker.dayOfMonth,
            object : DatePicker.OnDateChangedListener {
                override fun onDateChanged(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                    // 이미 선택한 날짜에 일기가 있는지 없는지 체크해야할 시간이다
                    checkedDay(year, monthOfYear, dayOfMonth)
                    var fileName:String = "$year$monthOfYear$dayOfMonth.txt"



                    btnSave.setOnClickListener {
                        // fileName을 넣고 저장 시키는 메소드를 호출
                        var key: String = "$year$monthOfYear$dayOfMonth"
                        val content = edtDiary.getText().toString()
                        //년도 월 일
                        var diaryYear:String = "$year"
                        var diaryMonth: String = "$monthOfYear"
                        var diaryDay : String = "$dayOfMonth"
                        //hashmap 만들기
                        val result = hashMapOf(key to content)
                        result.put("date", key)
                        result.put("diary", content)
                        var mDatabase = FirebaseDatabase.getInstance().getReference()
                        //firebase에 저장
                        mDatabase.child("user1").child(diaryYear).child(diaryMonth).child(diaryDay).push().setValue(content)
                        saveDiary(fileName)

                    }
                }
            })

        // 저장/수정 버튼 누르면 실행되는 리스너
        /*btnSave.setOnClickListener {
            // fileName을 넣고 저장 시키는 메소드를 호출
            saveDiary(fileName)
        }*/
    }


    // 일기 파일 읽기
    private fun checkedDay(year: Int, monthOfYear: Int, dayOfMonth: Int) {

        // 받은 날짜로 날짜 보여주는
        viewDatePick.setText("$year - $monthOfYear - $dayOfMonth")

        var fileName = "$year$monthOfYear$dayOfMonth.txt"


        // 읽어봐서 읽어지면 일기 가져오고
        // 없으면 catch 그냥 살아? 아주 위험한 생각같다..
        var fis: FileInputStream? = null
        try {
            fis = openFileInput(fileName)
            val fileData = ByteArray(fis!!.available())
            fis.read(fileData)
            fis.close()

            var str = String(fileData)
            // 읽어서 토스트 메시지로 보여줌
            Toast.makeText(applicationContext, "일기 써둔 날", Toast.LENGTH_SHORT).show()
            edtDiary.setText(str)
            btnSave.setText("수정하기")
        } catch (e: Exception) { // UnsupportedEncodingException , FileNotFoundException , IOException
            // 없어서 오류가 나면 일기가 없는 것 -> 일기를 쓰게 한다.
            Toast.makeText(applicationContext, "일기 없는 날", Toast.LENGTH_SHORT).show()
            edtDiary.setText("")
            btnSave.setText("새 일기 저장")
            e.printStackTrace()
        }

    }



    // 일기 저장하는 메소드0
    @SuppressLint("WrongConstant")
    private fun saveDiary(readDay: String)
    {
        var fos: FileOutputStream? = null

        try {
            fos =
                openFileOutput(readDay, Context.MODE_NO_LOCALIZED_COLLATORS) //MODE_WORLD_WRITEABLE
            var date: String = "a"
            val content = edtDiary.getText().toString()

            // String.getBytes() = 스트링을 배열형으로 변환?
            fos!!.write(content.toByteArray())
            //fos.flush();
            fos!!.close()

            // getApplicationContext() = 현재 클래스.this ?
            Toast.makeText(applicationContext, "일기 저장됨", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) { // Exception - 에러 종류 제일 상위 // FileNotFoundException , IOException
            e.printStackTrace()
            Toast.makeText(applicationContext, "오류오류", Toast.LENGTH_SHORT).show()
        }
    }

}
