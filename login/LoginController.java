public class LoginController {

    private final LoginService loginService = new LoginService();

    // Pseudo-method representing a POST /login handler
    public String handleLogin(String username, String password, Session session) {
        boolean success = loginService.authenticate(username, password);

        if (success) {
            // store user in session
            session.setAttribute("loggedInUser", username);
            // redirect to student dashboard
            return "redirect:/student/dashboard";
        } else {
            // set error message and stay on login page
            session.setAttribute("loginError", "Invalid username or password");
            return "login.html";
        }
    }
}
