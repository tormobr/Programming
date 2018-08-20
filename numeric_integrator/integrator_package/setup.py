#!/user/bin/python 

"""Creates a packgage/module for the integrator_package
   containing all scripts with all the different methods for numerical integration
   Type when in the setup.py folder: "python setup.py install" to install the package.
   then you can ie. type: "import numpy_i.numpy_integrator" to import the numpy integrator."""
   
from distutils.core import setup

setup(name='Integrator_package',
      version='1.0',
      description='Numerical intagration of functions',
      author='Tormod Brændshøi',
      author_email='tormod.brandshoi@gmail.com',
      py_modules=['integrator_package.numpy_i.numpy_integrator', 'integrator_package.numba_i.numba_integrator', 'integrator_package.functions.functions', 'integrator_package.contest.contest', 'integrator_package.normal.integrator', 'integrator_package.tests.test_integrator', 'integrator_package.tests.integrator_comparison'],
     )