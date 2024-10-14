package hms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String db_url = "jdbc:mysql://localhost/hospitalmanagementsystem";
    private static final String user = "root";
    private static final String password = "savaniK#506";
    // private static final String sql = "insert into student(id, name, city) values
    // (2, 'XYZ', 'Banglore')";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);

        try {
            Connection conn = DriverManager.getConnection(db_url, user, password);
            // Statement stmt = conn.createStatement();
            Patient patient = new Patient(conn, scanner);
            Doctor doctor = new Doctor(conn);

            while (true) {
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter your choice:");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        patient.addPatient();
                        break;

                    case 2:
                        patient.viewPatient();
                        break;

                    case 3:
                        doctor.viewDoctors();
                        break;

                    case 4:
                        bookAppointment(patient, doctor, conn, scanner);

                        break;

                    case 5:
                        System.out.println("Thank You for using Hospital Management System!!");
                        return;

                    default:
                        System.out.println("Enter Valid Choice");
                        break;
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner) {
        System.out.println("Enter Patient Id: ");
        int patientId = scanner.nextInt();

        System.out.println("Enter Doctor Id: ");
        int doctorId = scanner.nextInt();

        System.out.println("Enter appointment date (YYY--MM--DD): ");
        String appointmentDate = scanner.next();

        if (patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)) {
            if (checkAvailability(doctorId, appointmentDate, connection)) {
                String appointmentQuery = "insert into appointments(patient_id,doctor_id,appointment_date) values (?,?,?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);

                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("Appointment Booked Successfully!");
                    } else {
                        System.out.println("Failed to book Appointment!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Doctor not avilable");
            }
        } else {
            System.out.println("Either patient or doctor doesnt exist!!!");
        }

    }

    public static boolean checkAvailability(int doctorId, String appointmentDate, Connection connection) {
        String query = "select count(*) from appointments where doctorId=? and appointmentDate=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
