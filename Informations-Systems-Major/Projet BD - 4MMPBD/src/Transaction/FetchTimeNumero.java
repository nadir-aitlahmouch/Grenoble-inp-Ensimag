package Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by lial on 11/25/19.
 */
public class FetchTimeNumero {
    private Connection conn;
    public FetchTimeNumero(Connection conn){
        this.conn = conn;
    }

    public int getTotalTime(int codeSpectacle){
        int result = 0;
        try{

        // TODO changer en SUM(n.duree)
        String request = "Select n.durée from Numero n, estDansLeSpectacle s where (s.codespectacle = ? and n.coden = s.coden)";
        PreparedStatement prst = conn.prepareStatement(request);
        prst.setInt(1, codeSpectacle);
        ResultSet rs = prst.executeQuery();
        result = dumpResult(rs);

        prst.close();

        } catch (Exception errorStatement){
            System.err.println("Fetching failed");
            errorStatement.printStackTrace(System.err);
        }
        return result;
    }

    private int dumpResult (ResultSet rs) throws SQLException{
        int somme = 0;
            while(rs.next()){
                int duree = rs.getInt(1);
                somme += duree;
            }
        return somme;

    }
}
