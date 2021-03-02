/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.Message;
import beans.Partie;
import beans.Utilisateur;
import beans.Joueur;
import beans.Proposed;
import dao.MessageDao;
import dao.PartieDao;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import dao.JoueurDao;
import java.util.List;
import dao.ExercerPouvoirDao;
import beans.ExercerPouvoir;
import dao.DAOException;


/**
 * Contrôleur de jeu
 * @author Equipe 9
 */

@WebServlet(name = "Jeu", urlPatterns = {"/Jeu"})
public class Jeu extends HttpServlet {
    @Resource(name = "jdbc/bibliography")
    private DataSource ds;
    public static final String ATT_USER         = "utilisateur";
    public static final String ATT_MESSAGES     = "messages";
    public static final String ATT_MAITRE     = "maitrejeu";
    public static final String ATT_FORM         = "form";
    public static final String ATT_PERIODE      = "periode";
    public static final String ATT_JOUEUR = "joueur";
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
    public static final String VUE_JOUEUR              = "/WEB-INF/jeuJoueur.jsp";
    public static final String VUE_ARCHIVE              = "/WEB-INF/jeuArchive.jsp";
    public static final String VUE_MAITRE             = "/WEB-INF/jeuMaitre.jsp";
    public static final String VUE_FIN             = "/WEB-INF/fin.jsp";
    public static final String ACCES_PUBLIC     = "/WEB-INF/connexion.jsp";
    public static final String ROLE     = "role";
    public static final String EXERCER_PV     = "exercerPouvoir";
    public static final String HUMAIN     = "humain";
    public static final String JOUEURS     = "listJoueur";
    
    
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
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        /* Récupération de la session depuis la requête */
        try{
            HttpSession session = request.getSession();
            String action = request.getParameter("action");
            String maitre = request.getParameter(ATT_MAITRE);
            MessageDao messageDao = new MessageDao(ds);
            Partie partie = new Partie();
            PartieDao partiedao = new PartieDao(ds);
            partiedao.partieEnCours(partie);
            List<Proposed> proposed = partiedao.getProposed();
            request.setAttribute("proposed", proposed);
            Joueur mort = partiedao.nouveauMort();
            if (action == null){

                if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
                        /* Redirection vers la page publique */
                         this.getServletContext().getRequestDispatcher( ACCES_PUBLIC ).forward( request, response );
                } else {

                    List<Message> messages = messageDao.getListeMessages(partie.getPeriode()==null?"archive":partie.getPeriode());
                    String pseudonyme = ((Utilisateur)session.getAttribute(ATT_SESSION_USER)).getNom(); 
                    Joueur joueur = new Joueur(pseudonyme);
                    JoueurDao joueurdao = new JoueurDao(ds);
                    joueurdao.getInformations(joueur);
                    List<Joueur> villageois = joueurdao.getListeJoueursVivants();
                    /** Chercher les informations sur le joueur **/
                    request.setAttribute("joueurs", joueurdao.getListeJoueurs());
                    request.setAttribute(ATT_MESSAGES, messages);
                    request.setAttribute(ATT_PERIODE, partie.getPeriode());
                    String m = (String) session.getAttribute(ATT_MAITRE);
                    request.setAttribute("mort", mort);
                    if((m!=null && m.equals("1"))){
                            request.setAttribute(ATT_MAITRE, "1");
                            session.setAttribute(ATT_MAITRE, "1");
                            Boolean finpartie =  joueurdao.finPartie();
                            if(finpartie){
                                String gagant = joueurdao.gagnant();
                                request.setAttribute("gagnant", gagant);
                                request.setAttribute(ATT_MESSAGES, messageDao.getListeMessages("archive"));
                                partiedao.deletePartie();
                                this.getServletContext().getRequestDispatcher( VUE_FIN ).forward( request, response );
                            }
                            else{
                                // la partie n'est pas encore finie
                                this.getServletContext().getRequestDispatcher( VUE_MAITRE ).forward( request, response );
                            }
                        
                    } else {
                            // l'utilisateur n'est pas le maitre de jeu
                            session.setAttribute(ATT_MAITRE, "0");

                            request.setAttribute("villageois", villageois);
                            request.setAttribute(ATT_JOUEUR, joueur);
                            request.setAttribute(ATT_MAITRE, "0");
                            request.setAttribute("joueurs", joueurdao.getListeJoueurs());
                            /** vérifier si le joueur à un pouvoir */
                            exercerPouvoirVoyance(request,response);
                            exercerPouvoirContamination(request,response);
                            Boolean finpartie =  joueurdao.finPartie();
                            if(finpartie){
                                String gagant = joueurdao.gagnant();
                                request.setAttribute("gagnant", gagant);
                                request.setAttribute(ATT_MESSAGES, messageDao.getListeMessages("archive"));
                                partiedao.deletePartie();
                                // si la partie est finie redirection vers la fin de jeu
                                this.getServletContext().getRequestDispatcher( VUE_FIN ).forward( request, response );
                            
                            } else {
                                // sinon vers la page de joueur
                                this.getServletContext().getRequestDispatcher( VUE_JOUEUR ).forward( request, response );
                            }

                        }

                    }

                
            } else {
                
                if (session.getAttribute(ATT_SESSION_USER)==null){
                    this.getServletContext().getRequestDispatcher( ACCES_PUBLIC ).forward( request, response );
                }
                if (action.equals("addVote")){
                    String voter = ((Utilisateur)session.getAttribute(ATT_SESSION_USER)).getNom();
                    String pseudo = request.getParameter("id");
                    addVote(request, response, partiedao, proposed, pseudo, voter);
                    action = null;
                    response.sendRedirect("/projetAcol/Jeu");
                
                } else if (action.equals("removeVote")){
                    String voter = ((Utilisateur)session.getAttribute(ATT_SESSION_USER)).getNom();
                    String pseudo = request.getParameter("id");
                    partiedao.retirerVote(pseudo, voter);
                    action = null;
                    response.sendRedirect("/projetAcol/Jeu");
                }
                
            }
        } catch (DAOException e){
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
                request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        try {
            
            HttpSession session = request.getSession();
            MessageDao messageDao = new MessageDao(ds);
            Partie partie = new Partie();
            PartieDao partiedao = new PartieDao(ds);
            partiedao.partieEnCours(partie);
            List<Message> messages;
            
            /** si l'utilisateur veut envoyer un message **/
            if(action.equals("SendMess")){
               String pseudoName = ((Utilisateur)session.getAttribute(ATT_SESSION_USER)).getNom();
               String contenu = request.getParameter("contenu");
               if(!contenu.equals("")){
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
                String dateString = sdf.format(date);
                Message m = new Message(dateString, pseudoName, contenu, partie.getPeriode());
                messageDao.addMessage(m, partie.getPeriode());
               }
               messages = messageDao.getListeMessages(partie.getPeriode());
               request.setAttribute(ATT_MESSAGES, messages);
               response.sendRedirect("/projetAcol/Jeu");
           }
            /** si le joueur est éliminé **/
            if(action.equals("archive")){
                messages = messageDao.getListeMessages("archive");
                request.setAttribute(ATT_MESSAGES, messages);
                this.getServletContext().getRequestDispatcher( VUE_ARCHIVE).forward( request, response );
            }
            /** si le joueur possède un pouvoir de contamination **/
            if (action.equals("pouvoirContamination")){
                messages = messageDao.getListeMessages(partie.getPeriode());
                String pseudonyme = ((Utilisateur)session.getAttribute(ATT_SESSION_USER)).getNom(); 
                Joueur joueur = new Joueur(pseudonyme);
                JoueurDao joueurdao = new JoueurDao(ds);
                joueurdao.getInformations(joueur);
                /** Chercher les informations sur le joueur **/

                request.setAttribute(ATT_MESSAGES, messages);
                request.setAttribute(ATT_PERIODE, partie.getPeriode());
                request.setAttribute(ATT_JOUEUR, joueur);
                exercerPouvoirContamination(request,response);
                response.sendRedirect("/projetAcol/Jeu");
               
            }
            /** si le joueur possède un pouvoir de voyance **/
            if (action.equals("pouvoirVoyance")){

                messages = messageDao.getListeMessages(partie.getPeriode());
                String pseudonyme = ((Utilisateur)session.getAttribute(ATT_SESSION_USER)).getNom(); 
                Joueur joueur = new Joueur(pseudonyme);
                JoueurDao joueurdao = new JoueurDao(ds);
                joueurdao.getInformations(joueur);
                /** Chercher les informations sur le joueur **/

                request.setAttribute(ATT_MESSAGES, messages);
                request.setAttribute(ATT_PERIODE, partie.getPeriode());
                request.setAttribute(ATT_JOUEUR, joueur);

                exercerPouvoirVoyance(request,response);
                response.sendRedirect("/projetAcol/Jeu");
                
            }
            /** si le joueur propose un vote **/
            
            if (action.equals("proposer")){
                String nameProposed = request.getParameter("villageois");
                if (!nameProposed.equals("nothing")){
                    String voter = ((Utilisateur)session.getAttribute(ATT_SESSION_USER)).getNom();
                    if (request.getParameter("propose")!=null){
                        partiedao.retirerVote(request.getParameter("propose"), voter);
                    }
                    partiedao.proposerVillageois(nameProposed, voter);
                }

                response.sendRedirect("/projetAcol/Jeu");
            }
        
        
        } catch (DAOException e){
            erreurBD(request, response, e);
        }
        
    }
    
    /**
     * Gérer l'utilisation de pouvoir de voyance.
     */
    private void exercerPouvoirVoyance(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        try {
            /* Récupération de la session depuis la requête */
            HttpSession session = request.getSession();
            String pseudonyme = ((Utilisateur)session.getAttribute(ATT_SESSION_USER)).getNom(); 
            Joueur joueur = new Joueur(pseudonyme);
            JoueurDao joueurDao = new JoueurDao(ds);
            joueurDao.getInformations(joueur);
            // check if the player had already exercise its power
            Joueur exercerSur = joueurDao.checkExercerPv(joueur);
            if (exercerSur == null){
                request.setAttribute(EXERCER_PV,false);
                ExercerPouvoirDao exercerPvDao = new ExercerPouvoirDao(ds);
                List<Joueur> joueurs  =  exercerPvDao.getJoeurs(pseudonyme);
                request.setAttribute(JOUEURS,joueurs);
                /** exercer le pouvoir **/
                String name = request.getParameter("voyance");
                if (name != null){
                    /** le joueur veut appliqué son pouvoir **/ 
                    ExercerPouvoir exercerPv = new ExercerPouvoir();
                    exercerPv.setExercerPar(pseudonyme);
                    exercerPv.setExercerSur(name);
                    exercerPvDao.appliqueVoyance(exercerPv);
                    request.setAttribute(EXERCER_PV,true);
                    Joueur voyanceAp = new Joueur(name);
                    joueurDao.getInformations(voyanceAp);
                    request.setAttribute("voyanceAp",voyanceAp);
                }
            } else {
                request.setAttribute(EXERCER_PV,true);
                joueurDao.getInformations(exercerSur);
                request.setAttribute("voyanceAp",exercerSur);

            }
            
        } catch (DAOException e){
            erreurBD(request, response, e);
        }
    }
    
    /**
     * Gérer l'utilisation de pouvoir de contamination.
     */
    private void exercerPouvoirContamination(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            
            request.setCharacterEncoding("UTF-8");
            /* Récupération de la session depuis la requête */
            HttpSession session = request.getSession();
            String pseudonyme = ((Utilisateur)session.getAttribute(ATT_SESSION_USER)).getNom(); 
            Joueur joueur = new Joueur(pseudonyme);
            JoueurDao joueurDao = new JoueurDao(ds);
            joueurDao.getInformations(joueur);
            // check if the player had already exercise its power
            Joueur exercerSur = joueurDao.checkExercerPv(joueur);
            if (exercerSur == null){
                request.setAttribute(EXERCER_PV,false);
                ExercerPouvoirDao exercerPvDao = new ExercerPouvoirDao(ds);
                List<Joueur> humains  =  exercerPvDao.getHumains();
                request.setAttribute(HUMAIN,humains);
                /** exercer le pouvoir **/
                String name = request.getParameter("contamine");
                if (name != null){
                    /** le joueur veut appliqué son pouvoir **/ 
                    ExercerPouvoir exercerPv = new ExercerPouvoir();
                    exercerPv.setExercerPar(pseudonyme);
                    exercerPv.setExercerSur(name);
                    exercerPvDao.appliqueContamination(exercerPv);
                    request.setAttribute(EXERCER_PV,true);
                    request.setAttribute("exercerSur",name);
                }
            } else {
                request.setAttribute(EXERCER_PV,true);
                request.setAttribute("exercerSur",exercerSur.getPseudonyme());
            }
            
        } catch (DAOException e){
            erreurBD(request, response, e);
        }
           
    }
    
    /**
     * Ajouter un vote
     */
    private void addVote(HttpServletRequest request, 
                         HttpServletResponse response, PartieDao partiedao,
                         List<Proposed> proposed, String pseudo, String voter) 
            throws ServletException, IOException {
        try {
                for (Proposed joueur:proposed){

                    if (joueur.getVote().contains(voter)){
                        if (!joueur.getPseudonyme().equals(pseudo)){

                            partiedao.retirerVote(joueur.getPseudonyme(), voter);
                        }
                    }
                    else{
                        if (joueur.getPseudonyme().equals(pseudo)){

                            partiedao.proposerVillageois(pseudo, voter);
                        }
                    }
                }
            } catch (DAOException e){
            erreurBD(request, response, e);
        }
            
    }
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>


}
