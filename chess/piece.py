from termcolor import colored

class Piece:
	def __init__(self, name, color, symbol):
		self.name = name
		self.color = color
		self.symbol = symbol

	def __repr__(self):
		return colored(self.symbol, self.color)