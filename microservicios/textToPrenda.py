import http.client
import base64
import json
from flask import Flask, request, jsonify
from flasgger import Swagger

app = Flask(__name__)
swagger = Swagger(app)

def get_token():
  """Obtiene un token de acceso desde Inditex OAuth2 API y lo devuelve como un diccionario."""

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

    # Imprimir detalles para depuración
    print(f"HTTP Status: {res.status}")
    print(f"Response: {data}")

    # Manejar respuestas
    if res.status == 200:
      return json.loads(data)  # Convertir JSON string a diccionario
    else:
      print("Error al obtener el token.")
      return None

  except Exception as e:
    print(f"Error en get_token(): {e}")
    return None






def get_products(query, access_token):
  conn = http.client.HTTPSConnection("api.inditex.com")
  headers = {
      'Authorization': f'Bearer {access_token}',
      'Content-Type': 'application/json'
  }

  encoded_query = query.replace(' ', '%20')
  endpoint = f"/searchpmpa/products?query={encoded_query}"

  conn.request("GET", endpoint, headers=headers)
  res = conn.getresponse()
  data = res.read()

  return json.loads(data.decode("utf-8"))

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
    token = get_token().get("id_token")

    print(token)

    #Generación de una lista de "prompts" a partir de json
    clothes = request.get_json()
    listaPrompts = clothes.get("outfit", [])

    #Generar un json de outfits

    #Declaramos un json vacío
    data = {}

    for i in listaPrompts:
      productos = get_products(i, token)
      #Añadimos productos a un Yeison, con la i como key (para agruparlos)
      if productos:
        # Si la clave ya existe, agregamos los productos sin duplicar
        if i in data:
          data[i] = list(set(data[i] + productos))  # Añadir y evitar duplicados
        else:
          data[i] = productos  # Primera vez que se añade la clave
    return data

if __name__ == '__main__':
  app.run(debug=True, port=5002)

