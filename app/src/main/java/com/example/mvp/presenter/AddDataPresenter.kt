package com.example.mvp.presenter

import android.database.Cursor
import android.net.Uri
import com.example.mvp.contract.AddDataContract
import com.example.mvp.data.User
import com.example.mvp.data.local.ContentProviderUser
import com.example.mvp.data.local.DatabaseHelper
import com.example.mvp.data.local.DatabaseHelper.Companion.TABLE_NAME
import com.example.mvp.ui.AddData
import com.example.mvp.ui.AddData.Companion.myDb

class AddDataPresenter(var view: AddDataContract.View): AddDataContract.Presenter {

    val AUTHORITY = "com.example.mvp.data.local.ContentProviderUser"
    val CONTENT_PATH = "backupdata"
    val URL = "content://${AUTHORITY}/${CONTENT_PATH}"
    val CONTENT_URI = Uri.parse(URL)
    val ACCOUNT = "account"
    val PASSWORD = "password"

    override fun handleAddData(acc: String, pass: String) {
        var checkOK = true
        var temAcc: String
        var log: String

        if(acc.isEmpty() || pass.isEmpty() ){
            view.onAddDataFailure("Account or Password is not typed!")
        }else{
            var c = myDb.getCursorAll()
            c?.moveToFirst()

            while (c?.isAfterLast === false){
                temAcc = c.getString(0)
                if(temAcc.contentEquals(acc)){
                    checkOK = false
                    log = "This account is existed!"
                    view.onAddDataFailure(log)
                    c.moveToLast()
                }
            }
            if(checkOK){
                var temUser = User(acc, pass)
                myDb.addToDB(temUser)
                view.onAddDataSuccess(acc)
            }
        }
    }
}