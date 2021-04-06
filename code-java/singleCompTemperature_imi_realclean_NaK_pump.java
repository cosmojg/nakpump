package standaloneNaKpumpCodeForCosmo;

import MathObjects.RealVector;
import MathObjects.RealVectorField;
import MathObjects.singleCompTemperature_slow_imi_explicit;

public class singleCompTemperature_imi_realclean_NaK_pump extends RealVectorField{
	public RealVector p;
	public int dim;
	public double temp;
	public double reftemp;
	
	public singleCompTemperature_imi_realclean_NaK_pump(){
		super("singleCompTemp_imi_realclean_NaKpump");	
		int pdim =44;
		this.p = new RealVector(pdim);
		this.dim= 15;
		this.reftemp= 25;	
	}		
	public RealVector evaluate(RealVector v) {		
				
		double V = v.get(0);
	    double NaM = v.get(1);
	    double NaH = v.get(2);
	    double CaTM = v.get(3);
	    double CaTH = v.get(4);
	    double CaSM = v.get(5);
	    double CaSH = v.get(6);
	    double HM = v.get(7);
	    double KdM = v.get(8);
	    double KCaM = v.get(9);
	    double AM = v.get(10);
	    double AH = v.get(11);
	    double IMIM = v.get(12);
	    double IntCa = v.get(13);
	    double IntNa = v.get(14);

	    double CaRev = CaNernst(IntCa, temp);
		
//	    March 16th, 2021. Adding Sodium-Potassium pump current
//	    February 4th, 2019. Set normal values
	    double C = 1.;  // Capacitance (uF / cm^2)
	    
		// Ionic Currents (mV / ms)
	    
	    // double caF = p.get(17);  // Current (nA) to Concentration (uM) Conversion Factor (uM / nA)
	    // double Ca0 = p.get(18);  // Background Intracellular Calcium Concentration (uM)
	    // these numbers below convert calcium current to calcium concentration and have not been measured so people change them. 
//	    double Area = 0.628; Area = 1, will just remove this parameter
	    
	    // SEPTEMBER 2019. THIS MODEL IS NAMED 'CLEAN' BUT HAS ALL THE HISTORIC CRAP 

// 		NO AREA IN CLEAN VERSION	    
//	    WILL GO BACK TO HAVING THESE PARAMETERS MARCH 3, 2019. 
//	    SETTING AREA =0.628
//	    AND caF=0.94
//		double caF = 1.; 	//changing 0.94 to 1 		
		double caF = 0.94;
		double Ca0 = 0.05;  //original value
	    
//	    activation timescale of imi appears to be constant (golowash92) 
		double tauImi = 20;
		
		
//		double tauIntCa = p.get(38);  // Calcium buffer time constant (ms)
	    // Equilibrium Points of Calcium Sensors
		// p_g=[gNa, gCaT,gCaS,gA,gKCa,gKd,gH,g_leak]

		// Fixed Maximal Conductances
		double gNa  = p.get(0);    // Transient Sodium Maximal Conductance
		double gCaT = p.get(1);    // Low Threshold Calcium Maximal Conductance
		double gCaS = p.get(2);    // Slow Calcium Maximal Conductance
		double gA   = p.get(3);    // Transient Potassium Maximal Conductance
		double gKCa = p.get(4);  	 // Calcium Dependent Potassium Maximal Conductance
		double gKd  = p.get(5);    // Potassium Maximal Conductance
		double gH   = p.get(6);    // Hyperpolarization Activated Cation Maximal Conductance
		double gL   = p.get(7);    // Leak Maximal Conductance
		double gImi   = p.get(8);    // Imi Maximal Conductance

		// p_Erev = [e_leak,e_na,e_k,e_h]
		double EL = p.get(9);   // Leak Reversal Potential
		double ENa = p.get(10);  // Sodium Reversal Potential
		double ECaT = CaRev;    // Low Threshold Calcium Reversal Potential
		double ECaS = CaRev;    // Slow Calcium Reversal Potential
		
		
		double EIMI = -10; 		  // IMI reversal potential fixed at -10 mV
		double EKd = p.get(11);   // Potassium Reversal Potential
		double EKCa = p.get(11);  // Calcium Dependent Potassium Reversal Potential
		double EA = p.get(11);    // Transient Potassium Reversal Potential
		double EH = p.get(12);    // Hyperpolarization Activated Cation Reversal Potential

		// p_q10=[q10_gNa , q10_gNa_m , q10_gNa_h,q10_gCaT,q10_gCaT_m,q10_gCaT_h,q10_gCaS,q10_gCaS_m,q10_gCaS_h,q10_gA,q10_gA_m,q10_gA_h, q10_gKCa,q10_gKCa_m,q10_gKCa_h ,q10_gKdr,q10_gKdr_m,q10_gKdr_h,q10_gH,q10_gH_m,q10_gH_h, q10_g_leak,q10_tau_Ca]
		// p_q10=[
		double q10_gNa 		= p.get(13);
		double q10_gNa_m	= p.get(14);
		double q10_gNa_h	= p.get(15);

		double q10_gCaT 	= p.get(16);
		double q10_gCaT_m	= p.get(17);
		double q10_gCaT_h	= p.get(18);

		double q10_gCaS 	= p.get(19);
		double q10_gCaS_m	= p.get(20);
		double q10_gCaS_h	= p.get(21);

		double q10_gA		  = p.get(22);
		double q10_gA_m		= p.get(23);
		double q10_gA_h		= p.get(24);

		double q10_gKCa 	= p.get(25);
		double q10_gKCa_m = p.get(26);
		double q10_gKCa_h = p.get(27);

		double q10_gKdr		= p.get(28);
		double q10_gKdr_m   = p.get(29);
		double q10_gKdr_h   = p.get(30);

		double q10_gH		= p.get(31);
		double q10_gH_m		= p.get(32);
		double q10_gH_h		= p.get(33);

		double q10_g_leak	= p.get(34);
		
		double q10_g_imi	= p.get(35);
		double q10_g_imi_m	= p.get(36);
		double q10_tau_Ca	= p.get(37);
		
//		This is the clean version
		double tauIntCa = p.get(38)*1;  // Calcium buffer time constant (ms)

		
		double q10_Imaxpump	= p.get(39);
		
		double Imaxpump 	= p.get(40);
		double intNaS 		= p.get(41);	    
	    double intNahalf 	= p.get(42);
	    double alphaFvol 	= p.get(43);
		
	    // Applied Current
	    double Iapp = p.get(44);

	    // Steady State Gating Variables
	    double NaMinf  = boltzSS(V, 25.5, -5.29);  // m^3
	    double NaHinf  = boltzSS(V, 48.9, 5.18);  // h
	    double CaTMinf = boltzSS(V, 27.1, -7.20);  // m^3
	    double CaTHinf = boltzSS(V, 32.1, 5.50);  // h
	    double CaSMinf = boltzSS(V, 33.0, -8.1);  // m^3
	    double CaSHinf = boltzSS(V, 60.0, 6.20);  // h
	    double HMinf   = boltzSS(V, 70.0, 6.0);  // m
	    double KdMinf  = boltzSS(V, 12.3, -11.8);  // m^4
	    double KCaMinf = (IntCa/(IntCa + 3.0))*boltzSS(V, 28.3, -12.6);  // m^4
	    double AMinf   = boltzSS(V, 27.2, -8.70);  // m^3
	    double AHinf   = boltzSS(V, 56.9, 4.90);  // h
	    
	    double IMIMinf   = boltzSS(V, 55, -5);  // m
	    
	    // Time Constants (ms)
	    double tauNaM 	= tauX(V, 1.32, 1.26, 120.0, -25.0); 																 	
	    tauNaM = tauNaM * Math.pow(q10_gNa_m, 		-(temp-reftemp)/10.);
	    double tauNaH 	= tauX(V, 0.0, -0.67, 62.9, -10.0)*tauX(V, 1.50, -1.00, 34.9, 3.60); 	
	    tauNaH = tauNaH * Math.pow(q10_gNa_h, 	  -(temp-reftemp)/10.);
	    double tauCaTM 	= tauX(V, 21.7, 21.3, 68.1, -20.5); 																	
	    tauCaTM= tauCaTM*Math.pow(q10_gCaT_m, 	-(temp-reftemp)/10.);
	    double tauCaTH 	= tauX(V, 105.0, 89.8, 55.0, -16.9);																	
	    tauCaTH= tauCaTH*	Math.pow(q10_gCaT_h,	  -(temp-reftemp)/10.);
	    double tauCaSM 	= spectau(V, 1.40, 7.00, 27.0, 10.0, 70.0, -13.0);										
	    tauCaSM= tauCaSM* Math.pow(q10_gCaS_m, 	-(temp-reftemp)/10.);
	    double tauCaSH 	= spectau(V, 60.0, 150.0, 55.0, 9.00, 65.0, -16.0);										
	    tauCaSH= tauCaSH* Math.pow(q10_gCaS_h,	  -(temp-reftemp)/10.);
	    double tauHM 		= tauX(V, 272.0, -1499.0, 42.2, -8.73);																
	    tauHM	 = tauHM  * Math.pow(q10_gH_m,  	  -(temp-reftemp)/10.);
	    double tauKdM		= tauX(V, 7.20, 6.40, 28.3, -19.2);																		
	    tauKdM = tauKdM * Math.pow(q10_gKdr_m, 	-(temp-reftemp)/10.);
	    double tauKCaM 	= tauX(V, 90.3, 75.1, 46.0, -22.7);																		
	    tauKCaM= tauKCaM* Math.pow(q10_gKCa_m ,	-(temp-reftemp)/10.);
	    double tauAM 		= tauX(V, 11.6, 10.4, 32.9, -15.2);																		
	    tauAM	 = tauAM  * Math.pow(q10_gA_m,			-(temp-reftemp)/10.);
	    double tauAH 		= tauX(V, 38.6, 29.2, 38.9, -26.5);																	  
	    tauAH	 = tauAH  * Math.pow(q10_gA_h,			-(temp-reftemp)/10.);
	    
	    tauImi = tauImi* Math.pow(q10_g_imi_m, (temp-reftemp)/10.);
	    
		// cout<<pow(q10_gNa_m,(temp-reftemp)/10.)<<endl;
		gNa  = gNa	* Math.pow(q10_gNa, 		(temp-reftemp)/10.);
		gCaT = gCaT * Math.pow(q10_gCaT, 	(temp-reftemp)/10.);
		gCaS = gCaS	* Math.pow(q10_gCaS, 	(temp-reftemp)/10.);
		gA   = gA  	* Math.pow(q10_gA, 		(temp-reftemp)/10.);
		gKCa = gKCa	* Math.pow(q10_gKCa, 	(temp-reftemp)/10.);
		gKd  = gKd  * Math.pow(q10_gKdr, 	(temp-reftemp)/10.);
		gH   = gH   * Math.pow(q10_gH, 		(temp-reftemp)/10.);
		gL   = gL   * Math.pow(q10_g_leak, (temp-reftemp)/10.);
		gImi   = gImi   * Math.pow(q10_g_imi, (temp-reftemp)/10.);

		tauIntCa = tauIntCa * Math.pow(q10_tau_Ca, -(temp-reftemp)/10.);
		
		
		
	    double iNa  = 	iIonic(gNa	, NaM	, NaH	, 3	, V, ENa);
	    double iCaT = 	iIonic(gCaT	, CaTM, CaTH, 3	, V, ECaT);
	    double iCaS = 	iIonic(gCaS	, CaSM, CaSH, 3	, V, ECaS);
	    double iH   = 	iIonic(gH		, HM	, 1		, 1	, V, EH);
	    double iKd  = 	iIonic(gKd	, KdM	, 1		, 4	, V, EKd);
	    double iKCa = 	iIonic(gKCa	, KCaM, 1		, 4	, V, EKCa);
	    double iA   = 	iIonic(gA		, AM	, AH	, 3	, V, EA);
	    double iL   = 	iIonic(gL		, 1		, 1		, 1	, V, EL);	    
	    double iIMI   = iIonic(gImi		, IMIM		, 1		, 1	, V, EIMI);
	    
	    
	    
	    double iPump = q10_Imaxpump * Imaxpump / (1 + Math.exp((intNahalf -  IntNa)/intNaS));  

	    // State Equations
	    // Voltage Time Evolution: C*dV/dt = -I_ionic + I_applied; I_ionic = sum(g_i*m^q*h*(V - E_i))
			// double dV = (-(iNa + iCaT + iCaS + iH + iKd + iKCa + iA + iL) + iSyns + Iapp)/C;
			// double intrinsic_currents = iNa + iCaT + iCaS + iH + iKd + iKCa + iA + iL;
			// cout << intrinsic_currents <<endl;
	    double dV = (-(iNa + iCaT + iCaS + iH + iKd + iKCa + iA + iL + iIMI + iPump ) + Iapp)/C;
//	    double dV = (-(iNa + iCaT + iCaS + iH + iKd + iKCa + iA + iL + iIMI ) + Iapp)/C;
//	    System.out.println(Iapp);
//	    System.out.println(iPump);
	    // Gating Variable Time Evolution: dX/dt = (X_inf - X)/tau_X
	    double dNaM   = (NaMinf - NaM)/tauNaM;
	    double dNaH   = (NaHinf - NaH)/tauNaH;
	    double dCaTM  = (CaTMinf - CaTM)/tauCaTM;
	    double dCaTH  = (CaTHinf - CaTH)/tauCaTH;
	    double dCaSM  = (CaSMinf - CaSM)/tauCaSM;
	    double dCaSH	= (CaSHinf - CaSH)/tauCaSH;
	    double dHM 		= (HMinf - HM)/tauHM;
	    double dKdM 	= (KdMinf - KdM)/tauKdM;
	    double dKCaM	= (KCaMinf - KCaM)/tauKCaM;
	    double dAM 		= (AMinf - AM)/tauAM;
	    double dAH 		= (AHinf - AH)/tauAH;
	    
	    double dIMIM 	= (IMIMinf - IMIM)/tauImi;
	    
	    double dIntCa = (-caF*(iCaT + iCaS) - IntCa + Ca0)/tauIntCa;

	    double dIntNa = -(iNa + 3*iPump)/(alphaFvol);
//	    double dIntNa = 0;
	    
		RealVector y_dot = new RealVector(getDimension());
		y_dot.set(0, dV);
		y_dot.set(1, dNaM);y_dot.set(2, dNaH);y_dot.set(3, dCaTM);y_dot.set(4, dCaTH);y_dot.set(5, dCaSM);y_dot.set(6, dCaSH);y_dot.set(7, dHM);
		y_dot.set(8, dKdM);y_dot.set(9, dKCaM);y_dot.set(10, dAM);y_dot.set(11, dAH);y_dot.set(12, dIMIM);
		y_dot.set(13, dIntCa);
		y_dot.set(14, dIntNa);
	    return y_dot;
	}
	public void setTemperature(double newtemp){
		this.temp = newtemp;
	}
	@Override
	public int getDimension() {
		// TODO Auto-generated method stub
		return dim;
	}
	@Override
	public RealVector getParameters() {
		return p.copy();
	}
	@Override
	public void setParameters(RealVector x) {
		this.p = x.copy();
	}
	@Override
	public RealVectorField copy() {
		singleCompTemperature_imi_realclean_NaK_pump c = new singleCompTemperature_imi_realclean_NaK_pump(); 
		c.p = p.copy();
		return c;
	}	
	
	public double boltzSS(double Volt,double A,double B){
	    double act = 1./(1. + Math.exp((Volt + A)/B));
	    return act;
		}
	// Voltage-dependent time constants
	public double tauX(double Volt,double  CT,double  DT,double  AT,double  BT){
	    double timeconst = CT - DT/(1. + Math.exp((Volt + AT)/BT));
	    return timeconst;
			}
	// Special time constant function
	public double spectau(double Volt,double  CT,double  DT,double  AT,double  BT,double  AT2,double  BT2){
	    double spec = CT + DT/(Math.exp((Volt + AT)/BT) + Math.exp((Volt + AT2)/BT2));
	    return spec;
			}
	
	public double iIonic(double g,double  m,double  h,double  q,double  Volt,double  Erev){
	    double flux = g*Math.pow(m, q)*h*(Volt - Erev);
	    return flux;
			}	
	
	// Concentration dependent Ca reversal potential
	public double CaNernst(double CaIn,double temp){
		    double R = 8.314*Math.pow(10, 3); //  # Ideal Gas Constant (*10^3 to put into mV)
		    double T = 273.15 + temp; // # Temperature in Kelvin
		    double z = 2.0;//  # Valence of Caclium Ions
		    double Far = 96485.33; //  # Faraday's Constant
		    double CaOut = 3000.0;  //# Outer Ca Concentration (uM)
		    double CalRev = ((R*T)/(z*Far))*Math.log(CaOut/CaIn);
		    // #print 'calrev ', CalRev
		    return CalRev;
	}
	public void setRefTemp(double reftemp2) {
		// TODO Auto-generated method stub
		this.reftemp = reftemp2;		
	}
}
