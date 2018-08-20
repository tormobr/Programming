#!/usr/bin/python


class Polynomial:

    def __init__(self, coefficient):
        self.coef = coefficient

        if not all(isinstance(itm, int) for itm in self.coef):
            raise ArithmeticError

        while self.coef and self.coef[-1:][0] == 0:
            self.coef = self.coef[:-1]

    def degree(self):
        """Return the index of the highest nonzero coefficient.
        If there is no nonzero coefficient, return -1."""
        
        if len(self.coef) == 0:
        	return -1
        return len(self.coef) - 1 

    def coefficient(self):
        """Return the list of coef. 

        The i-th element of the list should be a_i, meaning that the last 
        element of the list is the coefficient of the highest degree term."""
        
        return self.coef

    def __call__(self, x):
        """Return the value of the polynomial evaluated at the number x"""
        deg = 0
        summ = 0
        for c in self.coef:
        	summ+=(c * (x**(deg)))
        	deg +=1
        return summ
 
    def __sub__(self, p):

    	res = self.coef

    	if len(self.coef) < len(p.coef):
    		for i in range(len(p.coef) - len(res)):
    			res.append(0)

    	for i in range(len(p.coef)):
    		print(res)
    		res[i] -= p.coef[i]

    	return Polynomial(res)
    def __add__(self, p):
        """Return the polynomial which is the sum of p and this polynomial
        Should assume p is Polynomial([p]) if p is int. 

        If p is not an int or Polynomial, should raise ArithmeticError."""
        if not isinstance(p, int) and not isinstance(p,Polynomial):
        	raise ArithmeticError
        elif isinstance(p, int):
        	p = Polynomial(p)
        	
        res = []
        if len(self.coef) > len(p.coef):
        	longest = self.coef
        	short = p.coef
        else:
        	longest = p.coef
        	short = self.coef

        for i in range(len(short)):
        	res.append(longest[i] + short[i])
        res += longest[len(short):]
        return Polynomial(res)
        

    def __mul__(self, c):
        """Return the polynomial which is this polynomial multiplied by given integer.
        Should raise ArithmeticError if c is not an int."""

        res = []
        if isinstance(c, int):
        	for i, a in enumerate(self.coef):
        		res.append(a * c)
        	return Polynomial(res)
        raise ArithmeticError

    def __rmul__(self, c):
        """Return the polynomial which is this polynomial multiplied by some integer"""

        res = []
        for i, a in enumerate(self.coef):
        	res.append(c * a)
        return Polynomial(res)


    def __repr__(self):
        """Return a nice string representation of polynomial.
        
        E.g.: x^6 - 5x^3 + 2x^2 + x - 1
        """
        ret = ""
        for index, item in enumerate(self.coef):
        	if item != 0:
	        	if index == 0:
	        		if item < 0:
	        			ret += "-" + str(abs(item)) 
	        		else:
	        			ret = str(item)

	        	elif index == 1:
	        		if item < 0:
	        			ret += " - " + str(abs(item)) + "x"
	        		else:
	        			ret += " + " + str(item) + "x"
    		
	        	else:
		        	if item < 0:
		        		ret += " - " + str(abs(item)) + "x" + "^" + str(index)
		        	else:
		        		ret += " + " + str(item) + "x" + "^" + str(index)

        return ret


    def __eq__(self, p):
        """Check if two polynomials have the same coef."""

        return self.coef == p.coef

def sample_usage():
    p = Polynomial([1, 2, 1]) # 1 + 2x + x^2
    q = Polynomial([9, 5, 0, 6]) # 9 + 5x + 6x^3
    
    
    print("The value of {} at {} is {}".format(p, 7, p(7)))

    print("The coef of {} are {}".format(p, p.coef()))

    
    print("\nAdding {} and {} yields {}".format(p, q, p+q))

    p, q, r = map(Polynomial,
                  [
                      [1, 0, 1], [0, 2, 0], [1, 2, 1]
                  ]
    )
    
    print("\nWill adding {} and {} be the same as {}? Answer: {}".format(
        p, q, r, p+q == r
    ))
    print("\nIs {} - {} the same as {}? Answer: {}".format(
        p, q, r, p-q == r
    ))
