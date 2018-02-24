import os
from flask import Flask, send_from_directory, request

app = Flask(__name__)

allUsers = {}

@app.route("/getMatch", methods=['GET'])
def homeRoute():
	SSID = request.args.get("SSID")
	name = request.args.get("name")
	if name == None or SSID == None:
		return ("Error with request, one or more fields are empty")

	if len(allUsers) == 1:
		return("There are no matches in your area.")
	
	i = 0
	user = allUsers.keys()[i]
	while user != SSID:
		user = allUsers.keys()[i]
	return("Your match is %s" % str(user))


@app.route("/add_user", methods=['POST'])
def add_user():
	name = request.form.get("name")
	sex = request.form.get("sex")
	location = request.form.get("location")
	preference = request.form.get("preference")
	SSID = request.form.get("SSID")
	if name == None  or sex == None or location == None or preference == None or SSID == None:
		return ("Error with request, one ore more fields are empty")

	if SSID in allUsers:
		allUsers[SSID].append({"name": name, "sex": sex, "location": location, "preference": preference})
	else:
		allUsers[SSID] = [{"name": name, "sex": sex, "location": location, "preference": preference}]
	print(allUsers)
	return("Successful Request")

if __name__ == '__main__':
	app.run(use_reloader=True, port=5000, threaded=True)
