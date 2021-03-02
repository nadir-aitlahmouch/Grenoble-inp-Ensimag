package Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by nguyene on 11/25/19.
 */
public class RequestJury {
    private Connection conn;
    public RequestJury(Connection conn) {
        this.conn = conn;
    }

    public HashMap<Integer, String> getNumerosTitre() throws SQLException {
        String query = "select distinct n.codeN, n.titre\n" +
                "from numero n " +
                "where n.codeN not in (" +
                "select distinct e.codeN " +
                "from evaluation e " +
                ") ";
        PreparedStatement prepStmt = this.conn.prepareStatement(query);
        ResultSet res = prepStmt.executeQuery();
        HashMap<Integer, String> titres = new HashMap<>();
        while(res.next()) {
            titres.put(res.getInt(1), res.getString(2));
        }
        return titres;
    }

    public HashMap<Integer, String> getAvailableExpert(int numero, boolean isSpecialise) throws SQLException {
        String expert = "select e.idArtiste, a.nom, a.prenom " +
                "from Artiste a, Expert e, Estdespecialite es, Specialite s, Numero n " +
                "where a.idArtiste = e.idArtiste " +
                "and a.idArtiste = es.idArtiste " +
                "and s.specialite = es.specialite " +
                "and n.codeN = ? " +
                "and e.codeCirque NOT IN (" +
                "select codeCirque " +
                "from estDuCirque edc " +
                "where edc.codeN = n.codeN " +
                ") " +
                "and a.idArtiste NOT IN (" +
                "select distinct eval.idArtiste " +
                "from evaluation eval " +
                "where n.codeN = eval.codeN " +
                ") " +
                "and n.theme = es.specialite " +
                "having (select count(eval.idArtiste) " +
                "from Evaluation eval " +
                "where eval.idArtiste = e.idArtiste) < 15 " +
                "group by e.idArtiste, a.nom, a.prenom ";
        if (isSpecialise) {
            expert = "select distinct e.idArtiste, e.nom, e.prenom " +
                    "from expert e " +
                    "where e.codeCirque NOT IN (" +
                    "select codeCirque " +
                    "from estDuCirque edc " +
                    "where edc.codeN = ? " +
                    ") " +
                    "and e.idArtiste NOT IN (" +
                    "select distinct eval.idArtiste " +
                    "from evaluation eval " +
                    "where eval.codeN = ?" +
                    ") " +
                    "having (select count(eval.idArtiste) " +
                    "from Evaluation eval " +
                    "where eval.idArtiste = e.idArtiste) < 15 " +
                    "group by e.idArtiste, e.nom, e.prenom " +
                    "minus " +
                    expert;
        }
        PreparedStatement expertsStmtPrep = conn.prepareStatement(expert);
        expertsStmtPrep.setInt(1, numero);
        if (isSpecialise) {
            expertsStmtPrep.setInt(2, numero);
            expertsStmtPrep.setInt(3, numero);
        }
        ResultSet res = expertsStmtPrep.executeQuery();
        HashMap<Integer, String> experts = new HashMap<>();
        while(res.next()) {
            String nom = res.getString(2) + " " + res.getString(3);
            experts.put(res.getInt(1), nom);
        }

        return experts;
    }

}
