package com.example.fitnesstrackerfirestore.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fitnesstrackerfirestore.result.FirestoreResult
import com.example.fitnesstrackerfirestore.R
import com.example.fitnesstrackerfirestore.databinding.FragmentStartTimerBinding
import com.example.fitnesstrackerfirestore.viewmodel.StartTimerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StartTimer : Fragment() {

    private var startTimer:FragmentStartTimerBinding?=null
    private val binding get() = startTimer!!

    private val timerArgs by navArgs<StartTimerArgs>()

    private var currentCycle = 1
    private var totalCycle = 0
    private var totalTimeMillis:Long = 0;

    private val startTimerViewModel:StartTimerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       startTimer = FragmentStartTimerBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCountDownTimer(timerArgs)
        observeSaveDataToFirestore()
        setCancelBtn()
    }

    private fun setCancelBtn() {
        binding.apply {
            Cancelbtn.setOnClickListener {
               val alertDialog = AlertDialog.Builder(requireContext()).apply {
                   setTitle("Cancel Timer")
                   setMessage("Are you sure you want to cancel the timer?")

                   setPositiveButton("Yes"){dialogue,_ ->
                      saveDataToFireStore(currentCycle -1 ,false)
                       Toast.makeText(requireContext(), "Timer Cancelled", Toast.LENGTH_SHORT).show()
                       dialogue.dismiss()
                   }
                   setNegativeButton("No"){dialogue,_->
                       dialogue.dismiss()
                   }
               }.create()
                alertDialog.show()
            }
        }
    }

    private  fun observeSaveDataToFirestore() {
       lifecycleScope.launch {
           try {
               startTimerViewModel.savedData.collectLatest {result->
                   when(result){
                       is FirestoreResult.Error -> {
                           Toast.makeText(requireContext(),result.message,Toast.LENGTH_SHORT).show()
                       }
                       is FirestoreResult.Loading ->{

                       }
                       is FirestoreResult.Success -> {
                           findNavController().navigate(R.id.action_startTimer_to_home)
                           Toast.makeText(requireContext()," Exercise Saved ",Toast.LENGTH_SHORT).show()
                       }
                   }
               }
           }catch (e:Exception){

           }
       }
    }

    private fun setCountDownTimer(timerArgs: StartTimerArgs) {

        totalCycle = timerArgs.timerdata.noOfExercise
        totalTimeMillis = (timerArgs.timerdata.time * 1000).toLong()

        if (totalTimeMillis<=0) return

        binding.circularprogress.max = (totalTimeMillis/1000).toInt()
        startCountDownTimer(totalTimeMillis)
    }

    private fun startCountDownTimer(timeinmillis:Long) {
        val countdowntimer = object :CountDownTimer(timeinmillis,1000){

            override fun onTick(millisUntilFinished: Long) {

                binding.apply {
                    val secondsRemaining = millisUntilFinished / 1000
                    txtviewtimer.text = secondsRemaining.toString()
                    circularprogress.progress = secondsRemaining.toInt()
                    binding.txtviewnoofexercise.text = (totalCycle - currentCycle).toString()

                }
            }

            override fun onFinish() {
                currentCycle++
                if (currentCycle<=totalCycle){
                    startCountDownTimer(timeinmillis)
                }else{
                    binding.txtviewtimer.text = "Done!"
                    binding.circularprogress.progress = 0
                    saveDataToFireStore(currentCycle-1 ,true)
                }

            }

        }
        countdowntimer.start()
    }

    private fun saveDataToFireStore(currentcycle:Int,isCompleted:Boolean) {
        try {
            val timer = hashMapOf(
                "currentcycles" to currentcycle,
                "totalcycles" to totalCycle,
                "totaltime" to totalTimeMillis/1000,
                "timestamp" to SimpleDateFormat("hh:mm a",Locale.getDefault()).format(Date()),
                "iscompleted" to isCompleted
            )
            startTimerViewModel.saveDataToFireStore(timer)
        }catch (e:Exception){

        }
    }

}