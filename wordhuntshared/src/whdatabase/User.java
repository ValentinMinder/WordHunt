package whdatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by David on 22.05.2015.
 */
public class User {



    public void registerUser(String nom , String email, String password){
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement("INSERT INTO utilisateur (nom_utilisateur, email, mot_de_passe) VALUES (?,?,SHA(?)) ");
            stmt.setString(1, nom);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.executeUpdate();
            System.out.println("Something");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void verifyCredentials(){

    }


    public void getToken(){

    }

    public boolean isAlreadyRegistered(String email){
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
        User usr = new User();
        usr.registerUser("David", "david@f.com", "othersecret");

        if(usr.isAlreadyRegistered("david@f.com")){
            System.out.println("Already registered");
        }



    }
}
