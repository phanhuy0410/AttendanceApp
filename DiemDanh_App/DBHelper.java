import java.sql.*;
import java.util.*;

public class DBHelper {
    private static final String URL = "jdbc:mysql://13.214.218.173:3306/attendance_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "pthsql@04";

    // private static final String URL = "jdbc:mysql://localhost:3306/attendance_db";
    // private static final String USER = "root";
    // private static final String PASS = "";
    
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void saveAttendance(String full_name, String mssv, String tenHP, String maHP, int nhom) throws SQLException {
        String query = "INSERT INTO attendance (full_name, mssv, tenHP, maHP, nhom, ngaygio) VALUES (?, ?, ?, ?, ?, ?)";
        String insertHistory = "INSERT INTO history (full_name, mssv, tenHP, maHP, nhom, ngaygio, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try ( Connection conn = connect();
                PreparedStatement stmt = conn.prepareStatement(query);
                PreparedStatement stmt2 = conn.prepareStatement(insertHistory);) {
            //attendance table
            stmt.setString(1, full_name);
            stmt.setString(2, mssv);
            stmt.setString(3, tenHP);
            stmt.setString(4, maHP);
            stmt.setInt(5, nhom);
            stmt.setTimestamp(6, Timestamp.valueOf(java.time.LocalDateTime.now()));
            stmt.executeUpdate();

            //history table
            stmt2.setString(1, full_name);
            stmt2.setString(2, mssv);
            stmt2.setString(3, tenHP);
            stmt2.setString(4, maHP);
            stmt2.setInt(5, nhom);
            stmt2.setTimestamp(6, Timestamp.valueOf(java.time.LocalDateTime.now()));
            stmt2.setBoolean(7, true);
            stmt2.executeUpdate();
        }
    }

    public static List<Attendance> getAttendanceHistory(String mssv) {
        List<Attendance> history = new ArrayList<>();
        try (Connection conn = connect()) {
            String sql = "SELECT * FROM history WHERE mssv = ? AND status = TRUE ORDER BY ngaygio DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, mssv);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("full_name");
                String id = rs.getString("mssv");
                String nameCourse = rs.getString("tenHP");
                String course = rs.getString("maHP");
                int group = rs.getInt("nhom");
                String time = rs.getString("ngaygio");
                history.add(new Attendance(name, id, nameCourse, course, group, time));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }

    public static void clearAttendanceHistory(String mssv) throws SQLException {
        String query = "UPDATE history SET status = FALSE WHERE mssv = ?";
        try (
                Connection conn = connect();
                PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.setString(1, mssv);
            stmt.executeUpdate();
        }
    }

    public static boolean hasAlreadyCheckedIn(String mssv, String maHP) {
        String sql = "SELECT COUNT(*) FROM attendance WHERE mssv = ? AND maHP = ? AND DATE(ngaygio) = CURDATE()";
        try (Connection conn = connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, mssv);
            stmt.setString(2, maHP);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Student getStudentInfo(String mssv) {
        Student student = null;
        String sql = "SELECT * FROM student WHERE mssv = ?";
        try (Connection conn = DBHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, mssv);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String fullName = rs.getString("hoten");
                String className = rs.getString("lop");
                String email = rs.getString("email");
                student = new Student(mssv, fullName, className, email);
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return student;
    }

    public static List<Course> searchCourses(String year, String semester, String courseId) {
    List<Course> list = new ArrayList<>();
    String sql = "SELECT nhom, maHP, tenHP FROM hocphan WHERE namhoc = ? AND hocki = ? AND maHP = ?";
    try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, year);
        stmt.setString(2, semester);
        stmt.setString(3, courseId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int group = rs.getInt("nhom");
            String maHP = rs.getString("maHP");
            String tenHP = rs.getString("tenHP");
            list.add(new Course(group, maHP, tenHP));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}



}
