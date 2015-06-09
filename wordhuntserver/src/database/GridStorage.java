package database;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import gridsolver.TileGrid;
import whobjects.Grid;
import whobjects.Score;
import whproperties.WHProperties;
import whprotocol.WHProtocol;

/**
 * Created by David on 27.05.2015.
 */
public class GridStorage {

    private static Gson gson = new Gson();
    private static int size = new WHProperties("frenchGrid.properties").getInteger("SIZE");

    private static GridStorage instance;

    public static GridStorage getInstance(){
        if(instance == null){
        	synchronized (GridStorage.class) {
				if (instance == null) {
					instance = new GridStorage();
				}
			}
        }
        return instance;
    }

    //Store a grid in DB and return its ID
    public int storeGrid(TileGrid grid) {
        //return grid ID
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = null;
        String content = gson.toJson(grid.getContent());
        Score aScore = Score.getInstance();
        Collection<String> solutions = grid.getSolutions();
        String sols = gson.toJson(solutions);
        int score = aScore.getScore(solutions, WHProtocol.WHPointsType.LENGTH);
        int id = 0;

        try {
            stmt = conn.prepareStatement("INSERT INTO grille (id_type, valeurs_cases, score_max) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, 1);
            stmt.setString(2, content);
            stmt.setInt(3, score);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()){
                id=rs.getInt(1);
            }

            stmt = conn.prepareStatement("INSERT INTO solutiongrille (id_grille, mots) VALUES (?,?)");
            stmt.setInt(1, id);
            stmt.setString(2, sols);
            stmt.executeUpdate();



        }catch(SQLException e)
        {
            e.printStackTrace();
        }


        return id;

    }
    
    /** returns any grid played by this username wanted, not played by the username not wanted */
    public Grid getGridByUser(String usernameWanted, int idUserNotWanted) {
    	Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = null;
        
        Grid grid = new Grid(size);
        try {
        	stmt = conn.prepareStatement("SELECT * FROM grille LEFT JOIN " +
            		" (SELECT id_grille FROM score WHERE id_utilisateur = ?) AS notWantedGrid" +
            		" ON grille.id_grille = notWantedGrid.id_grille" +
            		" LEFT JOIN (SELECT id_grille FROM score NATURAL JOIN utilisateur WHERE nom_utilisateur = ? ) AS wantedGrid " +
            		" ON grille.id_grille = wantedGrid.id_grille" +
            		" WHERE notWantedGrid.id_grille IS NULL AND wantedGrid.id_grill IS NOT NULL ");
        	stmt.setInt(1, idUserNotWanted);
        	stmt.setString(2, usernameWanted);
            ResultSet rs = stmt.executeQuery();
            String content = null;

            if (rs.next()){
                content = rs.getString("valeurs_cases");
                grid.setContent(gson.fromJson(content, char[][].class));
            }else{
                return null;
            }



        }catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
        
    	return grid;
    }
    
    /** returns any grid not played by the usernameNotWanted */
    public Grid getGridByNotUser(int idUserNotWanted) {
    	Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = null;
        
        Grid grid = new Grid(size);
        try {
            stmt = conn.prepareStatement("SELECT * FROM grille LEFT JOIN " +
            		" (SELECT id_grille FROM score WHERE id_utilisateur = ?) AS notWantedGrid" +
            		" ON grille.id_grille = notWantedGrid.id_grille" +
            		" WHERE notWantedGrid.id_grille IS NULL ");
            stmt.setInt(1, idUserNotWanted);
            ResultSet rs = stmt.executeQuery();
            String content = null;

            if (rs.next()){
                content = rs.getString("valeurs_cases");
                grid.setContent(gson.fromJson(content, char[][].class));
            }else{
                System.out.println("GRID does not exist");
                return null;
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
        
    	return grid;
    }

    /** returns the grid defined by this id */
    public Grid getGridByID(int id) {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = null;
        Grid grid = new Grid(size);

        try {
            stmt = conn.prepareStatement("SELECT* FROM grille WHERE id_grille = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            String content = null;

            if (rs.next()){
                content = rs.getString("valeurs_cases");
                grid.setContent(gson.fromJson(content, char[][].class));
//                System.out.println(grid.printGrid());
            }else{
                System.out.println("GRID does not exist");
            }



        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        return grid;
    }

    public static void main(String[] args) {
//        TileGrid grid = (TileGrid) GridGenerator.getInstance().nextValidGrid();
//        int id = GridStorage.getInstance().storeGrid(grid);
//
//        System.out.println("id: " + id);

        Grid grid2 = GridStorage.getInstance().getGridByID(3);
        System.out.println(grid2.printGrid());

    }


}
