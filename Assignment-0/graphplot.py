import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D

prob, width, time = [] ,[] , []

with open('output.txt') as result:
	file = result.read()
	file = file.replace('\n', ',').split(',')

	i = 0
	while i < len(file) - 3:
		prob.append(float(file[i]))
		width.append(int(file[i+1]))
		time.append((float(int(file[i+2])+int(file[i+5])+int(file[i+8])+int(file[i+11])+int(file[i+14]))/5))
		i += 15

fig = plt.figure()
graph = fig.add_subplot(111, projection='3d', xlabel='Probability', ylabel='Width', zlabel='Time (s)')
graph.plot(xs=prob, ys=width, zs=time)
graph.ticklabel_format(useOffset=False, style='plain')
plt.savefig('graph.png')
