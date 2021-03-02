package fr.ensimag.deca;

import fr.ensimag.ima.pseudocode.RegisterManager;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl16
 * @date 01/01/2020
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;
    public int getDebug() {
        return debug;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }
    
    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }
    private int debug;
    private boolean parallel;
    private boolean printBanner;
    private boolean verify;
    private boolean parser; 
    private boolean noverify;
    private int Rmax;
    private List<File> sourceFiles;
    public CompilerOptions()
    {
        debug = 0;
        parallel = false;
        printBanner = false;
        verify = false;
        parser = false; 
        noverify = false;
        Rmax = 15;
        sourceFiles = new ArrayList<File>();
    }

    public boolean getParser(){
        return parser;
    }
    
    public boolean getVerify(){
        return verify;
    }

    public int getRmax() {
        return Rmax;
    }
    
    public boolean getNoverify() {
        return noverify;
    }
    
    public void parseArgs(String[] args) throws CLIException {
        if(args.length != 0){
	for(int i = 0; i< args.length; i++){
            switch(args[i].charAt(0)){
                case '-':
                    switch(args[i].charAt(1)){
                        case 'b':
                            if(args.length > i + 1)
                            {
                                throw new CLIException("b option do not have a file as argument \n");
                            }
                            this.printBanner = true;
                            break;
                        case 'p':
                            if(args.length <= i + 1 || (args.length > i + 1 && args[i+1].equals("-b")))
                            {
                                throw new CLIException("you must enter the file name \n");
                            }
                            this.parser = true;                            
                            break;
                        case 'v' :
                            if(args.length <= i + 1 || (args.length > i + 1 && args[i+1].equals("-b")))
                            {
                                throw new CLIException("you must enter the file name \n");
                            }
                            this.verify = true;
                            break;
                        case 'P':
                            if(args.length <= i + 1 || (args.length > i + 1 && args[i+1].equals("-b")))
                            {
                                throw new CLIException("you must enter the file name \n");
                            }
                            this.parallel = true;
                            break;
                        case 'r':
                            if(args.length <= i + 1)
                            {
                               throw new CLIException("you must enter the number of registers\n");
                            }
                            if(!(Integer.parseInt(args[i+1]) >= 4  && Integer.parseInt(args[i+1]) < 16))
                            {
                                throw new CLIException("the number of register must be between 4 and 16\n");
                            }
                            if(args.length <= i + 2 || (args.length > i + 1 && args[i+1].equals("-b")))
                            {
                                throw new CLIException("you must enter the file name \n");
                            }
                            Rmax = Integer.parseInt(args[i+1]);
                            i += 1;
                            break;
                        case 'n':
                            if(args.length <= i + 1 || (args.length > i + 1 && args[i+1].equals("-b")))
                            {
                                throw new CLIException("you must enter the file name \n");
                            }
                            this.noverify = true;
                            break;
                        default:
                            throw new CLIException("Precisez svp les options !");
                    }
                break;
                default:
                    File newFile = new File(args[i]);                   
                    if(!newFile.exists())
                        throw new CLIException("Le fichier donnee en parametre n'existe pas");                  
                    sourceFiles.add(newFile);
            }
            
        }
        }
        else{
                throw new CLIException("");
        }
        
        //sourceFiles.add(new File(args[0])); //ligne ajoutee
        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
        case QUIET: break; // keep default
        case INFO:
            logger.setLevel(Level.INFO); break;
        case DEBUG:
            logger.setLevel(Level.DEBUG); break;
        case TRACE:
            logger.setLevel(Level.TRACE); break;
        default:
            logger.setLevel(Level.ALL); break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }

        
    }

    protected void displayUsage() {
        System.out.println("Ligne de commande de decac");
        System.out.println("syntaxe d'utilisation\n");
        System.out.println("decac [[-p | -v] [-n] [-r X] [-d]* [-P] [-w] <fichier deca>...] | [-b]");
        System.out.println("-b       (banner)       : affiche une bannière indiquant le nom de l’équipe\n");
        System.out.println("-p       (parse)        : arrête decac après l’étape de construction de\n"
                          +"                          l’arbre, et affiche la décompilation de ce dernier\n"
                          +"                          (i.e. s’il n’y a qu’un fichier source à\n"
                          +"                          compiler, la sortie doit être un programme\n"
                          +"                          deca syntaxiquement correct)\n");
        System.out.println(" -v       (verification) : arrête decac après l’étape de vérifications\n"
                          +                            " (ne produit aucune sortie en l’absence d’erreur)\n");
        System.out.println("-n       (no check)     : supprime les tests de débordement à l’exécution\n"
                          +"                           - débordement arithmétique\n"
                          +"                           - débordement mémoire\n"
                          +"                           - déréférencement de null\n");
        System.out.println("-r X     (registers)    : limite les registres banalisés disponibles à\n"
                          +"                          R0 ... R{X-1}, avec 4 <= X <= 16\n");
        System.out.println("-d       (debug)        : active les traces de debug. Répéter\n"
                          +"                          l’option plusieurs fois pour avoir plus de\n"
                          +"                          traces.");
        System.out.println("-P       (parallel)     : s’il y a plusieurs fichiers sources,\n"
                          +"                          lance la compilation des fichiers en\n"
                          +"                          parallèle (pour accélérer la compilation)\n");
    }
}
