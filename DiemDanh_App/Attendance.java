public class Attendance {
    private String studentName;
    private String studentId;
    private String courseName;
    private String courseId;
    private int courseGroup;
    private String timestamp;

    public Attendance(String studentName, String studentId, String courseName, String courseId, int courseGroup, String timestamp) {
        this.studentName = studentName;
        this.studentId = studentId;
        this.courseName = courseName;
        this.courseId = courseId;
        this.courseGroup = courseGroup;
        this.timestamp = timestamp;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseId() {
        return courseId;
    }

    public int getCourseGroup() {
        return courseGroup;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
}
