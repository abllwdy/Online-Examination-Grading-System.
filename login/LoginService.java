public class LoginService {

    private final StudentRepository studentRepository = new StudentRepository();

    public boolean authenticate(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        Student student = studentRepository.findByUsername(username);
        if (student == null) {
            return false;
        }

        // For demo: plain comparison. In real life, compare password hashes.
        return password.equals(student.getPasswordHash());
    }
}
