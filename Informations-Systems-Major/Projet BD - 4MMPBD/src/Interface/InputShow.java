
package Interface;

import Transaction.AjoutSpectacle;
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
import java.util.Date;
import java.util.HashMap;


/**
 * Created by lial on 11/21/19.
 */


public class InputShow extends JFrame implements ActionListener, WindowListener{
    private Connection conn;
    private SelectShow parent;
    private JTextField codeSpectacle;
    private JTextField prix;
    private JComboBox<String> theme;
    private JTextField date;
    private JComboBox<String> presentateur;
    private JComboBox<String> heureDebut;
    HashMap<Integer, String> cbTheme;
    HashMap<Integer, Integer> cbPresentateur;
    public InputShow(Connection conn, SelectShow parent){
        super();
        this.conn = conn;
        this.parent = parent;
        setTitle("Ajouter un spectacle");
        setSize(600, 400);
        JPanel panelPrincipal = new JPanel();
        GridLayout layout = new GridLayout(0,2);
        layout.setVgap(20);
        panelPrincipal.setLayout(layout);
        RequestCB request = new RequestCB(conn);

        JLabel lbl0 = new JLabel("Code Spectacle");
        panelPrincipal.add(lbl0);
        this.codeSpectacle = new JTextField(20);
        panelPrincipal.add(this.codeSpectacle);

        JLabel lbl1 = new JLabel("Prix");
        panelPrincipal.add(lbl1);
        this.prix = new JTextField(20);
        panelPrincipal.add(this.prix);

        JLabel lbl2 = new JLabel("Thème");
        panelPrincipal.add(lbl2);
        request.getThemes(false);
        this.theme = request.getCb();
        this.cbTheme = request.getMapString();
        panelPrincipal.add(this.theme);

        JLabel lbl3 = new JLabel("Date début (dd-MM-yyyy)");
        panelPrincipal.add(lbl3);
        this.date = new JTextField(20);
        panelPrincipal.add(this.date);

        JLabel lbl4 = new JLabel("Présentateur");
        panelPrincipal.add(lbl4);
        request.getArtistes();
        this.presentateur = request.getCb();
        this.cbPresentateur = request.getMap();
        panelPrincipal.add(this.presentateur);

        JLabel lbl5 = new JLabel("Heure du début");
        panelPrincipal.add(lbl5);
        this.heureDebut = new JComboBox<>();
        this.heureDebut.addItem("9h");
        this.heureDebut.addItem("14h");
        panelPrincipal.add(this.heureDebut);

        JPanel panelBot = new JPanel();
        JButton btn = new JButton("Envoyer");
        btn.addActionListener(this);
        panelBot.add(btn);

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
        try {
            int codeSpectacleint = Integer.parseInt(this.codeSpectacle.getText());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            long mills = dateFormat.parse(this.date.getText()).getTime();
            java.sql.Date dateDebut = new java.sql.Date(mills);
            int prixint = Integer.parseInt(this.prix.getText());
            if (prixint < 0) {
                throw new IllegalArgumentException("Le prix doit être positif");
            }
            String themeString = this.cbTheme.get(this.theme.getSelectedIndex());
            int presentateurint = this.cbPresentateur.get(this.presentateur.getSelectedIndex());
            int heureint = this.heureDebut.getSelectedIndex() == 0 ? 9 : 14;
            AjoutSpectacle ajout = new AjoutSpectacle();
            conn.setSavepoint();
            ajout.ajoutSpectacle(conn, codeSpectacleint, dateDebut, prixint, themeString, presentateurint, heureint);
            conn.commit();
            JOptionPane.showMessageDialog(this, "Ajout d'un nouveau spectacle réussi");
            this.parent.refresh();
        }
        catch (NumberFormatException error){
            JOptionPane.showMessageDialog(this, "Veuillez un entier pour le code Spectacle ou le prix");
        }
        catch (ParseException errorDate){
            JOptionPane.showMessageDialog(this, "Veuillez rentrer une date avec le bon format");
        }
        catch (IllegalArgumentException error){
            JOptionPane.showMessageDialog(this, error.getMessage());
        }
        catch (SQLException error){
            ConnectionManagement.rollback(conn);
            if(error.getErrorCode() == 1){
                JOptionPane.showMessageDialog(this, "Un spectacle possède déjà ce code Spectacle ou, la journée ou l'horaire est déjà occupée");
            }else{
                JOptionPane.showMessageDialog(this, error.getMessage());
            }

        }
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

