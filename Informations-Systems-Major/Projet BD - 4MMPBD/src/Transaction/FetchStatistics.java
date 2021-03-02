package Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nguyene on 11/26/19.
 */
public class FetchStatistics {
    private Connection conn;
    public FetchStatistics(Connection conn) {
        this.conn = conn;
    }

    public ArrayList<Object[]> getNumeroLeaderboardByTheme(String theme) throws SQLException {
        String query = "select n.coden, n.titre, avg(eval.note)\n" +
        "from numero n, evaluation eval\n" +
        "where n.coden = eval.coden\n" +
        "and n.theme like ?\n" +
        "group by n.coden, n.titre\n" +
        "order by 3 desc";


        PreparedStatement prepStmt = conn.prepareStatement(query);
        prepStmt.setString(1, theme);
        ResultSet res = prepStmt.executeQuery();
        ArrayList<Object[]> numeros = new ArrayList<>();
        while (res.next()) {
            numeros.add(new Object[]{res.getString(2), res.getDouble(3)});
        }
        prepStmt.close();
        return numeros;
    }
}
