package com.sportsbaazi.bootstrap.models

data class Data(
    val age: Int,
    val battingStyle: BattingStyle,
    val born: String,
    val bowlingStyle: BowlingStyle,
    val fullName: String,
    val playingRole: List<String>
)