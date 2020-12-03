/**
 * Author: William Howell
 * Brief: Employee class file that contains methods, to_strings, and fields.
 */
public class Employee {
    private StringBuilder name;
    private String username;
    private String password;
    private String email;

    private void setUsername(String username) {
        String[] splitName = username.split(" ");
        try {
            this.username = splitName[splitName.length - 2].toLowerCase().charAt(0) + splitName[splitName.length - 1].toLowerCase();
        } catch (ArrayIndexOutOfBoundsException e) {
            this.username = username;
        }
    }

    public String getUsername() {
        return username;
    }

    private boolean checkName(String name) {
        return name.contains(" ");
    }

    private void setEmail(String email) {
        String[] splitEmail = email.split(" ");
        try {
            this.email = splitEmail[splitEmail.length - 2].toLowerCase() + "." + splitEmail[splitEmail.length - 1].toLowerCase() + "@oracleacademy.Test";
        } catch (ArrayIndexOutOfBoundsException e) {
            this.email = email;
        }
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public StringBuilder getName() {
        return name;
    }

    private boolean isValidPassword(String password) {
        return password.matches("(?=.*[A-Z])(?=.*[a-z])(?=.*[^\\w\\d\\s:])([^\\s]){0,10}");

    }

    public Employee(String name, String password) {
        if (checkName(name)) {
            this.name = new StringBuilder(name);
            setUsername(name);
            setEmail(name);
        } else {
            this.name = new StringBuilder(name);
            setUsername("default");
            setEmail("user@oracleacademy.Test");
        }
        if (isValidPassword(password)) {
            this.password = password;
        } else {
            this.password = "pw";
        }
    }

    @Override
    public String toString() {
        return "Employee Details " + "\n" +
                " Name : " + name + "\n" +
                " Username : " + username + '\n' +
                " Email : " + email + '\n' +
                " Initial Password : " + password + '\n';
    }
}
