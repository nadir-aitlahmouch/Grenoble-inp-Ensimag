package Interface;

public class PairExpert {
    private String nom;
    private boolean isSpe;

    public PairExpert(String nom, boolean isSpe) {
        this.nom = nom;
        this.isSpe = isSpe;
    }

    public String getNom() {
        return nom;
    }

    public boolean isSpe() {
        return isSpe;
    }
}
