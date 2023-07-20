package com.example.evoting.ui.voting

import android.app.Activity
import android.os.Bundle
import com.db.williamchart.view.BarChartView
import com.example.evoting.R

class VotingActivity : Activity() {

    private val barSet = listOf(
        "Kandidat 1 (120)" to 120F,
        "Kandidat 2 (76)" to 76F,
        "Kandidat 3 (99)" to 99F,
    )

    private val animationDuration = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voting)

        val barChart: BarChartView = findViewById(R.id.barChart)
        barChart.animation.duration = animationDuration
        barChart.animate(barSet)



    }
}