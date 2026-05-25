# SmartMove IIUI FYP Chapter Review

Reviewed file: `E:/FinalYearProject/fyp chapters.docx`

Note: the DOCX has a malformed internal `NULL` relationship, so normal `python-docx` loading fails. The document content was reviewed through raw Word XML. The file contains 1,162 text paragraphs and 18 embedded images.

## Strong Areas

- Chapter 1 clearly explains the SmartMove IIUI motivation, scope, and three-role structure.
- Chapter 2 correctly identifies gaps in existing transport systems: no live tracking, no ETA, no notifications, weak communication, and no chatbot.
- Chapter 3 includes functional and non-functional requirements for authentication, reset password, tracking, schedules, announcements, queries, chatbot, staff actions, and admin management.
- Fully dressed use cases exist for core actors: commuter, transport staff, and system administrator.
- Chapter 4 includes visual design artifacts: ERD, class diagram, sequence diagrams, and activity diagram.

## High Priority Fixes Before Submission

1. Add fully dressed use cases for missing app screens:
   - Notifications Inbox
   - Notification Settings
   - Favorite Stops/Routes
   - Profile & Settings
   - Feedback & Ratings
   - Route Detail
   - Schedule Detail
   - Live Bus Monitor
   - Staff Profile
   - Announcement History
   - System Reports & Logs
   - Audit Logs

2. Fix numbering conflict:
   - `3.4.3 Transport Office Staff`
   - `3.4.3 System Administrator`
   - Rename administrator section to `3.4.4 System Administrator`.

3. Align reset password use case with the app:
   - Current text mentions reset link/instructions.
   - App uses a 3-step flow: email -> 6-box OTP -> new password.
   - Update UC_002 to include OTP validation, OTP expiry, resend OTP, invalid OTP, and password mismatch.

4. Add attachment/upload handling to relevant use cases:
   - Submit Query: optional image/PDF proof.
   - Publish Announcement: optional official circular/image.
   - Feedback: optional bus/stop photo.
   - Reports/Logs: export PDF/CSV.
   - Route/Schedule Manager: optional CSV/PDF upload for route or schedule sheets.

5. Update non-functional requirements for free-tier optimization:
   - Firestore listener reuse.
   - Cache-first schedules.
   - Static map route previews.
   - Throttled GPS updates.
   - Pagination for query, notification, and announcement lists.
   - WorkManager for background sync.

## UML / Diagram Recommendations

- Use Case Diagram: add missing use cases listed above and connect them to the correct actors.
- Class Diagram: include entities/classes for `Notification`, `FavoriteRoute`, `Feedback`, `Attachment`, `AuditLog`, `ReportSummary`, and `RouteStop`.
- Sequence Diagrams: add at least these flows:
  - Live tracking with cached route shape and throttled GPS update.
  - Submit query with optional attachment upload.
  - Staff posts announcement with optional file.
  - Admin updates schedule and commuters receive notification.
  - Forgot password OTP flow.
- Activity Diagram: include branching for loading, empty, error, success, locked account, invalid OTP, and upload failure.
- ERD: add relationships for:
  - User -> Query
  - Query -> Attachment
  - Announcement -> Attachment
  - User -> FavoriteRoute
  - Bus -> Route
  - Route -> RouteStop
  - Schedule -> Route/Bus
  - Admin -> AuditLog

## Suggested Fully Dressed Use Cases To Add

### UCC_005 Receive Notifications
- Primary Actor: Commuter
- Goal: Receive delay, cancellation, GPS, and announcement alerts.
- Main Flow: system checks user favorite routes -> sends relevant alert -> user opens notification -> system shows linked route/announcement.
- Extensions: notification disabled, network unavailable, alert expired.

### UCC_006 Manage Favorite Routes
- Primary Actor: Commuter
- Goal: Save frequently used routes/stops for quick ETA.
- Main Flow: user opens route -> taps favorite -> system saves preference -> dashboard shows quick ETA.
- Extensions: route removed, duplicate favorite, offline save.

### UCC_007 Submit Feedback
- Primary Actor: Commuter
- Goal: Rate bus trip and optionally upload image.
- Main Flow: user selects trip -> rates service -> adds tags/comment -> optional attachment -> submits.
- Extensions: upload fails, missing rating, duplicate feedback.

### UCS_003 Monitor Live Buses
- Primary Actor: Transport Office Staff
- Goal: Monitor fleet GPS, status, and delay alerts.
- Main Flow: staff opens monitor -> system loads active buses -> highlights weak GPS/delays -> staff opens bus detail.
- Extensions: GPS offline, no active buses, stale location.

### UCA_005 View Reports and Audit Logs
- Primary Actor: System Administrator
- Goal: Review system performance, reports, and admin changes.
- Main Flow: admin opens reports -> system loads aggregated stats -> admin filters/export logs.
- Extensions: no data, export fails, unauthorized access.

## Language / Formatting Issues

- Replace casual phrasing like “we shall figure out” with formal academic wording.
- Fix typos:
  - `regestered` -> `registered`
  - `not shown in plain text` -> `not be shown in plain text`
  - `if no queries exists` -> `if no queries exist`
- Standardize actor naming:
  - Use `Commuter (Student/Employee)` consistently.
  - Use `Transport Office Staff` consistently.
  - Use `System Administrator` consistently.
- Standardize use case IDs:
  - Common: `UC_001`, `UC_002`
  - Commuter: `UCC_001`...
  - Staff: `UCS_001`...
  - Admin: `UCA_001`...

## App Alignment Status

The Android frontend now covers the 28-screen inventory, including splash/onboarding, login states, OTP reset, student/staff/admin dashboards, tracking, ETA, notifications, announcements, queries, MoveBot, favorites, profile/settings, feedback, staff operations, admin management, reports, and audit logs.

The document should now be updated so Chapter 3 and Chapter 4 match this expanded frontend scope.
