#%%
import matplotlib.pyplot as plt
import matplotlib.gridspec as gridspec
import numpy as np
import subprocess

# Specify parameter set
hashname = '5ISKBL'

# Calculate pump current
def getipump(p, sol):
	ipump = p[39] * p[40] / (1 + np.exp((p[42] - sol[:,-1]) / p[41]))
	return ipump

# Load the model
pfname='optsol-'+hashname+'.txt'
p = np.genfromtxt(pfname)
originalp = p.copy()
print(f'{originalp[40] = }')
print(f'{originalp[41] = }')
print(f'{originalp[42] = }')

# Run the model
command = 'java -cp celltemp.jar findcells.integrateTempCompCell_imi_realclean_NaKpump_rk4 cis_realclean_NaKpump.txt '+pfname+' 20 25 0.05 > sol.txt'
subprocess.run(command, shell=True)
sol = np.genfromtxt('sol.txt')
time = np.linspace(0,20,len(sol))
ipump = getipump(p,sol)

#%%
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

# Restore control parameter set
p = originalp.copy()
np.savetxt(pfname, p)
#%%

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
	command = 'java -cp celltemp.jar findcells.integrateTempCompCell_imi_realclean_NaKpump_rk4 cis_realclean_NaKpump.txt '+pfname+' 20 25 0.05 > sol.txt'
	subprocess.run(command, shell=True)
	sol = np.genfromtxt('sol.txt')
	time = np.linspace(0,20,len(sol))

	# Plot the varied parameter set
	axs[i].plot(time[time>19], sol[:,0][time>19], color='black')
	axs[i].plot(time[time>19], -50*np.ones(len(sol))[time>19], color='#d95f02', ls='dashed')
	axs[i].tick_params(left=False, bottom=False, labelleft=False, labelbottom=False)
	if i != 5:
		axs[i].set_frame_on(False)
	else:
		axs[i].set_facecolor('#1b9e7780')
	
	if i == 11:
		axs[i].vlines(19.9, -50, 0, color='#808080', lw=4, zorder=10)
		axs[i].plot(19.9, -50, 's', color='#d95f02', markeredgecolor='black', markersize=8,
					zorder=11)
		axs[i].plot(19.9, 0, 's', color='#7570b3', markeredgecolor='black', markersize=8,
					zorder=11)
		bboxprops = dict(boxstyle='square', facecolor='white', edgecolor='black')
		axs[i].annotate('-50 mV', xy=(19.9, -50), xytext=(9, 0), color='#d95f02',
						textcoords='offset points', horizontalalignment='left', weight='bold',
						verticalalignment='center', annotation_clip=False, bbox=bboxprops)
		axs[i].annotate('0 mV', xy=(19.9, 0), xytext=(19.5, 0), color='#7570b3',
						textcoords='offset points', horizontalalignment='left', weight='bold',
						verticalalignment='center', annotation_clip=False, bbox=bboxprops)
		axs[i].tick_params(bottom=True, labelbottom=True, labelsize=15)
		axs[i].set_xlabel('Time (s)', fontsize=15)

	axs[i].set_ylabel(f'{percentage}%', fontsize=15, color='black')
	axs[i].set_xlim(19,20)

fig.suptitle(f'IMI-NaKpump | {pfname} | $I_{{max}}$ = 0% to 220%', fontsize=15)
fig.savefig('varied-I_max-'+hashname+'.png')

# Restore control parameter set
p = originalp.copy()
np.savetxt(pfname, p)
#%%

# Plot Na_is varied from -400% to 480% of control
fig, axs = plt.subplots(12, sharex=True, figsize=(9,12))
for i in range(12):
	# Vary Na_is by specified factor
	p = originalp.copy()
	factor = round((i - 5) * 0.8, 1)
	percentage = int(factor * 100)
	p[41] = p[41] * factor
	print(f'{p[41] = }')

	# Run the model
	np.savetxt(pfname, p)
	command = 'java -cp celltemp.jar findcells.integrateTempCompCell_imi_realclean_NaKpump_rk4 cis_realclean_NaKpump.txt '+pfname+' 20 25 0.05 > sol.txt'
	subprocess.run(command, shell=True)
	sol = np.genfromtxt('sol.txt')
	time = np.linspace(0,20,len(sol))

	# Plot the varied parameter set
	axs[i].plot(time[time>19], sol[:,0][time>19], color='black')
	axs[i].plot(time[time>19], -50*np.ones(len(sol))[time>19], color='#d95f02', ls='dashed')
	axs[i].tick_params(left=False, bottom=False, labelleft=False, labelbottom=False)
	if i != 5:
		axs[i].set_frame_on(False)
	else:
		axs[i].set_facecolor('#1b9e7780')
	
	if i == 11:
		axs[i].vlines(19.9, -50, 0, color='#808080', lw=4, zorder=10)
		axs[i].plot(19.9, -50, 's', color='#d95f02', markeredgecolor='black', markersize=8,
					zorder=11)
		axs[i].plot(19.9, 0, 's', color='#7570b3', markeredgecolor='black', markersize=8,
					zorder=11)
		bboxprops = dict(boxstyle='square', facecolor='white', edgecolor='black')
		axs[i].annotate('-50 mV', xy=(19.9, -50), xytext=(9, 0), color='#d95f02',
						textcoords='offset points', horizontalalignment='left', weight='bold',
						verticalalignment='center', annotation_clip=False, bbox=bboxprops)
		axs[i].annotate('0 mV', xy=(19.9, 0), xytext=(19.5, 0), color='#7570b3',
						textcoords='offset points', horizontalalignment='left', weight='bold',
						verticalalignment='center', annotation_clip=False, bbox=bboxprops)
		axs[i].tick_params(bottom=True, labelbottom=True, labelsize=15)
		axs[i].set_xlabel('Time (s)', fontsize=15)

	axs[i].set_ylabel(f'{percentage}%', fontsize=15, color='black')
	axs[i].set_xlim(19,20)

fig.suptitle(f'IMI-NaKpump | {pfname} | $[Na]_{{is}}$ = -400% to 480%', fontsize=15)
fig.savefig('varied-Na_is-'+hashname+'.png')

# Restore control parameter set
p = originalp.copy()
np.savetxt(pfname, p)
#%%

# Plot Na_ih varied from 6504% to 7516% of control
fig, axs = plt.subplots(12, sharex=True, figsize=(9,12))
for i in range(12):
	# Vary Na_ih by specified factor
	p = originalp.copy()
	factor = round((i + 70.7) * 0.92, 2)
	percentage = int(factor * 100)
	p[42] = p[42] * factor
	print(f'{p[42] = }')

	# Run the model
	np.savetxt(pfname, p)
	command = 'java -cp celltemp.jar findcells.integrateTempCompCell_imi_realclean_NaKpump_rk4 cis_realclean_NaKpump.txt '+pfname+' 20 25 0.05 > sol.txt'
	subprocess.run(command, shell=True)
	sol = np.genfromtxt('sol.txt')
	time = np.linspace(0,20,len(sol))

	# Plot the varied parameter set
	axs[i].plot(time[time>19], sol[:,0][time>19], color='black')
	axs[i].twinx().plot(time[time>19], sol[:,-1][time>19], color='red')
	axs[i].twinx().plot(time[time>19], getipump(p,sol)[time>19], color='blue')
	axs[i].plot(time[time>19], -50*np.ones(len(sol))[time>19], color='#d95f02', ls='dashed')
	axs[i].tick_params(left=False, bottom=False, labelleft=False, labelbottom=False)
	# if i != 5:
	axs[i].set_frame_on(False)
	# else:
		# axs[i].set_facecolor('#1b9e7780')
	
	if i == 11:
		axs[i].vlines(19.9, -50, 0, color='#808080', lw=4, zorder=10)
		axs[i].plot(19.9, -50, 's', color='#d95f02', markeredgecolor='black', markersize=8,
					zorder=11)
		axs[i].plot(19.9, 0, 's', color='#7570b3', markeredgecolor='black', markersize=8,
					zorder=11)
		bboxprops = dict(boxstyle='square', facecolor='white', edgecolor='black')
		axs[i].annotate('-50 mV', xy=(19.9, -50), xytext=(9, 0), color='#d95f02',
						textcoords='offset points', horizontalalignment='left', weight='bold',
						verticalalignment='center', annotation_clip=False, bbox=bboxprops)
		axs[i].annotate('0 mV', xy=(19.9, 0), xytext=(19.5, 0), color='#7570b3',
						textcoords='offset points', horizontalalignment='left', weight='bold',
						verticalalignment='center', annotation_clip=False, bbox=bboxprops)
		axs[i].tick_params(bottom=True, labelbottom=True, labelsize=15)
		axs[i].set_xlabel('Time (s)', fontsize=15)

	axs[i].set_ylabel(f'{percentage}%', fontsize=15, color='black')
	axs[i].set_xlim(19,20)

fig.suptitle(f'IMI-NaKpump | {pfname} | $[Na]_{{ih}}$ = 6504% to 7516%', fontsize=15)
fig.savefig('varied-Na_ih-'+hashname+'.png')

# Restore control parameter set
p = originalp.copy()
np.savetxt(pfname, p)
# %%
