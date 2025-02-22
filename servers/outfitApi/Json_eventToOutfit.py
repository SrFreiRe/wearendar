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

    prompt = f'''
    Basado en el siguiente evento:
    {json.dumps(event_data, indent=4)}

    **Genera una lista de prendas de ropa adecuadas para este evento.**
    - Ten en cuenta el tipo de evento (por ejemplo, reuniones formales, citas, actividades deportivas, etc.).
    - Considera la ubicación si está disponible.
    - Si la fecha está en invierno, sugiere ropa más abrigada. Si está en verano, sugiere ropa más ligera.
    - La lista debe incluir al menos 3 prendas, como camiseta/camisa, pantalón/falda y zapatos adecuados.
    - Devuelve solo una lista JSON con los nombres de las prendas, sin texto adicional.
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
