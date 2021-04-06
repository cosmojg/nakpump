package standaloneNaKpumpCodeForCosmo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import MathObjects.Matrix;
import MathObjects.RealVector;
import MathObjects.SimpleDomain;

public class FileUtil {
	
//	public static SimpleDomain getDomainFromFile(String file) throws IOException{
//		Matrix x = MatrixUtil.fileToMatrix(file);
//		RealVector guesspoint = x.getCol(0);
//		RealVector eps = x.getCol(1);
//		SimpleDomain sd = new SimpleDomain();
//		sd = sd.getEpsNeighboorhoodOfPoint(guesspoint, eps);
//		return sd;		
//	}
	
	
//	public static List<RealVector> listOfVectorsFromFile(String file) throws FileNotFoundException{
//		List<RealVector> pop =new ArrayList<RealVector>();
//		float[][] matrix = ReadMatrixFromFile.read(ReadMatrixFromFile.getBufferedReader(file));		
//		for(int i = 0 ; i < matrix.length ; i++){
//			RealVector individual = new RealVector(matrix[i].length);
//			for(int j = 0 ; j<matrix[i].length  ; j++){
//				individual.set(j, matrix[i][j]);
//			}			
//			pop.add(individual);
//		}		
//		return pop;
//	}
	
	
	public static void saveDomainEpsCenter(String filename, SimpleDomain sd) throws IOException{
		RealVector center = sd.getCenterOfDomain();
		RealVector eps = sd.getEpsCenterOfDomain();
		FileWriter fw = new FileWriter(filename);
		for (int i = 0; i < sd.getDimension(); i++) {
			fw.write(center.get(i) + " "  + eps.get(i) +"\n" );
		}
		fw.close();
	}
	
	public static void listOfIntegersToFile(List<Integer> list, String filename) throws IOException{
		FileWriter fw = new FileWriter(filename); 
		for (int i = 0; i < list.size(); i++) {
			fw.write(list.get(i) + "\n");
		}
		fw.close();
	}
	
	public static RealVector FileToVector(String path) throws IOException{		
		 BufferedReader fr = new BufferedReader(new FileReader(path));		 
		 String s;
		 int length = 0;		 
		 while((s = fr.readLine() )!=null ){
		 length++;
		 }		 
		 
		 @SuppressWarnings("resource")
		 BufferedReader fr2 = new BufferedReader(new FileReader(path));
		 RealVector r = new RealVector(length);
		 int i = 0;		 
		 while((s = fr2.readLine() )!=null ){			 			 			 
			 r.set(i, Double.parseDouble(s));
			 i++;			 			 
		 }
		 fr.close();
				
		return r;		
	}	
	public static double GetMax(String path) throws IOException{
		double a;
		RealVector x = FileToVector(path);
		a = x.getMax();		
		return a;
	}	
	public static double GetMin(String path) throws IOException{
		double a;
		RealVector x = FileToVector(path);
		a = x.getMin();		
		return a;
	}
	/*
	 * Writes RealVector x in a single column file "path"
	 */
	public static void WriteVectorToFile(RealVector x, String path) throws IOException{
		FileWriter fw = new FileWriter(path);		
		for(int i = 0; i<x.getDimension(); i++){
			fw.write(x.get(i)+"\n");
		}
		fw.close();		
	}
	
	/*
	 * Returns a RealVector with normalized (min,max) data from file "path". 
	 */
	
	public static RealVector FileToVectorNormalized(String path, double min, double max) throws IOException{
		RealVector r = FileToVector(path);		
		RealVector x = r.Normalize(min, max);		
		return x;
	}
	
	public static void AppendRealVectorToFile(RealVector x, File pete) throws IOException{
		FileWriter fw = new FileWriter(pete);
		fw.append(x.toStringBis() + "\n");		
		fw.close();		

	}
	
	public static void listOfRealVectorsToFile(List<RealVector> vectors, String filename) throws IOException{
		FileWriter fw = new FileWriter(filename);
		for (int i = 0; i < vectors.size(); i++) {
			fw.write(vectors.get(i).toStringBis() + "\n");			
		}
		fw.close();
	}
	
	public static void main(String[] args) throws IOException {
	}
}
