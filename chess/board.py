import sys
from termcolor import colored
from square import Square
from piece import Piece
class Board:

	def generate_board(self):
		squares = []
		letters = ["A", "B", "C", "D", "E", "F", "G", "H"]

		for i in range(8):
			squares.append([])
			for j in range(8):

				piece_info = self.set_pieces(i,j)
				piece = None
				if piece_info[0] != None:
					piece = Piece(piece_info[0], piece_info[1], piece_info[2])

				position = letters[j] + str(i+1)

				squares[i].append(Square(position, piece))
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

	def print_board(self):
		for x in hax:
			print("--------------------------------")
			for y in x:
				sys.stdout.write(" | ")
				sys.stdout.write(str(y))
				sys.stdout.write(" | ")
				#print(y,)
			print()

if __name__=="__main__":
	b = Board()
	hax = b.generate_board()
	b.print_board()
