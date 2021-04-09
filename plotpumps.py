import matplotlib as mpl
import matplotlib.pyplot as plt
import matplotlib.gridspec as gridspec
import numpy as np
import subprocess

# Calculate pump current
def getipump(p, sol):
	ipump = p[39] * p[40] / (1 + np.exp((p[42] - sol[:,-1]) / p[41]))
	return ipump

# Specify parameter set
hashname='5ISKBL'

# Load the model
pfname='optsol-'+hashname+'.txt'
p = np.genfromtxt(pfname)
originalp = p.copy()
print(f'{originalp[40] = }')

# Run the model
command = 'java -cp celltemp.jar findcells.integrateTempCompCell_imi_realclean_NaKpump_rk4 cis_realclean_NaKpump.txt '+pfname+' 20 25 0.05 > sol.txt'
subprocess.run(command, shell=True)
sol = np.genfromtxt('sol.txt')
time = np.linspace(0,20,len(sol))
ipump = getipump(p,sol)

# Plot the control parameter set
fig, axs = plt.subplots(3, sharex=True, figsize=(6,8))
plt.xticks(fontsize=12)
plt.yticks(fontsize=12)
axs[0].plot(time[time>19],sol[:,0][time>19],color='#1b9e77')
axs[0].set_ylabel('V (mV)', fontsize=12)
axs[0].set_xlim(19,20)
axs[0].tick_params(labelsize=12)
axs[1].plot(time[time>19],sol[:,-1][time>19],color='#d95f02')
axs[1].set_ylabel('[$Na^+$] (mmol/L)', fontsize=12)
axs[1].tick_params(labelsize=12)
axs[2].plot(time[time>19],ipump[time>19],color='#7570b3')
axs[2].set_xlabel('Time (s)', fontsize=12)
axs[2].set_ylabel('$I_{pump}$ (nA)', fontsize=12)
axs[2].tick_params(labelsize=12)
fig.suptitle(f'IMI-NaKpump | {pfname} | $I_{{max}}$ = {p[40]} nA', fontsize=12)
fig.savefig('control-'+hashname+'.png')