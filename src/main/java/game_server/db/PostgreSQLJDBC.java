package game_server.db;

import game.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

@Data
@AllArgsConstructor
public class PostgreSQLJDBC {

    public static void main( String args[] ) throws IOException {
        String filePath = "C:\\Users\\mgarc\\IdeaProjects\\UdemyJava\\TCG\\src\\main\\java\\game_server\\dbconfig\\data.properties";

        Properties prop = new Properties();
        prop.load(new FileInputStream(filePath));

        String username = prop.getProperty("username");
        String password = prop.getProperty("password");
        String dbName = prop.getProperty("dbname");
        String dbUrl = prop.getProperty("ip");
        String port = prop.getProperty("port");

        try {
            Class.forName("org.postgresql.Driver");
            String fullPath = "jdbc:postgresql://" + dbUrl + ":"+ port +"/" + dbName;
            Connection c = DriverManager.getConnection( fullPath, username, password);
            c.setAutoCommit(false);
            System.out.println("-- Opened database successfully");


            String userName = "marian";

            try {
                String query = ( "SELECT * FROM PUBLIC.USER WHERE username = ?;" );
                PreparedStatement stmt = c.prepareStatement(query);
                stmt.setString(1, userName);
                ResultSet rs = stmt.executeQuery();


                while ( rs.next() ) {
                    int id = rs.getInt("uid");
                    String name = rs.getString("username");
                    String  email = rs.getString("token");
                    System.out.println( "ID = " + id );
                    System.out.println( "NAME = " + name );
                    System.out.println( "Token = " + email );
                }
                rs.close();
                stmt.close();
                c.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("-- Operation done successfully");
    }

    public void insertUser(User player) {
//        startConnection();
    }
}


