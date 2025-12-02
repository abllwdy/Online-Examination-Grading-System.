import java.util.HashMap;
import java.util.Map;

public class StudentRepository {
    private static final Map<String, Student> students = new HashMap<>();

    static {
        // demo user: username: "22WMR12345", password: "default123"
        students.put("22WMR12345", new Student("22WMR12345", "default123"));
    }

    public Student findByUsername(String username) {
        return students.get(username);
    }
}
