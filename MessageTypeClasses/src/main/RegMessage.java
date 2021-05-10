public class RegMessage extends AbstractMessage{


    private String login;
    private String password;
    private String message;

    public RegMessage(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public RegMessage(String message) {
        this.message = message;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getMessage() {
        return message;
    }
}
