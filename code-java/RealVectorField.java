package MathObjects;

public abstract class RealVectorField extends Object {

	private String name;

	public abstract RealVector evaluate(RealVector x);

	public abstract void setParameters(RealVector x);

	public abstract RealVector getParameters();

	public abstract int getDimension();

	public RealVectorField(String name) {
		this.name = name;
	}
	
	
	public RealVector pointToci(RealVector point){
//		System.out.println(point.getPart(0, this.getDimension()).toStringBis());
		return point.getPart(0, this.getDimension());			
	}
	
	public RealVector pointTop(RealVector point){
//		System.out.println(point.getPart(this.getDimension(), this.getDimension() + this.getParameters().getDimension()).toStringBis());
		return point.getPart(this.getDimension(), this.getDimension() + this.getParameters().getDimension());			
	}
	
	
	public RealVector ciAndpToPoint(RealVector ci, RealVector p){
		if((ci.getDimension() + p.getDimension()) != this.getDimension() + this.getParameters().getDimension()){
			System.out.println("ERROR: dimension of point not equal to model dim + model.parameters.dim");
		}
		RealVector point = ci.concatenate(p);
		return point;
	}

	public String getName() {
		return this.name;
	}

	public abstract RealVectorField copy();
}
