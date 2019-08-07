# Intro
Small java script that peforms basic image processing tasks to count cells. It performs:
1. Histogram equalization
2. Gaussian blur
3. Otsu adaptive threshold
4. Watershed segmentation
5. Particle counting

# Known issues
For some images, the method under performs, especially when the cells are too close to one another. Especially, the proposed method tends to oversegment the cells. We would need a more advanced pipeline to tackle such images.

# Requirements
JDK 1.8
Maven

# Usage
Usage: mvn -Pexec -Dmain-class=LoadAndDisplayDataset
Enter the filepath to the folder containing the input images. Enter as well the output folder name.

# Results
The results are saved in the output folder in a csv file containing the image path and the cell count, separated by a space.
