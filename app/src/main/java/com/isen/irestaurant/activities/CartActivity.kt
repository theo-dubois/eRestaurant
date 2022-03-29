package com.isen.irestaurant.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.isen.irestaurant.databinding.ActivityCartBinding
import com.google.gson.Gson
import com.isen.irestaurant.R
import com.isen.irestaurant.adapter.BleAdapter
import com.isen.irestaurant.adapter.CartAdapter
import com.isen.irestaurant.objects.CartData
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class CartActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        affichepanier()

        binding.button2.setOnClickListener{
            create(this,"panier.json", "null")
            affichepanier()

        }
    }
    private fun affichepanier(){
        val maString = getFileString()

        if (maString!="null"){
            Log.d("Swag", maString)
            var items = chargePanier(maString)
            binding.recyclerViewCart.layoutManager = LinearLayoutManager(this)
            binding.recyclerViewCart.adapter = CartAdapter(items.data) {
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra(CategoryActivity.ITEM_KEY, it)

            }
        }else{
            binding.panierVideText.isVisible = true
            binding.recyclerViewCart.isVisible = false
            binding.button2.isVisible = false
        }
    }
    private fun chargePanier(jsonString: String): CartData {
            binding.panierVideText.isVisible = false
            val items = storeInData(jsonString)
            return items
    }
    private fun getFileString():String{
        val fos: FileInputStream = openFileInput("panier.json")
        val size: Int = fos.available()
        val buffer = ByteArray(size)
        fos.read(buffer)
        fos.close()
        return String(buffer, charset("UTF-8"))
    }
    private fun storeInData(jsonString: String):CartData{
        return Gson().fromJson(jsonString, CartData::class.java)
    }
    private fun create(context: Context, fileName: String, jsonString: String?): Boolean {
        val FILENAME = "panier.json"
        return try {
            val fos: FileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            if (jsonString != null) {
                fos.write(jsonString.toByteArray())
            }
            fos.close()
            true
        } catch (fileNotFound: FileNotFoundException) {
            false
        } catch (ioException: IOException) {
            false
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home,menu)
        Log.d("Panier",getFilesDir().getAbsolutePath())
        return super.onCreateOptionsMenu(menu)


    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        return super.onOptionsItemSelected(item)
    }

}