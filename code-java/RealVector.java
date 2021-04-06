package standaloneNaKpumpCodeForCosmo;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

//import MathTools.StatCalc;

public class RealVector {
	private double[] v;
	
	
	public RealVector(String stream){
		String numbers[] = stream.split(" ");
		v = new double[numbers.length];
		for (int i = 0; i < numbers.length; i++) {
			v[i] = Double.parseDouble(numbers[i]);
		}
	}
	
	public RealVector(String [] numbers){
		v = new double[numbers.length];
		for (int i = 0; i < numbers.length; i++) {
			v[i] = Double.parseDouble(numbers[i]);
		}
	}
	
	public RealVector(List<Double> v) {
		this.v = new double[v.size()];
		for (int i = 0; i < getDimension(); i++) {
			this.v[i] = v.get(i);
		}
	}
	
//	public RealVector(List<Double> numbers){
//		for (int i = 0; i < numbers.size(); i++) {
//			v[i] = numbers.get(i);
//		}		
//	}
	
	
	// CONSTRUCTOR
	public RealVector(int n) {
		v = new double[n];
		for (int i = 0; i < n; i++) {
			v[i] = 0;
		}
	}
	
	
	public void abs(){
		for (int i = 0; i < this.getDimension(); i++) {
			v[i]=Math.abs(v[i]);			
		}
	}
		
	public double std(){
		double mu = this.getMean(); 
		double r=0;
		for (int i = 0; i < this.getDimension(); i++) {
			r = r + (v[i] - mu)*(v[i] - mu); 			
		}
		return Math.sqrt(r/this.getDimension()); 
	}
	
	public boolean isNaN() {
		for (int i = 0; i < getDimension(); i++) {
			if (Double.isNaN(get(i))) {
				return true;
			}
		}
		return false;
	}

	public void fillWith(double x) {
		for (int i = 0; i < getDimension(); i++) {
			v[i] = x;
		}
	}

	public boolean isInDomain(SimpleDomain dm) {
		if (this.getDimension() != dm.getDimension()) {
			System.out.println("Dimensions are different!");
			return false;
		} else {
			for (int i = 0; i < getDimension(); i++) {
				double xi = get(i);
				double min = dm.getBounds(i)[0];
				double max = dm.getBounds(i)[1];
				if (xi < min || xi > max) {
					return false;
				}
			}
			return true;
		}
	}
	
	
	public boolean closedIsInDomain(SimpleDomain dm) {
		if (this.getDimension() != dm.getDimension()) {
			System.out.println("Dimensions are different!");
			return false;
		} else {
			for (int i = 0; i < getDimension(); i++) {
				double xi = get(i);
				double min = dm.getBounds(i)[0];
				double max = dm.getBounds(i)[1];
				if (xi < min || xi > max) {
					return false;
				}
			}
			return true;
		}
	}
	
	public RealVector(double[] v) {
		this.v = new double[v.length];
		for (int i = 0; i < getDimension(); i++) {
			this.v[i] = v[i];
		}
	}

	

	public void set(int i, double x) {
		v[i] = x;
	}

	public double get(int i) {
		return v[i];
	}
	
	public double getMean(){
		double mean = 0;
		for (int i = 0; i < this.getDimension(); i++) {
			mean+= this.get(i);
		}
		return mean/this.getDimension();
	}
	
	
//	public double getStandarDeviation(){
//		StatCalc calc = new StatCalc();
//		for (int i = 0; i < this.getDimension(); i++) {
//			calc.enter(this.get(i));
//		}
//		return calc.getStandardDeviation();
//	}
	
//	public double getMeanCalc(){
//		StatCalc calc = new StatCalc();
//		for (int i = 0; i < this.getDimension(); i++) {
//			calc.enter(this.get(i));
//		}
//		return calc.getMean();
//	}
	
	public int getDimension() {
		int result = v.length;
		return result;
	}

	public RealVector sum(RealVector other) {

		RealVector r = new RealVector(this.getDimension()); // this.cosas me
		// apunta al objeto
		// que esta atrás
		// del punto, en
		// general.

		for (int i = 0; i < getDimension(); i++) {
			r.v[i] = v[i] + other.v[i];
		}
		return r;
	}

	public RealVector scale(double a) {
		RealVector r = new RealVector(this.getDimension()); // this.cosas me
		for (int i = 0; i < getDimension(); i++) {
			r.v[i] = a * v[i];
		}
		return r;
	}

	public void add(RealVector other) {
		for (int i = 0; i < getDimension(); i++) {
			v[i] += other.v[i];
		}
	}

	public static RealVector getRandom01(int dim) {
		RealVector x = new RealVector(dim);
		for (int i = 0; i < dim; i++) {
			x.set(i, Math.random());
		}
		return x;
	}

	public RealVector getRandom(int dimension) {
		RealVector r = new RealVector(dimension);
		for (int i = 0; i < dimension; i++) {
			r.set(i, Math.random() - 0.5);
		}
		return r.scale(2);
	}

	public RealVector concatenate(RealVector other) {
		RealVector r = new RealVector(this.getDimension()
				+ other.getDimension());
		for (int i = 0; i < this.getDimension(); i++) {
			r.set(i, this.get(i));
		}
		for (int i = this.getDimension(); i < r.getDimension(); i++) {
			r.set(i, other.get(i - this.getDimension()));
		}
		return r;
	}

	public void normalize() {

		double mod = getModulus();
		for (int i = 0; i < getDimension(); i++) {
			this.set(i, this.get(i) / mod);
		}
	}

	public RealVector getNormalized() {

		double mod = getModulus();
		for (int i = 0; i < getDimension(); i++) {
			this.set(i, this.get(i) / mod);
		}
		return this;
	}

//	public String toString() {
//		String salida = "";
//
//		for (int i = 0; i < getDimension(); i++) {
//			if (i > 0) {
//				salida += "_";
//			}
//			salida += v[i];
//		}
//		return salida;
//
//	}
	public String toString() {
		return toStringBis();
	}
	
	public double getMax() {

		double a = this.get(0);
		for (int i = 0; i < this.getDimension(); i++) {
			if (this.get(i) >= a) {
				a = this.get(i);
			}
		}
		return a;
	}
	
	public int getMinPosition() {
		double a = this.get(0);
		int x = 0;
		for (int i = 0; i < this.getDimension(); i++) {
			if (this.get(i) <= a) {
				a = this.get(i);
				x = i;
			}
		}
		return x;
	}
	
	public double getMin() {
		double a = this.get(0);
		for (int i = 0; i < this.getDimension(); i++) {
			if (this.get(i) <= a) {
				a = this.get(i);
			}
		}
		return a;
	}

	public double getModulus() {
		double r = 0;
		for (int i = 0; i < getDimension(); i++) {
			r = r + this.get(i) * this.get(i);
		}
		r = Math.sqrt(r);

		return r;
	}

	/*
	 * Returns a new vector whose values range from (min, max) to
	 * (newmin,newmax)
	 */

	public RealVector Normalize(double newmin, double newmax) {

		double min = this.getMin();
		double max = this.getMax();
		double interval = max - min;
		double newinterval = newmax - newmin;
		RealVector x = new RealVector(this.getDimension());
		int length = this.getDimension();

		for (int i = 0; i < length; i++) {
			x.set(i, ((this.get(i) - min) / interval) * newinterval + newmin);
		}
		return x;
	}

	public RealVector keepAllAfter(int i) {
		RealVector x = new RealVector(this.getDimension() - i);

		for (int j = 0; j < x.getDimension(); j++) {
			x.set(j, this.get(i + j));
		}
		return x;
	}
	
	public void saveToFile(String filename) throws IOException{
		FileWriter fw = new FileWriter(filename);		
		for(int i = 0; i<this.getDimension(); i++){
			fw.write(this.get(i)+"\n");
		}
		fw.close();		
	}
	
	public String toStringBis() {
		String salida = "";
		for (int i = 0; i < getDimension(); i++) {
			if (i > 0) {
				salida += " ";
			}
			salida += v[i];
		}
		return salida;
	}
	
	public String toStringRegexp(String regexp){
		String salida = "";
		for (int i = 0; i < getDimension(); i++) {
			if (i > 0) {
				salida += regexp;
			}
			salida += v[i];
		}
		return salida;
	}
	
	
	
	public String getEncoding(int numbers){
//		System.out.println("Encoding (aca pierdo informacion. Deber�a definir decoding s�lido y reevaluar C en la decoding(encoding) para asegurar que tengo una cota");
		String encoding = "";
		for (int i = 0; i < this.getDimension(); i++) {
			double value = this.get(i);
			if(value>=0){
				encoding = encoding + "+";
			}
			encoding = encoding + String.format("%."+numbers+"f", value);
		}
		return encoding;
	}
	
	
	public RealVector getPart(int ini, int finexclusivo){
		RealVector r = new RealVector(finexclusivo-ini);
		for (int i = 0; i < r.getDimension(); i++) {
			r.set(i, this.get(i+ini));
		}	
		return r;
	}
	
	public double[] getArray(){
		return v;
	}
	
	public RealVector copy() {
		return new RealVector(v);
	}
	
	public static void main(String[] args) {
		RealVector x = new RealVector(5);
		x.set(0, 1);
		x.set(1, 2);
		x.set(2, 3);
		x.set(3, 4);
		x.set(4, 5);
		System.out.println(x.toStringBis());
		System.out.println("De 0 a 2");
		System.out.println(x.getPart(0, 2).toStringBis());
		System.out.println("De 1 a 4");
		System.out.println(x.getPart(1, 4).toStringBis());
		
	}
}
