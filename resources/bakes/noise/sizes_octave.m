data = dlmread('sizes.txt');
hist(data);

% Save plot as PNG file
print -dpng distribution_plot.png