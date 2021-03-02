package Transaction;

import Transaction.ConnectionManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AjoutNumeroDansSpectacle {
    public void AjoutNumeroDansSpectacle (Connection conn, int coden, int codespectacle) throws SQLException{
        String ajout = "insert into ESTDANSLESPECTACLE (CodeN, CodeSpectacle) values (?, ?)";
              PreparedStatement ajoutStmtPrep = conn.prepareStatement(ajout);
        ajoutStmtPrep.setInt(1, coden);
        ajoutStmtPrep.setInt(2, codespectacle);
        ajoutStmtPrep.executeQuery();

    }
}
