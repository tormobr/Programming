#!/usr/bin/python

import sys
import os


start = sys.argv[2]
pattern = sys.argv[1]

for path, subdir, files in os.walk(start):
	for name in files:
		if pattern in name:
			print(os.path.join(path, name))