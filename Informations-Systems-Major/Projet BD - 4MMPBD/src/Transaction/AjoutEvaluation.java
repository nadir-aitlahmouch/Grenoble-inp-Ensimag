package Transaction;

import Transaction.ConnectionManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AjoutEvaluation {
    public void ajoutEvaluation (Connection conn, int idArtist, int NumAjuger, int Note, String commentaire) throws SQLException{
        String ajout = "insert into Evaluation (CodeN, idArtiste, Note, Commentaire) values (?, ?, ?, ?)";

        PreparedStatement ajoutStmtPrep = conn.prepareStatement(ajout);
        ajoutStmtPrep.setInt(1, NumAjuger);
        ajoutStmtPrep.setInt(2, idArtist);
        ajoutStmtPrep.setInt(3, Note);
        ajoutStmtPrep.setString(4, commentaire);
        ajoutStmtPrep.executeQuery();

    }
}
