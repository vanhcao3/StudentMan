package com.example.studentmanrep

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private lateinit var studentList: MutableList<StudentModel>
    private lateinit var adapter: ArrayAdapter<StudentModel>
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Danh sách sinh viên
        studentList = mutableListOf(
            StudentModel("Nguyễn Văn A", "SV001"),
            StudentModel("Trần Thị B", "SV002")
        )

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Student Manager"

        // Gắn danh sách vào ListView
        listView = findViewById(R.id.list_view_students)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, studentList)
        listView.adapter = adapter

        // Đăng ký context menu cho ListView
        registerForContextMenu(listView)
    }

    // Tạo OptionMenu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // Xử lý chọn mục trong OptionMenu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add_new -> {
                // Mở Activity để thêm mới sinh viên
                val intent = Intent(this, AddEditStudentActivity::class.java)
                startActivityForResult(intent, REQUEST_ADD_STUDENT)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Tạo ContextMenu
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    // Xử lý chọn mục trong ContextMenu
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val student = studentList[info.position]

        when (item.itemId) {
            R.id.menu_edit -> {
                // Mở Activity để chỉnh sửa sinh viên
                val intent = Intent(this, AddEditStudentActivity::class.java)
                intent.putExtra("student", student)
                intent.putExtra("position", info.position)
                startActivityForResult(intent, REQUEST_EDIT_STUDENT)
                return true
            }
            R.id.menu_remove -> {
                // Xóa sinh viên
                studentList.removeAt(info.position)
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "${student.name} đã bị xóa", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    // Nhận kết quả từ AddEditStudentActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            val student = data.getParcelableExtra<StudentModel>("student")!!
            when (requestCode) {
                REQUEST_ADD_STUDENT -> {
                    studentList.add(student)
                }
                REQUEST_EDIT_STUDENT -> {
                    val position = data.getIntExtra("position", -1)
                    if (position != -1) {
                        studentList[position] = student
                    }
                }
            }
            adapter.notifyDataSetChanged()
        }
    }

    companion object {
        const val REQUEST_ADD_STUDENT = 1
        const val REQUEST_EDIT_STUDENT = 2
    }
}
