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
import java.util.*;

import Transaction.*;

/**
 * Created by lial on 11/21/19.
 */

public class InputEvaluation extends JFrame implements ActionListener{
    private JLabel codeN;
    private JLabel noms[];
    private ArrayList<JComboBox<String>> noteComboBox;
    private JTextArea evaluationTextArea[];
    private Connection conn;

    private int numero;
    private int identifiants[];

    public InputEvaluation(Connection conn, int numero, HashMap<Integer, PairExpert> selectedArtistes) throws SQLException{
        this.conn = conn;
        this.numero = numero;
        this.noms = new JLabel[5];
        this.noteComboBox = new ArrayList<>();

        this.evaluationTextArea = new JTextArea[5];
        this.identifiants = new int[5];
        setTitle("Ajouter une évaluation");
        setSize(600, 400);
        JPanel panelPrincipal = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        panelPrincipal.setLayout(layout);
        c.fill = GridBagConstraints.BOTH;


        JLabel lbl0 = new JLabel("Expert");

        lbl0.setFont(lbl0.getFont().deriveFont(lbl0.getFont().getStyle() | Font.BOLD));
        JLabel lbl1 = new JLabel("Note");
        lbl1.setFont(lbl1.getFont().deriveFont(lbl1.getFont().getStyle() | Font.BOLD));
        JLabel lbl2 = new JLabel("Evalutation");
        lbl2.setFont(lbl2.getFont().deriveFont(lbl2.getFont().getStyle() | Font.BOLD));

        c.insets = new Insets(5, 5, 5, 5);
        c.gridy = 0;
        c.weighty = 0;
        c.gridx = 0;
        c.weightx = 0.30;
        panelPrincipal.add(lbl0, c);
        c.gridx = 1;
        c.weightx = 0.03;
        panelPrincipal.add(lbl1, c);
        c.gridx = 2;
        c.weightx = 0.60;
        panelPrincipal.add(lbl2, c);

        c.weighty = 0.20;

        String notes[] = new String[11];
        for (int i = 0; i <= 10; i++) {
            notes[i] = Integer.toString(i);
        }
        int i = 0;
        for (Map.Entry<Integer, PairExpert> entry : selectedArtistes.entrySet()) {
            this.identifiants[i] = entry.getKey();
            PairExpert pair = entry.getValue();
            c.gridy = i + 1;
            String nom = pair.getNom();
            this.noms[i] = new JLabel();
            this.noms[i].setText(nom);
            c.gridx = 0;
            c.weightx = 0.30;
            panelPrincipal.add(this.noms[i], c);

            c.gridx = 1;
            c.weightx = 0.03;
            this.noteComboBox.add(new JComboBox<>(notes));
            panelPrincipal.add(this.noteComboBox.get(i), c);

            c.gridx = 2;
            c.weightx = 0.60;
            this.evaluationTextArea[i] = new JTextArea();
            panelPrincipal.add(this.evaluationTextArea[i], c);
            i++;
        }



        // Bottom
        JPanel panelBot = new JPanel();
        JButton btn = new JButton("Envoyer");
        btn.addActionListener(this);
        panelBot.add(btn);


        // Top
        JPanel panelTop = new JPanel();
        JPanel titleContainer = new JPanel();
        panelTop.add(titleContainer);
        JLabel lbl = new JLabel("Veuillez rentrer les informations");
        titleContainer.add(lbl);
        JPanel subTitleContainer = new JPanel();
        panelTop.add(subTitleContainer);
        this.codeN = new JLabel("Numéro évalué : " + numero);
        subTitleContainer.add(this.codeN);

        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        panelPrincipal.setBorder(padding);
        getContentPane().add(BorderLayout.NORTH, panelTop);
        getContentPane().add(BorderLayout.CENTER, panelPrincipal);
        getContentPane().add(BorderLayout.SOUTH, panelBot);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        int i = 0;
        for (JLabel nom : this.noms) {
            int idArtisteInt = this.identifiants[i];
            int codeNInt = this.numero;
            int noteInt = this.noteComboBox.get(i).getSelectedIndex();
            String evaluationString = this.evaluationTextArea[i].getText();
            AjoutEvaluation add = new AjoutEvaluation();

            try {
                conn.setSavepoint();
                add.ajoutEvaluation(conn, idArtisteInt, codeNInt, noteInt, evaluationString);
                conn.commit();
            }catch (SQLException error){
                ConnectionManagement.rollback(conn);
                // L'utilisateur a rentré des clés primaires qui existent déjà
                if (error.getErrorCode() == 1){
                    JOptionPane.showMessageDialog(this, "L'expert de la ligne " + (i + 1) + " a déjà évalué ce numéro");
                }
                // L'utilisateur a rentré une note illégale
                else if(error.getErrorCode() == 2290){
                    JOptionPane.showMessageDialog(this, "La note de l'expert de la ligne " + (i + 1) + " doit être comprise entre 0 et 10");
                }
                else{
                    System.err.println(error.getStackTrace());
                }
            }
            i++;
        }
        JOptionPane.showMessageDialog(this, "Ajouts d'évaluations terminée");
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
