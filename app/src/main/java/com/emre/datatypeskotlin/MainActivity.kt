package com.emre.datatypeskotlin

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parse.ParseObject
import com.parse.ParseQuery
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity() {

    private var progressDialog: ProgressDialog? = null
    private var objectId: String? = null

    private var popupInputDialogView: View? = null
    private var recyclerView: RecyclerView? = null

    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressDialog = ProgressDialog(this@MainActivity)

        val saveData = findViewById<Button>(R.id.saveData)
        val readData = findViewById<Button>(R.id.readData)
        val updateData = findViewById<Button>(R.id.updateData)


        saveData.setOnClickListener {
            try {
                saveDataTypes()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        readData.setOnClickListener { readObjects() }

        updateData.setOnClickListener { updateObject() }
    }

    private fun saveDataTypes() {
        val parseObject = ParseObject("DataTypes")

        parseObject.put("stringField", "String")
        parseObject.put("doubleField", 1.5)
        parseObject.put("intField", 2)
        parseObject.put("boolField", true)
        parseObject.put("dateField", Calendar.getInstance().time)


        val myObject = JSONObject()
        myObject.put("number", 1)
        myObject.put("string", "42")

        parseObject.put("jsonObject", myObject)

        val myArray = JSONArray()
        myArray.put(myObject)
        myArray.put(myObject)
        myArray.put(myObject)

        parseObject.put("jsonArray", myArray)

        val list: MutableList<String> = ArrayList()
        list.add("string1")
        list.add("string2")
        parseObject.put("listStringField", list)

        val listInt: MutableList<Int> = ArrayList()
        listInt.add(1)
        listInt.add(2)
        listInt.add(3)
        parseObject.put("listIntField", listInt)

        val listBool: MutableList<Boolean> = ArrayList()
        listBool.add(true)
        listBool.add(false)
        parseObject.put("listBoolField", listBool)

        progressDialog?.show()
        parseObject.saveInBackground {
            progressDialog?.dismiss()
            if (it == null) {
                Toast.makeText(this, "Object saved successfully...", Toast.LENGTH_SHORT).show()
                objectId = parseObject.objectId
            } else {
                objectId = null
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun readObjects() {
        if (objectId == null) {
            Toast.makeText(
                this,
                "None objectId. Click  Save Data button before.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val query = ParseQuery<ParseObject>("DataTypes")


        progressDialog?.show()
        query.getInBackground(
            objectId
        ) { obj, e ->
            progressDialog?.dismiss()
            if (e == null) {

                val list: MutableList<Data> = ArrayList()
                list.add(Data("Int list field", obj.get("listIntField").toString()))
                list.add(Data("String field",obj.get("stringField").toString()))
                list.add(Data("Double field", obj.get("doubleField").toString()))
                list.add(Data("Int field", obj.get("intField").toString()))
                list.add(Data("String list field", obj.get("listStringField").toString()))
                list.add(Data("Date field",obj.get("dateField").toString()))
                list.add(Data("Bool field", obj.get("boolField").toString()))
                list.add(Data("List Bool field", obj.get("listBoolField").toString()))
                list.add(Data("Json Object field", obj.get("jsonObject").toString()))
                list.add(Data("Json Array field", obj.get("jsonArray").toString()))
                showDataTypes(list)
            } else {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun updateObject() {
        if (objectId == null) {
            Toast.makeText(
                this,
                "None objectId. Click  Save Data button before.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val parseObject = ParseObject("DataTypes")
        parseObject.objectId = objectId
        parseObject.put("intField", 5)
        parseObject.put("stringField", "new String")

        progressDialog?.show()

        parseObject.saveInBackground {
            progressDialog?.dismiss()
            if (it == null) {
                Toast.makeText(this, "Object saved successfully...", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDataTypes(list: List<Data>) {
        val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
        alertDialogBuilder.setTitle("Data types")
        alertDialogBuilder.setCancelable(true)
        initPopupViewControls(list)
        //We are setting our custom popup view by AlertDialog.Builder
        alertDialogBuilder.setView(popupInputDialogView)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    @SuppressLint("InflateParams")
    private fun initPopupViewControls(list: List<Data>) {
        val layoutInflater = LayoutInflater.from(this@MainActivity)
        popupInputDialogView = layoutInflater.inflate(R.layout.custom_alert_dialog, null)
        recyclerView = popupInputDialogView?.findViewById(R.id.recyclerView)
        val adapter = ItemAdapter(this@MainActivity, list)
        recyclerView?.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView?.adapter = adapter
    }

}