import http.client
import base64
import json




def get_token():
  """Obtiene un token de acceso desde Inditex OAuth2 API y lo devuelve como un diccionario."""

  # Configuración de OAuth2
  HOST = "auth.inditex.com"
  PORT = 443
  TOKEN_ENDPOINT = "/openam/oauth2/itxid/itxidmp/sandbox/access_token"
  CLIENT_ID = "oauth-mkpsbox-oauthyabmuqumslafcelioysnbxpro"
  CLIENT_SECRET = "D8}2U0{6_e@s}w-."
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





# Prueba la función
if __name__ == "__main__":
  token_data = get_token()
  print("Token obtenido:", token_data)


  def get_products(query, access_token):
    """Realiza una petición GET a la API de Inditex para buscar productos."""
    HOST = "api-sandbox.inditex.com"
    PORT = 443
    SEARCH_ENDPOINT = "/searchpmpa-sandbox/products"
    """
    Realiza una petición GET a la API de Inditex para buscar productos.

    :param query: Término de búsqueda (ejemplo: "shirt")
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

      # Hacer la petición con el query
      endpoint = f"{SEARCH_ENDPOINT}?query={query}"
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



def getProducts_multiplePrompts(query):
    token = get_token().get("id_token")

    #Generación de una lista de "prompts" a partir de json


    listaPrompts = []

    #Generar un json de outfits

    for i in listaPrompts:
      productos = get_products(i, token)
      if productos:
        with open("productos.json", "r+") as file:
          data = json.load(file)
          data[i] = productos
          file.seek(0)
          json.dump(data, file, indent=4)





