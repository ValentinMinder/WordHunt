package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by David on 11.05.2015.
 */
public class DatabaseConnection {


    public static DatabaseConnection dbinstance;
    private Connection conn;
    private String dbName;
    private String port;
    private String user;
    private String pwd;

    private DatabaseConnection() {

        user = "root";
        pwd = "root";
        dbName = "wordhunt";
        port = "3306";


    }

    public static DatabaseConnection getInstance(){
        if(dbinstance == null){
            dbinstance = new DatabaseConnection();
        }

        return dbinstance;
    }

    public Connection getConnection() {

        try {
            conn=DriverManager.getConnection( "jdbc:mysql://localhost:" + port +"/" + dbName + "?user=" +user + "&password=" + pwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return conn;
    }

}
