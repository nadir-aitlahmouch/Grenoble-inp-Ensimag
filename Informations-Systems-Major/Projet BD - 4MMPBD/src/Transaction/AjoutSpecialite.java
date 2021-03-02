package Transaction;

import Transaction.ConnectionManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AjoutSpecialite {
    public AjoutSpecialite (Connection conn, int codeArtiste, String specialite) throws SQLException{
        String ajout = "insert into ESTDESPECIALITE (Specialite, idArtiste) values (?, ?)";

        PreparedStatement ajoutStmtPrep = conn.prepareStatement(ajout);
        ajoutStmtPrep.setString(1, specialite);
        ajoutStmtPrep.setInt(2, codeArtiste);
        ajoutStmtPrep.executeQuery();

    }
}
