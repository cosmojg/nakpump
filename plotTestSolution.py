from pylab import * 
import random
import string 
def id_generator(size=6, chars=string.ascii_uppercase + string.digits):
    return ''.join(random.choice(chars) for _ in range(size))    

import os

def getIpump(p, sol):
	# double iPump = q10_Imaxpump * Imaxpump / (1 + Math.exp((intNahalf -  IntNa)/intNaS));
	# double q10_Imaxpump	= p.get(39);		
	# double Imaxpump 	= p.get(40);
	# double intNaS 		= p.get(41);	    
 #    double intNahalf 	= p.get(42);
 #    double alphaFvol 	= p.get(43);
	iPump = p[39]*p[40]/(1 + exp((p[42] - sol[:,-1])/p[41] ))
	return iPump


hashname=id_generator()

popnum=805
pop = genfromtxt('burster_imi_targetfun_RK4_'+str(popnum)+'.pop')
p = pop[0]
pfname='optsol-'+hashname+'.txt'
savetxt(pfname, p) 

command = 'java -cp celltemp.jar findcells.integrateTempCompCell_imi_realclean_NaKpump_rk4 cis_realclean_NaKpump.txt '+pfname+' 5 25 0.1 > sol.txt'
os.system(command)


# sol = genfromtxt('nopumpsolution.txt')
sol = genfromtxt('sol.txt')
time= linspace(0,5,len(sol))
ipump = getIpump(p,sol)

figure(figsize=(10,8))
suptitle('realclean-imi-NaKpump -- ' + pfname)
subplot(311)
ylabel('V')
plot(time,sol[:,0],color='black')
ax1 = subplot(312)
ylabel('[Na]')
plot(time,sol[:,-1],color='black')
ax2 = ax1.twinx()
ax2.plot(time,ipump,color='red')
ylabel('Ipump (nA)')
subplot(313)
ylabel('[Ca]')
plot(time,sol[:,-2],color='black')
xlabel('time (sec)')
savefig('optsol-'+hashname+'.png')



figure(figsize=(10,8))
suptitle('realclean-imi-NaKpump -- ' + pfname)
subplot(311)
ylabel('V')
plot(time[time>4],sol[:,0][time>4],color='black')
xlim(4,5)
ax1 = subplot(312)
xlim(4,5)
ylabel('[Na]')
plot(time[time>4],sol[:,-1][time>4],color='black')
plot(time[time>4],p[42]*ones(len(sol))[time>4],color='blue', ls='dashed')
ax2 = ax1.twinx()
ax2.plot(time,ipump,color='red')
ylabel('Ipump (nA)')
xlim(4,5)
subplot(313)
ylabel('[Ca]')
plot(time[time>4],sol[:,-2][time>4],color='black')
xlabel('time (sec)')
xlim(4,5)
savefig('optsol-'+hashname+'.zoom.png')

# figure(figsize=(10,8))
# suptitle('realclean-imi-NaKpump -- ' + pfname)
# subplot(311)
# ylabel('V')
# plot(time,sol[:,0],color='black')
# xlim(4,5)
# ax1 = subplot(312)
# xlim(4,5)
# ylabel('[Na]')
# plot(time,sol[:,-1],color='black')
# ax2 = ax1.twinx()
# ax2.plot(time,ipump,color='red')
# ylabel('Ipump (nA)')
# xlim(4,5)
# subplot(313)
# ylabel('[Ca]')
# plot(time,sol[:,-2],color='black')
# xlabel('time (sec)')
# xlim(4,5)
# savefig('optsol-'+hashname+'.zoom.png')