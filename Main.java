import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    private static final String url = "jdbc:postgresql://localhost:5432/a3db";
    private static final String user = "postgres";
    private static final String pass = "postgres";

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("Choose an option:");
                System.out.println("1. Get all students");
                System.out.println("2. Add a student");
                System.out.println("3. Update student email");
                System.out.println("4. Delete a student");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        getAllStudents();
                        break;
                    case 2:
                        System.out.print("Enter first name: ");
                        String firstName = scanner.nextLine();
                        System.out.print("Enter last name: ");
                        String lastName = scanner.nextLine();
                        System.out.print("Enter email: ");
                        String email = scanner.nextLine();
                        System.out.print("Enter enrollment date (YYYY-MM-DD): ");
                        String enrollmentDate = scanner.nextLine();
                        addStudent(firstName, lastName, email, enrollmentDate);
                        break;
                    case 3:
                        System.out.print("Enter student ID: ");
                        int studentId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter new email: ");
                        String newEmail = scanner.nextLine();
                        updateStudentEmail(studentId, newEmail);
                        break;
                    case 4:
                        System.out.print("Enter student ID: ");
                        int id = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        deleteStudent(id);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void getAllStudents() throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM students")) {
            while (resultSet.next()) {
                System.out.println("Student ID: " + resultSet.getInt("student_id"));
                System.out.println("First Name: " + resultSet.getString("first_name"));
                System.out.println("Last Name: " + resultSet.getString("last_name"));
                System.out.println("Email: " + resultSet.getString("email"));
                System.out.println("Enrollment Date: " + resultSet.getDate("enrollment_date"));
                System.out.println();
            }
        }
    }

    private static void addStudent(String firstName, String lastName, String email, String enrollmentDate) throws SQLException {
        String sql = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, email);
            preparedStatement.setDate(4, java.sql.Date.valueOf(enrollmentDate));
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student added successfully.");
            } else {
                System.out.println("Failed to add student.");
            }
        }
    }

    private static void updateStudentEmail(int studentId, String newEmail) throws SQLException {
        String sql = "UPDATE students SET email = ? WHERE student_id = ?";
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newEmail);
            preparedStatement.setInt(2, studentId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Email updated successfully.");
            } else {
                System.out.println("No student found with the given ID.");
            }
        }
    }

    private static void deleteStudent(int studentId) throws SQLException {
        String sql = "DELETE FROM students WHERE student_id = ?";
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student deleted successfully.");
            } else {
                System.out.println("No student found with the given ID.");
            }
        }
    }
}
