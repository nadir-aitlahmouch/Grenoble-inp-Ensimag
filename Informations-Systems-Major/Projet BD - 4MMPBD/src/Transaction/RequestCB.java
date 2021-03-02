package Transaction;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lial on 11/21/19.
 */

/**
 * Created by benkerre on 12/3/19.
 */
public class RequestCB {
    private Connection conn;
    private JComboBox<String> cb;
    private HashMap<Integer, Integer> map;
    private HashMap<Integer, String> mapString;
    private ArrayList<Object[]> listModel;
    public RequestCB(Connection conn){
        this.conn = conn;
    }
    public void getCirques(){

        try {
            String request = "Select * from cirque";
            PreparedStatement prst = conn.prepareStatement(request);
            ResultSet rs = prst.executeQuery();
            treatCirqueRequest(rs);

            prst.close();

        } catch (Exception errorStatement){
            System.err.println("Fetching failed");
            errorStatement.printStackTrace(System.err);
        }

    }
     private void treatCirqueRequest(ResultSet rs) throws SQLException{
        this.cb = new JComboBox<String>();
        int pos = 0;
        this.map = new HashMap<>();
        while(rs.next()){
            int codeCirque = rs.getInt(1);
            String nomCirque = rs.getString(2);

            cb.addItem(codeCirque + ": " + nomCirque);
            this.map.put(pos, codeCirque);
            pos ++;
        }
    }

    public void getArtistesNumero(){

        try {

            String request = "Select a.idArtiste, a.nom, a.prenom from Artiste a" +
                    " left join Expert e " +
                    "on (a.idArtiste = e.idArtiste) " +
                    "where e.idArtiste is null";
            PreparedStatement prst = conn.prepareStatement(request);
            ResultSet rs = prst.executeQuery();
            treatArtisteNumeroRequest(rs);

            prst.close();

        } catch (Exception errorStatement){
            System.err.println("Fetching failed");
            errorStatement.printStackTrace(System.err);
        }

    }

    private void treatArtisteNumeroRequest(ResultSet rs) throws SQLException{
        this.listModel = new ArrayList<>();
        while(rs.next()){
            int idArtiste = rs.getInt(1);
            String nom = rs.getString(2);
            String prenom = rs.getString(3);
            Object[] row = {idArtiste, nom, prenom};
            listModel.add(row);
        }
    }
    public void getArtistes(){

        try {

            String request = "Select idArtiste, nom, prenom from Artiste";
            PreparedStatement prst = conn.prepareStatement(request);
            ResultSet rs = prst.executeQuery();
            treatArtisteRequest(rs);

            prst.close();

        } catch (Exception errorStatement){
            System.err.println("Fetching failed");
            errorStatement.printStackTrace(System.err);
        }

    }
    private void treatArtisteRequest(ResultSet rs) throws SQLException{
        this.cb = new JComboBox<String>();
        int pos = 0;
        this.map = new HashMap<>();
        while(rs.next()){
            int idArtiste = rs.getInt(1);
            String nom = rs.getString(2);
            String prenom = rs.getString(3);

            cb.addItem(idArtiste + ": " + nom + " " + prenom);
            this.map.put(pos, idArtiste);
            pos ++;
        }
    }


    // Cherche la liste des numeros avec le même thème que le spectacle
    public void getNumerosThemeSpectacle(int codeSpectacle){

        try {
            String request = "Select n.codeN, n.titre from Numero n, Spectacle s where (s.codespectacle = ? and n.theme = s.theme)";
            PreparedStatement prst = conn.prepareStatement(request);
            prst.setInt(1, codeSpectacle);
            ResultSet rs = prst.executeQuery();
            treatNumeroRequest(rs);

            prst.close();

        } catch (Exception errorStatement){
            System.err.println("Fetching failed");
            errorStatement.printStackTrace(System.err);
        }

    }

    private void treatNumeroRequest(ResultSet rs) throws SQLException{
        this.cb = new JComboBox<String>();
        int pos = 0;
        this.map = new HashMap<>();
        while(rs.next()){
            int codeN = rs.getInt(1);
            String titre = rs.getString(2);

            cb.addItem(codeN + ": " + titre);
            this.map.put(pos, codeN);
            pos ++;
        }
    }

    // Créer une liste des numéros d'un spectacle
    public void getNumerosSpectacle(int codeSpectacle){

        try {
            String request = "Select n.codeN, n.titre, n.durée from Numero n, EstDansLeSpectacle s where (s.codespectacle = ? and n.coden = s.coden)";
            PreparedStatement prst = conn.prepareStatement(request);
            prst.setInt(1, codeSpectacle);
            ResultSet rs = prst.executeQuery();
            treatNumeroSpectacle(rs);

            prst.close();

        } catch (Exception errorStatement){
            System.err.println("Fetching failed");
            errorStatement.printStackTrace(System.err);
        }

    }

    private void treatNumeroSpectacle(ResultSet rs) throws SQLException{
        this.listModel = new ArrayList<>();
        while(rs.next()){
            int codeN = rs.getInt(1);
            String titre = rs.getString(2);
            int duree = rs.getInt(3);
            Object[] row = {codeN, titre, duree};
            this.listModel.add(row);
        }
    }

    public void getThemes(Boolean list){

        try {
            String request = "Select * from Specialite";
            PreparedStatement prst = conn.prepareStatement(request);
            ResultSet rs = prst.executeQuery();
            if (!list){
                treatTheme(rs);
            }
            else{
                treatThemeList(rs);
            }
            prst.close();

        } catch (Exception errorStatement){
            System.err.println("Fetching failed");
            errorStatement.printStackTrace(System.err);
        }

    }
    private void treatTheme(ResultSet rs) throws SQLException{
        this.cb = new JComboBox<String>();
        int pos = 0;
        this.mapString = new HashMap<>();
        while(rs.next()){
            String specialite = rs.getString(1);
            cb.addItem(specialite);
            this.mapString.put(pos, specialite);
            pos ++;
        }
    }

    public ArrayList<Object[]> getListModel() {
        return listModel;
    }

    private void treatThemeList(ResultSet rs) throws SQLException{
        this.listModel = new ArrayList<>();
        while(rs.next()){
            String specialite = rs.getString(1);
            Object[] row = {specialite};
            this.listModel.add(row);

        }
    }


    public void getSpectacle(){

        try {
            String request = "Select distinct s.CodeSpectacle, h.DateDebut, h.HeureDebut from Spectacle s, HoraireSpectacle h where s.CodeSpectacle = h.CodeSpectacle";
            PreparedStatement prst = conn.prepareStatement(request);
            ResultSet rs = prst.executeQuery();
            treatSpectacleRequest(rs);

            prst.close();
        } catch (Exception errorStatement){
            System.err.println("Fetching failed");
            errorStatement.printStackTrace(System.err);
        }

    }
    private void treatSpectacleRequest(ResultSet rs) throws SQLException{
        this.listModel = new ArrayList<>();
        while(rs.next()){
            int codeSpectacle = rs.getInt(1);
            Date dateDebut = rs.getDate(2);
            int heureDebut = rs.getInt(3);
            Object[] row = {codeSpectacle, dateDebut, heureDebut};
            this.listModel.add(row);

        }
    }

    public void getArtisteNoInNumero(int idnumero){

        try {

            String request = "select A.idArtiste, A.nom, A.prenom\n" +
                    "from ARTISTE A\n" +
                    "where A.idArtiste \n" +
                    "NOT IN  (Select E.idArtiste from ESTPRESENT E \n where E.CODEN =" +  idnumero + ")";
            PreparedStatement prst = conn.prepareStatement(request);
            ResultSet rs = prst.executeQuery();
            treatArtisteNoNumero(rs);

            prst.close();

        } catch (Exception errorStatement){
            System.err.println("Fetching failed");
            errorStatement.printStackTrace(System.err);
        }

    }

    private void treatArtisteNoNumero(ResultSet rs) throws SQLException {
        this.cb = new JComboBox<String>();
        int pos = 0;
        this.map = new HashMap<>();
        while(rs.next()){
            int idArtiste = rs.getInt(1);
            String nom = rs.getString(2);
            String prenom = rs.getString(3);

            cb.addItem("id: " + idArtiste + ", nom: " + nom + ",prenom: " + prenom);
            this.map.put(pos, idArtiste);
            pos ++;
        }
    }

    public boolean getNumero(){

        try {

            String request = "Select CODEN, TITRE, THEME from NUMERO";
            PreparedStatement prst = conn.prepareStatement(request);
            ResultSet rs = prst.executeQuery();
            treatNumero(rs);

            prst.close();
            return true;

        } catch (Exception errorStatement){
            System.err.println("Fetching failed");
            errorStatement.printStackTrace(System.err);
            return false;
        }

    }

    private void treatNumero(ResultSet rs) throws SQLException {
        this.cb = new JComboBox<String>();
        int pos = 0;
        this.map = new HashMap<>();
        while(rs.next()){
            int CodeNumero = rs.getInt(1);
            String titre = rs.getString(2);
            String theme = rs.getString(3);

            cb.addItem("CodeN: " + CodeNumero + ": Titre: " + titre + ", Theme: " + theme);
            this.map.put(pos, CodeNumero);
            pos ++;
        }
    }
    public JComboBox<String> getCb() {
        return cb;
    }

    public HashMap<Integer, Integer> getMap() {
        return map;
    }

    public HashMap<Integer, String> getMapString(){ return mapString; }

}
