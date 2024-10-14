package hms;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//imports the Connection class from the java.sql package, which is used to manage a connection to a database.
import java.sql.SQLException;

public class Patient {
    private Connection connection;
    private Scanner scanner;
    // made pvt so that this inst. is accessible in this class only

    public Patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }
    // This allows an instance of the Patient class to initialize with an existing
    // database connection and a scanner for user input.

    public void addPatient() {
        System.out.println("Enter Patient's Name :");
        String name = scanner.next();

        System.out.println("Enter Patient's Age :");
        int age = scanner.nextInt();

        System.out.println("Enter Patient's Gender :");
        String gender = scanner.next();

        /*
         * sqlexception
         * Invalid SQL Syntax: For example, using incorrect SQL query syntax.
         * Database Connection Failure: The database server is down or unreachable.
         * Constraint Violations: Such as primary key, foreign key, or unique
         * constraints being violated.
         * Incorrect Database Credentials: Providing wrong username/password when trying
         * to connect to the database.
         * Missing Tables/Columns: If the code tries to query a table or column that
         * does not exist in the database schema.
         * Data Type Mismatch: Mismatch between Java types and database types
         */

        try {
            String query = "insert into patients(name,age,gender) values (?,?,?)";
            // placeholders (?) in the SQL statement that will be replaced with actual
            // values at runtime.

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            // placeholders (?) for parameters, which will be replaced by actual values
            // later using methods like setString(), setInt(), etc.

            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);

            int affectedRows = preparedStatement.executeUpdate();
            // used to execute an SQL INSERT, UPDATE, or DELETE statement and capture the
            // number of rows affected

            if (affectedRows > 0) {
                System.out.println("Patient Added Successfully!");
            } else {
                System.out.println("Failed to Add Patient :(");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewPatient() {
        String query = "select * from patients";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Patients :");
            System.out.println("+------------+----------------+-------------+----------------+");
            System.out.println("| Patient Id | Patient Name   | Patient Age | Patient Gender |");
            System.out.println("+------------+----------------+-------------+----------------+");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("| %-11s| %-15s| %-12s| %-15s|\n", id, name, age, gender);
                System.out.println("+------------+----------------+-------------+----------------+");

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id) {
        String query = "select * from patients where id=?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
