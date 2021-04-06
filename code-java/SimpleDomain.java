package standaloneNaKpumpCodeForCosmo;


import java.util.ArrayList;
import java.util.List;

import MathObjects.RealVector;

public class SimpleDomain {
	
	private List<Double> maximae;
	private List<Double> minimae;
	
	public SimpleDomain() {
		maximae = new ArrayList<Double>();
		minimae = new ArrayList<Double>();
	}
	
	/**
	 * Adds a Dimension to the Simple Domain
	 * @param min minimum value of the added dimension
	 * @param max maximum value of the added dimension
	 */
	
	public SimpleDomain(List<Double>maximae,List<Double> minimae) {
		this.maximae = maximae;
		this.minimae =minimae;
	}
	
	public void addDimension(double min, double max){
		maximae.add(max);
		minimae.add(min);		
	}
	
	/**
	 * Changes the bounds of dimension i
	 * @param i The dimension to be modified
	 * @param min Double min: The minimum value of the specified dimension
	 * @param max Double max: The maximum value of the specified dimension
	 */
	 
	public SimpleDomain getNeighboorhoodOfPoint(RealVector point, double eps){
		SimpleDomain bola = new SimpleDomain();
		for(int i = 0; i<point.getDimension(); i++){
			double max = point.get(i) + eps;
			double min = point.get(i) - eps;
			bola.addDimension(min, max);
		}
		return bola;
	}
	
	public RealVector getLowerBounds(){
		RealVector lb = new RealVector(this.getDimension()); 
		for (int i = 0; i < this.getDimension(); i++) {
			lb.set(i, minimae.get(i)); 
		}
		return lb; 
	}
	
	public RealVector getUpperBounds(){
		RealVector lb = new RealVector(this.getDimension()); 
		for (int i = 0; i < this.getDimension(); i++) {
			lb.set(i, maximae.get(i)); 
		}
		return lb; 
	}
	
	public SimpleDomain getIntersection(SimpleDomain A){
		if(A.getDimension()!= this.getDimension()){
			System.out.println("Domains dimensions are not equal!");
			return null;
		}
		SimpleDomain I = new SimpleDomain();
		
		for(int i = 0 ; i<A.getDimension() ; i++){
			double thismin = this.getBounds(i)[0]; 
			double thismax = this.getBounds(i)[1];
			double Amin = A.getBounds(i)[0];
			double Amax = A.getBounds(i)[1];
			
			double newmin;
			double newmax;
			
			if(thismin<Amin){
				newmin = Amin;
			}else{
				newmin = thismin;
			}
			if(thismax>Amax){
				newmax = Amax;
			}else{
				newmax = thismax;
			}
			I.addDimension(newmin, newmax);
			
		}
		return I;
	}
	
	
	public SimpleDomain getEpsNeighboorhoodOfPoint(RealVector point, RealVector eps){
		SimpleDomain bola = new SimpleDomain();
		for(int i = 0; i<point.getDimension(); i++){
			double max = point.get(i) + eps.get(i);
			double min = point.get(i) - eps.get(i);
			bola.addDimension(min, max);
		}
		return bola;
	}
	
	
	public void setBounds(int i, double min, double max){
		if(i<getDimension()){
			minimae.set(i, min);
			maximae.set(i, max);
		}else{
			System.out.println("The dimension "+i+" does not exist");
		}
	}
	
	/**
	 * Returns the dimension of the Rn simple domain.
	 * @return int
	 */
	
	public int getDimension(){
		int r = maximae.size();
		return r;		
	}
	
	/**
	 * Returns the bounds of the ith dimension of the SimpleDomain
	 * @param i Indicates the dimension index
	 * @return double[1] the minimun [0] and the maximum [1] value of the dimension
	 */
	
	public double[] getBounds(int i){
		double[] r = new double[2];
		r[0] = minimae.get(i);
		r[1] = maximae.get(i);	
		return r;		
	}
	
	public List<Double> scale(List<Double> point){
		if(point.size() != maximae.size()){
		throw new RuntimeException("Chupala, gil de goma!");
		}
		List<Double> result = new ArrayList<Double>();		
		for(int i = 0 ; i < point.size();i++){
			double x = point.get(i);
			double scaled = (maximae.get(i) - minimae.get(i)) * x  + minimae.get(i);
			result.add(scaled);
		}		
		return result;
	}
	
	public RealVector getScaledToDomain(RealVector point){
		if(point.getDimension() != maximae.size()){
			throw new RuntimeException("Chupala, gil de goma!");
			}
			RealVector result = new RealVector(point.getDimension());		
			for(int i = 0 ; i < point.getDimension();i++){
				double x = point.get(i);
				double scaled = (maximae.get(i) - minimae.get(i)) * x  + minimae.get(i);
				result.set(i,scaled);
			}		
			return result;	
	}
	
	
	public List<Double> scale(RealVector point){
		if(point.getDimension() != maximae.size()){
			throw new RuntimeException("Chupala, gil de goma!");
			}
			List<Double> result = new ArrayList<Double>();		
			for(int i = 0 ; i < point.getDimension();i++){
				double x = point.get(i);
				double scaled = (maximae.get(i) - minimae.get(i)) * x  + minimae.get(i);
				result.add(scaled);
			}		
			return result;	
	}
	
	public SimpleDomain getRegularDomain(int dim, double size){
		SimpleDomain dom = new SimpleDomain();
		for(int i = 0; i < dim ; i++){
			dom.addDimension(-size, size);			
		}
		return dom;
	}
	
	public RealVector scaleToDomain(RealVector x){
		RealVector y = new RealVector(x.getDimension());
		for(int i = 0 ; i < y.getDimension() ; i ++){
			double value = ( ( getBounds(i)[1] - getBounds(i)[0] ) * x.get(i) ) + getBounds(i)[0];   			
			y.set(i, value);
		}
		
		return y;
	}
	
//	public List<RealVector> getPointsInGrid(Grid grid){
//		List<RealVector> points = new ArrayList<RealVector>();
//		do {
//			RealVector zeroone = new RealVector(this.getDimension());
//			for (int i = 0; i < this.getDimension(); i++) {
//				zeroone.set(i, grid.getStateVector().get(i)
//						/ grid.bounds.get(i));
//			}
//			RealVector point = this.scaleToDomain(zeroone);
////			System.out.println(point.toStringBis());
//			points.add(point);
//		}while (grid.next());
//		return points;
//	}
	
	public RealVector getRandomVectorInDomain(){
		RealVector x = RealVector.getRandom01(getDimension());		
		return scaleToDomain(x);
	}
	
	public List<RealVector> getRandomPopulationInDomain(int howmany){
		List<RealVector> testpop = new ArrayList<RealVector>();
		for(int i = 0 ; i < howmany; i++){
			testpop.add(getRandomVectorInDomain());
		}
		return testpop;
	}
	
	public RealVector getCenterOfDomain(){
		RealVector center = new RealVector(this.getDimension());
		for (int i = 0; i < center.getDimension(); i++) {
			
			if(this.getBounds(i)[0]==this.getBounds(i)[1]){
				center.set(i, this.getBounds(i)[0]);
			}else{
			center.set(i, (this.getBounds(i)[1]-this.getBounds(i)[0])/2  + this.getBounds(i)[0]);
			}
		}
		return center;
	}
	
	
	public void concatenate(SimpleDomain other){
		for (int i = 0; i < other.getDimension(); i++) {
			this.addDimension(other.getBounds(i)[0], other.getBounds(i)[1]);			
		}
	}
	
	public RealVector getEpsCenterOfDomain(){
		RealVector eps = new RealVector(this.getDimension());
		for (int i = 0; i < eps.getDimension(); i++) {
			eps.set(i, (this.getBounds(i)[1]-this.getBounds(i)[0])/2); 
		}
		return eps;
	}
	
	public void setBound(int which, double newmin, double newmax){
		this.minimae.set(which, newmin);
		this.maximae.set(which, newmax);
		
	}
	
	public SimpleDomain copy(){
		return new SimpleDomain(this.maximae, this.minimae);
	}
	
	
	public String toString() {		
		return "{Simple Domain: " + maximae + ", " + minimae + "}";
	}	
	
	public static void main(String[] args) {
		SimpleDomain sd = new SimpleDomain();
		
		sd.addDimension(-1, 0);
		sd.addDimension(-2, 4);
		
		System.out.println(sd.toString());
		
		sd.setBounds(0, -4, 10);
		System.out.println(sd.toString());
		
	}
	
}
