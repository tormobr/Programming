#!/user/bin/python

import numba as nb
from numba import optional
import time
import math

def integrate(f, a, b, N):
	res = 0.0
	@nb.jit(nopython=True)
	def hax(a,b,N,res):

		deltaX = (b-a)/N
		for i in range(N):
			res += (f(a) * deltaX)	
			a += deltaX
		return res
	return hax(a,b,N,res)
	


def integrate_midpoint(f, a, b, N):
	res = 0.0
	@nb.jit(nopython=True)
	def hax(a,b,N,res):

		deltaX = (b-a)/N
		for i in range(N):
			res += f((a+a+deltaX)/2) * deltaX
			a += deltaX
		return res
	return hax(a,b,N,res)