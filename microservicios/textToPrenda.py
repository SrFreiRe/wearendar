import http.client
import base64
import json
import time
import os
from flask import Flask, request, jsonify
from flasgger import Swagger

TOKEN_FILE = "token.json"

def get_token():
    """Obtiene un token de acceso desde Inditex OAuth2 API y lo almacena en un archivo local con un timestamp."""

    # Si el archivo de token existe, verificamos si el token sigue siendo válido
    if os.path.exists(TOKEN_FILE):
        with open(TOKEN_FILE, "r") as file:
            data = json.load(file)
            timestamp = data.get("timestamp", 0)
            current_time = time.time()

            # Si el token tiene menos de 58 minutos, reutilizarlo
            if current_time - timestamp < 3480:  # 58 minutos en segundos
                return data.get("id_token")

    # Configuración de OAuth2
    HOST = "auth.inditex.com"
    PORT = 443
    TOKEN_ENDPOINT = "/openam/oauth2/itxid/itxidmp/access_token"
    CLIENT_ID = "oauth-mkplace-oauthbgpcpiyyxjbigjylsbpropro"
    CLIENT_SECRET = "EiV8_qt5a8-93._p"
    SCOPE = "technology.catalog.read"

    try:
        # Codificar credenciales en Base64 para Basic Auth
        credentials = f"{CLIENT_ID}:{CLIENT_SECRET}"
        encoded_credentials = base64.b64encode(credentials.encode()).decode()

        # Configurar la conexión HTTPS
        conn = http.client.HTTPSConnection(HOST, PORT)

        # Crear payload
        payload = f"grant_type=client_credentials&scope={SCOPE}"

        # Configurar headers
        headers = {
            "Content-Type": "application/x-www-form-urlencoded",
            "Authorization": f"Basic {encoded_credentials}"
        }

        # Hacer la petición
        conn.request("POST", TOKEN_ENDPOINT, payload, headers)
        res = conn.getresponse()
        data = res.read().decode("utf-8")

        # Manejar respuestas
        if res.status == 200:
            token_data = json.loads(data)
            token_data["timestamp"] = time.time()

            # Guardar el token en un archivo
            with open(TOKEN_FILE, "w") as file:
                json.dump(token_data, file)

            return token_data.get("id_token")
        else:
            print("Error al obtener el token.")
            return None
    except Exception as e:
        print(f"Error en get_token(): {e}")
        return None


import urllib.parse

def get_products(query, access_token):
    conn = http.client.HTTPSConnection("api.inditex.com")
    headers = {
        'Authorization': f'Bearer {access_token}',
        'Content-Type': 'application/json'
    }

    encoded_query = urllib.parse.quote(query)  # Codifica correctamente los caracteres especiales en la URL
    endpoint = f"/searchpmpa/products?query={encoded_query}"

    conn.request("GET", endpoint, headers=headers)
    res = conn.getresponse()
    data = res.read()

    return json.loads(data.decode("utf-8"))



app = Flask(__name__)
swagger = Swagger(app)


@app.route('/generate_outfit', methods=['POST'])
def getProducts_multiplePrompts():
    """
  Genera una lista de productos basada en los outfits generados.
  ---
  tags:
    - Outfits
  parameters:
    - name: clothes
      in: body
      required: true
      schema:
        type: object
        properties:
          outfit:
            type: array
            items:
              type: string
              example: "Camisa de botones blanca"
  responses:
    200:
      description: JSON con los productos recomendados para cada prenda
      content:
        application/json:
          schema:
            type: object
            additionalProperties:
              type: array
              items:
                type: object
                properties:
                  name:
                    type: string
                    example: "Camisa de lino blanca"
                  price:
                    type: number
                    example: 39.99
                  link:
                    type: string
                    example: "https://ejemplo.com/camisa-blanca"
    400:
      description: Error en los datos enviados
    500:
      description: Error interno del servidor
  """
    token = get_token()
    if not token:
        return jsonify({"error": "No se pudo obtener el token"}), 500

    clothes = request.get_json()
    listaPrompts = clothes.get("outfit", [])
    data = {}

    for i in listaPrompts:
        print(i)
        productos = get_products(i, token)
        if productos:
            if i in data:
                data[i] = list(set(data[i] + productos))
            else:
                data[i] = productos

    return jsonify(data)


if __name__ == '__main__':
    app.run(debug=True, port=5002)
