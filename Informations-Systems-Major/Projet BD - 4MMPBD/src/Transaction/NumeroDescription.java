package Transaction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class NumeroDescription {
    /* Implémente un numéro */
    public NumeroDescription(java.sql.Connection conn, int coden, String titre, String resume, int duree,
                                        int nbartistes, String theme, int artisteprincipal) throws SQLException {
        String numero = "" +
                "insert into Numero (CodeN, Titre, Resume, Durée, NbArtiste, Theme, ArtistePrincipal) values " +
                "(?,?,?,?,?,?,?)";

        PreparedStatement numeroStmtPrep = conn.prepareStatement(numero);
        numeroStmtPrep.setInt(1, coden);
        numeroStmtPrep.setString(2, titre);
        numeroStmtPrep.setString(3, resume);
        numeroStmtPrep.setInt(4, duree);
        numeroStmtPrep.setInt(5, nbartistes);
        numeroStmtPrep.setString(6, theme);
        numeroStmtPrep.setInt(7, artisteprincipal);
        ResultSet res = numeroStmtPrep.executeQuery();
            // res.getRowId();
    }
}
