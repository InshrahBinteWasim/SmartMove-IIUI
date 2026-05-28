package com.example.smartmoveiiui.data

import android.net.Uri
import com.example.smartmoveiiui.data.model.Announcement
import com.example.smartmoveiiui.data.model.AppNotification
import com.example.smartmoveiiui.data.model.AuditLog
import com.example.smartmoveiiui.data.model.Bus
import com.example.smartmoveiiui.data.model.BusLocation
import com.example.smartmoveiiui.data.model.FavoriteRoute
import com.example.smartmoveiiui.data.model.Feedback
import com.example.smartmoveiiui.data.model.Query
import com.example.smartmoveiiui.data.model.Route
import com.example.smartmoveiiui.data.model.Schedule
import com.example.smartmoveiiui.data.model.Stop
import com.example.smartmoveiiui.data.model.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage

data class DynamicRow(
    val title: String,
    val subtitle: String,
    val meta: String,
    val type: String,
)

object SmartMoveRepository {
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val storage: FirebaseStorage by lazy { FirebaseStorage.getInstance() }

    private fun userId(): String = auth.currentUser?.uid ?: "demo_user"

    fun observeRows(featureId: String, onRows: (List<DynamicRow>) -> Unit): ListenerRegistration? {
        val collection = collectionFor(featureId) ?: return null
        val query = when (collection) {
            "announcements", "queries", "notifications", "feedback", "audit_logs" ->
                db.collection(collection).limit(12)
            else -> db.collection(collection).limit(12)
        }

        return query.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) {
                onRows(emptyList())
                return@addSnapshotListener
            }

            val rows = snapshot.documents.mapNotNull { doc ->
                when (collection) {
                    "announcements" -> doc.toObject(Announcement::class.java)?.let {
                        DynamicRow(it.title.ifBlank { "Announcement" }, it.description, it.priority, "announcement")
                    }
                    "queries" -> doc.toObject(Query::class.java)?.let {
                        DynamicRow(it.queryId.ifBlank { doc.id }, "${it.category} - ${it.description}", it.status, "query")
                    }
                    "notifications" -> doc.toObject(AppNotification::class.java)?.let {
                        DynamicRow(it.title, it.body, if (it.isRead) "Seen" else "New", "notification")
                    }
                    "favorites" -> doc.toObject(FavoriteRoute::class.java)?.let {
                        DynamicRow(it.stopName, "${it.routeName} - ${it.busNumber}", "${it.etaMinutes} min", "favorite")
                    }
                    "feedback" -> doc.toObject(Feedback::class.java)?.let {
                        DynamicRow(it.busNumber, it.comment.ifBlank { it.tags.joinToString(", ") }, it.rating.toString(), "feedback")
                    }
                    "buses" -> doc.toObject(Bus::class.java)?.let {
                        DynamicRow(it.busNumber, "Capacity ${it.capacity} - GPS ${it.gpsDeviceId}", it.status, "bus")
                    }
                    "routes" -> doc.toObject(Route::class.java)?.let {
                        DynamicRow(it.routeName, "${it.stops.size} stops configured", "Active", "route")
                    }
                    "schedules" -> doc.toObject(Schedule::class.java)?.let {
                        DynamicRow(it.routeId, it.stopTimings.entries.joinToString { e -> "${e.key}: ${e.value}" }, it.busId, "schedule")
                    }
                    "bus_locations" -> doc.toObject(BusLocation::class.java)?.let {
                        DynamicRow(it.busId, "Lat ${"%.4f".format(it.latitude)}, Lng ${"%.4f".format(it.longitude)}", "Live", "tracking")
                    }
                    "audit_logs" -> doc.toObject(AuditLog::class.java)?.let {
                        DynamicRow(it.action, it.target, "Audit", "audit")
                    }
                    else -> null
                }
            }
            onRows(rows)
        }
    }

    fun submitQuery(
        category: String,
        routeName: String,
        description: String,
        attachmentName: String,
        onComplete: (Boolean, String) -> Unit,
    ) {
        val doc = db.collection("queries").document()
        val query = Query(
            queryId = doc.id,
            commuterId = userId(),
            description = description,
            category = category,
            routeName = routeName,
            attachmentName = attachmentName,
            status = "Open",
            time = Timestamp.now(),
        )
        doc.set(query)
            .addOnSuccessListener { onComplete(true, "Query submitted: ${doc.id.takeLast(5).uppercase()}") }
            .addOnFailureListener { onComplete(false, it.message ?: "Could not submit query") }
    }

    fun uploadAttachment(
        uri: Uri,
        folder: String,
        onComplete: (Boolean, String, String) -> Unit,
    ) {
        val fileName = uri.lastPathSegment?.substringAfterLast('/')?.ifBlank { null }
            ?: "attachment_${System.currentTimeMillis()}"
        val path = "$folder/${userId()}/${System.currentTimeMillis()}_$fileName"
        storage.reference.child(path).putFile(uri)
            .addOnSuccessListener { onComplete(true, fileName, path) }
            .addOnFailureListener { onComplete(false, fileName, it.message ?: "Upload failed") }
    }

    fun publishAnnouncement(
        title: String,
        description: String,
        audience: String,
        priority: String,
        attachmentName: String,
        onComplete: (Boolean, String) -> Unit,
    ) {
        val doc = db.collection("announcements").document()
        val announcement = Announcement(
            announcementId = doc.id,
            staffId = userId(),
            title = title,
            description = description,
            audience = audience,
            priority = priority,
            attachmentName = attachmentName,
            publishDate = Timestamp.now(),
        )
        doc.set(announcement)
            .addOnSuccessListener {
                createNotificationForAnnouncement(announcement)
                onComplete(true, "Announcement published")
            }
            .addOnFailureListener { onComplete(false, it.message ?: "Could not publish announcement") }
    }

    fun submitFeedback(
        busNumber: String,
        rating: Double,
        tags: List<String>,
        comment: String,
        attachmentName: String,
        onComplete: (Boolean, String) -> Unit,
    ) {
        val doc = db.collection("feedback").document()
        val feedback = Feedback(
            feedbackId = doc.id,
            userId = userId(),
            busNumber = busNumber,
            rating = rating,
            tags = tags,
            comment = comment,
            attachmentName = attachmentName,
            createdAt = Timestamp.now(),
        )
        doc.set(feedback)
            .addOnSuccessListener { onComplete(true, "Feedback saved") }
            .addOnFailureListener { onComplete(false, it.message ?: "Could not save feedback") }
    }

    fun saveFavorite(routeName: String, stopName: String, busNumber: String, onComplete: (Boolean, String) -> Unit) {
        val doc = db.collection("favorites").document("${userId()}_${routeName.hashCode()}_${stopName.hashCode()}")
        val favorite = FavoriteRoute(doc.id, userId(), routeName, stopName, busNumber, etaMinutes = 4)
        doc.set(favorite)
            .addOnSuccessListener { onComplete(true, "Favorite saved") }
            .addOnFailureListener { onComplete(false, it.message ?: "Could not save favorite") }
    }

    fun updateBusStatus(busNumber: String, status: String, onComplete: (Boolean, String) -> Unit) {
        db.collection("buses").document(busNumber)
            .update("status", status)
            .addOnSuccessListener { onComplete(true, "$busNumber marked $status") }
            .addOnFailureListener { onComplete(false, it.message ?: "Could not update bus") }
    }

    fun upsertUser(name: String, email: String, role: String, onComplete: (Boolean, String) -> Unit) {
        val id = email.ifBlank { "demo_${System.currentTimeMillis()}" }.lowercase().replace(Regex("[^a-z0-9]+"), "_")
        val user = User(id, name, email, role)
        db.collection("users").document(id).set(user)
            .addOnSuccessListener {
                writeAudit("User saved", "$name ($role)")
                onComplete(true, "User saved")
            }
            .addOnFailureListener { onComplete(false, it.message ?: "Could not save user") }
    }

    fun removeUser(email: String, onComplete: (Boolean, String) -> Unit) {
        val id = email.lowercase().replace(Regex("[^a-z0-9]+"), "_")
        db.collection("users").document(id).delete()
            .addOnSuccessListener {
                writeAudit("User removed", email)
                onComplete(true, "User removed")
            }
            .addOnFailureListener { onComplete(false, it.message ?: "Could not remove user") }
    }

    fun upsertBus(busNumber: String, capacity: Int, driverName: String, status: String, onComplete: (Boolean, String) -> Unit) {
        val bus = Bus("bus_${busNumber.filter(Char::isDigit)}", busNumber, capacity, driverName, status, "GPS-${busNumber.filter(Char::isDigit)}")
        db.collection("buses").document(busNumber).set(bus)
            .addOnSuccessListener {
                writeAudit("Bus saved", "$busNumber - $status")
                onComplete(true, "Bus saved")
            }
            .addOnFailureListener { onComplete(false, it.message ?: "Could not save bus") }
    }

    fun removeBus(busNumber: String, onComplete: (Boolean, String) -> Unit) {
        db.collection("buses").document(busNumber).delete()
            .addOnSuccessListener {
                writeAudit("Bus removed", busNumber)
                onComplete(true, "Bus removed")
            }
            .addOnFailureListener { onComplete(false, it.message ?: "Could not remove bus") }
    }

    fun upsertRoute(routeName: String, stopsCsv: String, onComplete: (Boolean, String) -> Unit) {
        val routeId = routeName.lowercase().replace(Regex("[^a-z0-9]+"), "_")
        val stops = stopsCsv.split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .mapIndexed { index, stop -> Stop("${routeId}_${index + 1}", stop, index + 1) }
        val route = Route(routeId, routeName, stops)
        db.collection("routes").document(routeId).set(route)
            .addOnSuccessListener {
                writeAudit("Route saved", "$routeName (${stops.size} stops)")
                onComplete(true, "Route saved")
            }
            .addOnFailureListener { onComplete(false, it.message ?: "Could not save route") }
    }

    fun removeRoute(routeName: String, onComplete: (Boolean, String) -> Unit) {
        val routeId = routeName.lowercase().replace(Regex("[^a-z0-9]+"), "_")
        db.collection("routes").document(routeId).delete()
            .addOnSuccessListener {
                writeAudit("Route removed", routeName)
                onComplete(true, "Route removed")
            }
            .addOnFailureListener { onComplete(false, it.message ?: "Could not remove route") }
    }

    fun upsertSchedule(routeName: String, busNumber: String, slots: Map<String, String>, onComplete: (Boolean, String) -> Unit) {
        val id = "sch_${routeName.lowercase().replace(Regex("[^a-z0-9]+"), "_")}_${busNumber.lowercase().replace(Regex("[^a-z0-9]+"), "_")}"
        val schedule = Schedule(id, busNumber, routeName, Timestamp.now(), slots)
        db.collection("schedules").document(id).set(schedule)
            .addOnSuccessListener {
                writeAudit("Schedule saved", "$routeName - $busNumber")
                onComplete(true, "Schedule saved")
            }
            .addOnFailureListener { onComplete(false, it.message ?: "Could not save schedule") }
    }

    fun removeSchedule(routeName: String, busNumber: String, onComplete: (Boolean, String) -> Unit) {
        val id = "sch_${routeName.lowercase().replace(Regex("[^a-z0-9]+"), "_")}_${busNumber.lowercase().replace(Regex("[^a-z0-9]+"), "_")}"
        db.collection("schedules").document(id).delete()
            .addOnSuccessListener {
                writeAudit("Schedule removed", "$routeName - $busNumber")
                onComplete(true, "Schedule removed")
            }
            .addOnFailureListener { onComplete(false, it.message ?: "Could not remove schedule") }
    }

    fun resolveLatestQuery(onComplete: (Boolean, String) -> Unit) {
        db.collection("queries").limit(1).get()
            .addOnSuccessListener { snapshot ->
                val doc = snapshot.documents.firstOrNull()
                if (doc == null) {
                    onComplete(false, "No query found")
                } else {
                    doc.reference.update("status", "Resolved")
                        .addOnSuccessListener {
                            writeAudit("Query resolved", doc.id)
                            onComplete(true, "Query marked resolved")
                        }
                        .addOnFailureListener { onComplete(false, it.message ?: "Could not resolve query") }
                }
            }
            .addOnFailureListener { onComplete(false, it.message ?: "Could not load queries") }
    }

    fun markNotificationsRead(onComplete: (Boolean, String) -> Unit) {
        db.collection("notifications").limit(20).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.isEmpty) {
                    onComplete(false, "No notifications found")
                    return@addOnSuccessListener
                }
                val batch = db.batch()
                snapshot.documents.forEach { batch.update(it.reference, "isRead", true) }
                batch.commit()
                    .addOnSuccessListener { onComplete(true, "Notifications marked as read") }
                    .addOnFailureListener { onComplete(false, it.message ?: "Could not update notifications") }
            }
            .addOnFailureListener { onComplete(false, it.message ?: "Could not load notifications") }
    }

    fun removeFavorite(routeName: String, stopName: String, onComplete: (Boolean, String) -> Unit) {
        val doc = db.collection("favorites").document("${userId()}_${routeName.hashCode()}_${stopName.hashCode()}")
        doc.delete()
            .addOnSuccessListener { onComplete(true, "Favorite removed") }
            .addOnFailureListener { onComplete(false, it.message ?: "Could not remove favorite") }
    }

    fun askMoveBot(question: String, onAnswer: (String) -> Unit) {
        val q = question.lowercase()
        val instant = when {
            q.contains("g-10") || q.contains("f-10") -> "Bus 07 covers G-10/F-10. Current demo ETA is about 4 minutes for G-10 Markaz."
            q.contains("faizabad") -> "Faizabad route is served by Bus 11. It may be slow near Murree Road during peak hours."
            q.contains("saddar") || q.contains("committee") -> "Rawalpindi Saddar route uses Bus 05 and passes Committee Chowk before IIUI."
            q.contains("female") || q.contains("girls") -> "Female schedule: 06:00 AM -> 08:15 AM, 12:40 PM -> 02:30 PM, Friday 01:30 PM -> 03:20 PM, evening 05:40 PM -> 08:00 PM."
            q.contains("male") || q.contains("boys") -> "Male schedule: 06:00 AM -> 08:15 AM, 01:40 PM -> 03:30 PM, and 07:20 PM -> 09:00 PM."
            q.contains("delay") -> "For delay alerts, check Notifications or Announcements. High priority notices are also created when staff posts announcements."
            q.contains("query") || q.contains("complaint") -> "Open Submit Query, choose category, route, optional attachment, then submit. You can track it in My Queries."
            else -> null
        }
        if (instant != null) {
            onAnswer(instant)
            return
        }

        db.collection("routes").limit(3).get()
            .addOnSuccessListener { snapshot ->
                val routes = snapshot.documents.mapNotNull { it.toObject(Route::class.java)?.routeName }
                onAnswer(
                    if (routes.isNotEmpty()) {
                        "I found these active routes: ${routes.joinToString()}. Ask me about a route name, bus number, schedule, delay, or query."
                    } else {
                        "Ask me about G-10, F-10, Faizabad, Saddar, schedules, delays, or how to submit a query."
                    }
                )
            }
            .addOnFailureListener {
                onAnswer("I can help with G-10/F-10, Faizabad, Saddar, male/female schedules, delay alerts, and query submission.")
            }
    }

    private fun createNotificationForAnnouncement(announcement: Announcement) {
        val doc = db.collection("notifications").document()
        val notification = AppNotification(
            notificationId = doc.id,
            userId = "all",
            title = announcement.title,
            body = announcement.description,
            type = announcement.priority,
            createdAt = Timestamp.now(),
        )
        doc.set(notification)
    }

    private fun writeAudit(action: String, target: String) {
        val doc = db.collection("audit_logs").document()
        doc.set(AuditLog(doc.id, userId(), action, target, Timestamp.now()))
    }

    private fun collectionFor(featureId: String): String? = when {
        featureId.contains("announcement") -> "announcements"
        featureId.contains("quer") -> "queries"
        featureId.contains("notification") -> "notifications"
        featureId.contains("favorite") -> "favorites"
        featureId.contains("feedback") -> "feedback"
        featureId.contains("bus") || featureId.contains("fleet") -> "buses"
        featureId.contains("route") -> "routes"
        featureId.contains("schedule") -> "schedules"
        featureId.contains("tracking") || featureId.contains("monitor") -> "bus_locations"
        featureId.contains("report") || featureId.contains("audit") -> "audit_logs"
        featureId.contains("user") || featureId.contains("profile") -> "users"
        else -> null
    }
}
