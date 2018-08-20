
import re
import codecs
from urllib.request import urlopen
import requests
from requests.exceptions import ConnectionError

#f=codecs.open("test.html", 'r')


####
# In the corect result in the example.py file there are some emails with "_" in the domain
# In the assignment description it says that this isnt allowed. Therefor my scraper doesnt include
# these email even tho they are labeled as 'correct' in the examples.py file.
# This would have been easy to fix, just by adding '_' as an allowed character in the regex/domain.
####

emails = []
urls = []
def find_emails(txt):
	#finds email adresses based on the rules given in the assignment.
	reg = re.compile(r"[A-Z0-9.#$%&~’*+-/=?‘|{}_]+@[A-Z0-9.#$%&~’*+-/=?‘|{}_]+\.[A-Z]+[A-Z]", re.IGNORECASE)
	emails = re.findall(reg, txt)

	return emails

#Found all the urls given in the out file on github, but i also found a couple of more.
#It looks like it follows all the rules for urls, and therefor i didnt exclude them.
def find_urls(txt):
	#finds all urls based one the rules given in the assignment.
	reg = re.compile(r'<a href="(http[s]?:[A-Z.-~]+\.[A-Z/.-~]+)">', re.IGNORECASE)
	urls = re.findall(reg, txt)

	#returns a set, to exclude the duplicates
	a = []
	for url in urls:
		if(url not in a):
			a.append(url)
	return a


#ret= find_urls(f.read())
#for u in ret:
#	print(u)

def find_urls_hax(txt):
	reg = re.compile(r'<a href="(?![http])([A-Z.-~]+\.[A-Z/.-~]+)">', re.IGNORECASE)
	urls = re.findall(reg, txt)

	#returns a set, to exclude the duplicates
	a = []
	for url in urls:
		if("http://"+url not in a):
			a.append("http://" + url)
	return a


##
# Some of the urls given in the test html file doesnt connect propperly. This couses a HTTPerror(connection error)
# Therefor if one of those urls, occour it will just skip it, and go on with the next url in the list.
##
def all_the_emails(url, depth):
	global urls
	global emails
	try:
		txt = requests.get(url).text
	except ConnectionError:
		print("Not valid url")
		return -1

	new_urls = find_urls(txt)
	new_mails = find_emails(txt)

	for e in new_mails:
		if e not in emails:
			emails.append(e)

	for u in new_urls:
		if u not in urls:
			urls.append(u)

	if(depth > 0):
		for url in new_urls:
			all_the_emails(url, depth-1)


all_the_emails('https://www.nrk.no/', 1)
print("emails: ", emails)
print("urls: ", urls)
