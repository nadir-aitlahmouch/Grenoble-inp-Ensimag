
Nx=100;
Ny=100;
nu=0.01;
Lx=1;
Ly=1;
Tf=0.5;
kappa=nu
function u=conv(y,x)
   alpha=1;
   beta=1;
   u=beta*[cos(alpha)*x-sin(alpha)*y,sin(alpha)*x+cos(alpha)*y];
endfunction

function z=phi_0(y,x)
  p_0=[0.5 0.3];
  r_0=0.2;
 if (x-p_0(1))**2+(y-p_0(2))**2>r_0**2 then
    z=0;
  else
    z=1-((x-p_0(1))**2+(y-p_0(2))**2)/r_0**2;
  end
endfunction

exec("dif-conv-2D.sce")


//clf()
//fig = gcf()
//gcf().color_map = jetcolormap(64)
//colorbar(min(Z),max(Z))
//plot3d1(X,Y,Z)

