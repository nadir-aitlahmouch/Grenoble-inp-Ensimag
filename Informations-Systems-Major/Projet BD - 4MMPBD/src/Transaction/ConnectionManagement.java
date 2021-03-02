package Transaction; /**
 * Created by lial on 11/19/19.
 */

import javax.swing.*;
import java.sql.*;

public class ConnectionManagement {
    static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    static final String USER = "benkerre";
    static final String PASSWD = "benkerre";
    private Connection conn;

    public java.sql.Connection getConn() {
        return conn;
    }

    public void openConnection(){
        try {
            // Enregistrement du driver Oracle
            System.out.print("Loading Oracle driver... ");
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            System.out.println("loaded");
            // Etablissement de la connection
            System.out.print("Connecting to the database... ");
            this.conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);
            System.out.println("connected");
            this.conn.setAutoCommit(false);

        }catch (SQLException e) {
            System.err.println("Connection Failed");
            e.printStackTrace(System.err);
        }






    }


    public static void rollback(Connection conn){
        try{
            conn.rollback();

        }catch (Exception error){
           System.err.print(error.getStackTrace());
        }
    }
    public void closeConnection(){
        try{
            // Fermeture
            this.conn.close();
            System.out.println("Connection closed");
        }catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
        }


    }
}
