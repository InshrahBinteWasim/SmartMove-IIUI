package com.example.smartmoveiiui.data.model

import com.google.firebase.Timestamp

data class User(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "",
    val contactNumber: String = ""
)

data class Bus(
    val busId: String = "",
    val busNumber: String = "",
    val capacity: Int = 0,
    val driverId: String = "",
    val status: String = "Active",
    val gpsDeviceId: String = ""
)

data class Route(
    val routeId: String = "",
    val routeName: String = "",
    val stops: List<Stop> = emptyList()
)

data class Stop(
    val stopId: String = "",
    val stopName: String = "",
    val stopOrder: Int = 0
)

data class Schedule(
    val scheduleId: String = "",
    val busId: String = "",
    val routeId: String = "",
    val createdAt: Timestamp? = null,
    val stopTimings: Map<String, String> = emptyMap() // stopId to expectedTime
)

data class Query(
    val queryId: String = "",
    val commuterId: String = "",
    val description: String = "",
    val category: String = "General",
    val status: String = "Pending",
    val time: Timestamp? = null,
    val responses: List<QueryResponse> = emptyList()
)

data class QueryResponse(
    val responseId: String = "",
    val queryId: String = "",
    val staffId: String = "",
    val responseText: String = "",
    val responseTime: Timestamp? = null
)

data class Announcement(
    val announcementId: String = "",
    val staffId: String = "",
    val title: String = "",
    val description: String = "",
    val publishDate: Timestamp? = null,
    val isPublished: Boolean = true
)

data class BusLocation(
    val busId: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val timestamp: Timestamp? = null
)
