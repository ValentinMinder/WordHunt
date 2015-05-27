package database;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import gridsolver.TileGrid;
import whdatabase.DatabaseConnection;
import whobjects.Grid;
import whobjects.Score;
import whprotocol.WHMessage;
import whprotocol.WHProtocol;

/**
 * Created by David on 27.05.2015.
 */
public class GridStorage {

    private static Gson gson = new Gson();


    public WHMessage storeGrid(TileGrid grid) {
        //return grid ID
        Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = null;
        String content = gson.toJson(grid);
        Score aScore = new Score();
        int score = aScore.getScore(grid.getSolution());


        try {
            stmt = conn.prepareStatement("INSERT INTO grille (id_type, valeurs_cases, score_max) VALUES (?,?,?)");
            stmt.executeUpdate();
            stmt.setInt(1, 1);
            stmt.setString(2, content);
            stmt.setString(3, score);
        }catch(SQLException e)
        {
            e.printStackTrace();
        }


        return null;

    }

    public Grid getGrid(int id) {
        Grid grid = new Grid();

        return grid;
    }


}
