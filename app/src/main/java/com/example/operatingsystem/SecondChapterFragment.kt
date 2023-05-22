package com.example.operatingsystem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.operatingsystem.databinding.FragmentSecondChapterBinding

class SecondChapterFragment : Fragment() {

    private lateinit var binding: FragmentSecondChapterBinding
    private lateinit var navController: NavController
    private val requestsList = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSecondChapterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = NavHostFragment.findNavController(this)
        var startingTrack = binding.startingTrack.toString()
        var first = binding.first.toString()
        var second = binding.second.toString()
        var third = binding.third.toString()
        var fourth = binding.fourth.toString()
        var fifth = binding.fifth.toString()
        var sixth = binding.sixth.toString()
        var seventh = binding.seven.toString()
        var eight = binding.eight.toString()
        var nine = binding.nine.toString()
        if (first.isNotEmpty()) requestsList.add(first.toInt())
        if (second.isNotEmpty()) requestsList.add(second.toInt())
        if (third.isNotEmpty()) requestsList.add(third.toInt())
        if (fourth.isNotEmpty()) requestsList.add(fourth.toInt())
        if (fifth.isNotEmpty()) requestsList.add(fifth.toInt())
        if (sixth.isNotEmpty()) requestsList.add(sixth.toInt())
        if (seventh.isNotEmpty()) requestsList.add(seventh.toInt())
        if (eight.isNotEmpty()) requestsList.add(eight.toInt())
        if (nine.isNotEmpty()) requestsList.add(nine.toInt())
        if (startingTrack.isEmpty())
            Toast.makeText(requireContext(), "Enter Starting Track", Toast.LENGTH_LONG).show()
        else if (requestsList.isEmpty())
            Toast.makeText(requireContext(), "Enter 1 place at least", Toast.LENGTH_LONG).show()
        else {
            binding.fcfs.setOnClickListener {
                binding.output.text =
                    firstComeFirstServe(startingTrack.toInt(), requestsList.toIntArray())
            }
            binding.SSTF.setOnClickListener {
                binding.output.text =
                    shortestSeekTime(startingTrack.toInt(), requestsList.toIntArray())
            }
            binding.nstep.setOnClickListener {
                Toast.makeText(requireContext(), "Enter value for n", Toast.LENGTH_LONG).show()
                binding.enterN.visibility = View.VISIBLE
                binding.nValue.visibility = View.VISIBLE
                binding.output.text = nStepScanAlgorithm(
                    requestsList.toList(),
                    startingTrack.toInt(),
                    binding.nValue.toString().toInt()
                )
            }
            binding.cScan.setOnClickListener {
                binding.output.text = cScanAlgorithm(requestsList.toList(), startingTrack.toInt())
            }
            binding.cLook.setOnClickListener {
                binding.output.text = cLookAlgorithm(requestsList.toList(), startingTrack.toInt())
            }
            binding.LOOK.setOnClickListener {
                binding.output.text =
                    scanLook(headPosition = startingTrack.toInt(), requestsList.toIntArray())
            }

            binding.compute.setOnClickListener {
                clearInputs()
            }
        }
    }

    private fun clearInputs() {
        binding.apply {
            first.text.clear()
            second.text.clear()
            third.text.clear()
            fourth.text.clear()
            fifth.text.clear()
            sixth.text.clear()
            seven.text.clear()
            eight.text.clear()
            nine.text.clear()
            startingTrack.text.clear()
        }
    }

    fun scanLook(headPosition: Int, requests: IntArray): String {
        var totalSeekTime = 0
        var currentPosition = headPosition
        val remainingRequests = requests.copyOf()
        remainingRequests.sort()
        var index = remainingRequests.binarySearch(currentPosition)
        if (index < 0) {
            index = -index - 1
        }
        var direction = 1
        val trackList = mutableListOf<Int>()
        while (index < remainingRequests.size || index >= 0) {
            if (direction == 1) {
                if (index < remainingRequests.size) {
                    val tracksTraveled = kotlin.math.abs(currentPosition - remainingRequests[index])
                    totalSeekTime += tracksTraveled
                    currentPosition = remainingRequests[index]
                    index++
                    trackList.add(tracksTraveled)
                } else {
                    direction = -1
                    index--
                }
            } else {
                if (index >= 0) {
                    val tracksTraveled = kotlin.math.abs(currentPosition - remainingRequests[index])
                    totalSeekTime += tracksTraveled
                    currentPosition = remainingRequests[index]
                    index--
                    trackList.add(tracksTraveled)
                } else {
                    break
                }
            }
        }

        val averageTracksTraveled = trackList.average()
        val trackInfo = trackList.joinToString(separator = "\n") {
            "Traveled tracks for Request ${
                trackList.indexOf(it) + 1
            }: $it"
        }
        return "$trackInfo\nTotal seek time: $totalSeekTime\nAverage number of tracks traveled: %.2f".format(
            averageTracksTraveled
        )
    }

    fun cLookAlgorithm(requests: List<Int>, startTrack: Int): String {
        val sortedRequests = requests.sorted()
        var totalSeekTime = 0
        var currentTrack = startTrack
        var tracksTraveled = mutableListOf<Int>()

        while (sortedRequests.isNotEmpty()) {
            val next = sortedRequests.find { it >= currentTrack }
                ?: sortedRequests.first()
            totalSeekTime += next - currentTrack
            tracksTraveled.add(next)
            sortedRequests.drop(next)
            currentTrack = next
        }

        val avgTracksTraveled = tracksTraveled.average()

        return buildString {
            for ((index, track) in tracksTraveled.withIndex()) {
                appendLine("Traveled tracks for Request ${index + 1}: $track")
            }
            appendLine("Total Seek Time: $totalSeekTime")
            appendLine("Average Tracks Traveled: $avgTracksTraveled")
        }
    }

    fun cScanAlgorithm(requests: List<Int>, startTrack: Int): String {
        val sortedRequests = requests.sorted()
        var totalSeekTime = 0
        var currentTrack = startTrack
        var tracksTraveled = mutableListOf<Int>()
        var direction = 1 // 1 for towards higher tracks, -1 for towards lower tracks
        var index = 1

        while (sortedRequests.isNotEmpty()) {
            if (direction == 1) {
                val next: Int = sortedRequests.find { it >= currentTrack }
                    ?: sortedRequests.maxOrNull()!!
                totalSeekTime += next - currentTrack
                tracksTraveled.add(next)
                sortedRequests.drop(next)
                currentTrack = next
                if (sortedRequests.isEmpty() || currentTrack == sortedRequests.last()) {
                    totalSeekTime += currentTrack
                    tracksTraveled.add(0)
                    currentTrack = 0
                    direction = -1
                }
            } else {
                val next = sortedRequests.findLast { it <= currentTrack }
                    ?: sortedRequests.minOrNull()!!
                totalSeekTime += currentTrack - next
                tracksTraveled.add(next)
                sortedRequests.drop(next)
                currentTrack = next
                if (sortedRequests.isEmpty() || currentTrack == sortedRequests.first()) {
                    totalSeekTime += 9999 - currentTrack
                    tracksTraveled.add(9999)
                    currentTrack = 9999
                    direction = 1
                }
            }
        }

        val avgTracksTraveled = tracksTraveled.average()

        return buildString {
            for (track in tracksTraveled) {
                appendLine("Traveled tracks for Request $index: $track")
                index++
            }
            appendLine("Total Seek Time: $totalSeekTime")
            appendLine("Average Tracks Traveled: $avgTracksTraveled")
        }
    }

    fun nStepScanAlgorithm(requests: List<Int>, startTrack: Int, n: Int): String {
        var totalSeekTime = 0
        var currentPosition = startTrack
        val tracksTraveled = mutableListOf<String>()

        // Sort the requests in ascending order
        val sortedRequests = requests.sorted()

        // Find the first request that is greater than or equal to the current position
        val startIndex = sortedRequests.indexOfFirst { it >= currentPosition }

        // Scan towards the higher numbered tracks
        for (i in startIndex until sortedRequests.size) {
            val track = sortedRequests[i]
            if (track - currentPosition <= n) {
                // Add the distance traveled to the total seek time
                totalSeekTime += track - currentPosition

                // Add the track to the list of tracks traveled
                tracksTraveled.add("$currentPosition->$track")

                // Update the current position
                currentPosition = track
            }
        }

        // If there are still requests remaining, scan towards the lower numbered tracks
        if (startIndex != 0) {
            for (i in startIndex - 1 downTo 0) {
                val track = sortedRequests[i]
                if (currentPosition - track <= n) {
                    // Add the distance traveled to the total seek time
                    totalSeekTime += currentPosition - track

                    // Add the track to the list of tracks traveled
                    tracksTraveled.add("$currentPosition->$track")

                    // Update the current position
                    currentPosition = track
                }
            }
        }

        // Calculate the average number of tracks traveled per request
        val avgTracksTraveled = if (requests.isNotEmpty()) {
            tracksTraveled.size.toDouble() / requests.size.toDouble()
        } else {
            0.0
        }

        // Return the result as a formatted string
        return "${tracksTraveled.joinToString(", ")}\nTotal seek time: $totalSeekTime\nAverage tracks traveled per request: $avgTracksTraveled"
    }

    fun shortestSeekTime(headPosition: Int, requests: IntArray): String {
        var currentPosition = headPosition
        var totalSeekTime = 0
        var numTracksTraveled = 0
        val trackList = mutableListOf<Int>()

        // Create a copy of the original array to avoid modifying it
        val remainingRequests = requests.copyOf()

        while (remainingRequests.any { it != -1 }) {
            var shortestIndex = -1
            var shortestDistance = Integer.MAX_VALUE

            for (i in remainingRequests.indices) {
                if (remainingRequests[i] != -1) {
                    val distance = kotlin.math.abs(remainingRequests[i] - currentPosition)
                    if (distance < shortestDistance) {
                        shortestDistance = distance
                        shortestIndex = i
                    }
                }
            }

            if (shortestIndex != -1) {
                val tracksTraveled =
                    kotlin.math.abs(currentPosition - remainingRequests[shortestIndex])
                numTracksTraveled += tracksTraveled
                totalSeekTime += tracksTraveled
                currentPosition = remainingRequests[shortestIndex]
                trackList.add(tracksTraveled)
                remainingRequests[shortestIndex] = -1
            }
        }

        val averageTracksTraveled = numTracksTraveled.toFloat() / requests.size
        val trackInfo = trackList.joinToString(separator = "\n") {
            "Traveled tracks for Request ${
                trackList.indexOf(it) + 1
            }: $it"
        }

        return "$trackInfo\nTotal seek time: $totalSeekTime\nAverage number of tracks traveled: $averageTracksTraveled"
    }

    fun firstComeFirstServe(headPosition: Int, requests: IntArray): String {
        var totalSeekTime = 0
        var currentPosition = headPosition
        val result = StringBuilder()
        for (request in requests) {
            val traveledTracks = Math.abs(request - currentPosition)
            result.append("Traveled tracks for request $request: $traveledTracks\n")
            totalSeekTime += traveledTracks
            currentPosition = request
        }
        val averageTracks = totalSeekTime.toDouble() / requests.size
        result.append("Total seek time: $totalSeekTime\n")
        result.append("Average number of tracks traveled: ${String.format("%.2f", averageTracks)}")
        return result.toString()
    }

}