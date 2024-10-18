package com.example.fitnesstrackerfirestore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.fitnesstrackerfirestore.databinding.FirebasedataHomeRcviewBinding
import com.example.fitnesstrackerfirestore.models.Timer

class HomeAdapter:RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    private var timerdata:List<Timer> = emptyList()

    class HomeViewHolder(private val binding:FirebasedataHomeRcviewBinding):ViewHolder(binding.root){

        fun bindTimer(timer: Timer){
            binding.apply {
               txtviewcurrent.text = timer.currentcycles.toString()
                txtviewtotal.text = timer.totalcycles.toString()
                txtviewtimestamp.text = timer.timestamp
                txtviewtotaltime.text = timer.totaltime.toString()
                timerdatabind =timer
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(FirebasedataHomeRcviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return timerdata.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val currentdata = timerdata[position]
        holder.bindTimer(currentdata)
    }

    fun setTimer(timer: List<Timer>){
        this.timerdata = timer
        notifyDataSetChanged()

    }

}