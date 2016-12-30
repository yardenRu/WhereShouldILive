package com.example.administrator.whereshouldilive

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.additional_person.view.*
import java.util.*

/**
 * Created by Administrator on 22/12/2016.
 */
class CustomAdapter(val con: Context) : ArrayAdapter<Unit>(con, R.layout.additional_person, R.id.dump_text) {

    val people = ArrayList<LinearLayout>()
    var jobs = arrayOf("Managers", "Professionals", "Technicians and associate professionals", "Clerical support workers",
            "Service and sales workers", "Skilled agricultural, forestry and fishery workers", "Craft and related trades workers",
            "Plant and machine operators, and assemblers", "Elementary occupations", "Armed forces occupations", "Students", "Unemployed")

    val info = {}


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        return super.getView(position, convertView, parent)
        val lay = LayoutInflater.from(con).inflate(R.layout.additional_person, parent, false) as LinearLayout
        lay.jobs_spinner.setItems("Managers", "Professionals", "Technicians and associate professionals", "Clerical support workers",
                "Service and sales workers", "Skilled agricultural, forestry and fishery workers", "Craft and related trades workers",
                "Plant and machine operators, and assemblers", "Elementary occupations", "Armed forces occupations", "Students", "Unemployed")
        lay.jobs_spinner.setOnItemSelectedListener { spinner, i, l, any -> lay.jobs_spinner.text = jobs[i] }

        people.add(lay)

        return lay
    }

//    fun getInformation(): ArrayList<Pair<Int, String>> {
//
//    }
}