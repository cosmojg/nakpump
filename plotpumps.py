import matplotlib.pyplot as plt
import matplotlib.gridspec as gridspec
import numpy as np
import subprocess

# Specify parameter set
hashname = '5ISKBL'

# Specify colors
colorwheel = ['#517990','#1f78b4','#608a35','#33a02c','#fb9a99','#e31a1c','#fdbf6f',
				'#ff7f00','#756081','#6a3d9a','#777711','#b15928']

# Calculate pump current
def getipump(p, sol):
	ipump = p[39] * p[40] / (1 + np.exp((p[42] - sol[:,-1]) / p[41]))
	return ipump

# Load the model
pfname='optsol-'+hashname+'.txt'
p = np.genfromtxt(pfname)
originalp = p.copy()
print(f'{originalp[40] = }')

# Run the model
command = 'java -cp celltemp.jar findcells.integrateTempCompCell_imi_realclean_NaKpump_rk4 cis_realclean_NaKpump.txt '+pfname+' 20 25 0.1 > sol.txt'
subprocess.run(command, shell=True)
sol = np.genfromtxt('sol.txt')
time = np.linspace(0,20,len(sol))
ipump = getipump(p,sol)

# Plot the control parameter set
fig, axs = plt.subplots(3, sharex=True, figsize=(6,8))
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

# Plot I_max varied from 0% to 220% of control
fig, axs = plt.subplots(12, sharex=True, figsize=(9,12))
for i in range(12):
	# Vary I_max by specified factor
	p = originalp.copy()
	factor = round(i * 0.2, 1)
	percentage = int(factor * 100)
	p[40] = p[40] * factor
	print(f'{p[40] = }')

	# Run the model
	np.savetxt(pfname, p)
	command = 'java -cp celltemp.jar findcells.integrateTempCompCell_imi_realclean_NaKpump_rk4 cis_realclean_NaKpump.txt '+pfname+' 20 25 0.1 > sol.txt'
	subprocess.run(command, shell=True)
	sol = np.genfromtxt('sol.txt')
	time = np.linspace(0,20,len(sol))

	# Plot the varied parameter set
	axs[i].plot(time[time>19], sol[:,0][time>19], color=colorwheel[i])
	axs[i].plot(time[time>19], -50*np.ones(len(sol))[time>19], color='black', ls='dashed')
	axs[i].vlines(19.9, -50, 0, lw=5, color='black')
	axs[i].plot(19.9, -50, 's', color='#1b9e77')
	axs[i].plot(19.9, 0, 's', color='#d95f02')
	bboxprops = dict(boxstyle='square', facecolor='white', edgecolor='black')
	axs[i].annotate('-50 mV', xy=(19.9, -50), xytext=(9, 0), color='#1b9e77',
					textcoords='offset points', horizontalalignment='left', weight='bold',
					verticalalignment='center', annotation_clip=False, bbox=bboxprops)
	axs[i].annotate('0 mV', xy=(19.9, 0), xytext=(19.5, 0), color='#d95f02',
					textcoords='offset points', horizontalalignment='left', weight='bold',
					verticalalignment='center', annotation_clip=False, bbox=bboxprops)
	axs[i].set_xlim(19,20)
	axs[i].tick_params(left=False, bottom=False, labelleft=False, labelbottom=False)
	if i != 5:
		axs[i].set_frame_on(False)
	axs[i].set_ylabel(f'{percentage}%', fontsize=15, color=colorwheel[i])

fig.suptitle(f'IMI-NaKpump | {pfname} | $I_{{max}}$ = 0% to 220%', fontsize=15)
fig.savefig('varied-I_max-'+hashname+'.png')

# Restore control parameter set
p = originalp.copy()
np.savetxt(pfname, p)