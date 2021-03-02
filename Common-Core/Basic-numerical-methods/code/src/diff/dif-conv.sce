Nx=200;
Nt=200;
kappa=0.01;
exec("my_cholesky.sce")
dt = 1/Nt
dx = 1/Nx
function y=phi_0(x)
    if (x >= 0) & (x < 0.25) then
        y = 0
    elseif (x >= 0.25) & (x < 0.375) then
        y = 2*(x - 0.25)
    elseif (x >= 0.375) & (x < 0.5) then
        y = 2*(0.5 - x)
    elseif (x >= 0.5) & (x <= 1) then
        y = 0
    end
endfunction

function y=conv(x)
    y = 0.4*(x - 0.25)
endfunction

function A=matrice_M(N)
    A = zeros(N,N)
    for j=1:N
        c=conv((j-1)*dx);
        A(j,j) = 1-c^2*dt^2/dx^2
        if j>=2 then
            A(j-1,j) = -c*dt/(2*dx) + c^2*dt^2/(2*dx^2);
        else
            A(N,1) = -c*dt/(2*dx) + c^2*dt^2/(2*dx^2);
        end;
        if j<=N-1 then
            A(j+1,j) = c*dt/(2*dx) + c^2*dt^2/(2*dx^2);
        else
            A(1,N) = c*dt/(2*dx) + c^2*dt^2/(2*dx^2);
        end;
    end;
endfunction

function A=matrice_N(N)
    A = zeros(N,N)
    for i=1:N
        A(i,i)=1+2*kappa*(dt/dx^2);
        if i+1<=N then
            A(i,i+1)=-kappa*(dt/dx^2);
        else //donc i=Nt
            A(i,1)=-kappa*(dt/dx^2);
        end;
        if i-1>=1 then
            A(i,i-1)=-kappa*(dt/dx^2);
        else //donc i=1
            A(i,N)=-kappa*(dt/dx^2);
        end;
    end;
endfunction


maillage = 0:dx:dx*(Nx-1)
//--------------------
//initialisation phi et assembler les matrices M et N
//-------------------
phi_i = zeros(Nx,1);
N = matrice_N(Nx);
M = matrice_M(Nx);

for k=1:Nx
    phi_i(k) = phi_0((k-1)*dx);
end

fin=Nt;
phi = phi_i
for i=1:fin
    phi=my_cholesky(N,M*phi);
end
scf;
plot(maillage,[phi_i,phi]);
