function [Lef]=Helm_No_Lef(f0,V,a)

% Calls Helm_No_Lef to calculate the missing parameter for a given f0;

% Define variables:
c = 343;
S = pi*a^2; % Calculate S (area)

Lef = ((c^2)*S)/(((2*pi*f0)^2)*V);


end
