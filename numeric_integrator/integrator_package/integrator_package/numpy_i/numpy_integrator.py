#!/user/bin/python


import numpy as np
from matplotlib import pyplot as plt
import math


def f(x):
	return math.sin(x)
def f2(x):
	return 2


def integrate(f, a, b, N):
	deltaX = (b-a)/N
	x = np.linspace(a, b-deltaX, N)
	res = np.sum(np.ones(x.shape)*f(x)*deltaX)
	return res

def integrate_midpoint(f, a, b, N):
	deltaX = (b-a)/N
	x = np.linspace(a+deltaX/2, b-deltaX/2 , N)
	func = f(x)
	res = np.sum(func*deltaX)
	return res
