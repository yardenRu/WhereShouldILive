package com.example.administrator.whereshouldilive

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.result_layout.*

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val functions = dataFunctions()
        val ages: IntArray = intent.extras["ages"] as IntArray
        val professions: Array<String> = intent.extras["professions"] as Array<String>
        val incomes: Array<Double> = intent.extras["incomes"] as Array<Double>

        winning_country.text = functions.BestCountry(ages, professions, incomes)

    }
}
