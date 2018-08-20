#!/user/bin/python
import integrator_package.normal.integrator as p_i
import integrator_package.numpy_i.numpy_integrator as np_i
import integrator_package.numba_i.numba_integrator as nb_i
import integrator_package.contest.contest as c_i
import integrator_package.functions.functions as func
import integrator_package.tests.integrator_comparison as i_c
import math
from matplotlib import pyplot as plt
import time



def calculate_error(N):
	filename = "quadratic_error.png"
	x=[]
	y=[]
	z=[]
	for i in range(2,N, 2):
		x.append(i)
		y.append(abs(p_i.integrate(func.f3, 0, 1, i) - (1/3)))
		z.append(abs(p_i.integrate_midpoint(func.f3, 0, 1, i) - (1/3)))

	plt.style.use("ggplot")
	

	plt.plot(x,y,linewidth=2, label="normal")
	plt.plot(x,z,linewidth=2, label="trapz")

	plt.title("Quadratic error", fontsize=19, fontname="serif")
	plt.xlabel("N", fontsize=12, fontname="serif")
	plt.ylabel("Error", fontsize=12, fontname="serif")
	plt.legend()
	plt.show()
	plt.savefig(filename)

def test_integral_of_constant_function():
	assert abs(p_i.integrate(func.f, 0, 1, 1) - 2) < 10**-14
	assert abs(p_i.integrate(func.f, 0, 1, 10) - 2) < 10**-14
	assert abs(p_i.integrate(func.f, 0, 1, 100) - 2) < 10**-14

	assert abs(np_i.integrate(func.f, 0, 1, 1) - 2) < 10**-14
	assert abs(np_i.integrate(func.f, 0, 1, 10) - 2) < 10**-14
	assert abs(np_i.integrate(func.f, 0, 1, 100) - 2) < 10**-14

	assert abs(nb_i.integrate(func.nb_f, 0, 1, 1) - 2) < 10**-14
	assert abs(nb_i.integrate(func.nb_f, 0, 1, 10) - 2) < 10**-14
	assert abs(nb_i.integrate(func.nb_f, 0, 1, 100) - 2) < 10**-14


#Testing integrals on linear func with every p_i(normal, numpy and numba)
def test_integral_of_linear_function():
	assert abs(p_i.integrate(func.f2, 0, 1, 100) - 1) < 1/99
	assert abs(p_i.integrate(func.f2, 0, 1, 1000) - 1) < 1/999
	assert abs(p_i.integrate(func.f2, 0, 1, 10000) - 1) < 1/9999
	assert abs(p_i.integrate(func.f2, 0, 1, 100000) - 1) < 1/99999

	assert abs(np_i.integrate(func.f2, 0, 1, 100) - 1) < 1/99
	assert abs(np_i.integrate(func.f2, 0, 1, 1000) - 1) < 1/999
	assert abs(np_i.integrate(func.f2, 0, 1, 10000) - 1) < 1/9999
	assert abs(np_i.integrate(func.f2, 0, 1, 100000) - 1) < 1/99999

	assert abs(nb_i.integrate(func.nb_f2, 0, 1, 100) - 1) < 1/99
	assert abs(nb_i.integrate(func.nb_f2, 0, 1, 1000) - 1) < 1/999
	assert abs(nb_i.integrate(func.nb_f2, 0, 1, 10000) - 1) < 1/9999
	assert abs(nb_i.integrate(func.nb_f2, 0, 1, 100000) - 1) < 1/99999


#Testing the numpy p_i compared to the normal on the function x**2 from -10 to 10. correct answer=2000/3
def test_numpy():
	#print (p_i.integrate(func.f3, -10, 10, 100000))
	assert abs(p_i.integrate(func.f3, -10, 10, 1000000) - (2000/3)) < 1E-8
	assert abs(p_i.integrate(func.f3, -10, 10, 1000000) - (2000/3)) < 1E-8

	assert abs(np_i.integrate(func.f3, -10, 10, 1000000) - (2000/3)) < 1/1E-8
	assert abs(np_i.integrate(func.f3, -10, 10, 1000000) - (2000/3)) < 1/1E-8

def compare_time():
	print("\n\n******* Function=2*x**3 + 4*x**2 + 10*x -- a=-10 -- b=10 -- N=1000000 *******\n")

	t0 = time.clock()
	print("***PYTHON***")
	print (p_i.integrate(func.f6, -10, 10, 1000000))
	print ("runtime is: ", time.clock() - t0, "\n")

	t0 = time.clock()
	print("***NUMPY***")
	print (np_i.integrate(func.f6, -10, 10, 1000000))
	print ("runtime is: ", time.clock() - t0, "\n")

	t0 = time.clock()
	print("***NUMBA***")
	print (nb_i.integrate(func.nb_f6, -10, 10, 1000000))
	print ("runtime is: ", time.clock() - t0, "\n")

	print("*************************************************************************************")
	print("******* Function=2*x**3 + 4*x**2 + 10*x -- a=-10 -- b=10 -- N=20000000 *******\n\n")
	t0 = time.clock()
	print("***PYTHON***")
	print (p_i.integrate(func.f6, -10, 10, 20000000))
	print ("runtime is: ", time.clock() - t0, "\n")

	t0 = time.clock()
	print("***NUMPY***")
	print (np_i.integrate(func.f6, -10, 10, 20000000))
	print ("runtime is: ", time.clock() - t0, "\n")

	t0 = time.clock()
	print("***NUMBA***")
	print (nb_i.integrate_midpoint(func.nb_f6, -10, 10, 20000000))
	print ("runtime is: ", time.clock() - t0, "\n")


def compare_iterations():
	i_c.compare_iterations()

def test_comp():

	print ("function 1: ", c_i.integrate_simpson(c_i.f1, 10**-20, 10**7, 24000000))
	print ("function 1: ", c_i.integrate_simpson(c_i.f2, 10**-20, 10**7, 24000000))
	print ("function 3: ", c_i.integrate_simpson(c_i.f3, 10**-50, 10**7, 24000000))
	print ("function 4: ", c_i.integrate_simpson(c_i.f4, 10**-50, 10**7, 24000000))
	print ("function 5: ", c_i.integrate_simpson(c_i.f5, 10**-50, 10**7, 24000000))
	print ("function 6: ", c_i.integrate_simpson(c_i.f6, 10**-50, 10**7, 24000000))
	print ("function 7: ", c_i.integrate_simpson(c_i.f7, 10**-50, 10**7, 24000000))
