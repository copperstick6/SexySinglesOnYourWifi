import os
from flask import Flask, send_from_directory

app = Flask(__name__)

@app.route("/")
def homeRoute():
	return "Hello World"


if __name__ == '__main__':
	app.run(use_reloader=True, port=5000, threaded=True)
