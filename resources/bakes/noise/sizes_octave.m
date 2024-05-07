data = dlmread('distribution.txt');
hist(data);

% Save plot as PNG file
print -dpng distribution_plot.png