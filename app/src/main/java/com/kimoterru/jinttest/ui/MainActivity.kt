 package com.kimoterru.jinttest.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kimoterru.jinttest.data.local.model.BinEntity
import com.kimoterru.jinttest.data.remote.model.BinResponse
import com.kimoterru.jinttest.databinding.ActivityMainBinding
import com.kimoterru.jinttest.ui.util.BinClickInterface
import com.kimoterru.jinttest.ui.util.Status.*

 class MainActivity : AppCompatActivity(), BinClickInterface {

     private val binding: ActivityMainBinding by viewBinding(CreateMethod.INFLATE)
     private val viewModel: MainViewModel by viewModels()

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setBin()
        initObserve()
     }

     private fun setBin() {
         binding.searchBin.setOnEditorActionListener { _, actionId, _ ->
             if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                 val query = binding.searchBin.text.trim().toString()
                 if (query.isNotEmpty()) {
                     viewModel.getBINFromBank(query.toInt())
                     viewModel.insertBinInDB(query.toLong())
                 } else {
                     Toast.makeText(this, "empty!", Toast.LENGTH_SHORT).show()
                 }
                 true
             } else false
         }
     }

     private fun showViews() = with(binding) {
         progressBar.visibility = View.GONE
         binLayout.visibility = View.VISIBLE
     }

     private fun hideViews() = with(binding) {
         progressBar.visibility = View.GONE
         binLayout.visibility = View.VISIBLE
     }

     private fun initObserve() {
         viewModel.binRemoteLiveData.observe(this) {
             when(it.status) {
                 SUCCESS -> {
                     showViews()
                     it.data?.let { it1 -> displayBin(it1) }
                 }
                 ERROR -> {
                     hideViews()
                     Log.d("errorLogD", it.message!!)
                 }
                 LOADING -> {
                     binding.progressBar.visibility = View.VISIBLE
                 }
             }
         }
         viewModel.binLocalLiveData.observe(this) {
             displayRV(it)
         }
     }

     private fun displayRV(data: List<BinEntity>) {
         binding.rvBin.apply {
             adapter = BinAdapter(data,this@MainActivity)
         }
     }

     override fun clickOnBinItem(binNumber: Int) {
         viewModel.getBinFromDb(binNumber)
         viewModel.mutableBinNumber.observe(this) {
             viewModel.getBINFromBank(it)
         }
     }

     @SuppressLint("SetTextI18n")
     private fun displayBin(data: BinResponse) = with(binding) {
         scheme.text = "scheme: ${data.scheme}"
         type.text = "type: ${data.type}"
         brand.text = "brand: ${data.brand}"
         prepaid.text = "prepaid: ${data.prepaid}"
         locationDetail.text = data.country?.name + "," + " " + data.bank?.city + " " + data.country?.emoji + " " + data.country?.numeric + "," + " currency - " + data.country?.currency
         bankName.text = data.bank?.name

         bankUrl.apply {
             text = data.bank?.url + " " + "clickable"
             setOnClickListener {
                 val intent = Intent(Intent.ACTION_VIEW)
                 intent.data = Uri.parse("http:" + data.bank?.url.toString())
                 startActivity(intent)
                 Toast.makeText(this@MainActivity, data.bank?.url, Toast.LENGTH_SHORT).show()
             }
         }

         bankPhone.apply {
             text = data.bank?.phone + " " + "clickable"
             setOnClickListener {
                 val intent = Intent(Intent.ACTION_DIAL)
                 intent.data = Uri.parse("tel:" + data.bank?.phone)
                 startActivity(intent)
             }
         }
     }

 }