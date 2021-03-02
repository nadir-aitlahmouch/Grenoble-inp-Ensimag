
// Retourne la fréquence d'échantillonage de la transformée de Fourier discrète
function [freqs]=fftfreq(N, L)
    // TODO: Calculer les fréquences d'échantillonage en fonction de L et de la parité de N
    if modulo(N,2) == 0 then
        [freq_pos] = linspace(0,(N/2)-1,N/2)
        [freq_neg] = linspace(-N/2,-1,N/2)
    else [freq_pos] = linspace(0,(N-1)/2,(N+1)/2)
         [freq_neg] = linspace(-(N-1)/2,-1,(N-1)/2) 
    end
    freqs = cat(2, [freq_pos], [freq_neg]) ./ L
endfunction


// Résolution de l'équation de Poisson en dimension 2 en utilisant la FFT
//    laplacien(psi) = f
// Entrée: f de taille (Ny,Nx) sur une domaine de taille (Ly,Lx)
// Sortie: psi, solution de l'équation
function [psi]=poisson_2d(f, Nx, Ny, Lx, Ly)
    kx = 2 * %i * %pi .* fftfreq(Nx, Lx)
    ky = 2 * %i * %pi .* fftfreq(Ny, Ly)
    psi_hat = zeros(Ny,Nx)
    f_hat = fft(f, "nonsymmetric")
    // commeantaire à faire
    for p=1:Ny
        for q=1:Nx
            if p == 1 & q == 1 then
                psi_hat(p,q) = 0 
            else psi_hat(p,q) = f_hat(p, q) / (kx(q) ** 2 + ky(p)**2)
            end
        end
    end
    psi = real(ifft(psi_hat, "nonsymmetric"))
endfunction

// Résolution de l'équation de Poisson avec rot en dimension 2 en utilisant la FFT
//    laplacien(Ux) = -dW/dy
//    laplacien(Uy) = +dW/dx
// Entrée: champs de vorticité W de taille (Ny,Nx) sur un domaine de taille (Ly,Lx)
// Sortie: Ux et Uy, vitesses solution des équations
function [Ux,Uy]=poisson_curl_2d(W, Nx, Ny, Lx, Ly)
    // TODO: Calculer Ux et Uy à partir de la vorticité par FFT avec l'option 'nonsymmetric'
    kx = 2 * %i * %pi .* fftfreq(Nx, Lx)
    ky = 2 * %i * %pi .* fftfreq(Ny, Ly)
    Ux_hat = zeros(Ny,Nx)
    Uy_hat = zeros(Ny,Nx)
    W_hat = fft(W, "nonsymmetric")
    // commeantaire à faire
    for p=1:Ny
        for q=1:Nx
            if p == 1 & q == 1  then
                 Ux_hat(p,q) = 0 
                 Uy_hat(p,q) = 0
            else 
                Ux_hat(p,q) = -ky(p) * W_hat(p, q) / (kx(q) ** 2 + ky(p)**2) 
                Uy_hat(p,q) = kx(q) * W_hat(p, q) / (kx(q) ** 2 + ky(p) ** 2)
            end
        end
    end
    Ux = real(ifft(Ux_hat, "nonsymmetric"))
    Uy = real(ifft(Uy_hat, "nonsymmetric"))
endfunction

