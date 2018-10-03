class Square:
	def __init__(self, position, piece, x, y):
		self.position = position
		self.piece = piece
		self.x = x
		self.y = y

	def __repr__(self):
		if self.piece != None:
			return str(self.piece)
		else:
			return " "
