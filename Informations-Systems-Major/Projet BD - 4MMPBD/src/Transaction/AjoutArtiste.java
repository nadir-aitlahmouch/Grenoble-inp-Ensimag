package Transaction;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;

public class AjoutArtiste {
    public AjoutArtiste(java.sql.Connection conn, int id_artiste, String nom, String prenom, Date dateNaissance, int codeCirque, String tel) throws SQLException{
        String artiste = "" +
                         "insert into ARTISTE (IDArtiste, Nom, Prenom, DateNaissance, Codecirque, tel)" +
                         " values (?, ?, ?, ?, ?, ?)";


            PreparedStatement pstmt = conn.prepareStatement(artiste);
            pstmt.setInt(1, id_artiste);
            pstmt.setString(2, nom);
            pstmt.setString(3, prenom);
            pstmt.setDate(4,dateNaissance);
            if(codeCirque == -1){
                pstmt.setNull(5, Types.INTEGER);
            }
            else{
                pstmt.setInt(5, codeCirque);
            }
            pstmt.setString(6, tel);
            ResultSet res = pstmt.executeQuery();

    }
  }
