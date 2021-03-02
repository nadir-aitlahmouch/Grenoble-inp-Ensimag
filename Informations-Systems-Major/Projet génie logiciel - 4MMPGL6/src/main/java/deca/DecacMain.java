package fr.ensimag.deca;

import java.io.File;
import static java.lang.Runtime.getRuntime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import static java.util.concurrent.Executors.newFixedThreadPool;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl16
 * @date 01/01/2020
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);
    
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner()) {
            System.out.println("******************************");
            System.out.println("* Groupe g3   | Equipe gl16  *");
            System.out.println("* Ange Romuald Ossohou       *");
            System.out.println("* Nadir Ait Lahmouch         *");
            System.out.println("* Hamza Benjelloun           *");
            System.out.println("* Oussama Fennane            *");
            System.out.println("* Younes Zaibila             *");
            System.out.println("******************************");
        }
        if (options.getSourceFiles().isEmpty()) {
            //throw new UnsupportedOperationException("decac without argument not yet implemented");
        }
        if (options.getParallel()) {
            // A FAIRE : instancier DecacCompiler pour chaque fichier à
            // compiler, et lancer l'exécution des méthodes compile() de chaque
            // instance en parallèle. Il est conseillé d'utiliser
            // java.util.concurrent de la bibliothèque standard Java.
            //throw new UnsupportedOperationException("Parallel build not yet implemented");
            ExecutorService pool = newFixedThreadPool(getRuntime().availableProcessors());
            for (File source : options.getSourceFiles())
            {
            Future future;
            future = pool.submit(new DecacRunnable(new DecacCompiler(options, source))); 
            future.get();
            }
   }

        else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
        }
        System.exit(error ? 1 : 0);
    }
}
