package com.example.administrator.whereshouldilive

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout

/**
 * Created by Administrator on 21/12/2016.
 */
class PersonDataBox : Fragment() {

    fun getAge()= (view!!.findViewById(R.id.age_editText) as EditText).text.toString().toInt()
    fun getIncome()= (view!!.findViewById(R.id.age_editText) as EditText).text.toString().toInt()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.additional_person, container, false) as RelativeLayout
        // return super.onCreateView(inflater, container, savedInstanceState)
    }
}