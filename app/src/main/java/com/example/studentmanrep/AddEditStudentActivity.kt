package com.example.studentmanrep

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddEditStudentActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextId: EditText
    private var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_student)

        editTextName = findViewById(R.id.edit_student_name)
        editTextId = findViewById(R.id.edit_student_id)
        val buttonSave = findViewById<Button>(R.id.btn_save)

        // Retrieve data from Intent
        val student = intent.getParcelableExtra<StudentModel>("student")
        position = intent.getIntExtra("position", -1)

        if (student != null) {
            editTextName.setText(student.name)
            editTextId.setText(student.id)
        }

        buttonSave.setOnClickListener {
            val updatedStudent = StudentModel(
                name = editTextName.text.toString(),
                id = editTextId.text.toString()
            )
            val resultIntent = Intent()
            resultIntent.putExtra("student", updatedStudent)
            resultIntent.putExtra("position", position)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
