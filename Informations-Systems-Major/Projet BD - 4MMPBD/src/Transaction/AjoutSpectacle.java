package Transaction;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
/**
 * Created by benkerre on 11/26/19.
 */

public class AjoutSpectacle {
    public void ajoutSpectacle (Connection conn, int codeSpectacle, Date date, int prix, String theme, int idPresentateur, int heure) throws SQLException {
        String ajout = "insert into SPECTACLE (CODESPECTACLE, PRIXENTREE, THEME, " +
                "PRESENTATEUR) values (?, ?, ?, ?)";
        PreparedStatement ajoutStmtPrep = conn.prepareStatement(ajout);
        ajoutStmtPrep.setInt(1, codeSpectacle);
        ajoutStmtPrep.setInt(2, prix);
        ajoutStmtPrep.setString(3, theme);
        ajoutStmtPrep.setInt(4, idPresentateur);
        ajoutStmtPrep.executeQuery();
        this.ajoutHoraireSpectacle(conn, codeSpectacle, date, heure);
    }

    private void ajoutHoraireSpectacle(Connection conn, int codeSpectacle, Date date, int heure) throws  SQLException {
        String ajout = "insert into HoraireSpectacle (datedebut, heuredebut, codespectacle) values (?, ?, ?)";
        PreparedStatement ajoutStmtPrep = conn.prepareStatement(ajout);
        ajoutStmtPrep.setDate(1, date);
        ajoutStmtPrep.setInt(2, heure);
        ajoutStmtPrep.setInt(3, codeSpectacle);
        ajoutStmtPrep.executeQuery();
    }
}
