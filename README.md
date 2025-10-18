CollabChat - Java Web Group Chat
================================

Overview

Build & Run (Maven)
-------------------
1. Prerequisites: Maven 3+, JDK 8+, Tomcat 9.
2. From the project root run (PowerShell):

```powershell
mvn clean package
```

This produces `target/collabchat.war`.

3. Deploy the WAR to Tomcat: copy `target\collabchat.war` into `C:\path\to\tomcat9\webapps` and Tomcat will explode and deploy it.

4. Set DB credentials using environment variables or edit `src/com/collabchat/util/DBUtil.java` before building:

```powershell
---------
- `WebContent/` - JSPs, CSS, JS and `WEB-INF/web.xml`
- `src/com/collabchat/...` - Java source: servlets, DAOs, models, util
- `db/schema.sql` - SQL schema to create the required tables
```

5. Start Tomcat and open `http://localhost:8080/collabchat/`.

Prerequisites
-------------
- JDK 8+ and Apache Tomcat 9
- MySQL server
- Add the following jars to `WebContent/WEB-INF/lib/` before deployment:
  - MySQL Connector/J (mysql-connector-java-8.x.jar)
  - jBCrypt (jbcrypt-0.4.jar) — for BCrypt hashing

DB Setup
--------
1. Create a database (example `collabchat`):

   CREATE DATABASE collabchat CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
   USE collabchat;

2. Run the SQL in `db/schema.sql` to create tables.

Configuration
-------------
Edit `src/com/collabchat/util/DBUtil.java` to set your DB URL, username and password.

Build & Run
-----------
1. Compile the Java sources (your IDE or build system). Ensure the required jars are on the classpath.
2. Deploy the `WebContent` folder as a webapp in Tomcat (context path `CollabChat` recommended).
3. Open `http://localhost:8080/CollabChat/`.

Notes
-----
- This project uses simple polling AJAX (future: replace with WebSocket for real-time push).
- Error handling is minimal; production apps should use prepared connection pools, input validation, CSRF protection, and stronger session management.

Swing Classroom Collaboration (Optional)
---------------------------------------
This repo also contains a demo Swing client and a minimal WebSocket broadcast server for classroom collaboration: chat, whiteboard, tic-tac-toe, and quiz.

Run the WebSocket Server
1. Open a terminal in `collabchat-swing/server` and ensure `java-websocket` is on classpath if needed (or use your IDE).
2. Compile and run `server/CollabServer.java` (main method). Default port: 8887.

Run the Swing Client
1. Open a terminal in `collabchat-swing/client`.
2. Compile and run `client/CollabClient.java` (main method).
3. Login dialog asks for username and role (teacher/student). No backend auth in this demo.
4. The app connects to `ws://localhost:8887` and broadcasts messages to all connected clients.

Modules
- Chat: send/receive messages in real time.
- Whiteboard: drag to draw; strokes are broadcast to peers.
- Games: simple tic-tac-toe; moves are broadcast.
- Quiz: teacher sends MCQ; students answer with a click.