class Movement:

	def __init__(self, board):
		self.board = board

	def move(self, board, piece, start, end):
		self.board = board
		if piece.name == "pawn": return self.pawn(piece, start, end)
		elif piece.name == "bish": return self.bishop(piece, start, end)
		elif piece.name == "knig": return self.knight(piece, start, end)
		elif piece.name == "rook": return self.rook(piece, start, end)
		elif piece.name == "quen": return self.queen(piece, start, end)
		elif piece.name == "king": return self.king(piece, start, end)


	def pawn(self, piece, start, end):
		print("moving pawn")
		distX = end.x - start.x
		distY = 0
		if piece.color == "white":
			distY = end.y - start.y
		else:
			distY = start.y - end.y
		print(distX, distY)

		if distY == 1 and distX == 1 and end.piece != None:
			print("capture")
			return True

		if piece.moved == False:
			if (distY == 2 or distY == 1) and distX == 0:
				return True
		else:
			if distY == 1 and distX == 0:
				return True
		return False

	def bishop(self, piece, start, end):
		distX = end.x - start.x
		distY = end.y - start.y

		if not self.check_path(piece, start, end, distX, distY): return False

		if abs(distX) == abs(distY): return True
		return False

	def knight(self, piece, start, end):
		distX = abs(end.x - start.x)
		distY = abs(end.y - start.y)

		if distX == 2 and distY == 1: return True
		elif distX == 1 and distY == 2: return True
		return False

	def rook(self, piece, start, end):
		distX = end.x - start.x
		distY = end.y - start.y

		if not self.check_path(piece, start, end, distX, distY): return False

		if abs(distX) == 0 or abs(distY) == 0: return True
		return False
		
	def queen(self, piece, start, end):
		return self.rook(piece, start, end) or self.bishop(piece, start, end)

	def king(self, piece, start, end):
		distX = abs(end.x - start.x)
		distY = abs(end.y - start.y)

		if distX <= 1 and distY <= 1: return True

		return False

	def check_path(self, piece, start, end, distX, distY):
		moveX = 0
		moveY = 0

		if distX < 0: moveX = -1
		elif distX > 0: moveX = 1
		if distY < 0: moveY = -1
		elif distY > 0: moveY = 1

		
		print("movex: {}  movey: {} ".format(moveX, moveY))

		x = start.x + moveX
		y = start.y + moveY

		while x != end.x or y != end.y:
			print("x: {}  y: {} ".format(x, y))
			
			if self.board[y][x].piece != None  and self.board[y][x].piece.color == piece.color:
				print("square: {}  piece: {} in the way".format(self.board[y][x].position, self.board[y][x].piece))
				return False
			x += moveX
			y += moveY
		return True

	def check_check(self, board, white_king, black_king):
		self.board = board
		check_white = False
		check_black = False

		print("king 1: ", white_king)
		print("king 2: ", black_king)
		permutations = [(0,1), (0,-1), (1,0), (-1,0), (1,1), (1,-1), (-1,1), (-1,-1)]

		#for p in permutations:
		#	if self.check_check_help(p, "white", white_king) == True:
		#		check_white = True

		for p in permutations:
			if self.check_check_help(p, "red", black_king) == True:
				check_black = True
		
		return (check_white, check_black)



	def check_check_help(self, movement, color, king):
		threats = ["knig", "bish", "quen", "pawn", "rook"]
		x = king.x + movement[0]
		y = king.y + movement[1]
		while 0 <= x <= 7 and 0 <= y <= 7:
			if self.board[y][x].piece != None and self.board[y][x].piece.name in threats and self.board[y][x].piece.color != color:
				print("check detected!!!!!!!!!")
				return True
			x += movement[0]
			y += movement[1]
		return False