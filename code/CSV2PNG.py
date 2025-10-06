import numpy as np
from PIL import Image
import csv
from scipy.ndimage import gaussian_filter

# Read CSV to 2D numpy array


def read_csv_to_array(csv_file):
    with open(csv_file, newline='') as f:
        reader = csv.reader(f)
        data = np.array([[float(val) for val in row] for row in reader])
    return data

# Defining color mapping function


def get_color(value, color):
    if (color == True):
        if value < 70:
            return (0, 0, 80)            # Very deep water (darker blue)
        elif value < 75:
            return (0, 0, 100)           # Deep water (dark blue)
        elif value < 80:
            return (0, 0, 130)           # Medium deep water (blue)
        elif value < 85:
            return (0, 0, 150)           # Water (blue)
        elif value < 90:
            return (0, 100, 200)         # Shallow water (lighter blue)
        elif value < 93:
            return (0, 150, 255)         # Very shallow water (light blue)
        elif value < 95:
            return (220, 220, 128)       # Wet sand / shoreline (pale yellow)
        elif value < 100:
            return (240, 240, 64)        # Sand / beach (yellowish)
        elif value < 105:
            return (175, 210, 90)        # Grass transition (yellow-green)
        elif value < 125:
            return (115, 210, 0)         # Grassland (light green)
        elif value < 135:
            return (60, 180, 75)         # Dense grass
        elif value < 158:
            return (34, 139, 34)         # Forest green
        elif value < 160:
            return (115, 74, 18)         # Rocky terrain brown
        elif value < 162:
            return (160, 82, 45)         # Mountain brown
        elif value < 165:
            return (160, 160, 160)       # Bare rock / high altitude gray

        # TODO: ADD DIFFERENT MOUNTAIN COLORS!!!

        elif value < 170:
            return (180, 180, 180)
        elif value < 175:
            return (200, 200, 200)
        elif value < 180:
            return (220, 220, 220)
        else:
            # print(value)
            return (255, 255, 255)       # Snow / peak (white)

    # b&w version
    else:
        return grayscale_10_shades(value)

# helper function for grey-scale color


def grayscale_10_shades(value):
    # range from 0 to 1
    normalized = (value + 1) / 2
    # 0, 28, 56, ..., 255
    shades = [int(x * 255 / 9) for x in range(10)]
    # scaling to 0-9 indices
    index = min(int(normalized * 10), 9)

    gray = shades[index]
    return (gray, gray, gray)


# map array to blocky B&W image


def csv_to_image(csv_file, output_png):
    arr = read_csv_to_array(csv_file)
    height, width = arr.shape
    img = Image.new("RGB", (width, height))
    for y in range(height):
        for x in range(width):
            img.putpixel((x, y), get_color(arr[y, x], False))
    img.save(output_png)

# get coloured version


def bw_to_color(input_png, output_png):
    imgBW = Image.open(input_png).convert("L")
    arr = np.array(imgBW)
    height, width = arr.shape

    imgColor = Image.new("RGB", (width, height))
    for y in range(height):
        for x in range(width):
            imgColor.putpixel((x, y), get_color(arr[y, x], True))
    imgColor.save(output_png)

# map blocky image to smooth to simulate multiple octaves


def png_smooth(input_png, output_png):

    sigma = int(input("Enter desired sigma value (1..10 recommended): "))

    img = Image.open(input_png)
    data = np.array(img)
    smooth = gaussian_filter(data, sigma=sigma)
    smoothImg = Image.fromarray(np.uint8(smooth))
    smoothImg.save(output_png)


def main():
    csv_to_image("output.csv", "output_1_pixel.png")
    png_smooth("output_1_pixel.png", "output_2_smooth.png")
    bw_to_color("output_2_smooth.png", "output_3_color.png")


if __name__ == "__main__":
    main()
