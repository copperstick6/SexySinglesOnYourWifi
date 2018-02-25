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

	if len(allUsers) == 0: # for debugging
		return("Error, length should always be >= 1")
	for ssid_ in allUsers:
		if ssid_ == SSID:
			# You are the only one on the wifi network
			if len(allUsers[ssid_] == 1):
				return("There are no matches in your area.")
			i = 0
			while allUsers[ssid_][i]["name"] == name:
				i += 1
			return("Your match it %s" % allUsers[ssid_][i])

@app.route("/updatePosition", methods = ['GET'])
def updatePosition():
	name = request.args.get("name")
	latitude = request.args.get("latitude")
	longitude = request.args.get("longitude")
	if name == None or latitude == None or longitude == None:
		return ("Error with request, one or more fields are empty")
	print allUsers
	for all_ssids in allUsers:
		print all_ssids
		for user in allUsers[all_ssids]:
			print(user)
			if user['name'] == name:
				user['latitude'] = latitude
				user['longitude'] = longitude
				return("Successful request")
	return("Name not found")


@app.route("/getPosition", methods = ['GET'])
def getPosition():
	name = request.args.get("name")
	if name == None:
		return ("Error with request, one or more fields are empty")
	for all_ssids in allUsers:
		for user in allUsers[all_ssids]:
			if user['name'] == name:
				return("{latitude: " + str(user['latitude']) + ", longitude: " + str(user['longitude']) + "}")
	return("Name not found")

@app.route("/updateSSID", methods=['GET'])
def updateSSID():
	name = request.args.get("name")
	SSID = request.args.get("SSID")
	if name == None or SSID == None:
		return ("Error with request, one or more fields are empty")
	for all_ssids in allUsers:
		for user in allUsers[all_ssids]:
			if user['name'] == name:
				user['SSID'] = SSID
				return("Successful request")
	return("Name not found")

@app.route("/updatePreference", methods=['GET'])
def updatePreferences():
	name = request.args.get("name")
	preference = request.args.get("preference")
	if name == None or preference == None:
		return ("Error with request, one or more fields are empty")
	for all_ssids in allUsers:
		for user in allUsers[all_ssids]:
			if user['name'] == name:
				user['preference'] = preference
				return("Successful request")
	return("Name not found")

@app.route("/updateSex", methods=['GET'])
def updateSex():
	name = request.args.get("name")
	sex = request.args.get("sex")
	if name == None or sex == None:
		return ("Error with request, one or more fields are empty")
	for all_ssids in allUsers:
		for user in allUsers[all_ssids]:
			if user['name'] == name:
				user['sex'] = sex
				return("Successful request")
	return("Name not found")


@app.route("/add_user", methods=['GET'])
def add_user():
	name = request.args.get("name")
	sex = request.args.get("sex")
	latitude = request.args.get("latitude")
	longitude = request.args.get("longitude")
	preference = request.args.get("preference")
	SSID = request.args.get("SSID")
	if name == None  or sex == None or latitude == None or longitude == None or preference == None or SSID == None:
		return ("Error with request, one ore more fields are empty")

	if SSID in allUsers:
		allUsers[SSID].append({"name": name, "sex": sex, "latitude": latitude, "longitude": longitude, "preference": preference})
	else:
		allUsers[SSID] = [{"name": name, "sex": sex, "latitude": latitude, "longitude": longitude, "preference": preference}]
	print(allUsers)
	return("Successful Request")

if __name__ == '__main__':
	app.run(use_reloader=True, port=5000, threaded=True)
