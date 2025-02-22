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






# Configuración de la API
HOST = "api.inditex.com"
PORT = 443
SEARCH_ENDPOINT = "/searchpmpa/products"

def get_products(query, access_token):
    """
    Realiza una petición GET a la API de Inditex para buscar productos.

    :param query: Término de búsqueda (ejemplo: "wool sweater")
    :param access_token: Token de acceso OAuth2 válido
    :return: Diccionario con los resultados de la API o None si hay error.
    """
    try:
        # Configurar la conexión HTTPS
        conn = http.client.HTTPSConnection(HOST, PORT)

        # Configurar headers con el token de acceso
        headers = {
            "Authorization": f"Bearer {access_token}",
            "Content-Type": "application/json"
        }

        # Codificar el query correctamente (espacios y caracteres especiales)
        encoded_query = query.replace(" ", "%20")

        # Hacer la petición con el query
        endpoint = f"{SEARCH_ENDPOINT}?query={encoded_query}"
        conn.request("GET", endpoint, "", headers)

        # Obtener la respuesta
        res = conn.getresponse()
        data = res.read().decode("utf-8")

        # Imprimir detalles para depuración
        print(f"HTTP Status: {res.status}")
        print(f"Response: {data}")

        # Convertir la respuesta a diccionario
        if res.status == 200:
            return json.loads(data)
        else:
            print("Error en la solicitud de productos.")
            return None

    except Exception as e:
        print(f"Error en get_products(): {e}")
        return None

# Prueba la función
if __name__ == "__main__":
    test_token = "TU_ACCESS_TOKEN_AQUI"  # Reemplázalo con un token válido
    productos = get_products("wool sweater", test_token)
    print("Productos encontrados:", productos)


#main
if __name__ == "__main__":
  token_data = get_token()
  print("Token obtenido:", token_data.get("access_token"))
  products = get_products("shirt", token_data.get("access_token"))
  print("Productos encontrados:", products)

