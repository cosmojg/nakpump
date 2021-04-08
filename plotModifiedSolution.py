from pylab import *
import matplotlib.pyplot as plt
import matplotlib.gridspec as gridspec
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
	# print('sol[:,-1] = {}'.format(sol[:,-1]))
	iPump = p[39]*p[40]/(1 + exp((p[42] - sol[:,-1])/p[41] ))
	return iPump


# Remember:
# Imaxpump 	= p[40]
# intNaS 	= p[41]
# intNahalf = p[42]
# alphaFvol = p[43]

# hashname=id_generator()
hashname='ZTOLB4'

pfname='optsol-'+hashname+'.txt'
p = genfromtxt(pfname)
# savetxt(pfname, p)
originalp = p.copy()
print(f'{originalp[40] = }')

# Plot Imaxpump varied from 0% to 220%
fig = plt.figure(figsize=(15,12))
figz = plt.figure(figsize=(15,12))
outer = gridspec.GridSpec(3, 4, wspace=0.3, hspace=0.2)
for i in range(12):
	inner = gridspec.GridSpecFromSubplotSpec(2, 1,
		subplot_spec=outer[i], wspace=0.1, hspace=0.1)
	
	p = originalp.copy()
	factor = round(i * 0.2, 1)
	percentage = int(factor * 100)
	p[40] = p[40] * factor
	print(f'{p[40] = }')

	savetxt(pfname, p)
	command = 'java -cp celltemp.jar findcells.integrateTempCompCell_imi_realclean_NaKpump_rk4 cis_realclean_NaKpump.txt '+pfname+' 5 25 0.05 > sol.txt'
	os.system(command)

	sol = genfromtxt('sol.txt')
	time = linspace(0,5,len(sol))
	ipump = getIpump(p,sol)

	for j in range(2):
		if j == 0:
			# ax = plt.Subplot(fig, inner[j])
			ax = fig.add_subplot(inner[j])
			axz = figz.add_subplot(inner[j])
			ax.set_title(f'$I_{{max}}$ = {percentage}%')
			axz.set_title(f'$I_{{max}}$ = {percentage}%')
			ax.set_xticks([])
			axz.set_xticks([])
			ax.set_xlim(0,5)
			axz.set_xlim(4,4.5)
			if i == 0 or i == 4 or i == 8:
				ax.set_ylabel('V')
				axz.set_ylabel('V')
			ax.plot(time,sol[:,0],color='black')
			axz.plot(time[time>4],sol[:,0][time>4],color='black')
			fig.add_subplot(ax)
			figz.add_subplot(axz)
		if j == 1:
			# ax = plt.Subplot(fig, inner[j])
			ax = fig.add_subplot(inner[j])
			axz = figz.add_subplot(inner[j])
			ax.plot(time,sol[:,-1],color='black')
			axz.plot(time[time>4],sol[:,-1][time>4],color='black')
			axz.plot(time[time>4],p[42]*ones(len(sol))[time>4],color='blue', ls='dashed')
			ax.set_xlim(0,5)
			axz.set_xlim(4,4.5)
			if i == 0 or i == 4 or i == 8:
				ax.set_ylabel('[Na]')
				axz.set_ylabel('[Na]')
			
			ax2 = ax.twinx()
			axz2 = axz.twinx()
			ax2.plot(time,ipump,color='red')
			axz2.plot(time,ipump,color='red')
			if i == 3 or i == 7 or i == 11:
				ax2.set_ylabel('$I_{{pump}}$ (nA)')
				axz2.set_ylabel('$I_{{pump}}$ (nA)')
			if i > 7:
				ax.set_xlabel('time (sec)')
				axz.set_xlabel('time (sec)')
			if i <= 7:
				ax.set_xticks([])
				axz.set_xticks([])
			fig.add_subplot(ax)
			figz.add_subplot(axz)

fig.suptitle('realclean-imi-NaKpump -- ' + pfname)
figz.suptitle('realclean-imi-NaKpump -- ' + pfname)
fig.savefig('optsol-'+hashname+'.Imaxpump.png')
figz.savefig('optsol-'+hashname+'.Imaxpump.zoom.png')

# Plot the optimal parameter set
print(f'{originalp[40] = }')

p = originalp.copy()
savetxt(pfname, p)
command = 'java -cp celltemp.jar findcells.integrateTempCompCell_imi_realclean_NaKpump_rk4 cis_realclean_NaKpump.txt '+pfname+' 5 25 0.1 > sol.txt'
os.system(command)

# sol = genfromtxt('nopumpsolution.txt')
sol = genfromtxt('sol.txt')
time= linspace(0,5,len(sol))
ipump = getIpump(p,sol)

figure(figsize=(10,8))
suptitle(f'realclean-imi-NaKpump -- $I_{{max}}$ = {p[40]} nA -- ' + pfname)
subplot(311)
ylabel('V')
plot(time,sol[:,0],color='black')
ax1 = subplot(312)
ylabel('[Na]')
plot(time,sol[:,-1],color='black')
ax2 = ax1.twinx()
ax2.plot(time,ipump,color='red')
ylabel('$I_{{pump}}$ (nA)')
subplot(313)
ylabel('[Ca]')
plot(time,sol[:,-2],color='black')
xlabel('time (sec)')
savefig('optsol-'+hashname+'.png')

figure(figsize=(10,8))
suptitle(f'realclean-imi-NaKpump -- $I_{{max}}$ = {p[40]} nA -- ' + pfname)
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
ylabel('$I_{{pump}}$ (nA)')
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