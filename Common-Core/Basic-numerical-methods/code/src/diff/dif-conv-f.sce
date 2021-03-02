    
function dt=calcul_dt(U,dx)
  dt=dx/max(abs(U));
endfunction



function vort=solveur_1D(vort, Ux, Nx, kappa, dt, dx)
    //----------------------------------------------
    //TODO implémenter une itération temporelle 
    // de l'algo 1D codé dans le script dif-conv.sce
    //   vort = champ transporté (correspond à phi dans le sujet)
    //   Ux = vitesse sur la composante x
    //   Nx = nombre de points de discrétisation spatiale en x et y.
    //   kappa = constante de diffusion dynamique
    //   dt = pas de temps
    //   dx = pas d'espace
	N = spzeros(Nx,Nx);
        //Construction de la matrice N
    for i=2:Nx 
        N(i-1,i-1) = 1 + 2*kappa*(dt/((dx)^2));
        N(i,i-1) = -kappa*(dt/((dx)^2));
        N(i-1,i) = -kappa*(dt/((dx)^2));
    end
    N(1,Nx) = -kappa*(dt/((dx)^2));
    N(Nx,1) = -kappa*(dt/((dx)^2));
    N(Nx,Nx) = 1 + 2*kappa*(dt/((dx)^2));
    
        //construction de M
	M = spzeros(Nx,Nx);
    for i=2:Nx
    M(i-1,i-1) = 1 - Ux(i-1)^2*(dt^2)/(dx^2);
    M(i,i-1) = Ux(i)^2*(dt^2)/(2*dx^2) + Ux(i)*(dt)/(2*dx);
    M(i-1,i) = Ux(i-1)^2*(dt^2)/(2*dx^2) - Ux(i-1)*dt/(2*dx);
    end

    M(1,Nx) = Ux(1)^2*(dt^2)/(2*dx^2) + Ux(1)*dt/(2*dx);
    M(Nx,1) = Ux(Nx)^2*(dt^2)/(2*dx^2) - Ux(Nx)*dt/(2*dx);
    M(Nx,Nx) = 1 - Ux(Nx)^2*(dt^2)/(dx^2);

	vort = umfpack(N,"\",M*vort)

endfunction

function vort=solveur_2D(vort, Ux, Uy, Nx, Ny, kappa, dt, dx, dy)
    //----------------------------------------------
    //TODO implémenter une itération temporelle 
    // de l'algo de splitting 2D utilisant l'algo 1D
    //   vort = champ 2D transporté (correspond à phi dans le sujet)
    //   Ux = vitesse sur la composante x
    //   Uy = vitesse sur la composante y
    //   (Nx, Ny) nombre de points de discrétisation spatiale en x et y.
    //   kappa = constante de diffusion dynamique
    //   dt    = pas de temps
    //   (dx, dy) pas d'espaces dans chaque direction
    //-----------------------------------------------
    for i=1:Nx
       vort(i,:) = solveur_1D(vort(i,:)',Ux(i,:),Nx,kappa,dt,dx);
    end
    
    for j=1:Ny
        vort(:,j) = solveur_1D(vort(:,j),Uy(:,j),Ny,kappa,dt,dy);
    end

    
endfunction
