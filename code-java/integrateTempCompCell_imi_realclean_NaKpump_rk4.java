package standaloneNaKpumpCodeForCosmo;
import java.io.IOException;
import MathObjects.Integrador;
import MathObjects.RealVector;

public class integrateTempCompCell_imi_realclean_NaKpump_rk4 {
	public static void main(String[] args) throws IOException {
//		System.out.println("testing here");
		RealVector cis = FileUtil.FileToVector(args[0]);
		RealVector parameters = FileUtil.FileToVector(args[1]);
		double nsecs = Double.parseDouble(args[2]);
		double temp = Integer.parseInt(args[3]);		
		double dt = Double.parseDouble(args[4]);
		singleCompTemperature_imi_realclean_NaK_pump model = new singleCompTemperature_imi_realclean_NaK_pump();
		model.setParameters(parameters);
		model.setTemperature(temp);
	
		Integrador rk = new Integrador(dt);
		double nsteps = (1000*nsecs)/dt;
		RealVector s = cis.copy();
		for(int i = 0; i<nsteps; i++){
//			System.out.println(s.get(0));
			System.out.println(s);
			s= rk.rk4(model, s);			
		}		
		s.saveToFile("finalstate.txt");		
	}
}
