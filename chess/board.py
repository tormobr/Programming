import sys
from termcolor import colored
from square import Square
from piece import Piece
from movement import Movement
class Board:
	letters = ["A", "B", "C", "D", "E", "F", "G", "H"]

	def __init__(self):
		self.squares = self.generate_board()
		self.movement = Movement(self.squares)
		

	def get_index(self, start, end):
		start_i = ord(start[0])-65
		start_j = int(start[1])-1

		end_i = ord(end[0])-65
		end_j = int(end[1])-1
		#print(start_i, start_j)
		return[(start_j, start_i), (end_j, end_i)]

	def move(self, start, end):
		indexes = self.get_index(start, end)
		start_square = self.squares[indexes[0][0]][indexes[0][1]]
		end_square = self.squares[indexes[1][0]][indexes[1][1]]
		piece = start_square.piece

		
		if end_square.piece != None and start_square.piece.color == end_square.piece.color:
			print("CAN'T STRIKE OWN PIECE YOU PLEB")
			return

		allowed = self.movement.move(self.squares, piece, start_square, end_square)
		if allowed:
			print("allowed")
			tmp = start_square.piece
			end_square.piece = tmp
			start_square.piece = None
			end_square.piece.moved = True
		else:
			print("not allowed")

	def occupied(self, index):
		if self.squares[index[0]][index[1]].piece == None: 
			return True
		return False

	def generate_board(self):
		squares = []

		for i in range(8):
			squares.append([])
			for j in range(8):

				piece_info = self.set_pieces(i,j)
				piece = None
				if piece_info[0] != None:
					piece = Piece(piece_info[0], piece_info[1], piece_info[2])

				position = self.letters[j] + str(i+1)

				squares[i].append(Square(position, piece, j, i))
		return squares

	def set_pieces(self, i, j):
		piece = None
		color = None
		symbol = " "
		if i <= 1:
			color = "white"
		elif i >= 6:
			color = "red"
		if i == 6 or i == 1:
			piece = "pawn"
			symbol = u'\u265F'
			
		elif i == 0 or i == 7:
			if j == 0 or j == 7:
				piece = "rook"
				symbol = u'\u265C'
			elif j == 1 or j == 6:
				piece = "knig"
				symbol = u'\u265E'

			elif j == 2 or j == 5:
				piece = "bish"
				symbol = u'\u265D'
			elif j == 3:
				piece = "quen"
				symbol = u'\u265B'

			elif j == 4:
				piece = "king"
				symbol = u'\u265A'


		return (piece,color, symbol)


	def __repr__(self):
		"""
		simply prints the board with pieces.
		"""
		ret = ""
		for i, x in enumerate(self.squares):

			ret += "\t"
			for j in range(32): ret += u"\u2015"
			ret += "\n\t|"
			for y in x:
				ret += str(y)
				ret += " | "

			ret += str(i+1) + "\n"

		ret += "\t"
		for i in range(32): ret += u"\u2015"
		ret += "\n         "

		for l in self.letters:
			ret += l+"   "
		return ret

if __name__=="__main__":
	b = Board()
	hax = b.generate_board()
	print(b)
