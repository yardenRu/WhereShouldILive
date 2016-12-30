package com.example.administrator.whereshouldilive

/**
 * Created by Administrator on 21/12/2016.
 */
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_scrolling.*
import java.util.*

class MainActivity : AppCompatActivity() {

    var people: LinearLayout? = null
    val peopleList = ArrayList<PersonDataBox>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        // val fragMan = fragmentManager


        // people = findViewById(R.id.people) as LinearLayout

        val peopleList = findViewById(R.id.people_list) as ListView

        val adapter = CustomAdapter(this) // <3
        peopleList.adapter = adapter


        val fab = findViewById(R.id.fab_add) as FloatingActionButton
        fab.setOnClickListener { view ->
            adapter.add(Unit)
        }


    }
    // fun addPerson(person: PersonDataBox) = peopleList.add(person)


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_settings) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
