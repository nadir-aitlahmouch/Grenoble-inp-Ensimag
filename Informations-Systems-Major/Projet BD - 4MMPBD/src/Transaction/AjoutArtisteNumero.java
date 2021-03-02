package Transaction;

import Transaction.ConnectionManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class AjoutArtisteNumero {
    public void AjoutArtisteNumero (Connection conn, int codeN, int codeArtiste) throws SQLException{
        String ajout = "insert into ESTPRESENT (CodeN, idArtiste) values (?, ?)";
        PreparedStatement ajoutStmtPrep = conn.prepareStatement(ajout);
        ajoutStmtPrep.setInt(1, codeN);
        ajoutStmtPrep.setInt(2, codeArtiste);
        ajoutStmtPrep.executeQuery();

    }

    public HashMap<Integer, String> getNumerosTitre(Connection conn) throws SQLException {
        String query = "select distinct n.codeN, n.titre\n" +
                "from numero n " +
                "where n.codeN not in (" +
                "select distinct e.codeN " +
                "from evaluation e " +
                ") ";
        PreparedStatement prepStmt = conn.prepareStatement(query);
        ResultSet res = prepStmt.executeQuery();
        HashMap<Integer, String> titres = new HashMap<>();
        while(res.next()) {
            titres.put(res.getInt(1), res.getString(2));
        }
        return titres;
    }

    public HashMap<Integer, String> getArtisteNoInNumero(Connection conn, int idnumero) throws SQLException{
        String request = "select A.idArtiste, A.nom, A.prenom\n" +
                "from ARTISTE A\n" +
                "where A.idArtiste \n" +
                "NOT IN  (Select E.idArtiste from ESTPRESENT E \n where E.CODEN =" +  idnumero + ")";
        PreparedStatement prst = conn.prepareStatement(request);
        ResultSet res = prst.executeQuery();
        HashMap<Integer, String> titres = new HashMap<>();
        while(res.next()) {
            titres.put(res.getInt(1), res.getString(2) + res.getString(3));
        }
        prst.close();
        return titres;

    }
}
