from matplotlib import pyplot as plt
from PIL import Image
import numpy as np
from math import floor

columns = 20
rows = 5

string = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~"

out = [[0, 0, 0, 0] for i in range(255)]

def map(x, in_min, in_max, out_min, out_max):
  return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min


i = 0
for x in string:
	ascii = ord(x)

	column = floor(i / columns)
	row = i % columns

	out[ascii] = [float(row)/rows*255, map(float(column)/columns, 0, 1, 1, 0)*255, 0, 255]

	i += 1

print(out)
N = len(out)

# Create an empty numpy array with 1 row and N columns
image_data = np.zeros((1, N, 4), dtype=np.uint8)

# Fill the numpy array with the tuple values
for i in range(N):
    image_data[0, i, 0] = out[i][0]  # Red
    image_data[0, i, 1] = out[i][1]  # Green
    image_data[0, i, 2] = out[i][2]  # Blue
    image_data[0, i, 3] = out[i][3]  # Alpha

print(image_data)

img = Image.fromarray(image_data, 'RGBA')
img.save('font1indices.png')
img.show()