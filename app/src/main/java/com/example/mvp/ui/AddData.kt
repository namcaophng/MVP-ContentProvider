package com.example.mvp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.mvp.R
import com.example.mvp.contract.AddDataContract
import com.example.mvp.data.local.DatabaseHelper
import com.example.mvp.presenter.AddDataPresenter
import kotlinx.android.synthetic.main.activity_main.*

class AddData : AppCompatActivity(), AddDataContract.View, View.OnClickListener {

    lateinit var presenter: AddDataPresenter
    companion object{
        lateinit var myDb: DatabaseHelper
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = AddDataPresenter(this)

        myDb = DatabaseHelper(this)

        btn_signUp.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_signUp ->signUp()
        }
    }

    private fun signUp() {
        presenter.handleAddData(ed_acc.text.toString(), ed_pass.text.toString())
        textView.text = ""
        textView2.text = ""
    }

    override fun onAddDataSuccess(acc: String) {
        Toast.makeText(applicationContext, "Added $acc to Database!", Toast.LENGTH_SHORT).show()
    }

    override fun onAddDataFailure(log: String) {
        Toast.makeText(applicationContext, log, Toast.LENGTH_SHORT).show()
    }
}
