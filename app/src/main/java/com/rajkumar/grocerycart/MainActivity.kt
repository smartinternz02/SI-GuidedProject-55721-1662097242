package com.rajkumar.grocerycart

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class  MainActivity : AppCompatActivity(), GroceryRVAdapter.GroceryItemClickInterface{
    lateinit var itemsRV: RecyclerView
    lateinit var addFAB: FloatingActionButton
    lateinit var list: List<GroceryItems>
    lateinit var groceryRVAdapter: GroceryRVAdapter
    lateinit var groceryViewModel: GroceryViewModel




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        itemsRV = findViewById(R.id.idRVItems)
        addFAB = findViewById(R.id.idFABAdd)
        list = ArrayList<GroceryItems>()
        groceryRVAdapter = GroceryRVAdapter(list,this)
        itemsRV.layoutManager = LinearLayoutManager(this)
        itemsRV.adapter = groceryRVAdapter
        val groceryRepository = GroceryRepository(GroceryDatabase(this))
        val factory = GroceryViewModelFactory(groceryRepository)
        groceryViewModel = ViewModelProvider(this,factory).get(GroceryViewModel::class.java)
        groceryViewModel.getAllGroceryItems().observe(this, Observer {
            groceryRVAdapter.list = it
            groceryRVAdapter.notifyDataSetChanged()
        })


        addFAB.setOnClickListener {
            openDialog()
        }
    }


    fun openDialog(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.grocery_add_dialog)
        val cancelbtn = dialog.findViewById<AppCompatButton>(R.id.idBtnCancel)
        val addbtn = dialog.findViewById<AppCompatButton>(R.id.idBtnAdd)
        val itemEdt = dialog.findViewById<EditText>(R.id.idEDtItemName)
        val itemPriceEdt = dialog.findViewById<EditText>(R.id.idEDtItemPrice)
        val itemQuantityEdt = dialog.findViewById<EditText>(R.id.idTVQuantity)
        cancelbtn.setOnClickListener {
            dialog.dismiss()
        }
        addbtn.setOnClickListener {
            val itemname:String = itemEdt.text.toString()
            val itemprice:String = itemPriceEdt.text.toString()
            val itemquantity:String = itemQuantityEdt.text.toString()
            val qty : Double = itemquantity.toDouble()
            val pr : Double = itemprice.toDouble()
            if (itemname.isNotEmpty() && itemprice.isNotEmpty() && itemquantity.isNotEmpty()){
                val items = GroceryItems(itemname,qty,pr)
                groceryViewModel.insert(items)
                Toast.makeText(applicationContext,"Item Added",Toast.LENGTH_SHORT).show()
                groceryRVAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }
            else{
                Toast.makeText(applicationContext,"Please fill all details properly",Toast.LENGTH_SHORT).show()
            }

        }
        dialog.show()
    }




    override fun onItemClick(groceryItems: GroceryItems) {
        groceryViewModel.delete(groceryItems)
        groceryRVAdapter.notifyDataSetChanged()
        Toast.makeText(applicationContext,"Item Deleted Successfully.", Toast.LENGTH_SHORT).show()
    }
}
