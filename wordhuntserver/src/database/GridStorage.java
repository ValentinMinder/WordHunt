package database;

import gridsolver.TileGrid;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import whobjects.Grid;
import whobjects.Score;
import whproperties.WHProperties;
import whprotocol.WHProtocol;
import whprotocol.WHProtocol.WHLangage;

import com.google.gson.Gson;

/**
 * Created by David on 27.05.2015.
 */
public class GridStorage {

    private static Gson gson = new Gson();
    private static int size = new WHProperties(Score.PROPERTYFILENAME).getInteger("SIZE");

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
        String hashs = gson.toJson(grid.getHashedSolutions());
        int score = aScore.getScore(solutions, WHProtocol.WHPointsType.LENGTH);
        int id = 0;

        try {
            stmt = conn.prepareStatement("INSERT INTO grille (id_lang, valeurs_cases, score_max, hashs) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, WHLangage.FRENCH.ordinal()); // default: french.
            stmt.setString(2, content);
            stmt.setInt(3, score);
            stmt.setString(4, hashs);

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
            		" LEFT JOIN (SELECT id_grille, score AS best_score FROM score NATURAL JOIN utilisateur WHERE nom_utilisateur = ? ) AS wantedGrid " +
            		" ON grille.id_grille = wantedGrid.id_grille" +
            		" WHERE notWantedGrid.id_grille IS NULL AND wantedGrid.id_grille IS NOT NULL ");
        	stmt.setInt(1, idUserNotWanted);
        	stmt.setString(2, usernameWanted);
            ResultSet rs = stmt.executeQuery();
            String content = null;

            if (rs.next()){
                content = rs.getString("valeurs_cases");
                grid.setContent(gson.fromJson(content, char[][].class));
                content = rs.getString("hashs");
                grid.setHashedSolutions(gson.fromJson(content, int[].class));
                grid.setGridID(rs.getInt("id_grille"));
                grid.setBestScore(rs.getInt("best_score"));

            }else{
            	System.err.println("Not grid for selection, bad !");
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
            		" (SELECT id_grille, score AS best_score FROM score WHERE id_utilisateur = ?) AS notWantedGrid" +
            		" ON grille.id_grille = notWantedGrid.id_grille" +
            		" WHERE notWantedGrid.id_grille IS NULL ");
            stmt.setInt(1, idUserNotWanted);
            ResultSet rs = stmt.executeQuery();
            String content = null;

            if (rs.next()){
            	content = rs.getString("valeurs_cases");
                grid.setContent(gson.fromJson(content, char[][].class));
                content = rs.getString("hashs");
                grid.setHashedSolutions(gson.fromJson(content, int[].class));
                grid.setGridID(rs.getInt("id_grille"));
                grid.setBestScore(rs.getInt("best_score"));
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

        System.out.println("searching grid " + id);
        try {
            stmt = conn.prepareStatement(
                    "SELECT * FROM grille " +
                    "left join score on grille.id_grille = score.id_grille " +
                    "WHERE grille.id_grille = ? ");

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            String content = null;

            if (rs.next()){
            	content = rs.getString("valeurs_cases");
                grid.setContent(gson.fromJson(content, char[][].class));
                content = rs.getString("hashs");
                grid.setHashedSolutions(gson.fromJson(content, int[].class));
                grid.setGridID(rs.getInt("id_grille"));
                grid.setBestScore(rs.getInt("score"));


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
    public String[] getGridSolutionsByID(int id) {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement("SELECT * FROM SolutionGrille WHERE id_grille = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            String content = null;

            if (rs.next()){
            	content = rs.getString("mots");
                return gson.fromJson(content, String[].class);
            }else{
                System.out.println("GRID does not exist");
                return null;
            }



        }catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
	public void storeScore(int userId, int gridId, int score) {	   
		Connection conn = DatabaseConnection.getInstance().getConnection();
		PreparedStatement stmt = null;
		

		try {
			stmt = conn
					.prepareStatement(
							"INSERT INTO score (id_utilisateur, id_grille, score) VALUES (?, ?, ?)",
							Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1,userId);
			stmt.setInt(2,gridId);
			stmt.setInt(3,score);

			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
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
