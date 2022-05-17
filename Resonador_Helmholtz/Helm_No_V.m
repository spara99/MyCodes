function [V]=Helm_No_V(f0,Lef,a)

% Calls Helm_No_V to calculate the missing parameter for a given f0;

% Define variables:
c = 343;
S = pi*a^2; % Calculate S (area)

V = ((c^2)*S)/((2*pi*f0)^2*Lef);


end
