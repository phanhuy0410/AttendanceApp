public class AttendanceRabbitMQ {
    private String studentName;
    private String studentId;
    private String timestamp;
    private String courseId;
    private int courseGroup;

    public AttendanceRabbitMQ(String studentName, String studentId, String timestamp, String courseId, int courseGroup) {
        this.studentName = studentName;
        this.studentId = studentId;
        this.timestamp = timestamp;
        this.courseId = courseId;
        this.courseGroup = courseGroup;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getMaHP() {
        return courseId;
    }

    public int getNhom() {
        return courseGroup;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
