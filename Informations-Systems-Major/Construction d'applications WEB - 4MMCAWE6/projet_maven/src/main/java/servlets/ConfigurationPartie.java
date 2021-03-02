/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.Partie;
import dao.DAOException;
import dao.PartieDao;
import dao.JoueurDao;
import forms.PartieForm;
import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;
import beans.Utilisateur;
import dao.UtilisateurDao;
import java.util.List;
import beans.Joueur;
import java.util.ArrayList;


/**
 * classe sui controle la création d'une partie par un utilisateur.
 * @author Equipe 9
 */
@WebServlet(name = "ConfigurationPartie", urlPatterns = {"/configurationpartie"})
public class ConfigurationPartie extends HttpServlet {

    @Resource(name = "jdbc/bibliography")
    private DataSource ds;
    public static final String ATT_PARTIE = "partie";
    public static final String ATT_FORM = "form";
    public static final String ACCES_PUBLIC     = "/WEB-INF/connexion.jsp";
    public static final String VUE = "/WEB-INF/partie.jsp";
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
    private List<Joueur> joueurs = new ArrayList<Joueur>();
    private List<String> userAjouter = new ArrayList<String>();
    private Partie partie;

    /* pages d'erreurs */
    private void invalidParameters(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/controleurErreur.jsp").forward(request, response);
    }

    /** Page d'erreur à afficher en cas d'une erreur de base de données **/
    private void erreurBD(HttpServletRequest request,
            HttpServletResponse response, DAOException e)
            throws ServletException, IOException {
        e.printStackTrace(); // permet d’avoir le détail de l’erreur dans catalina.out
        request.setAttribute("erreurMessage", e.getMessage());
        request.getRequestDispatcher("/WEB-INF/bdErreur.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *  Redirection vers la page d'inscription ou vérification de l'expiration de la session.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /* Affichage de la page de configuration */
       
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        UtilisateurDao userDao = new UtilisateurDao(ds);

        try {
            if (action == null) {
                
                if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
                    /* Redirection vers la page publique  si la session est expiré*/
                     this.getServletContext().getRequestDispatcher( ACCES_PUBLIC ).forward( request, response );
                } else {
                   
                    /* récuperer le nom du maitre de jeu */
                    Utilisateur maitre = (Utilisateur) session.getAttribute(Connexion.ATT_SESSION_USER);
                    assert(maitre != null);
                    /** Récuperer la liste des utilisateur de l'application **/
                    List<Utilisateur> utilisateurs = userDao.getListeUtilisateurs(maitre.getNom());
                    
                    /** vérifier les utilisateurs non encore ajouté àla partie **/
                    List<Utilisateur> notAdded = new ArrayList<>();
                    for (Utilisateur user : utilisateurs){
                        if (!this.userAjouter.contains(user.getNom())){
                            notAdded.add(user);
                         }
                    }
                    
                    /** mise à jours de la liste des utilisateurs non encore ajouté à la partie de jeu **/
                    request.setAttribute("utilisateur", notAdded);
                    
                    this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
                } 
            } else if (action.equals("addUser")){
                
                /**ajouté un utilisateur à la partie de jeu **/
                actionAddUser(request, response, userDao);
               
            }else if (action.equals("archive")){
                this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
            }
        } catch (DAOException e) {
            erreurBD(request, response, e);
        }
        
        
    }
    
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /* Préparation de l'objet formulaire */
      
        /* Appel au traitement et à la validation de la requête, et récupération du bean en résultant */
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        try {
             if (action.equals("LancerPartie")){
                Utilisateur maitre = (Utilisateur) session.getAttribute(Connexion.ATT_SESSION_USER);
                assert(maitre != null);
                PartieForm partieform = new PartieForm();
                PartieDao partieDao = new PartieDao(ds);
                UtilisateurDao userDao = new UtilisateurDao(ds);
                JoueurDao joueursDao = new JoueurDao(ds);
               
                /** configurer et créer une partie de jeu **/
                this.partie = partieform.configurerPartie(request, partieDao, this.joueurs, joueursDao, ds);
                /* Stockage du formulaire et du bean dans l'objet request */
                request.setAttribute(ATT_FORM, partieform);
                request.setAttribute(ATT_PARTIE, partie);
               
                if ( partieform.getErreurs().isEmpty() ) {
                    /* cas où la partie est créé avec succès */
                    /** retour à la pge principale **/
                    String redirectURL = "/projetAcol/restriction";
                    response.sendRedirect(redirectURL);
                } else {
                    /** la configuration de la partie a échoué */
                    this.joueurs.clear(); // réinitialiser les joueurs ajouté à la partie
                    this.userAjouter.clear(); 
                    List<Utilisateur> utilisateurs = userDao.getListeUtilisateurs(maitre.getNom()); 
                    List<Utilisateur> notAdded = new ArrayList<Utilisateur>();
                    for (Utilisateur user : utilisateurs){
                        if (!this.userAjouter.contains(user.getNom())){
                            notAdded.add(user);
                         }
                    }
                    /** renvoyer vers la page de configuration de la partie **/
                    request.setAttribute("utilisateur", notAdded);
                    this.getServletContext().getRequestDispatcher("/WEB-INF/partie.jsp").forward(request, response);
                }
                
                
                
            }
        } catch (DAOException e) {
            erreurBD(request, response, e);
        }

    }
    
    /**
     * Ajouté un utilisatuer à la partie
     */
    public void actionAddUser(HttpServletRequest request, HttpServletResponse response, 
            UtilisateurDao userDao) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        
        String name = request.getParameter("name");
        Joueur joueur = new Joueur(name);
        // ajouter le joueurs à la liste des joueurs existant.
        this.joueurs.add(joueur);
        this.userAjouter.add(name);
        
        // mis à jours de la liste des utilisateurs 
        Utilisateur maitre = (Utilisateur) session.getAttribute(Connexion.ATT_SESSION_USER);
        List<Utilisateur> utilisateurs = userDao.getListeUtilisateurs(maitre.getNom());
        List<Utilisateur> notAdded = new ArrayList<>();
        for (Utilisateur user : utilisateurs){
            if (!this.userAjouter.contains(user.getNom())){
                notAdded.add(user);
             }
        }
        request.setAttribute("utilisateur", notAdded);
        this.getServletContext().getRequestDispatcher("/WEB-INF/partie.jsp").forward(request, response);
    }
       
}
