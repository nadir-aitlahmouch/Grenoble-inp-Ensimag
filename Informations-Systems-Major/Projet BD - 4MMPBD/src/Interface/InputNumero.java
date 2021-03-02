package Interface;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import Transaction.*;

/**
 * Created by lial on 11/21/19.
 */

public class InputNumero extends JFrame implements ActionListener, WindowListener{
    private Connection conn;
    private JTextField codeN;
    private JTextField titre;
    private JTextField resume;
    private JTextField duree;
    private JComboBox<String> theme;
    private JComboBox<String> artistePrincipal;
    private JButton confirm;
    private JButton addArtiste;
    private JPanel panelPrincipal;
    private HashMap<Integer, String> cbTheme;
    private HashMap<Integer, Integer> cbArtiste;
    private Set<Object[]> rowsSelected;


    public void setRowsSelected(Set<Object[]> rowsSelected) {
        this.rowsSelected = rowsSelected;
    }

    public InputNumero(Connection conn) throws SQLException{
        super();
        this.rowsSelected = new HashSet<>();
        this.conn = conn;
        RequestCB request = new RequestCB(conn);
        setTitle("Ajouter un numéro");
        setSize(600, 400);
        this.panelPrincipal = new JPanel();
        GridLayout layout = new GridLayout(0,2);
        layout.setVgap(20);
        panelPrincipal.setLayout(layout);



        JLabel lbl1 = new JLabel("Code Numéro");
        panelPrincipal.add(lbl1);
        this.codeN = new JTextField();
        panelPrincipal.add(this.codeN);

        JLabel lbl2 = new JLabel("Titre");
        panelPrincipal.add(lbl2);
        this.titre = new JTextField(20);
        panelPrincipal.add(this.titre);

        JLabel lbl3 = new JLabel("Résumé");
        panelPrincipal.add(lbl3);
        this.resume = new JTextField(20);
        panelPrincipal.add(this.resume);

        JLabel lbl4 = new JLabel("Durée");
        panelPrincipal.add(lbl4);
        this.duree = new JTextField(20);
        panelPrincipal.add(this.duree);

        JLabel lbl6 = new JLabel("Thème du numéro");
        panelPrincipal.add(lbl6);
        request.getThemes(false);
        this.theme = request.getCb();
        this.cbTheme = request.getMapString();
        panelPrincipal.add(this.theme);

        JLabel lbl7 = new JLabel("Artiste Principal");
        panelPrincipal.add(lbl7);
        if(this.rowsSelected.size() == 0){
            //L'utilisateur n'a pas encore choisi d'artiste
            this.artistePrincipal = new JComboBox<>();
            this.artistePrincipal.addItem("--Veuillez d'abord choisir les artistes du numéro");
            panelPrincipal.add(this.artistePrincipal);
        }
        else{
            this.artistePrincipal = new JComboBox<>();
            this.cbArtiste = new HashMap<>();
            int pos = 0;
            for (Object[] row : rowsSelected){
                String item = row[0] + ": " + row[1] + " " + row[2];
                this.artistePrincipal.addItem(item);
                this.cbArtiste.put(pos, (int) row[0]);
                pos ++;
            }
        }
        panelPrincipal.add(this.artistePrincipal);


        JPanel panelBot = new JPanel();
        confirm= new JButton("Envoyer");
        confirm.addActionListener(this);
        this.addArtiste = new JButton("Ajouter des artistes");
        addArtiste.addActionListener(this);
        panelBot.add(addArtiste);
        panelBot.add(confirm);

        JPanel panelTop = new JPanel();
        JLabel lbl = new JLabel("Veuillez rentrer les informations");
        panelTop.add(lbl);

        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        panelPrincipal.setBorder(padding);
        getContentPane().add(BorderLayout.NORTH, panelTop);
        getContentPane().add(BorderLayout.CENTER, panelPrincipal);
        getContentPane().add(BorderLayout.SOUTH, panelBot);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == this.confirm){

            try{
                int codeNint = Integer.parseInt(this.codeN.getText());
                String titreString = this.titre.getText();
                if (titreString.equals("")){
                    throw new IllegalArgumentException("Le titre ne peut pas être nul");
                }
                String resumeString = this.resume.getText();
                if (titreString.equals("")){
                    throw new IllegalArgumentException("Le résumé ne peut pas être nul");
                }
                int dureeint = Integer.parseInt(this.duree.getText());
                if(dureeint < 10 || dureeint > 30){
                    throw new IllegalArgumentException("La durée du numéro doit être comprise entre 10 et 30 min");
                }
                int nbArtisteint = this.rowsSelected.size();
                if(nbArtisteint < 1){
                    throw new IllegalArgumentException("Il y a au moins 1 artiste dans le numéro");
                }
                String themeString = this.cbTheme.get(this.theme.getSelectedIndex());

                // Si l'utilisateur n'a pas choisi d'artiste, il peut tj sélectionner l'option du combobox ce qui va déclencher une erreur
                // Il faut donc regarder la valeur de artistePrincipal après d'avoir assurer qu'il existe au moins 1 artiste.
                int artistePrincipalint = this.cbArtiste.get(this.artistePrincipal.getSelectedIndex());
                conn.setSavepoint();
                new NumeroDescription(conn, codeNint, titreString, resumeString, dureeint, nbArtisteint, themeString, artistePrincipalint);
                for(Object[] row : this.rowsSelected){
                    new AjoutArtisteNumero().AjoutArtisteNumero(conn, codeNint, (int)row[0]);
                }
                conn.commit();
                JOptionPane.showMessageDialog(this, "Inscription réussie");
            }
            catch (SQLException exception) {
                ConnectionManagement.rollback(conn);
                if (exception.getErrorCode() == 1){
                    JOptionPane.showMessageDialog(this, "Le code du numéro est déjà pris");
                }
                else{
                    JOptionPane.showMessageDialog(this, exception.getMessage());
                }


            }
            catch (NumberFormatException error){
                JOptionPane.showMessageDialog(this, "Code numéro ou durée n'est pas un entier");
            }
            catch (IllegalArgumentException error){
                JOptionPane.showMessageDialog(this, error.getMessage());
            }


        }

        else{// User pressed addArtiste
            new AddArtiste(conn, this);
        }


    }
    public void refresh(){
        // Refreshes only the values on the combobox for Artiste Principal
        this.panelPrincipal.remove(this.artistePrincipal);
        if(this.rowsSelected.size() == 0){
            //L'utilisateur n'a pas encore choisi d'artiste
            this.artistePrincipal = new JComboBox<>();
            this.artistePrincipal.addItem("--Veuillez d'abord choisir les artistes du numéro");
            panelPrincipal.add(this.artistePrincipal);
        }
        else{
            this.artistePrincipal = new JComboBox<>();
            this.cbArtiste = new HashMap<>();
            int pos = 0;
            for (Object[] row : rowsSelected){
                String item = row[0] + ": " + row[1] + " " + row[2];
                this.artistePrincipal.addItem(item);
                this.cbArtiste.put(pos, (int) row[0]);
                pos ++;
            }
        }
        this.panelPrincipal.add(this.artistePrincipal);
        this.panelPrincipal.revalidate();
        this.panelPrincipal.repaint();

    }
    public Set<Object[]> getRowsSelected() {
        return rowsSelected;
    }

    // Methods to manage window methods
    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        setVisible(false);
        dispose();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
