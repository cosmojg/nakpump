package standaloneNaKpumpCodeForCosmo;
import MathObjects.RealVector;
import MathObjects.RealVectorField;

public class Integrador extends Object {
	public double dt;	
	public Integrador(double dt) {
		this.dt = dt;
	}
	
	public void setdt(double newdt){
		this.dt = newdt;
	}
	
	public RealVector rk4(RealVectorField f, RealVector x) {
		RealVector k1 = f.evaluate(x);
		RealVector k2 = f.evaluate(x.sum(k1.scale(dt / 2)));
		RealVector k3 = f.evaluate(x.sum(k2.scale(dt / 2)));
		RealVector k4 = f.evaluate(x.sum(k3.scale(dt)));
		RealVector delta = k1;
		delta = delta.sum(k2.scale(2.0));
		delta = delta.sum(k3.scale(2.0));
		delta = delta.sum(k4);
		delta = delta.scale(dt / 6);
		return x.sum(delta);
	}
}
