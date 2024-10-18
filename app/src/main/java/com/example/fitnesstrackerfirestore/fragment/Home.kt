    package com.example.fitnesstrackerfirestore.fragment

    import android.os.Bundle
    import android.util.Log
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.fragment.app.Fragment
    import androidx.fragment.app.viewModels
    import androidx.lifecycle.lifecycleScope
    import androidx.lifecycle.viewModelScope
    import androidx.navigation.fragment.findNavController
    import androidx.recyclerview.widget.LinearLayoutManager
    import com.example.fitnesstrackerfirestore.R
    import com.example.fitnesstrackerfirestore.adapter.HomeAdapter
    import com.example.fitnesstrackerfirestore.databinding.FragmentHomeBinding
    import com.example.fitnesstrackerfirestore.result.FirestoreResult
    import com.example.fitnesstrackerfirestore.viewmodel.HomeViewModel
    import kotlinx.coroutines.flow.collectLatest
    import kotlinx.coroutines.launch

    class Home : Fragment() {

        private var home:FragmentHomeBinding?=null
        private val binding get() = home!!

        private val homeViewModel:HomeViewModel by viewModels()
        private val homeadapter:HomeAdapter by lazy { HomeAdapter() }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
           home = FragmentHomeBinding.inflate(inflater,container,false)
            homeViewModel.fetchDataFromFireStore()
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            actionToSetTimer()
            observeFetchedData()
            setHomeAdapter()
        }

        private fun setHomeAdapter() {
            binding.apply {
                recyclerView.adapter = homeadapter
                recyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            }
        }

        private fun observeFetchedData() {
            lifecycleScope.launch {
                homeViewModel.fetcheddata.collectLatest {result->
                    when(result){
                        is FirestoreResult.Error -> {

                        }
                        is FirestoreResult.Loading -> {

                        }
                        is FirestoreResult.Success -> {
                            result.data?.let {
                                homeadapter.setTimer(it)
                                Log.d("timerlist",it.toString())
                            }
                        }
                    }

                }
            }
        }

        private fun actionToSetTimer() {
            binding.apply {
                actionToSettimerbtn.setOnClickListener {
                    findNavController().navigate(R.id.action_home_to_setTimer)
                }
            }
        }

    }