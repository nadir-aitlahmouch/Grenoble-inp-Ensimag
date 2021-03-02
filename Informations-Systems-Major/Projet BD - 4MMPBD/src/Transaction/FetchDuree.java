package Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by lial on 11/25/19.
 */
public class FetchDuree {
    private Connection conn;
    public FetchDuree(Connection conn){
        this.conn = conn;
    }

    public int getDuree(int codeN){
        int result = 0;
        try {
            String request = "Select durée from Numero where codeN = ?";
            PreparedStatement prst = conn.prepareStatement(request);
            prst.setInt(1, codeN);
            ResultSet rs = prst.executeQuery();
            result = dumpResult(rs);
            prst.close();

        } catch (Exception errorStatement){
            System.err.println("Fetching failed");
            errorStatement.printStackTrace(System.err);
        }
        return result;
    }

    private int dumpResult(ResultSet rs) throws SQLException{
        int result = 0;
        while (rs.next()){
            result =  rs.getInt(1);
        }
        return result;
    }
}
