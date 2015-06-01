package whdatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import whprotocol.WHMessage;
import whprotocol.WHProtocol;

/**
 * Created by David on 22.05.2015.
 */
public class User {

	private final static String CRYPTO_SALT = "WordHuntGameGEN2015";
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

    /**
     * To create account: need for email.
     */
    public User(String name , String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }
    
    /**
     * To check connection: no need for email.
     */
    public User(String name, String password){
        this.name = name;
        this.email = null;
        this.password = password;
    }

    public WHMessage registerUser(){
    	if (email == null) {
    		throw new RuntimeException("email needed to register");
    	}
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = null;

        try {
            if(!isAlreadyRegistered()) {
                stmt = conn.prepareStatement("INSERT INTO utilisateur (nom_utilisateur, email, mot_de_passe) VALUES (?,?,SHA(?)) ");
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, CRYPTO_SALT + password);
                stmt.executeUpdate();
            }else{
                return new WHMessage(WHProtocol.WHMessageHeader.SERVER_ERROR_500, "Email " + email + " or username " + name + " already registered");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new WHMessage(WHProtocol.WHMessageHeader.REGISTER_ACCOUNT_CREATED_201,  email + " is now registered and known as " + name );

    }

    public WHMessage correctCredentials(){
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            if(isAlreadyRegistered()){
                stmt = conn.prepareStatement("SELECT * FROM utilisateur WHERE nom_utilisateur = ? AND mot_de_passe = SHA(?)");
                stmt.setString(1, name);
                stmt.setString(2, CRYPTO_SALT + password);
                rs = stmt.executeQuery();
                if(!rs.isBeforeFirst()){
                    //Wrong Credentials
                    return new WHMessage(WHProtocol.WHMessageHeader.AUTHENTICATE_BAD_CREDENTIALS, "Bad Credentials for " + name);
                }
//                int token = getToken();
                int token = 1;
                return new WHMessage(WHProtocol.WHMessageHeader.AUTH_TOKEN, Integer.toString(token));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        //Not Registered
        return new WHMessage(WHProtocol.WHMessageHeader.AUTHENTICATE_BAD_CREDENTIALS, "Please register first");


    }


    public void getToken(){


    }

    public boolean isAlreadyRegistered(){
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement("SELECT * FROM utilisateur WHERE email = ? OR nom_utilisateur = ?");
            stmt.setString(1, email);
            stmt.setString(2, name);
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
        System.out.println(usr.registerUser());
        System.out.println(usr.registerUser());

        if(usr.isAlreadyRegistered()){
            System.out.println("Already registered here");
        }

        User me = new User("Jean", "david@f.com", "othersecret");
        System.out.println(me.registerUser());
        User me2 = new User("David", "other@f.com", "othersecret");
        System.out.println(me2.registerUser());
        
        WHMessage message = me.correctCredentials();
        if(message.getHeader() == WHProtocol.WHMessageHeader.AUTH_TOKEN){
            System.out.println("OK You're logged");
            System.out.println(message.getContent().toString());
        }else{
            System.out.println("Nope go away!!!");
        }
        
        User newMe = new User("David", "othersecret");
        System.out.println(newMe.correctCredentials());



    }
}
