package Interface;

import Transaction.AjoutArtisteNumero;
import Transaction.ConnectionManagement;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by benkerre on 12/3/19.
 */

public class InputArtisteToNumero extends JFrame implements ActionListener, WindowListener {
    private JComboBox<KeyValue> idArtiste;
    private JComboBox<KeyValue> idNumero;
    private Connection conn;
    private AjoutArtisteNumero ajoutArtisteNumero;
    private JButton btnValider;

    public InputArtisteToNumero(Connection conn) throws SQLException {
        super();
        this.conn = conn;
        this.ajoutArtisteNumero = new AjoutArtisteNumero();
        setTitle("Ajouter un artiste à un numéro existant");
        setSize(400, 400);
        JPanel panelPrincipal = new JPanel();
        GridLayout layout = new GridLayout(0, 2);
        layout.setVgap(20);
        panelPrincipal.setLayout(layout);

        JLabel lbl1 = new JLabel("Numero");
        panelPrincipal.add(lbl1);
        this.idNumero =  new JComboBox<>();
        this.idNumero.addItem(new KeyValue(-1, "--Veuillez choisir un numéro"));
        HashMap<Integer, String> numeros = this.ajoutArtisteNumero.getNumerosTitre(conn);
        for (Map.Entry<Integer, String> entry : numeros.entrySet()) {
            this.idNumero.addItem(new KeyValue(entry.getKey(), "Id : " + entry.getKey() + " : " +entry.getValue()));
        }
        this.idNumero.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                clearIdArtiste();
                KeyValue s = (KeyValue) idNumero.getSelectedItem();
                if (s != null) {
                    if (s.getKey() != -1) {
                        try {
                            HashMap<Integer, String> numeros = ajoutArtisteNumero.getArtisteNoInNumero(conn, s.getKey());
                            for (Map.Entry<Integer, String> entry : numeros.entrySet()) {
                                idArtiste.addItem(new KeyValue(entry.getKey(), "Id : " + entry.getKey() + " : " +entry.getValue()));
                            }
                            idArtiste.setEnabled(true);
                        } catch (SQLException error) {
                            System.err.println("Select Numero Failed");
                            error.printStackTrace(System.err);
                        }
                    } else {
                        idArtiste.setEnabled(false);
                    }
                }
            }
        });
        panelPrincipal.add(this.idNumero);


        JLabel lbl0 = new JLabel("idArtiste non affecté au numéro choisi");
        panelPrincipal.add(lbl0);
        this.idArtiste = new JComboBox<>();
        this.idArtiste.addItem(new KeyValue(-1, "--"));
        this.idArtiste.setEnabled(false);
        this.idArtiste.setSelectedIndex(-1);
        this.idArtiste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                KeyValue s = (KeyValue) idArtiste.getSelectedItem();
                if (s != null) {
                    if (s.getKey() == -1) {
                        btnValider.setEnabled(false);
                    } else {
                        btnValider.setEnabled(true);
                    }
                }
            }
        });
        panelPrincipal.add(this.idArtiste);


        JPanel panelBot = new JPanel();
        this.btnValider = new JButton("Envoyer");
        this.btnValider.addActionListener(this);
        this.btnValider.setEnabled(false);
        panelBot.add(this.btnValider);


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
        int idNumero = ((KeyValue) this.idNumero.getSelectedItem()).getKey();
        int idArtisteint = ((KeyValue) this.idArtiste.getSelectedItem()).getKey();
        if (idNumero != -1 && idArtisteint != -1) {
            try {
                conn.setSavepoint();
                this.ajoutArtisteNumero.AjoutArtisteNumero(conn, idNumero, idArtisteint);
                conn.commit();
                JOptionPane.showMessageDialog(this, "Ajout de l'artiste réussi !");
                setVisible(false);
                dispose();
            } catch (SQLException e1) {
                ConnectionManagement.rollback(conn);
                JOptionPane.showMessageDialog(this, e1.getMessage());
            }
        }
    }

    private void clearIdArtiste() {
        this.idArtiste.removeAllItems();
        this.idArtiste.addItem(new KeyValue(-1, "--"));
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
