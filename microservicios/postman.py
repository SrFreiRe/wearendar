import http.client
import base64
import json

# Configuración de OAuth2
HOST = "auth.inditex.com"
PORT = 443
TOKEN_ENDPOINT = "/openam/oauth2/itxid/itxidmp/sandbox/access_token"
CLIENT_ID = "oauth-mkpsbox-oauthyabmuqumslafcelioysnbxpro"
CLIENT_SECRET = "D8}2U0{6_e@s}w-."
SCOPE = "technology.catalog.read"


def get_token():
  """Obtiene un token de acceso desde Inditex OAuth2 API y lo devuelve como un diccionario."""
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
  print("Token obtenido:", token_data.get("access_token"))

