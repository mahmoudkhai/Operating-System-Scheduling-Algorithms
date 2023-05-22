package com.example.operatingsystem

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.operatingsystem.databinding.FragmentRoundRobinBinding
import com.github.vipulasri.timelineview.TimelineView
import java.util.*

class RoundRobinFragment : Fragment() {
    private lateinit var binding: FragmentRoundRobinBinding
    private val taskList:MutableList<Task> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRoundRobinBinding.inflate(inflater, container, false)
        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.enterAnotherTaskBtn.setOnClickListener {
            taskList.add(getTask())
            clearInputs()
        }
        binding.apply {
            roundRobin.setOnClickListener {  }
            edf.setOnClickListener {  }
            firstCFS.setOnClickListener {  }
            minumumLaxity.setOnClickListener {  }
        }
        binding.clear.setOnClickListener {
            clearInputs()
        }
    }

    private fun clearInputs() {
        binding.firstPeriod.text.clear()
        binding.executionTime.text.clear()
        binding.deadline.text.clear()
        binding.taskName.text.clear()
        binding.firstReleaseTime.text.clear()
    }

    private fun getTask():Task {
        val taskReleaseTime = binding.firstReleaseTime.toString().toInt()
        val taskName = binding.firstReleaseTime.toString()
        val firstDeadline = binding.deadline.toString().toInt()
        val firstPeriod = binding.firstPeriod.toString().toInt()
        val firstExecutionTime = binding.executionTime.toString().toInt()
        return Task (taskName , taskReleaseTime , firstPeriod , firstExecutionTime , firstDeadline)

    }

}