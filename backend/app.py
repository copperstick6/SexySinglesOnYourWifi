import os
from flask import Flask, send_from_directory, request

app = Flask(__name__)

allUsers = {}

@app.route("/getMatch", methods=['GET'])
def homeRoute():
	print(request.args.get("name"))
	return "Hello World"


@app.route("/add_user", methods=['POST'])
def add_user():
	name = request.form.get("name")
	print(name)
	return "Hello World #2"

if __name__ == '__main__':
	app.run(use_reloader=True, port=5000, threaded=True)
