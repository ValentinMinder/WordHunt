package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import whprotocol.WHMessage;
import whprotocol.WHProtocol;
import whprotocol.WHProtocol.WHMessageHeader;

/**
 * Created by David on 22.05.2015.
 */
public class User {

	private static Random rand = new Random();

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
    	if (email == null || name == null) {
    		throw new RuntimeException("email & name needed to register");
    	}
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = null;

        try {
            if(!isAlreadyRegistered()) {
                stmt = conn.prepareStatement("INSERT INTO utilisateur (nom_utilisateur, email, salt, mot_de_passe) VALUES (?,?,?,SHA(?)) ");
                stmt.setString(1, name);
                stmt.setString(2, email);
                String salt = generateToken();
                stmt.setString(3, salt);
                stmt.setString(4, salt + password);
                stmt.executeUpdate();
            }else{
                return new WHMessage(WHProtocol.WHMessageHeader.BAD_REQUEST_400, "Email " + email + " or username " + name + " already registered.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new WHMessage(WHProtocol.WHMessageHeader.REGISTER_ACCOUNT_CREATED_201,  email + " is now registered and known as " + name + ".");

    }

    public WHMessage correctCredentials(){
    	if (name == null) {
    		throw new RuntimeException("name needed to connect");
    	}
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
        	String salt = getSaltForUser();
            if (salt != null) {
                stmt = conn.prepareStatement("SELECT * FROM utilisateur WHERE nom_utilisateur = ? AND mot_de_passe = SHA(?)");
                stmt.setString(1, name);
                stmt.setString(2, salt + password);
                rs = stmt.executeQuery();
                if(!rs.isBeforeFirst()){
                    //Wrong Credentials
                    return new WHMessage(WHProtocol.WHMessageHeader.AUTHENTICATE_BAD_CREDENTIALS, 
                    		"Bad Credentials for " + name);
                }
                // right credentials: give token.
                String token = generateToken();
                return storeToken(token);
            } else {
            	//Not Registered
                return new WHMessage(WHProtocol.WHMessageHeader.AUTHENTICATE_BAD_CREDENTIALS, 
                		"Account + " + name + " not known. Please register first");
            }


        } catch (SQLException e) {
            e.printStackTrace();
            return new WHMessage(WHMessageHeader.SERVER_ERROR_500, 
            		"Server error. Please try again");
        }
    }

    /**
     * Store the token given for the user.
     */
    private WHMessage storeToken(String token) {
    	Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("UPDATE utilisateur SET token = ? WHERE nom_utilisateur = ?");
			stmt.setString(1, token);
			stmt.setString(2, name);
			stmt.executeUpdate();
			// if nothing is thrown, then success!
			return new WHMessage(WHProtocol.WHMessageHeader.AUTH_TOKEN, token);
		} catch (SQLException e) {
			e.printStackTrace();
			return new WHMessage(WHProtocol.WHMessageHeader.SERVER_ERROR_500, 
					"Could not store or deliver token. Please try again later.");
		}
	}

    private static int length = 32;
	/** generates a new token.*/
    private String generateToken(){
    	StringBuilder sb = new StringBuilder(length);
    	for (int i = 0; i < length; i++) {
			sb.append((char) (rand.nextInt('Z'-'A') + 'A')); // random A-Z
		}
    	return sb.toString();
    }

    /** Checks if the user is already registered */
    public boolean isAlreadyRegistered() throws SQLException {
    	return (null != getSaltForUser());
    }

    /** Get the cryptographic salt for this user. Return null if user not known. */
    private String getSaltForUser() throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement("SELECT salt FROM utilisateur WHERE email = ? OR nom_utilisateur = ?");
            stmt.setString(1, email);
            stmt.setString(2, name);
            rs = stmt.executeQuery();
            if(!rs.isBeforeFirst()){ // no result: not known.
                return null;
            }
            rs.next();
            return rs.getString("salt");
        } catch (SQLException e) {
            throw e;
        }
    }
    
    /** 
     * Check if the token is the admin one. <br> 
     * Returns also false if admin not known or sql error.
     */
    public static boolean checkAdminToken (String token) {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement("SELECT token FROM utilisateur WHERE nom_utilisateur = ?");
            stmt.setString(1, "admin");
            rs = stmt.executeQuery();
            if(!rs.isBeforeFirst()){ // no result: not known.
                return false;
            }
            rs.next();
            return token.equals(rs.getString("token")); // check that it's the same
        } catch (SQLException e) {
           	return false;
        }
    }


    public static void main(String[] args) {
        User usr = new User("David", "david@f.com", "othersecret");
        System.out.println(usr.registerUser());
        System.out.println(usr.registerUser());

        try {
			if(usr.isAlreadyRegistered()){
			    System.out.println("Already registered here");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
