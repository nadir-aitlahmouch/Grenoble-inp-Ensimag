package Transaction;

import Transaction.ConnectionManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteSpectacle {
    public void deleteSpectacle (Connection conn, int codespectacle) throws SQLException{
        String spectacle = "DELETE from SPECTACLE where codespectacle = ?";
        String estDansLeSpectacle = "DELETE from ESTDANSLESPECTACLE where codespectacle = ?";

        /** Firstly : delete from estDansLeSpectacle */
        PreparedStatement ajoutStmtPrep2 = conn.prepareStatement(estDansLeSpectacle);
        ajoutStmtPrep2.setInt(1, codespectacle);
        ajoutStmtPrep2.executeQuery();
        /** Finaly : delete from spectacle */
        PreparedStatement ajoutStmtPrep1 = conn.prepareStatement(spectacle);
        ajoutStmtPrep1.setInt(1, codespectacle);
        ajoutStmtPrep1.executeQuery();

    }
}
