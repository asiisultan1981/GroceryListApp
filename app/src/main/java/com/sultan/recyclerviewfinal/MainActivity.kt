package com.sultan.recyclerviewfinal

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.sultan.recyclerviewfinal.databinding.ActivityMainBinding
import com.sultan.recyclerviewfinal.room.ItemDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ItemAdapter
    private var selectedType: String = "pulses" // default
    private var mainList = mutableListOf<Item>()
    private var selectedList = mutableListOf<Item>()
    private var wishList = mutableListOf<Item>()
    private lateinit var clickListenerSelected: ClickListener
    private lateinit var clickListenerMain: ClickListener
    private lateinit var toggle: ActionBarDrawerToggle



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initUI()

        mainRecyclerView()

        binding.buttonAddItems.setOnClickListener {
            addNewItem()
        }



        binding.customToolbar.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })

        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.customToolbar.navHamburger.setOnClickListener {
            openOrCloseDrawer()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_pulses -> {
                    selectedType = "pulses"
                    filterList("")
                    openOrCloseDrawer()

                }
                R.id.nav_baking -> {
                    selectedType = "baking"
                    filterList("")
                    openOrCloseDrawer()
                }
                R.id.nav_grains -> {
                    selectedType = "grains"
                    filterList("")
                    openOrCloseDrawer()

                }
                R.id.nav_produce -> {
                    selectedType = "produce"
                    filterList("")
                    openOrCloseDrawer()

                }
                R.id.nav_bakery -> {
                    selectedType = "bakery"
                    filterList("")
                    openOrCloseDrawer()
                }
                R.id.nav_saucesOils -> {
                    selectedType = "saucesoils"
                    filterList("")
                    openOrCloseDrawer()
                }
                R.id.nav_snacks -> {
                    selectedType = "snacks"
                    filterList("")
                    openOrCloseDrawer()
                }
                R.id.nav_condiments -> {
                    selectedType = "condiments"
                    filterList("")
                    openOrCloseDrawer()
                }
                R.id.nav_spices -> {
                    selectedType = "spices"
                    filterList("")
                    openOrCloseDrawer()
                }
                R.id.nav_baby -> {
                    selectedType = "baby"
                    filterList("")
                    openOrCloseDrawer()

                }
                R.id.nav_personal -> {
                    selectedType = "personal"
                    filterList("")
                    openOrCloseDrawer()
                }
                R.id.nav_household -> {
                    selectedType = "household"
                    filterList("")
                    openOrCloseDrawer()
                }
            }
            true
        }

        filterList("")

        binding.buttonShowCheckedItems.setOnClickListener {
            mainList.forEach {
                if (it.isSelected) {
                    if (!selectedList.contains(it)) {
                        selectedList.add(it)
                    }

                }
            }
            adapter.notifyDataSetChanged()
        }

    }

    private fun filterList(text: String?) {
        val filteredList = mutableListOf<Item>()
        if (text == null || text == "") {
            mainList.forEach {
                if (it.type == selectedType) {
                    filteredList.add(it)
                }
            }
        } else {
            mainList.forEach {
                if (it.type == selectedType && it.name.lowercase().contains(text)) {
                    filteredList.add(it)
                }
            }
        }
        adapter.setMyList(filteredList)
        if (filteredList.isEmpty()) {
            Log.e("click", "filterList: No Data Found")
            Toast.makeText(this@MainActivity, "No items added yet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openOrCloseDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun addNewItem() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val dialogView: View = layoutInflater.inflate(R.layout.add_item_dialog, null)
        builder.setView(dialogView)
        val dialog: AlertDialog = builder.create()
        val itemName = dialogView.findViewById<EditText>(R.id.etName)
        val itemRate = dialogView.findViewById<EditText>(R.id.etRate)
        val itemQuantity = dialogView.findViewById<EditText>(R.id.etQuantity)
        val btnSaveItem = dialogView.findViewById<Button>(R.id.BtnSaveItem)
        val btnCancel = dialogView.findViewById<Button>(R.id.BtnCancel)
        btnSaveItem?.setOnClickListener {
            Log.e("d", "addNewItem: clicked" )
            val name = itemName?.text.toString()
            val rateText = itemRate?.text.toString()
            val quantityText = itemQuantity?.text.toString()

            if (rateText.isNotEmpty() && quantityText.isNotEmpty()) {
                val rate = rateText.toDouble()
                val quantity = quantityText.toDouble()
                saveData(name, selectedType, rate, quantity)
                filterList("")
                Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Please enter valid values.", Toast.LENGTH_LONG).show()
            }
        }

        btnCancel?.setOnClickListener {
            Log.e("d", "addNewItem: clicked" )
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()
    }

    private fun editOrDeleteDialog(position: Int) {
      showCustomDialog(
          R.style.RoundedCornersDialog,
          R.layout.edit_delete_item_dialog,
          R.id.btnEdit,
          R.id.btnDelete,
          {openEditDialog(position)},
          {deleteItemAtPosition(position)
          adapter.setMyList(mainList)}
      )
    }

    private fun openEditDialog(position: Int) {
        val alertDialog = Dialog(this)
        alertDialog.setCancelable(false)
        alertDialog.setContentView(R.layout.edit_item_dialog)
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        alertDialog.window!!.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
        // Initialize and set values to the EditText fields based on the item at the specified position
        // Set up save and cancel buttons similar to your addNewItem function
        val item = adapter.getMyList().get(position)

        val itemName = alertDialog.findViewById<EditText>(R.id.editName)
        val itemRate = alertDialog.findViewById<EditText>(R.id.editRate)
        val itemQuantity = alertDialog.findViewById<EditText>(R.id.editQuantity)
        val btnSaveItem = alertDialog.findViewById<Button>(R.id.editBtnSaveItem)
        val btnCancel = alertDialog.findViewById<Button>(R.id.editBtnCancel)
        itemName.setText(item.name)
        itemRate.setText(item.rate.toString())
        itemQuantity.setText(item.quantity.toString())

        btnSaveItem.setOnClickListener {
            val newName = itemName.text.toString()
            val newRate = itemRate.text.toString()
            val newQuantity = itemQuantity.text.toString()

            if (newRate.isNotEmpty() && newQuantity.isNotEmpty()) {
                val rate = newRate.toDouble()
                val quantity = newQuantity.toDouble()

                updateItemData(position, newName, rate, quantity)
                filterList("")
                Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show()
                alertDialog.dismiss()
            } else {
                Toast.makeText(this, "Please enter valid values.", Toast.LENGTH_LONG).show()
            }
        }

        btnCancel.setOnClickListener {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            alertDialog.dismiss()
        }
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

    private fun deleteItemAtPosition(position: Int) {
        Log.e("remove", "Before Delete: ${mainList.size}, ${selectedList.size}")
        // Remove the item from mainList at the specified position
        if (position in 0 until mainList.size) {
            mainList.removeAt(position)
            adapter.notifyItemRemoved(position)
            Log.e("remove", "after delete: ${mainList.size}, ${selectedList.size}")
            toast("deleted", Toast.LENGTH_SHORT)
        }
    }

    private fun updateItemData(
        position: Int,
        newName: String,
        newRate: Double,
        newQuantity: Double
    ) {
        val item = adapter.getMyList()[position]
        item.name = newName
        item.rate = newRate
        item.quantity = newQuantity
        // Notify the adapter of the data change
        adapter.notifyItemChanged(position)


    }

    private fun saveData(name: String, type: String, rate: Double, quantity: Double) {

        CoroutineScope(Dispatchers.IO).launch {
            val newItem = Item(name, type, rate, quantity)
            ItemDatabase.getInstance(this@MainActivity).itemDao.insertItem(newItem)
           mainList.add(newItem)

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {

            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initUI() {

        binding.customToolbar.btnCart.setOnClickListener {
            Log.e("click", "initUI: you clicked: CART Button")
            Toast.makeText(
                this, "CART Button is clicked",
                Toast.LENGTH_LONG
            ).show()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mainRecyclerView()
            }
        })


    }

    private fun selectedRecyclerView() {
        clickListenerSelected = object : ClickListener {
            override fun onCheckBoxClick(position: Int) {
                onClickSelected(position)
            }

            override fun onCbHeartClick(position: Int) {
                onWishSelected(position)
            }

            override fun onItemViewClick(position: Int) {
                TODO("Not yet implemented")
            }

        }
        adapter = ItemAdapter(this, clickListenerSelected, selectedList)
        binding.content.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.content.recyclerView.adapter = adapter

    }

    private fun onWishSelected(position: Int) {
        val item = adapter.getMyList().get(position)
        if (item.isWished) {
            item.isWished = false
            adapter.notifyDataSetChanged()
        } else {
            item.isWished = true
            adapter.notifyDataSetChanged()
        }
    }

    private fun mainRecyclerView() {
        Log.e("wish", "mainRecyclerView: now showing main list ")
        clickListenerMain = object : ClickListener {
            override fun onCheckBoxClick(position: Int) {
                onClickMain(position)
            }

            override fun onCbHeartClick(position: Int) {
                onWishMain(position)
            }

            override fun onItemViewClick(position: Int) {
                onClickMain(position)
            }

        }
        CoroutineScope(Dispatchers.IO).launch {
            mainList = ItemDatabase.getInstance(this@MainActivity).itemDao.showALlItems() as MutableList<Item>



        }
        adapter = ItemAdapter(this@MainActivity, clickListenerMain, mainList)
        binding.content.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.content.recyclerView.adapter = adapter
    }

    private fun onWishMain(position: Int) {
        val item = adapter.getMyList().get(position)
        if (item.isWished) {
            item.isWished = false
            adapter.notifyDataSetChanged()
            selectedList.remove(item)
            Log.e("wish", "onWishMain: selectedList size:${selectedList.size} ")
            adapter.notifyItemRemoved(position)

        } else {
            item.isWished = true
            adapter.notifyDataSetChanged()
            if (!selectedList.contains(item)) {
                selectedList.add(item)

                Log.e("color", "initUI: color has changed")
                Log.e("wish", "onWishMain: selectedList size:${selectedList.size} ")
                adapter.notifyItemInserted(position)
            } else {
                Toast.makeText(this, "Already added to wishlist..", Toast.LENGTH_SHORT).show()
            }

        }
        if (selectedList.size > 0) {
            Log.e("color", "onWishMain:${selectedList.size} ")
            binding.customToolbar.ivWish.setColorFilter(Color.WHITE)
        } else {
            Log.e("color", "onWishMain:${selectedList.size} ")
            binding.customToolbar.ivWish.setColorFilter(Color.RED)
        }
    }

    private fun onClickMain(position: Int) {
        val item = adapter.getMyList()[position]
        if (item.isSelected) {
            item.isSelected = false


            adapter.notifyDataSetChanged()
            Log.e("t", "Unchecked: selectedList size: ${selectedList.size} ")
            Log.e("t", "Unchecked: mainList size: ${selectedList.size} ")
            Log.e("t", "Unchecked: wishList size: ${wishList.size} ")
        } else {
            item.isSelected = true
            editOrDeleteDialog(position)
            adapter.notifyDataSetChanged()

        }


    }

    private fun onClickSelected(position: Int) {
        val item = adapter.getMyList().get(position)
        if (item.isSelected) {
            item.isSelected = false

            adapter.setMyList(selectedList)
            Log.e("t", "Unchecked: wishList size: ${wishList.size} ")
            Log.e("t", "Unchecked: selectedList size: ${selectedList.size} ")
            Log.e("t", "Unchecked: mainList size: ${mainList.size} ")


        } else {
            item.isSelected = true
            adapter.setMyList(selectedList)
            Log.e("t", "checked: wishList size: ${wishList.size} ")
            Log.e("t", "checked: selectedList size: ${selectedList.size} ")
            Log.e("t", "checked: mainList size: ${mainList.size} ")

        }

    }


}