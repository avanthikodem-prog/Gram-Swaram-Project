import os
from flask import Flask, jsonify
from flask_cors import CORS

app = Flask(__name__)
CORS(app)

# Do not use localhost in cloud environments; set this in Render env vars.
JAVA_ORDER_API = os.environ.get("JAVA_ORDER_API", "https://example.com/api/orders")


@app.route("/", methods=["GET"])
def root():
    return "Gram Swaram AI is Live", 200


@app.route("/health", methods=["GET"])
def health():
    return jsonify(
        {
            "status": "ok",
            "service": "GramSwaram",
            "javaOrderApi": JAVA_ORDER_API,
        }
    )


if __name__ == "__main__":
    port = int(os.environ.get("PORT", 10000))
    app.run(host="0.0.0.0", port=port)