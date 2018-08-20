#!/user/bin/python

import normal.integrator as p_i
import numpy_i.numpy_integrator as np_i
import numba_i.numba_integrator as nb_i
import functions.functions as func
import math


def compare_iterations():
	 run = True
	 a = False
	 b = False
	 c = False
	 d = False
	 e = False
	 f = False
	 n = 89000
	 while run:
	 	if abs(p_i.integrate_midpoint(func.f4, 0, math.pi, n) -  2) < 10**-10 and not a:
	 		print("Python-midpoint integrator   N = ", n)
	 		a = True

	 	if abs(np_i.integrate_midpoint(func.np_f4, 0, math.pi, n) -  2) < 10**-10 and not b:
	 		print("Numpy-midpoint integrator   N = ", n)
	 		b = True 

	 	if abs(nb_i.integrate_midpoint(func.nb_f4, 0, math.pi, n) -  2) < 10**-10 and not c:
	 		print("Numba-midpoint integrator   N = ", n)
	 		c = True 

	 	if abs(p_i.integrate(func.f4, 0, math.pi, n) -  2) < 10**-10 and not d:
	 		print("Python integrator 		N = ", n)
	 		d = True

	 	if abs(np_i.integrate(func.np_f4, 0, math.pi, n) -  2) < 10**-10 and not e:
	 		print("Numpy integrator 		N = ", n)
	 		e = True 

	 	if abs(nb_i.integrate(func.nb_f4, 0, math.pi, n) -  2) < 10**-10 and not f:
	 		print("Numba integrator  		N = ", n)
	 		f = True 

	 	if a and b and c and d and e and f:
	 		run = False
	 	else:
	 		n += 500

