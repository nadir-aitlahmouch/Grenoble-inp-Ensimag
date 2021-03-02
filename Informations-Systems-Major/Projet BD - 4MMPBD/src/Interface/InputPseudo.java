package Interface;

/**
 * Created by benkerre on 11/26/19.
 */

import Transaction.AjoutEvaluation;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import java.util.HashMap;
import Transaction.*;

public class InputPseudo extends JFrame implements ActionListener, WindowListener {
    private JComboBox<String> idArtiste;
    private JTextField pseudo;
    private Connection conn;
    HashMap<Integer, Integer> cbCorrespondance;

    public InputPseudo(Connection conn) throws SQLException {
        super();
        this.conn = conn;
        setTitle("Ajouter un Pseudo");
        setSize(400, 400);
        JPanel panelPrincipal = new JPanel();
        GridLayout layout = new GridLayout(0, 2);
        layout.setVgap(20);
        panelPrincipal.setLayout(layout);

        RequestCB request = new RequestCB(conn); //
        JLabel lbl0 = new JLabel("idArtiste");
        panelPrincipal.add(lbl0);
        request.getArtistes();
        this.idArtiste = request.getCb();
        this.cbCorrespondance = request.getMap();
        panelPrincipal.add(this.idArtiste);


        JLabel lbl1 = new JLabel("Pseudo");
        panelPrincipal.add(lbl1);
        this.pseudo = new JTextField(20);
        panelPrincipal.add(this.pseudo);

        JPanel panelBot = new JPanel();
        JButton btn = new JButton("Envoyer");
        btn.addActionListener(this);
        panelBot.add(btn);


        JPanel panelTop = new JPanel();
        JLabel lbl = new JLabel("Veuillez rentrer les informations");
        panelTop.add(lbl);

        Border padding = BorderFactory.createEmptyBorder(100, 10, 100, 10);
        panelPrincipal.setBorder(padding);
        getContentPane().add(BorderLayout.NORTH, panelTop);
        getContentPane().add(BorderLayout.CENTER, panelPrincipal);
        getContentPane().add(BorderLayout.SOUTH, panelBot);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e){
        int idArtisteint = this.cbCorrespondance.get(this.idArtiste.getSelectedIndex());
        String pseudo = this.pseudo.getText();
        try {
            conn.setSavepoint();
            AjoutPseudo ajout = new AjoutPseudo(conn, idArtisteint, pseudo);
            conn.commit();
            JOptionPane.showMessageDialog(this, "Ajout du pseudo réussi");
            setVisible(false);
            dispose();
        } catch (SQLException e1) {
            ConnectionManagement.rollback(conn);
            if (pseudo.length() == 0){
                JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ pseudo");
            }else {
                JOptionPane.showMessageDialog(this, "L'artiste choisi a déjà ce Pseudo\n        Donnez lui un autre ;)");
            }
        }


    }

    // Methods to manage window methods
    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

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


