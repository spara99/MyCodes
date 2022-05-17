function [a]=Helm_No_a(f0,Lef,V)

% Calls Helm_No_a to calculate the missing parameter for a given f0;

% Define variables:
c = 343;

S =(((2*pi*f0)^2)*Lef*V)/(c^2);
a = sqrt(S/pi);

end
