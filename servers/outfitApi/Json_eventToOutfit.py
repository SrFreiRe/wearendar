import datetime
import os
from flask import Flask, request, jsonify
from dotenv import load_dotenv
from openai import OpenAI
from flasgger import Swagger
from flask_httpauth import HTTPTokenAuth
import json
import re

# Cargar variables de entorno
load_dotenv()
OPENAI_API_KEY = os.getenv('OPENAI_API_KEY')
API_TOKEN = os.getenv('API_TOKEN')

client = OpenAI(api_key=OPENAI_API_KEY)

# Configuración de Flask
app = Flask(__name__)
swagger = Swagger(app)
auth = HTTPTokenAuth(scheme='Bearer')


@auth.verify_token
def verify_token(token):
    return token == API_TOKEN


@app.route('/generate_outfit', methods=['POST'])
# @auth.login_required
def generate_outfit():
    """
    Genera una lista de prendas de ropa adecuadas para el evento proporcionado.
    ---
    parameters:
      - name: event_data
        in: body
        required: true
        schema:
          type: array
          items:
            type: object
            properties:
              description:
                type: string
                example: ""
              end:
                type: string
                example: "2025-02-23"
              location:
                type: string
                example: "No especificada"
              start:
                type: string
                example: "2025-02-22"
              summary:
                type: string
                example: "Reunión con el jefe de producción"
    responses:
      200:
        description: Lista de prendas generada correctamente
        content:
          application/json:
            schema:
              type: object
              properties:
                outfit:
                  type: array
                  items:
                    type: string
      400:
        description: Error en los datos enviados
      500:
        description: Error interno del servidor
    """
    event_data = request.get_json()
    if not event_data:
        return jsonify({'error': 'No se proporcionó información del evento.'}), 400

    event_summary = event_data.get("summary", "General event")
    event_location = event_data.get("location", "Unknown location")
    event_date = event_data.get("start", "Unknown date")
    event_description = event_data.get("description", "")

    prompt = f'''
        Based on the following event, describe an ideal outfit with enough detail to help a fashion search engine find similar products.

        **Event Details:**
        - **Event Name**: {event_summary}
        - **Location**: {event_location}
        - **Date**: {event_date}
        - **Description**: {event_description}

        **Guidelines for the description:**
        1. **Type of clothing**: Clearly state if it's a shirt, pants, jacket, dress, etc.
        2. **Material**: Mention if it's made of cotton, linen, leather, wool, etc.
        3. **Color and shades**: Be specific (e.g., navy blue instead of just blue).
        4. **Style**: Indicate if it's formal, casual, sporty, elegant, streetwear, etc.
        5. **Details**: Include relevant features (e.g., buttons, zippers, pockets, patterns).
        6. **Weather suitability**: Mention if it's ideal for summer, winter, rainy days, etc.
        7. **Avoid mentioning brands.**
        8. **Write the entire description in English.**

        Example output:
        "A navy blue slim-fit cotton shirt with a button-up design, long sleeves, and a standard collar. Perfect for business casual occasions or formal meetings."

        Now, based on the event details, describe a full outfit in English.
        '''

    try:
        response = client.chat.completions.create(
            model="gpt-4o-mini",
            messages=[
                {"role": "system", "content": "Eres un asistente experto en moda y estilismo."},
                {"role": "user", "content": prompt}
            ]
        )

        outfit_json = response.choices[0].message.content

        ### Limpieza del json
        # 🔹 1️Eliminar los bloques ```json ... ```
        clean_json_str = re.sub(r"^```json|```$", "", outfit_json.strip(), flags=re.MULTILINE).strip()

        # 🔹 2️ Eliminar caracteres de escape innecesarios (\n y \")
        clean_json_str = clean_json_str.replace("\n", "").replace("\\n", "").replace('\\"', '"')

        # 🔹 3️ Convertir el string limpio en JSON real
        try:
            outfit_list = json.loads(clean_json_str)
        except json.JSONDecodeError as e:
            return jsonify({'error': f'JSON inválido generado por OpenAI: {str(e)}', 'raw_output': outfit_json}), 500

        return jsonify({'outfit': outfit_list})
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True, port=5002)
