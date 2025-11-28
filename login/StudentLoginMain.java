import java.util.Scanner;

public class StudentLoginMain {
    public static void main(String[] args) {
        LoginService loginService = new LoginService();
        Scanner scanner = new Scanner(System.in);

        boolean exitApp = false;

        while (!exitApp) {
            System.out.println("=== Online Examination Grading System ===");
            System.out.println("1. Login");
            System.out.println("2. Quit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            if ("2".equals(choice)) {
                System.out.println("Exiting application. Goodbye.");
                exitApp = true;
                break;
            }

            if (!"1".equals(choice)) {
                System.out.println("Invalid option.\n");
                continue;
            }

            boolean loggedIn = false;
            int attempts = 0;
            final int MAX_ATTEMPTS = 3;

            while (!loggedIn && attempts < MAX_ATTEMPTS) {
                System.out.print("Enter username: ");
                String username = scanner.nextLine();

                System.out.print("Enter password: ");
                String password = scanner.nextLine();

                if (loginService.authenticate(username, password)) {
                    loggedIn = true;
                    System.out.println("Login successful. You are now logged in and can access your exams.\n");

                    boolean inDashboard = true;
                    while (inDashboard) {
                        System.out.println("=== Student Dashboard ===");
                        System.out.println("1. View exams (placeholder)");
                        System.out.println("2. Logout");
                        System.out.print("Choose an option: ");
                        String dashChoice = scanner.nextLine();

                        if ("2".equals(dashChoice)) {
                            System.out.println("You have been logged out. Returning to login.\n");
                            inDashboard = false; // return to main menu (login/quit)
                        } else if ("1".equals(dashChoice)) {
                            System.out.println("[Placeholder] Here you would see your exams.\n");
                        } else {
                            System.out.println("Invalid option.\n");
                        }
                    }

                } else {
                    attempts++;
                    System.out.println("Invalid username or password. Attempt " + attempts + " of " + MAX_ATTEMPTS + ".");

                    if (attempts >= MAX_ATTEMPTS) {
                        System.out.print("Maximum attempts reached. Do you want to try again (Y/N)? ");
                        String retry = scanner.nextLine().trim().toUpperCase();
                        if ("Y".equals(retry)) {
                            attempts = 0; // reset attempts and loop back to login input
                        } else {
                            System.out.println("Returning to main menu.\n");
                            break; // break login loop, go back to main menu (login/quit)
                        }
                    } else {
                        System.out.print("Do you want to re-enter credentials (Y/N)? ");
                        String retry = scanner.nextLine().trim().toUpperCase();
                        if (!"Y".equals(retry)) {
                            System.out.println("Returning to main menu.\n");
                            break; // break login loop, go back to main menu
                        }
                    }
                }
            }
        }

        scanner.close();
    }
}
