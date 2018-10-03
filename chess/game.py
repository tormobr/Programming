from board import Board
class Game:

	def __init__(self):
		self.board = Board() 

	def move(self):
		start = input("Moving from : ")
		end = input("Moving to   : ")
		self.board.move(start, end)


if __name__ == "__main__":
	game = Game()
	while True:
		print(game.board)
		game.move()