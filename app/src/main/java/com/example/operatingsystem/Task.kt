package com.example.operatingsystem

data class Task(
    var taskName:String,
    var releaseTime:Int,
    var period:Int,
    var executionTime:Int,
    var deadline:Int,
)