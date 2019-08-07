import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import ij.IJ;
import ij.ImagePlus;
import ij.IJ;
import ij.plugin.ContrastEnhancer;
import ij.measure.ResultsTable;
import ij.plugin.filter.ParticleAnalyzer;
import ij.process.ImageConverter;

/** Loads and displays a dataset using the ImageJ API. */
public class CellCounting{
	public static void main(String[] args) {

		//Ask user for input and prepare output
		Scanner reader = new Scanner(System.in);  
		System.out.println("Enter the input folder path: ");
		String input_filepath = reader.nextLine(); 
		System.out.println("Enter the output folder path: ");
		String output_folder_path = reader.nextLine();


		File dir = new File(input_filepath);
		if (!dir.exists())
		{
			System.out.println(input_filepath + "is not a valid input folder path");
		}
		File[] directoryListing = dir.listFiles();
		System.out.println(directoryListing);

		//Create output folder if does not exist
		File dir_out = new File(output_folder_path);
		if (!dir_out.exists()){
			if (dir_out.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
				}
		}

		//Start the algorithm
		try{
			FileWriter writer = new FileWriter(Paths.get(output_folder_path,"test.csv").toString());
		if (directoryListing != null) {
			for (File child : directoryListing) {
				System.out.println("Processing image "+child.getName());
				
				//Read image and convert to 8 bits
				ImagePlus img = new ImagePlus(child.toString());
				ImageConverter ic = new ImageConverter(img);
				ic.convertToGray8();
				//Perform hist equalization to improve contrast
				ContrastEnhancer ce = new ContrastEnhancer();
				ce.equalize(img);
				//Perform gaussian blur
				IJ.run(img, "Gaussian Blur...", "sigma=2");
				//Perform Otsu threshold
				IJ.setAutoThreshold(img, "Otsu dark");
				//Perform watershed segmentation
				IJ.run(img, "Watershed", "");
				//Count the cells,exclude too small cells
				ResultsTable rt = new ResultsTable();
				ParticleAnalyzer pa = new ParticleAnalyzer(ParticleAnalyzer.SHOW_NONE, ParticleAnalyzer.RECT, rt, 400, Double.POSITIVE_INFINITY);
				pa.analyze(img);
				//Save the results
				System.out.println("The number of nuclei is " + rt.getCounter());
				writer.append(child.getName() +" "+  Integer.toString(rt.getCounter()));
				writer.append("\n");

			}
			writer.close();
		}
		else{
			System.out.println("Wrong directory");
		}
		}catch(Exception e){System.out.println(e);}

	}
}

