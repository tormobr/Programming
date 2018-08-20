

import os
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt


class Climate():
	""" Class including info over temperatures and co2 emmision over the last couple of hundred years
	"""
	def __init__(self, temperature_file, co2_file, co2_country_file):
		"""init function that sets the class-variables
		parameters:
			temperatur_file - csv file containing data over temperatures
			co2_file - csv file containing data over co2 emmision
			co2_country _file - csv file containing data over co2 emmision in different countries
		"""
		self.temperature_file = temperature_file
		self.co2_file = co2_file
		self.co2_country_file = co2_country_file

		plt.style.use("dark_background")
		plt.rcParams["figure.figsize"] = (16, 4)
		plt.grid()

	def plot_temp(self, start, end, month, filename):
		"""Function that plots the temperatur based on the parameters given
		parameters:
			start - the start year of the interval we want to plot
			end - the end year of the interval we want to plot
			month - the month we want plot
			filename - the filename of the PNG-file we generate when plotting
		"""
		plt.hold(False)
		temperature_data = pd.read_csv(self.temperature_file, index_col = "Year")
		plot_data = temperature_data.loc[start:end, month]
		plot_data.plot(linewidth = 2, color="orange")

		plt.title("Temperatures for {} in the period: {} - {}".format(month, start, end), fontsize=20, fontname="serif")
		plt.xlabel("Year", fontsize=16, fontname="serif")
		plt.ylabel("Temperature in celsius", fontsize=16, fontname="serif")
		
		plt.savefig(os.path.join("static/temp", filename), bbox_inches = "tight")

	def plot_co2(self, start, end, filename):
		"""Function that plots the co2 emission over years
		parameters:
			start - the start year of the interval we want to plot
			end - the end year of the interval we want to plot
			filename - the filename of the PNG-file we generate when plotting
		"""
		plt.hold(False)
		co2_data = pd.read_csv(self.co2_file, index_col = "Year")
		plot_data = co2_data.loc[start:end]
		plot_data.plot(linewidth = 2, color="orange")

		plt.title("Co2 emission in the period: {} - {}".format( start, end), fontsize=20, fontname="serif")
		plt.xlabel("Year", fontsize=16, fontname="serif")
		plt.ylabel("co2 emmision in ton", fontsize=16, fontname="serif")
		
		plt.savefig(os.path.join("static/co2", filename), bbox_inches = "tight")

	def plot_co2_country(self, year, minimum, maximum, filename):
		"""Function that plots the co2 emission over years
		parameters:
			year - the year we want to plot
			minimum - the minimum treshold for emission to be plotted
			maximum - the maximum treshold for emission to be plotted
			filename - the filename of the PNG-file we generate when plotting
		"""
		plt.hold(False)  # Disable hold o make sure new figure is always used

		co2_data = pd.read_csv(self.co2_country_file)          # Read all data
		co2_data = co2_data.set_index(co2_data[co2_data.columns[1]])  # Set country name index

		co2_data = co2_data[co2_data[str(year)] >= minimum]    # Filter out below treshold
		co2_data = co2_data[co2_data[str(year)] <= maximum]    # Filter out above treshold

		co2_data.loc[:, str(year)].plot(kind = "bar")                   # Bar chart for a year

		plt.title("CO$_2$ emissions in {} for countries".format(year), fontsize = 18)       # Generate title
		plt.ylabel("CO$_2$ emissions int 10^6 tons", fontsize = 16) #Label on y-axis

		plt.savefig(os.path.join("static/co2_country", filename), bbox_inches = "tight")

if (__name__ == "__main__"):
    climate = Climate("temps.csv")

    climate.plot_temp(2000,2012, "May")
    climate.plot_temp(2000,2012, "July")
    plt.show()
