package Transaction;

import Transaction.ConnectionManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteNumero {
    public void deleteNumero (Connection conn, int codeN, int codespectacle) throws SQLException{
        String ajout = "delete from ESTDANSLESPECTACLE where codespectacle = ?  and coden = ?";
        PreparedStatement ajoutStmtPrep = conn.prepareStatement(ajout);
        ajoutStmtPrep.setInt(1, codespectacle);
        ajoutStmtPrep.setInt(2, codeN);
        ajoutStmtPrep.executeQuery();
    }
}
