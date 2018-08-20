#!/user/bin/python

from matplotlib import pyplot as plt
import numpy 

def integrate(f, a, b, N, make_plt=False):
	res = 0.0
	x = []
	y = []
	deltaX = (b-a)/N
	for i in range(N):
		
		x.append(a)
		y.append(f(a))

		res += (f(a) * deltaX)
		a += deltaX
	if make_plt is True:
		x.append(a)
		y.append(f(a))
		generate_plot(x, y, deltaX)
	return res


def generate_plot(x, y, dx):
	plt.style.use("seaborn-poster")
	plt.grid()
	plt.plot(x, y, linewidth=3, color="green")
	plt.bar(x[:-1], y[:-1], dx, alpha=0.50, linewidth=2, facecolor="grey")

	plt.show()

def integrate_midpoint(f, a, b, N):
	res = 0.0
	deltaX = (b-a)/N
	for i in range(N):
		res += f((a + a+deltaX)/2)*deltaX
		a += deltaX
	return res

