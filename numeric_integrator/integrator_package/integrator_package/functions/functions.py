#!/user/bin/python
import numba as nb
import numpy as np
import math

#python functions
def f(x):
	return 2

def f2(x):
	return 2*x

def f3(x):
	return x**2

def f4(x):
	return math.sin(x)

def f5(x):
	return 2*x + math.sin(x)*math.exp(x)

def f6(x):
	return 2*x**3 + 4*x**2 + 10*x


#numba functions
@nb.jit(nopython=True)
def nb_f(x):
	return 2

@nb.jit(nopython=True)
def nb_f2(x):
	return 2*x

@nb.jit(nopython=True)
def nb_f3(x):
	return x**2

@nb.jit(nopython=True)
def nb_f4(x):
	return math.sin(x)

@nb.jit(nopython=True)
def nb_f5(x):
	return 2*x + math.sin(x)*math.exp(x)

@nb.jit(nopython=True)
def nb_f6(x):
	return 2*x**3 + 4*x**2 + 10*x

#numpy
def np_f4(x):
	return np.sin(x)
