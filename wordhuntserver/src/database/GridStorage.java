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

    private static GridStorage instance;

    public static GridStorage getInstance(){
        if(instance == null){
            instance = new GridStorage();
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

    public Grid getGrid(int id) {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = null;
        int size = new WHProperties("wordhuntserver/frenchGrid.properties").getInteger("SIZE");
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

        Grid grid2 = GridStorage.getInstance().getGrid(3);
        System.out.println(grid2.printGrid());

    }


}
