package com.example.studentmanrep

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var students: MutableList<StudentModel>
    private lateinit var studentAdapter: StudentAdapter
    private var deletedStudent: StudentModel? = null
    private var deletedStudentPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        students = mutableListOf(
            StudentModel("Nguyễn Văn An", "SV001"),
            StudentModel("Trần Thị Bảo", "SV002"),
            StudentModel("Lê Hoàng Cường", "SV003"),
            StudentModel("Phạm Thị Dung", "SV004"),
            StudentModel("Đỗ Minh Đức", "SV005")
        )

        studentAdapter = StudentAdapter(students,
            onEditClick = { student ->
                showEditStudentDialog(student)
            },
            onDeleteClick = { student ->
                showDeleteStudentDialog(student)
            }
        )

        findViewById<RecyclerView>(R.id.recycler_view_students).apply {
            adapter = studentAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        findViewById<View>(R.id.btn_add_new_student).setOnClickListener {
            showAddStudentDialog()
        }
    }

    // Show dialog to add a new student
    private fun showAddStudentDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_student, null)
        val studentNameEditText: EditText = dialogView.findViewById(R.id.edit_student_name)
        val studentIdEditText: EditText = dialogView.findViewById(R.id.edit_student_id)

        AlertDialog.Builder(this)
            .setTitle("Add New Student")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = studentNameEditText.text.toString()
                val id = studentIdEditText.text.toString()
                val newStudent = StudentModel(name, id)

                students.add(newStudent)
                studentAdapter.notifyItemInserted(students.size - 1)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // Show dialog to edit student details
    private fun showEditStudentDialog(student: StudentModel) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_student, null)
        val studentNameEditText: EditText = dialogView.findViewById(R.id.edit_student_name)
        val studentIdEditText: EditText = dialogView.findViewById(R.id.edit_student_id)

        studentNameEditText.setText(student.studentName)
        studentIdEditText.setText(student.studentId)

        AlertDialog.Builder(this)
            .setTitle("Edit Student")
            .setView(dialogView)
            .setPositiveButton("Lưu") { _, _ ->
                val newName = studentNameEditText.text.toString()
                val newId = studentIdEditText.text.toString()

                student.studentName = newName
                student.studentId = newId

                studentAdapter.notifyDataSetChanged()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    // Show confirmation dialog before deleting a student
    private fun showDeleteStudentDialog(student: StudentModel) {
        AlertDialog.Builder(this)
            .setTitle("Xóa sinh viên")
            .setMessage("Xác nhận xóa sinh viên ${student.studentName}?")
            .setPositiveButton("Xác nhận") { _, _ ->
                deletedStudent = student
                deletedStudentPosition = students.indexOf(student)

                // Remove student from the list
                students.remove(student)
                studentAdapter.notifyDataSetChanged()

                // Show Snackbar with Undo option
                showUndoSnackbar()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    // Show Snackbar after deletion with undo option
    private fun showUndoSnackbar() {
        val snackbar = Snackbar.make(
            findViewById(R.id.recycler_view_students),
            "Đã xóa sinh viên",
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction("Undo") {
            // Restore the deleted student
            deletedStudent?.let {
                students.add(deletedStudentPosition, it)
                studentAdapter.notifyItemInserted(deletedStudentPosition)
            }
        }
        snackbar.show()
    }
}
