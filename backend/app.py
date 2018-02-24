import os
from flask import Flask, send_from_directory, request

app = Flask(__name__)

allUsers = {}

@app.route("/getMatch", methods=['GET'])
def homeRoute():
	print(request.args.get("SSID"))

	return "Hello World"


@app.route("/add_user", methods=['POST'])
def add_user():
	name = request.form.get("name")
	sex = request.form.get("sex")
	location = request.form.get("location")
	preference = request.form.get("preference")
	SSID = request.form.get("SSID")
	if name == None  or sex == None or location == None or preference == None or SSID == None:
		return ("Error with request, one field is empty")

	if SSID in allUsers:
		allUsers["SSID"]
	return "Hello World #2"

if __name__ == '__main__':
	app.run(use_reloader=True, port=5000, threaded=True)
