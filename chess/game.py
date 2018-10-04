from board import Board
import time
import pygame
class Game:

	moves = 0
	def __init__(self):
		self.board = Board() 
		#self.file = open("input.txt", "r")
		pygame.init()

		display_width = 800
		display_height = 800

		self.gameDisplay = pygame.display.set_mode((display_width,display_height))
		self.board_image = pygame.image.load('images/board.png')
		#self.gameDisplay.fill((255,255,255))
		self.gameDisplay.blit(self.board_image, (0,0))
		pygame.display.set_caption('A bit Racey')

		self.white = (255,255,255)

		self.clock = pygame.time.Clock()
		self.crashed = False
		
		self.pawn_black = pygame.image.load('images/pawn_black.png')
		self.pawn_white = pygame.image.load('images/pawn_white.png')
		self.king_black = pygame.image.load('images/king_black.png')
		self.king_white = pygame.image.load('images/king_white.png')
		self.quen_black = pygame.image.load('images/quen_black.png')
		self.quen_white = pygame.image.load('images/quen_white.png')
		self.bish_black = pygame.image.load('images/bish_black.png')
		self.bish_white = pygame.image.load('images/bish_white.png')
		self.knig_black = pygame.image.load('images/knig_black.png')
		self.knig_white = pygame.image.load('images/knig_white.png')
		self.rook_black = pygame.image.load('images/rook_black.png')
		self.rook_white = pygame.image.load('images/rook_white.png')

		self.images = {"pawn_red": self.pawn_black, "pawn_white": self.pawn_white, 
		"knig_red": self.knig_black, "knig_white": self.knig_white, 
		"bish_red": self.bish_black, "bish_white": self.bish_white, 
		"king_red": self.king_black, "king_white": self.king_white, 
		"quen_red": self.quen_black, "quen_white": self.quen_white,
		"rook_red": self.rook_black, "rook_white": self.rook_white,
		}



	# def move(self, x, y):
	# 	if self.moves % 2 == 0:
	# 		print("white moving")
	# 		color = "white"
	# 	else:
	# 		print("black moving")
	# 		color = "red"

	# 	start = input("Moving from : ")
	# 	end = input("Moving to   : ")
	# 	self.moves -= self.board.move(start, end, color)
	# 	time.sleep(.1)



	def update(self):
		clicks = 0
		crashed = False
		#print(game.board)


		while not crashed:

			if self.moves % 2 == 0:
				#print("white moving")
				color = "white"
			else:
				#print("black moving")
				color = "red"
			for event in pygame.event.get():
				if event.type == pygame.QUIT:
					crashed = True


				elif event.type == pygame.MOUSEBUTTONUP:
					pos = pygame.mouse.get_pos()
					pos = (pos[0]//100, pos[1]//100)
					print(pos)
					clicks += 1
					if clicks == 1:
						start = pos
					elif clicks == 2:
						end = pos
						clicks = 0
						self.moves += 1
						tmp_board = self.board.move(start, end, color)
						self.draw_board(tmp_board)

			

			
			pygame.display.update()
			self.clock.tick(60)
		pygame.quit()
		quit()

	def draw_board(self, board):
		self.gameDisplay.blit(self.board_image, (0,0))
		for i in range(len(board)):
			for j in range(len(board[i])):
				if board[i][j].piece != None:
					self.gameDisplay.blit(self.images[board[i][j].piece.name + "_" + board[i][j].piece.color], (j*96,i*96))
					pygame.display.update()

if __name__ == "__main__":
	game = Game()
	game.update()
		