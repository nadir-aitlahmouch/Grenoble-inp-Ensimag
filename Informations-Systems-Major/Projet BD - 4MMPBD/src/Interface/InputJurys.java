package Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.SQLException;

public class InputJurys extends JFrame {
    private JurysIHM vue;

    public InputJurys(Connection conn) throws SQLException {
        this.vue = new JurysIHM(conn);
        this.setTitle("Création d'un jury pour un numéro");
        this.setLayout(new GridLayout(1,1));
        this.add(this.vue);
        this.setSize(800, 600);
        this.setVisible(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }



}
