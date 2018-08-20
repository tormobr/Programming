
import integrator_package as ip
from integrator_package import *

import integrator_package.tests.test_integrator as ti

ti.test_integral_of_constant_function()
ti.test_integral_of_linear_function()
ti.compare_time()
ti.compare_iterations()
ti.calculate_error(200)
ti.test_comp()



