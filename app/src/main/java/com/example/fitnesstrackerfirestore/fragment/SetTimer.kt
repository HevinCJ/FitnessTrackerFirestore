package com.example.fitnesstrackerfirestore.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.fitnesstrackerfirestore.R
import com.example.fitnesstrackerfirestore.databinding.FragmentSetTimerBinding
import com.example.fitnesstrackerfirestore.models.TimerParameter

class SetTimer : Fragment() {

    private var settimer:FragmentSetTimerBinding?=null
    private val binding get() = settimer!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       settimer = FragmentSetTimerBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFromUi()
        setCancleBtn()
    }

    private fun setCancleBtn() {
        binding.apply {
            cnclebtn.setOnClickListener {
                findNavController().navigate(R.id.action_setTimer_to_home)
            }
        }
    }

    private fun getDataFromUi() {
       binding.startbtn.setOnClickListener {
           binding.apply {
               val exercisetime = editTexttime.text.toString()
               val noOfExercise = edttexttotalno.text.toString()

               if(exercisetime.isNotEmpty() && noOfExercise.isNotEmpty()){
                   try {

                       val time = exercisetime.toInt()
                       val exerciseno = noOfExercise.toInt()

                       val timer = TimerParameter(time,exerciseno)
                       val action = SetTimerDirections.actionSetTimerToStartTimer(timer)
                       findNavController().navigate(action)
                   }catch (e:NumberFormatException){
                       Toast.makeText(requireContext(),e.message,Toast.LENGTH_SHORT).show()
                   }
               }else{
                   Toast.makeText(requireContext(),"Please Fill required fields",Toast.LENGTH_SHORT).show()
               }
           }
       }
    }

}