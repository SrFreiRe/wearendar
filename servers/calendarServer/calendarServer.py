from __future__ import print_function
import os
import datetime as dt
import json
from flask import Flask, jsonify, request
from google.oauth2.credentials import Credentials
from google_auth_oauthlib.flow import InstalledAppFlow
from google.auth.transport.requests import Request
from googleapiclient.discovery import build
from googleapiclient.errors import HttpError

SCOPES = ["https://www.googleapis.com/auth/calendar"]

class GoogleCalendarManager:
    def __init__(self):
        self.service = self._authenticate()

    def _authenticate(self):
        creds = None
        if os.path.exists("token.json"):
            creds = Credentials.from_authorized_user_file("token.json", SCOPES)

        if not creds or not creds.valid:
            if creds and creds.expired and creds.refresh_token:
                creds.refresh(Request())
            else:
                flow = InstalledAppFlow.from_client_secrets_file("client_secret_app_escritorio_oauth.json", SCOPES)
                creds = flow.run_local_server(port=0)

            with open("token.json", "w") as token:
                token.write(creds.to_json())

        try:
            return build("calendar", "v3", credentials=creds)
        except Exception as e:
            print(f"❌ Error en authenticate al inicializar Google Calendar API: {e}")
            return None

    def list_calendars(self):
        if not self.service:
            print("❌ Error in list_calendars: self.service es None. Falló la autenticación.")
            return []

        calendars_result = self.service.calendarList().list().execute()

        calendars = calendars_result.get('items', [])
        for calendar in calendars:
            print(f"📅 Nombre: {calendar['summary']} | ID: {calendar['id']}")

        # Filtrar calendarios que contengan 'holiday' en su ID
        filtered_calendars = [cal for cal in calendars if 'holiday' not in cal['id'].lower()]
        return filtered_calendars

    def list_upcoming_events(self, max_results=10):
        calendar_ids = self.list_calendars()
        if not calendar_ids:
            return []

        all_events = []
        now = dt.datetime.utcnow().isoformat() + "Z"
        for calendar in calendar_ids:
            events_result = self.service.events().list(
                calendarId=calendar['id'], timeMin=now,
                maxResults=max_results, singleEvents=True,
                orderBy="startTime"
            ).execute()
            events = events_result.get('items', [])
            for event in events:
                all_events.append({
                    "id": event.get("id", "No ID"),
                    "title": event.get("summary", "Sin título"),
                    "description": event.get("description", ""),
                    "startTime": event['start'].get('dateTime', event['start'].get('date')),
                    "endTime": event['end'].get('dateTime', event['end'].get('date')),
                    "location": event.get("location", "No especificada"),
                    "type": event.get("kind", "no type").split("#")[-1]
                })
        return all_events

app = Flask(__name__)
calendar_manager = GoogleCalendarManager()

@app.route('/events', methods=['GET'])
def get_events():
    events = calendar_manager.list_upcoming_events()
    return jsonify(events)

if __name__ == '__main__':
    app.run(debug=True, port=5000)
    CORS(app)
