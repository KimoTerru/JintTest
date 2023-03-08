package com.kimoterru.jinttest.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kimoterru.jinttest.data.local.model.BinEntity
import com.kimoterru.jinttest.databinding.BinItemBinding
import com.kimoterru.jinttest.ui.util.BinClickInterface

class BinAdapter(
    private val listBin: List<BinEntity>,
    private val listener: BinClickInterface
): RecyclerView.Adapter<BinAdapter.BinViewHolder>() {

    inner class BinViewHolder(
        private val binding: BinItemBinding,
        private val listener: BinClickInterface
    ): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(binData: BinEntity) {
            with(binding) {
                itemView.setOnClickListener {
                    binData.id?.let { it1 -> listener.clickOnBinItem(it1) }
                }
                idItem.text = "request - " + binData.id
                binNumberView.text = binData.binNumber.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BinViewHolder {
        return BinViewHolder(
            BinItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), listener
        )
    }

    override fun onBindViewHolder(holder: BinViewHolder, position: Int) {
        holder.bind(listBin[position])
    }

    override fun getItemCount(): Int = listBin.size
}
