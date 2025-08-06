public class Course {
    private int group;
    private String courseId;
    private String courseName;

    public Course(int group, String courseId, String courseName) {
        this.group = group;
        this.courseId = courseId;
        this.courseName = courseName;
    }

    public Integer getGroup() { return group; }
    public String getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
}
