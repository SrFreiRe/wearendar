import datetime
import os
import requests  # 🔹 Importamos requests para hacer la petición HTTP
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

TEXT_TO_CLOTHES_URL = "http://127.0.0.1:5002/generate_outfit"  # 🔹 URL del servidor de la API de inditex

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

    # Es una lista de 1 elemento, por lo que lo tomamos
    if isinstance(event_data, list):
        event_data = event_data[0]

    event_summary = event_data.get("summary", "General event")
    event_location = event_data.get("location", "Unknown location")
    event_date = event_data.get("start", "Unknown date")
    event_description = event_data.get("description", "")
    genero = event_data.get("gender", "indefinido")


    json_template = json.dumps(
        {
            "outfit": [
                {
                    "description": "Camisa blanca algodón, cuello clásico.",
                    "category": "Formal"
                },
                {
                    "description": "Camisa azul lino, manga larga.",
                    "category": "Arreglado"
                },
                {
                    "description": "Pantalón chino beige, corte recto.",
                    "category": "Casual"
                },
                {
                    "description": "Pantalón negro vestir, bolsillos laterales.",
                    "category": "Formal"
                },
                {
                    "description": "Blazer gris lana, solapas estrechas.",
                    "category": "Formal"
                },
                {
                    "description": "Chaqueta negra cuero, cierre cremallera.",
                    "category": "Casual"
                },
                {
                    "description": "Abrigo largo lana, doble botonadura.",
                    "category": "Arreglado"
                },
                {
                    "description": "Gabardina beige, cinturón y solapas anchas.",
                    "category": "Arreglado"
                },
                {
                    "description": "Vestido rojo de lentejuelas, corte entallado y espalda descubierta.",
                    "category": "Festivo"
                },
                {
                    "description": "Jersey azul lana, cuello alto.",
                    "category": "Casual"
                },
                {
                    "description": "Sudadera gris algodón, capucha ajustable.",
                    "category": "Deportivo"
                },
                {
                    "description": "Zapatos Oxford marrón, suela de cuero.",
                    "category": "Formal"
                },
                {
                    "description": "Deportivas blancas, ajuste cómodo.",
                    "category": "Deportivo"
                },
                {
                    "description": "Bolso negro piel, asa metálica.",
                    "category": "Arreglado"
                },
                {
                    "description": "Bufanda gris lana, tacto suave.",
                    "category": "Casual"
                }
            ]
        }
    )

    prompt = f'''
        **Basado en el siguiente evento, describe un conjunto de prendas ideales con suficiente detalle para ayudar a un motor de búsqueda de moda a encontrar productos similares.**
        **La descripción debe ser clara y concisa, con como máximo 6 palabras**

        **Detalles del evento:**
        - **Nombre del evento**: {event_summary}
        - **Ubicación**: {event_location}
        - **Fecha**: {event_date}
        - **Descripción**: {event_description}

        **Instrucciones para la descripción:**
        1. **Indica claramente el tipo de prenda**: camisa, pantalón, chaqueta, vestido, etc.
        2. **Menciona el material**: algodón, lino, cuero, lana, etc.
        3. **Especifica el color y tonalidades**: Sé preciso (ej. azul marino en lugar de solo azul).
        4. **Indica el estilo**: Formal, casual, deportivo, elegante, streetwear, etc.
        5. **Incluye detalles relevantes**: Botones, cremalleras, bolsillos, patrones, tipo de ajuste (entallado, holgado, clásico).
        6. **Menciona la adecuación climática**: Ideal para verano, invierno, días lluviosos, etc.
        7. **No menciones marcas.**
        8. **Devuelve solo una lista JSON válida con las descripciones de las prendas, sin texto adicional.**
        9. **Clasifica cada prenda en una de estas categorías según su nivel de formalidad y propósito:**
               - **Formal**: Prendas para reuniones importantes, eventos de gala o contextos profesionales.
               - **Arreglado**: Prendas elegantes pero menos estrictas que lo formal, ideales para citas, cenas o trabajo informal.
               - **Festivo**: Prendas llamativas o especiales para celebraciones y eventos sociales.
               - **Casual**: Ropa relajada para el día a día o situaciones informales.
               - **Deportivo**: Prendas diseñadas para actividad física o looks sport.
        9. **No uses tildes ni la letra 'ñ', usa 'n' en su lugar y evita caracteres especiales.**
        10. **Pon como máximo 4 prendas para el outfit de cada evento.**

        **Ejemplo de salida:**
        {json_template}
        '''

    try:
        response = client.chat.completions.create(
            model="gpt-4o-mini",
            messages=[
                {"role": "system", "content":  "You are an expert in fasion and esthetics"},
                {"role": "user", "content": prompt}
            ]
        )

        outfit_json = response.choices[0].message.content

        ### Limpieza del json
        clean_json_str = re.sub(r"^```json|```$", "", outfit_json.strip(), flags=re.MULTILINE).strip()
        clean_json_str = clean_json_str.replace("\n", "").replace("\\n", "").replace('\\"', '"')

        # 🔹 Convertir el string limpio en JSON real
        try:
            outfit_list = json.loads(clean_json_str)
        except json.JSONDecodeError as e:
            return jsonify({'error': f'JSON inválido generado por OpenAI: {str(e)}', 'raw_output': outfit_json}), 500

        # 🔹 Enviar el resultado al servidor de Inditex para obtener productos
        response = requests.post(TEXT_TO_CLOTHES_URL, json=outfit_list)
        # Para conexión con "textToPrenda"
        if response.status_code == 200:
            return response.json()  # Devuelve la respuesta de Inditex
        else:
            return jsonify({'error': 'Error en la consulta a Inditex', 'details': response.text}), 500

        # Para pruebas en local comentar el if de justo arriba
        #return jsonify(outfit_list)
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True, port=5001)
