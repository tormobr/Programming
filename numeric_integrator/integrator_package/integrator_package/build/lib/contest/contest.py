#!/user/bin/python

import numba as nb
import numpy as np
import time
import math


@nb.jit(nopython=True)
def f100(x):
	return x**3

@nb.jit(nopython=True)
def f1(x):
	return (1/math.pi) * (math.sin(x)/x) * (math.sin(x/3)/(x/3)) * (math.sin(x/5)/(x/5))


@nb.jit(nopython=True)
def f2(x):
	return (1/math.pi) * (math.sin(x)/x) * (math.sin(x/3)/(x/3)) * (math.sin(x/5)/(x/5)) * (math.sin(x/7)/(x/7))


@nb.jit(nopython=True)
def f3(x):
	return (1/math.pi) * (math.sin(x)/x) * (math.sin(x/3)/(x/3)) * (math.sin(x/5)/(x/5)) * (math.sin(x/7)/(x/7)) * (math.sin(x/9)/(x/9))

@nb.jit(nopython=True)
def f4(x):
	return (1/math.pi) * (math.sin(x)/x) * (math.sin(x/3)/(x/3)) * (math.sin(x/5)/(x/5)) * (math.sin(x/7)/(x/7)) * (math.sin(x/9)/(x/9)) * (math.sin(x/11)/(x/11))

@nb.jit(nopython=True)
def f5(x):
	return (1/math.pi) * (math.sin(x)/x) * (math.sin(x/3)/(x/3)) * (math.sin(x/5)/(x/5)) * (math.sin(x/7)/(x/7)) * (math.sin(x/9)/(x/9)) * (math.sin(x/11)/(x/11)) * (math.sin(x/13)/(x/13))

@nb.jit(nopython=True)
def f6(x):
	return (1/math.pi) * (math.sin(x)/x) * (math.sin(x/3)/(x/3)) * (math.sin(x/5)/(x/5)) * (math.sin(x/7)/(x/7)) * (math.sin(x/9)/(x/9)) * (math.sin(x/11)/(x/11)) * (math.sin(x/13)/(x/13)) * (math.sin(x/15)/(x/15))

@nb.jit(nopython=True)
def f7(x):
	return (1/math.pi) * (math.sin(x)/x) * (math.sin(x/4)/(x/4)) * (math.sin(x/4)/(x/4)) * (math.sin(x/7)/(x/7)) * (math.sin(x/7)/(x/7)) * (math.sin(x/9)/(x/9)) * (math.sin(x/9)/(x/9))



def integrate_simpson(f, a, b, N):
	res = np.float64(0.0)
	@nb.jit(nopython=True)
	def hax(a,b,N,res):
		deltaX = (b-a)/(N)
		for i in range(N+1):
			if i == N or i == 0:
				res += f(a)	
			elif i % 2 == 0:
				res += 2*f(a)
			else:
				res += 4*f(a) 
			a += deltaX
		return res*deltaX/3
	return hax(a,b,N,res)