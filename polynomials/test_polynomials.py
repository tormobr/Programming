from polynomials import Polynomial

# Usage: python -m pytest -v

a = Polynomial([1,2,3])
b = Polynomial([1,2,3,0,0])
c = Polynomial([2,5])
d = Polynomial([0,0,0,0,0])
e = Polynomial([1,2,3,4,-5,-6,])
f = Polynomial([8])
g = Polynomial([2,4,6])
h = Polynomial([1,-1,-1,1,-1])

def test_init():
    assert a.coef == [1,2,3]
    assert b.coef == [1,2,3]
    assert d.coef == []

def test_degree():
    assert a.degree() == 2
    assert c.degree() == 1
    assert d.degree() == -1
    assert e.degree() == 5
    assert f.degree() == 0

def test_add():
    assert (a + b).coef == [2,4,6]
    assert (a + e).coef == [2,4,6,4,-5,-6]
    assert (a + d).coef == a.coef

def test_mul():
    assert (a * 2).coef ==  [2,4,6]
    assert (d * 3).coef ==  []
    assert (f * 1).coef ==[8]

def test_rmul():
    assert (2 * a).coef == [2,4,6]
    assert (d * 3).coef == []

def test_sub():
    assert (e - c).coef ==  [-1,-3,3,4,-5,-6]
    assert (a - a).coef == []
    assert (h - g).coef ==  [-1,-5,-7,1,-1]



