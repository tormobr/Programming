import os
from flask import Flask
from flask import request
from flask import render_template
from data_plotter import Climate

from time import gmtime, strftime

app = Flask(__name__)
#os.system("pydoc -w temperature_CO2_plotter")
os.system("pydoc -w data_plotter")
os.system("mv data_plotter.html templates")

clim = Climate("csv_files/temps.csv", "csv_files/co2.csv", "csv_files/co2_country.csv")
init = False
temp_filename = None
co2_filename = None
co2_country_filename = None

@app.route("/", methods = ["GET", "POST"])
def visualize_data():
	"""Function that reads the input from the user on the website, and calls the plot functions in Climate
	with the right params."""
	global init
	global temp_filename
	global co2_filename
	global co2_country_filename

	if not init:									#If its the first time opening the webpage
		if(temp_filename is None):					#If files doesnt exist
			temp_filename = "temperature_plot.png"	
			co2_filename = "co2_plot.png"
			co2_country_filename = "co2_country_plot.png"

		#plotting with init parameters
		clim.plot_temp(1960, 2012, "January",temp_filename)
		clim.plot_co2(1960, 2012, co2_filename)
		clim.plot_co2_country(2012, 10.0, 30.0, co2_country_filename)
		init = True


	else:
		#If we get a post request from the webpage
		if (request.method == "POST"):	
			addon = strftime("%Y-%m-%d;%H:%M:%S", gmtime())		#gets the datetime
			os.system("rm -f static/temp/*.png")	#removing old file
			os.system("rm -f static/co2/*.png")		#removing old file
			os.system("rm -f static/co2_country/*.png")		#removing old file

			temp_filename = "temperature_plot"+addon+".png"		#adding the datetime to the filename
			co2_filename = "co2_plot"+addon+".png"				#adding the datetime to the filename
			co2_country_filename = "co2_country_plot"+addon+".png"				#adding the datetime to the filename

			#calling the plot function in Climate
			clim.plot_temp(request.form["startyear temp"],request.form["endyear temp"], request.form["month"], temp_filename)
			clim.plot_co2(request.form["startyear co2"],request.form["endyear co2"], co2_filename)
			clim.plot_co2_country(request.form["year co2_country"], float(request.form["min co2_country"]), float(request.form["max co2_country"]), co2_country_filename)

	#renders the html-page
	return render_template("main_page.html", tempfile="/static/temp/"+temp_filename, co2file="/static/co2/"+co2_filename, co2countryfile="/static/co2_country/"+co2_country_filename)



#Just a litle easter-egg
@app.route("/help")
def show_help():
	"""Shows the pydoc in html"""
	return render_template("data_plotter.html")

#Just a litle easter-egg
@app.route("/chief")
def show_chief():
	"""Just a litle easteregg"""
	return render_template("chief.html")

app.run(host="0.0.0.0", port=1234, debug=True)