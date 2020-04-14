package com.example.mvp.contract

import com.example.mvp.data.User

interface AddDataContract {

    interface View{
        fun onAddDataSuccess(acc: String)

        fun onAddDataFailure(log: String)
    }

    interface Presenter{
        fun handleAddData(acc: String, pass: String)
    }
}