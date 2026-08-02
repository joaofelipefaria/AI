package br.com.jfelipefaria.acme.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

/**
 * ManageDbApp is a utility class for managing the Acme PostgreSQL database.
 * It provides methods to create, populate, drop, clean, and inspect the database
 * using SQL scripts and JDBC. The class is designed for automation and maintenance
 * tasks, and can be executed with command-line arguments specifying the desired actions.
 *
 * Usage example:
 *   java ManageDbApp CREATE,POPULATE,SHOW_DB,SHOW_DB_DETAILS
 *   
 * All options:
 * CREATE,POPULATE,SHOW_DB,DROP,CLEAN_DB,SHOW_DB,SHOW_DB_DETAILS
 */
public class ManageDbApp {

    // Database connection properties
    private final String url = "jdbc:postgresql://localhost:5432/acme_db";
    private final String user = "acme_admin_user";
    private final String password = "acme_admin_pwd";

    /**
     * Establishes a connection to the PostgreSQL database.
     * @return Connection object or null if connection fails.
     */
    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * Executes an SQL script file located in the resources/scripts folder.
     * Used for creating, populating, dropping, or cleaning the database.
     * @param conn Active database connection.
     * @param fileName Name of the SQL file to execute.
     */
    private void executeSQLFile(Connection conn, String fileName) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("\\scripts\\"+fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
             Statement stmt = conn.createStatement()) {
            String line;
            StringBuilder sql = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
            }
            stmt.execute(sql.toString());
            System.out.println(fileName + " executed successfully.");
        } catch (Exception e) {
            System.out.println("Error executing " + fileName + ": " + e.getMessage());
        }
    }

    /**
     * Lists all tables and sequences in the public schema of the database.
     * @param conn Active database connection.
     */
    private void showDB(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'");
            System.out.println("Tables:");
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
            rs = stmt.executeQuery("SELECT sequence_name FROM information_schema.sequences WHERE sequence_schema = 'public'");
            System.out.println("Sequences:");
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (SQLException e) {
            System.out.println("Error showing DB: " + e.getMessage());
        }
    }

    /**
     * Shows detailed contents of all tables and sequences in the public schema.
     * Prints all rows for each table and the last value for each sequence.
     * @param conn Active database connection.
     */
    private void showDBDetails(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            ResultSet rsTables = stmt.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'");
            System.out.println("Table Details:");
            while (rsTables.next()) {
                String tableName = rsTables.getString(1);
                System.out.println("Table: " + tableName);
                try (Statement tableStmt = conn.createStatement();
                     ResultSet tableRS = tableStmt.executeQuery("SELECT * FROM " + tableName)) {
                    while (tableRS.next()) {
                        int columnCount = tableRS.getMetaData().getColumnCount();
                        StringBuilder row = new StringBuilder();
                        for (int i = 1; i <= columnCount; i++) {
                            row.append(tableRS.getString(i)).append(" ");
                        }
                        System.out.println(row.toString().trim());
                    }
                }
            }

            ResultSet rsSequences = stmt.executeQuery("SELECT sequencename, last_value FROM pg_sequences");
            System.out.println("Sequence Details:");
            while (rsSequences.next()) {
                System.out.println("Sequence: " + rsSequences.getString(1) + ", Last Value: " + rsSequences.getString(2));
            }
        } catch (SQLException e) {
            System.out.println("Error showing DB details: " + e.getMessage());
        }
    }

    /**
     * Processes arguments and executes corresponding actions.
     * Supported commands: CREATE, POPULATE, DROP, CLEAN_DB, SHOW_DB, SHOW_DB_DETAILS.
     * @param conn Active database connection.
     * @param args Array of command-line arguments.
     */
    public void processArgs(Connection conn, String[] args) {
        List<String> commands = Arrays.stream(args[0].split(",")).map(String::toUpperCase).collect(Collectors.toList());
        for (String command : commands) {
            switch (command) {
                case "CREATE":
                    executeSQLFile(conn, "create_db.sql");
                    break;
                case "POPULATE":
                    executeSQLFile(conn, "populate.sql");
                    break;
                case "DROP":
                    executeSQLFile(conn, "drop.sql");
                    break;
                case "CLEAN_DB":
                    executeSQLFile(conn, "clean_db.sql");
                    break;
                case "SHOW_DB":
                    showDB(conn);
                    break;
                case "SHOW_DB_DETAILS":
                    showDBDetails(conn);
                    break;
                default:
                    System.out.println("Unknown command: " + command);
            }
        }
    }

    /**
     * Main entry point of the application.
     * Connects to the database and processes the provided arguments.
     * @param args Command-line arguments specifying actions to perform.
     */
    public static void main(String[] args) {
        ManageDbApp app = new ManageDbApp();
        try (Connection conn = app.connect()) {
            if (conn != null) {
            	System.out.println("Successfull Connection");
            	if (args != null && args.length > 0) {
                    app.processArgs(conn, args);
            	}
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }
}
