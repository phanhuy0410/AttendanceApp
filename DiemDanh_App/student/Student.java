public class Student {
    private String mssv;
    private String hoten;
    private String lop;
    private String email;

    public Student(String mssv, String hoten, String lop, String email) {
        this.mssv = mssv;
        this.hoten = hoten;
        this.lop = lop;
        this.email = email;
    }
    public String getFullName(){
        return hoten;
    }
    public String getMSSV(){
        return mssv;
    }
    public String getClassName(){
        return lop;
    }
    public String getEmail(){
        return email;
    }
}
