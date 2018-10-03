class Square:
	def __init__(self, position, piece):
		self.position = position
		self.piece = piece

	def __repr__(self):
		if self.piece != None:
			return str(self.piece)
		else:
			return " "
