CollabChat local DB configuration

The application reads database configuration from environment variables. Defaults are safe for a local development MySQL server.

Environment variables (optional):

- COLLABCHAT_DB_URL - JDBC URL (default: jdbc:mysql://localhost:3306/collabchat?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC)
- COLLABCHAT_DB_USER - DB username (default: appuser)
- COLLABCHAT_DB_PASS - DB password (default: AppPass123)

To create the database quickly (MySQL):

1. Connect to MySQL as an admin and run the SQL in `db/schema.sql`.
2. Create the app user if desired:

   CREATE USER 'appuser'@'localhost' IDENTIFIED BY 'AppPass123';
   GRANT ALL PRIVILEGES ON collabchat.* TO 'appuser'@'localhost';
   FLUSH PRIVILEGES;

If you're deploying to a server, override the environment variables or set up a secure application.properties file and update `DBUtil` to read it.