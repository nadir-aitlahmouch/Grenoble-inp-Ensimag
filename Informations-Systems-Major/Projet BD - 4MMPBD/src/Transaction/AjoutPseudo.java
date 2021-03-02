package Transaction;

import Transaction.ConnectionManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by benkerre on 11/26/19.
 */
public class AjoutPseudo {
    public AjoutPseudo (Connection conn, int idArtiste, String pseudo) throws SQLException {
        String ajoutdansPseudonume = "insert into PSEUDONYME (PSEUDO) values (?) ";
        String ajoutdansApourPseudo = "insert into APOURPSEUDO (PSEUDO, IDARTISTE) values (?, ?) ";
        PreparedStatement ajoutStmtPrep1 = conn.prepareStatement(ajoutdansPseudonume);
        PreparedStatement ajoutStmtPrep2 = conn.prepareStatement(ajoutdansApourPseudo);

        // Le try suivant s'execute ssi le pseudo n'existe pas dan sla table Pseudonyme.
        // Si oui la requete est ignoré, si non exxecuté.

        try{
            ajoutStmtPrep1.setString(1, pseudo);
            ajoutStmtPrep1.executeQuery();
        }catch (Exception e){}


        ajoutStmtPrep2.setString(1, pseudo);
        ajoutStmtPrep2.setInt(2, idArtiste);
        ajoutStmtPrep2.executeQuery();

    }
}
