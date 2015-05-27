package whdatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by David on 22.05.2015.
 */
public class User {

    private String name;
    private String email;

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    private String password;

    public User(String name , String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void registerUser(){
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = null;

        try {
            if(!isAlreadyRegistered()) {
                stmt = conn.prepareStatement("INSERT INTO utilisateur (nom_utilisateur, email, mot_de_passe) VALUES (?,?,SHA(?)) ");
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, password);
                stmt.executeUpdate();
            }else{
                System.out.println("Already registered!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean correctCredentials(){
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            if(isAlreadyRegistered()){
                stmt = conn.prepareStatement("SELECT * FROM utilisateur WHERE email = ? AND mot_de_passe = SHA(?)");
                stmt.setString(1, email);
                stmt.setString(2, password);
                rs = stmt.executeQuery();
                if(!rs.isBeforeFirst()){
                    //Wrong Credentials
                    return false;
                }
                return true;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        //Not Registered
        return false;


    }


    public void getToken(){

    }

    public boolean isAlreadyRegistered(){
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement("SELECT * FROM utilisateur WHERE email = ?");
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            if(!rs.isBeforeFirst()){
                return false;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }


    public static void main(String[] args) {
        User usr = new User("David", "david@f.com", "othersecret");
        usr.registerUser();

        if(usr.isAlreadyRegistered()){
            System.out.println("Already registered here");
        }

        User me = new User("Jean", "david@f.com", "othersecret");
        me.registerUser();
        if(me.correctCredentials()){
            System.out.println("OK You're logged");
        }else{
            System.out.println("Nope go away!!!");
        }




    }
}
