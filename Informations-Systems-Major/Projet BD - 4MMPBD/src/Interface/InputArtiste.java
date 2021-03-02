package Interface;

import Transaction.AjoutArtiste;
import Transaction.AjoutExpert;
import Transaction.AjoutSpecialite;
import Transaction.AjoutExpert;
import Transaction.ConnectionManagement;
import Transaction.RequestCB;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lial on 11/21/19.
 */

public class InputArtiste extends JFrame implements ActionListener, WindowListener {
    private Connection conn;
    private JTextField idArtiste;
    private JTextField nom;
    private JTextField prenom;
    private JTextField date;
    private JComboBox<String> codeCirque;
    private JTextField tel;
    private JCheckBox expertCheck;

    HashMap<Integer, Integer> cbCorrespondance;
    private Set<String> specialite;
    private JButton confirm;
    private JButton ajoutSpecialte;

    public InputArtiste(Connection conn) throws SQLException {
        super();
        this.conn = conn;
        this.specialite = new HashSet<>();
        setTitle("Ajouter un artiste");
        setSize(600, 400);
        JPanel panelPrincipal = new JPanel();
        GridLayout layout = new GridLayout(0, 2);
        layout.setVgap(20);
        panelPrincipal.setLayout(layout);

        expertCheck = new JCheckBox("Expert");
        JLabel lbl0Moins1 = new JLabel("Est-ce un expert ?");
        panelPrincipal.add(lbl0Moins1);
        panelPrincipal.add(expertCheck);


        JLabel lbl0 = new JLabel("idArtiste");
        panelPrincipal.add(lbl0);
        this.idArtiste = new JTextField(20);
        panelPrincipal.add(this.idArtiste);

        JLabel lbl1 = new JLabel("Nom");
        panelPrincipal.add(lbl1);
        this.nom = new JTextField(20);
        panelPrincipal.add(this.nom);

        JLabel lbl2 = new JLabel("Prénom");
        panelPrincipal.add(lbl2);
        this.prenom = new JTextField(20);
        panelPrincipal.add(this.prenom);

        JLabel lbl3 = new JLabel("Date de naissance (dd-MM-yyyy)");
        panelPrincipal.add(lbl3);
        this.date = new JTextField(20);
        panelPrincipal.add(this.date);

        RequestCB request = new RequestCB(conn);
        JLabel lbl4 = new JLabel("Code du cirque d'origine");
        panelPrincipal.add(lbl4);
        request.getCirques();
        this.codeCirque = request.getCb();
        this.cbCorrespondance = request.getMap();
        // Ajout d'une option code cirque nul;
        this.codeCirque.addItem("Aucun");
        panelPrincipal.add(this.codeCirque);

        JLabel lbl5 = new JLabel("Numero de telephone");
        panelPrincipal.add(lbl5);
        this.tel = new JTextField(20);
        panelPrincipal.add(this.tel);

        JPanel panelBot = new JPanel();
        this.ajoutSpecialte = new JButton("Ajouter des spécialités");
        ajoutSpecialte.addActionListener(this);
        panelBot.add(ajoutSpecialte);
        this.confirm = new JButton("Envoyer");
        confirm.addActionListener(this);
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
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.ajoutSpecialte) {
            new AddSpecialite(conn, this);
        } else {
            if (this.specialite.isEmpty()) {
                JOptionPane.showMessageDialog(this, "L'artiste doit avoir au moins une spécialité");
            } else {

                try {
                    int idArtisteint = Integer.parseInt(this.idArtiste.getText());
                    String nomString = this.nom.getText();
                    if (nomString.equals("")){
                        throw new IllegalArgumentException("Nom ne peut pas être nul");
                    }
                    String prenomString = this.prenom.getText();
                    if (prenomString.equals("")){
                        throw new IllegalArgumentException("Prénom ne peut pas être nul");
                    }
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    long mills = 0;
                    mills = dateFormat.parse(this.date.getText()).getTime();
                    java.sql.Date dateNaissance = new java.sql.Date(mills);
                    int codeCirqueint;
                    if (this.codeCirque.getSelectedIndex() == this.codeCirque.getItemCount() - 1) {
                        // L'utilisateur a choisi "Aucun", on pose codeCirqueint à -1 pour placer un null dans la base de données
                        codeCirqueint = -1;
                    } else {
                        codeCirqueint = this.cbCorrespondance.get(this.codeCirque.getSelectedIndex());
                    }
                    String telString = this.tel.getText();
                    if (telString.equals("")){
                        throw new IllegalArgumentException("Le numéro de téléphone ne peut pas être nul");
                    }
                    conn.setSavepoint();
                    new AjoutArtiste(conn, idArtisteint, nomString, prenomString, dateNaissance, codeCirqueint, telString);

                    if (expertCheck.isSelected()) {
                        new AjoutExpert(conn, idArtisteint, nomString, prenomString, dateNaissance, codeCirqueint, telString);
                    }
                    for (String specialiteString : this.specialite) {
                        new AjoutSpecialite(conn, idArtisteint, specialiteString);
                    }
                    conn.commit();
                    JOptionPane.showMessageDialog(this, "L'artiste a bien été ajouté");


                } catch (NumberFormatException error) {
                    JOptionPane.showMessageDialog(this, "Veuillez rentrer un idArtiste valide");
                } catch (ParseException error) {
                    JOptionPane.showMessageDialog(this, "Veuillez rentrer une date avec un bon format");
                } catch (SQLException error) {
                    ConnectionManagement.rollback(conn);
                    if (error.getErrorCode() == 1){
                        // La clé primaire existe déjà
                        JOptionPane.showMessageDialog(this, "L'idArtiste existe déjà");
                    }
                    else{
                        JOptionPane.showMessageDialog(this, error.getMessage());
                    }

                }
                catch (IllegalArgumentException error){
                    JOptionPane.showMessageDialog(this, error.getMessage());
                }
            }
        }
    }


    public Set<String> getSpecialite() {
        return specialite;
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
