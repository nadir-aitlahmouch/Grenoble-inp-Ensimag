package Transaction;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;
import java.util.Scanner;
import oracle.jdbc.driver.OracleDriver;
import java.text.SimpleDateFormat;

public class AjoutExpert {
    /* ajoute un expert */
    public AjoutExpert(java.sql.Connection conn, int id_artiste, String nom, String prenom, Date dateNaissance, int codeCirque, String tel) throws SQLException{
        String Expert = "" +
                         "insert into Expert (IDArtiste, Nom, Prenom, DateNaissance, Codecirque, tel)" +
                         " values (?, ?, ?, ?, ?, ?)";

        PreparedStatement pstmt = conn.prepareStatement(Expert);
        pstmt.setInt(1, id_artiste);
        pstmt.setString(2, nom);
        pstmt.setString(3, prenom);
        pstmt.setDate(4,dateNaissance);
        pstmt.setInt(5, codeCirque);
        pstmt.setString(6, tel);
        ResultSet res = pstmt.executeQuery();

    }
  }
