
function [A]=cholesky_fact(A)
[n,m] = size(A);
T = zeros(n, m);
T(1,1) = sqrt(A(1,1));
for i=1:n
    T(i,1)=A(i,1)/T(1,1);
end
for p=2:m
    somme =  0        
    for k=1:p-1
        somme = somme + T(p,k)^2
    end
    T(p,p)=sqrt(A(p,p) - somme);
    for i=1:n
        somme2 = 0
        for k=1:p-1
            somme2 = somme2 + T(i,k)*T(p,k)
        end
        T(i,p) = (A(i,p) - somme2)/T(p,p);
    end
end
A=T
endfunction

function [y]=up_sweep_cholesky(A,x)
  [m,n]=size(A);
  if (m~=n) then
    print(%io(2), "error, not a square matrix");
  else
//-------------------------
y = zeros(n,1);
y(n) = x(n)/A(n,n);
for k=n-1:-1:1
    somme = 0
    for i=n:-1:n-k+1
        somme = somme+A(k,i)*y(i);
    end
    y(k)= (x(k) - somme)/A(k,k);
end
//------------------------
end
endfunction

function [y]=down_sweep_cholesky(A,x)
    [m,n]=size(A);
    if (m~=n) then
      print(%io(2), "error, not a square matrix");
    else
        y=x;
        y(1)=y(1)/A(1,1);
        for i=2:n,
            y(i)=y(i)-A(i,1:i-1)*y(1:i-1);
            y(i)=y(i)/A(i,i);
        end;
    end;
endfunction

function [U]=my_cholesky(N,S)
    [m,n]=size(N);
    if (m~=n) then
        print(%io(2), "error, not a square matrix");
    else
        T = cholesky_fact(N);
        U=down_sweep_cholesky(T,S);
        U=up_sweep_cholesky(T',U);
    end;
endfunction
