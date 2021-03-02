package Interface;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import Transaction.RequestJury;

public class JurysController implements ActionListener {
    private enum Etat{
        SELECT_NUMERO,
        SELECT_EXPERTS,
        ENTER_EVALUATIONS,
    }
    private Connection conn;
    private Etat etat;
    private JurysIHM vue;
    private HashMap<Integer, PairExpert> expertsSelectionnes;
    private RequestJury requestJury;

    public JurysController(Connection conn, JurysIHM vue) throws SQLException {
        this.conn = conn;
        this.vue = vue;
        this.expertsSelectionnes = new HashMap<>();
        this.requestJury = new RequestJury(conn);
        traiterInit();
    }

    public void addExpertsSelectionne(Integer identifiant, String nom, boolean isSpe) {
        PairExpert pair = new PairExpert(nom, isSpe);
        this.expertsSelectionnes.put(identifiant, pair);
    }

    public void removeExpertsSelectionne(Integer identifiant) {
        this.expertsSelectionnes.remove(identifiant);
    }

    public Set<Integer> getExpertsSelectionnes(boolean isSpe) {
        return this.expertsSelectionnes.entrySet().stream()
                .filter(x -> x.getValue().isSpe() == isSpe)
                .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue())).keySet();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        System.out.println("Ancien Etat : " + this.etat);
        JButton btn = (JButton) actionEvent.getSource();
        try {
            switch(etat) {
                case SELECT_EXPERTS:
                    if (btn.getText().equals("Valider")) {
                        traiterSelectExpert();
                    }
                    break;
                default:
                    return;
            }
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }
        System.out.println("---> Nouvel Etat : " + this.etat);
    }


    public void traiterInit() {
        try {
            this.etat = Etat.SELECT_NUMERO;

            HashMap<Integer, String> titresMap = this.requestJury.getNumerosTitre();
            JComboBox<KeyValue> listeNumeros = this.vue.getListeNumeros();
            listeNumeros.addItem(new KeyValue(-1, "--"));
            for (Map.Entry<Integer, String> entry : titresMap.entrySet()) {
                listeNumeros.addItem(new KeyValue(entry.getKey(), entry.getValue()));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this.vue, e.getMessage());
        }
    }

    public void traiterSelectNumero(int numero) {
        try {
            this.etat = Etat.SELECT_EXPERTS;
            HashMap<Integer, String> expertNonSpe = this.requestJury.getAvailableExpert(numero, false);
            HashMap<Integer, String> expertSpe = this.requestJury.getAvailableExpert(numero, true);

            DefaultTableModel modelNonSpe = this.vue.getModelExpertNonSpe();
            for (Map.Entry<Integer, String> entry : expertNonSpe.entrySet()) {
                modelNonSpe.addRow(new Object[]{entry.getKey(), entry.getValue(), false});
            }

            DefaultTableModel modelSpe = this.vue.getModelExpertSpe();
            for (Map.Entry<Integer, String> entry : expertSpe.entrySet()) {
                modelSpe.addRow(new Object[]{entry.getKey(), entry.getValue(), false});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this.vue, e.getMessage());
        }
    }

    private void traiterSelectExpert() throws SQLException {
        this.etat = Etat.ENTER_EVALUATIONS;
        this.vue.setEnabled(false);
        int numero = this.vue.getSelectedNumero();
        InputEvaluation inputEvaluation = new InputEvaluation(conn, numero, this.expertsSelectionnes);
    }

    public void clearExpertsSelectionnes() {
        this.expertsSelectionnes.clear();
    }



}
