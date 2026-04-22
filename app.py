from flask import Flask, jsonify, request
from flask_cors import CORS
import speech_recognition as sr
import requests
import pyttsx3

app = Flask(__name__)
CORS(app)

JAVA_ORDER_API = "http://localhost:8081/api/orders"


def speak_telugu(text):
    try:
        engine = pyttsx3.init()
        engine.setProperty("rate", 145)
        engine.say(text)
        engine.runAndWait()
    except Exception:
        print("TTS fallback:", text)


def resolve_address_from_gps(latitude, longitude):
    url = "https://nominatim.openstreetmap.org/reverse"
    params = {
        "format": "jsonv2",
        "lat": latitude,
        "lon": longitude
    }
    headers = {
        "User-Agent": "GramSwaramAI/1.0"
    }
    response = requests.get(url, params=params, headers=headers, timeout=8)
    response.raise_for_status()
    data = response.json()
    return data.get("display_name", "తెలియని ప్రాంతం")


def confirm_gps_address_by_voice(latitude, longitude):
    address = resolve_address_from_gps(latitude, longitude)
    message = f"మీ ప్రస్తుత చిరునామా {address}. ఇదే కదా?"
    speak_telugu(message)
    return address, message


def create_order_from_voice_text(spoken_text, address):
    payload = {
        "itemNames": spoken_text,
        "address": address,
        "paymentMethod": "COD",
        "totalPrice": 0.0
    }
    response = requests.post(JAVA_ORDER_API, json=payload, timeout=10)
    response.raise_for_status()
    return response.json()

@app.route('/speak', methods=['POST'])
def speak_text():
    data = request.get_json(silent=True) or {}
    text = data.get("text", "")
    if not text:
        return jsonify({"status": "error", "message": "text is required"}), 400
    speak_telugu(text)
    return jsonify({"status": "success", "spokenText": text})

@app.route('/greet', methods=['GET'])
def greet():
    welcome_msg = "నమస్కారం! గ్రామ స్వరం కి స్వాగతం. మీకు ఏ వస్తువు కావాలి?"
    return jsonify({"message": welcome_msg, "status": "success"})


@app.route('/confirm-gps-address', methods=['GET'])
def confirm_gps_address():
    latitude = request.args.get("lat", type=float)
    longitude = request.args.get("lng", type=float)
    if latitude is None or longitude is None:
        return jsonify({"status": "error", "message": "lat and lng are required"}), 400
    try:
        address, message = confirm_gps_address_by_voice(latitude, longitude)
        return jsonify({"status": "success", "address": address, "message": message})
    except requests.RequestException as err:
        return jsonify({"status": "error", "message": str(err)}), 502


@app.route('/listen-telugu', methods=['GET'])
def listen_telugu():
    recognizer = sr.Recognizer()
    with sr.Microphone() as source:
        print("వింటున్నాను...")
        recognizer.adjust_for_ambient_noise(source, duration=1.0)
        try:
            print("మాట్లాడండి...")
            audio = recognizer.listen(source, timeout=10, phrase_time_limit=10)
            text = recognizer.recognize_google(audio, language='te-IN')
            print(f"User said: {text}")

            latitude = request.args.get("lat", type=float)
            longitude = request.args.get("lng", type=float)
            order_address = "Voice order - address pending"
            gps_message = None

            if latitude is not None and longitude is not None:
                try:
                    order_address, gps_message = confirm_gps_address_by_voice(latitude, longitude)
                except requests.RequestException:
                    gps_message = "GPS చిరునామా కనుగొనలేకపోయాం. డిఫాల్ట్ చిరునామా వాడుతున్నాం."

            try:
                order_response = create_order_from_voice_text(text, order_address)
                return jsonify({
                    "status": "success",
                    "text": text,
                    "gpsMessage": gps_message,
                    "addressUsed": order_address,
                    "orderCreated": True,
                    "order": order_response
                })
            except requests.RequestException as req_err:
                return jsonify({
                    "status": "partial_success",
                    "text": text,
                    "gpsMessage": gps_message,
                    "addressUsed": order_address,
                    "orderCreated": False,
                    "message": f"Speech captured, but order API call failed: {req_err}"
                }), 502
        except sr.WaitTimeoutError:
            return jsonify({"status": "timeout", "message": "సమయం మించిపోయింది, మళ్ళీ చెప్పండి."})
        except sr.UnknownValueError:
            return jsonify({"status": "unclear", "message": "క్షమించండి, అర్థం కాలేదు."})
        except Exception as e:
            return jsonify({"status": "error", "message": str(e)})

@app.route('/listen-telugu-text', methods=['GET'])
def listen_telugu_text():
    recognizer = sr.Recognizer()
    with sr.Microphone() as source:
        print("వింటున్నాను...")
        recognizer.adjust_for_ambient_noise(source, duration=1.0)
        try:
            print("మాట్లాడండి...")
            audio = recognizer.listen(source, timeout=10, phrase_time_limit=10)
            text = recognizer.recognize_google(audio, language='te-IN')
            print(f"User said: {text}")
            return jsonify({"status": "success", "text": text})
        except sr.WaitTimeoutError:
            return jsonify({"status": "timeout", "message": "సమయం మించిపోయింది, మళ్ళీ చెప్పండి."})
        except sr.UnknownValueError:
            return jsonify({"status": "unclear", "message": "క్షమించండి, అర్థం కాలేదు."})
        except Exception as e:
            return jsonify({"status": "error", "message": str(e)})

if __name__ == '__main__':
    app.run(port=5000, debug=True)