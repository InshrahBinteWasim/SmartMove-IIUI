# SmartMove IIUI Feature Test Guide

## Run

1. Open `C:\Users\DELL\AndroidStudioProjects\SmartMoveIIUI` in Android Studio.
2. Set Gradle JDK to `C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot`.
3. Run the app on an emulator or phone.
4. Demo login:
   - Email: `demo@iiu.edu.pk`
   - Password: `SmartMove123`

## Common Screens

- Splash: wait or tap anywhere. It should continue to onboarding.
- Onboarding: swipe/continue through pages. Dark mode should also work.
- Login: test empty fields, wrong values, locked state, and demo success.
- Forgot Password: use OTP `451826`.

## Student Screens

- Live Bus Tracking: open from dashboard. Bus rows should come from Firestore `bus_locations` when available.
- Route Detail: save route in the route manager/admin flow, then verify route rows update.
- Schedule Browser/Detail: schedule rows should come from Firestore `schedules`.
- Notifications Inbox: posting a staff announcement creates a notification row.
- Submit Query:
  1. Open Submit Query.
  2. Tap Attach proof to choose a file.
  3. Tap Submit Query.
  4. Check My Queries for the new query.
- My Queries: query rows are loaded from Firestore `queries`.
- MoveBot Chatbot:
  1. Ask `Which bus goes to G-10?`
  2. Ask `female schedule`
  3. Ask `how to submit query`
  4. MoveBot should answer instantly.
- Announcements: rows load from Firestore `announcements`.
- Favorites: route favorites load from Firestore `favorites`.
- Feedback:
  1. Attach image optionally.
  2. Submit feedback.
  3. The feedback row should appear from Firestore `feedback`.

## Staff Screens

- Post Announcement:
  1. Attach a file optionally.
  2. Tap Publish update.
  3. Verify Announcements and Notifications update.
- Manage Queries: submitted student queries should appear.
- Live Bus Monitor: bus/fleet rows load from Firestore `buses`.
- Staff Profile: profile-style module is visible.
- Announcement History: delivery history panel visible; announcement rows are dynamic.

## Admin Screens

- User & Role Management:
  1. Edit name/email/role.
  2. Tap Save / update.
  3. Row appears in dynamic data if rules allow.
  4. Tap Remove to delete Firestore document.
- Bus & Route Manager:
  - Bus manager: edit bus number/capacity/driver/status, save/update, remove.
  - Route manager: edit route name and stops CSV, upload optional file, save/update, remove.
- Schedule Manager:
  1. Edit route, bus, morning/afternoon slots.
  2. Save/update.
  3. Remove if needed.
- Reports & Logs:
  - Admin changes create audit log records in Firestore `audit_logs`.

## Firebase Requirements

For full dynamic testing, Firebase rules must allow the current user/demo session to read/write:

- `users`
- `buses`
- `routes`
- `schedules`
- `queries`
- `announcements`
- `notifications`
- `favorites`
- `feedback`
- `audit_logs`
- Storage folders: `query_attachments`, `announcement_attachments`, `feedback_attachments`, `route_uploads`

If rules block an action, the app should show a failure toast rather than crashing.
