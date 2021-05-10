import java.sql.*;

public class DBConnection {

    private static Connection connection;
    private static Statement stmt;

    static void connect() throws SQLException, ClassNotFoundException{
        System.out.println("Trying to connect to Database");
        try{
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:user.db");
            stmt = connection.createStatement();
            System.out.println("Connected to Database");
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Connection error");
        }
    }

    public static int getIdByLoginByLoginAndPassword(String login, String password) throws SQLException{
        String statement = String.format("SELECT id FROM users WHERE login = '%s' AND password = '%s", login, password);
        try{
            ResultSet rs = stmt.executeQuery(statement);
            if(rs.next()){
                return rs.getInt(1);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public static void registration(String login, String password) throws SQLException{
        try {
            String statement = "INSERT INTO user (login, password) VALUES (?, ?);";
            PreparedStatement ps = connection.prepareStatement(statement);
            ps.setString(1, login);
            ps.setString(2, password);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
