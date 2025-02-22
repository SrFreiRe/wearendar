import http.client
import json


def search_products(token: str, query: str):
    conn = http.client.HTTPSConnection("api.inditex.com")
    headers = {
        'Authorization': f'Bearer {token}',
        'Content-Type': 'application/json'
    }

    encoded_query = query.replace(' ', '%20')
    endpoint = f"/searchpmpa/products?query={encoded_query}"

    conn.request("GET", endpoint, headers=headers)
    res = conn.getresponse()
    data = res.read()

    return json.loads(data.decode("utf-8"))

# Ejemplo de uso
# token = "tu_token_aqui"
# query = "wool sweater"
# resultado = search_products(token, query)
# print(resultado)

# Ejemplo de uso
if __name__ == "__main__":
    token = "eyJ0eXAiOiJKV1QiLCJraWQiOiJZMjZSVjltUFc3dkc0bWF4NU80bDBBd2NpSVE9IiwiYWxnIjoiUlMyNTYifQ.eyJhdF9oYXNoIjoieExzcWl0dlYxaFprdlVFLUE3Y3BFUSIsInN1YiI6Im9hdXRoLW1rcGxhY2Utb2F1dGhiZ3BjcGl5eXhqYmlnanlsc2Jwcm9wcm8iLCJhdWRpdFRyYWNraW5nSWQiOiJiYzE4MjA4Mi01OWZjLTQ5NjItYjllNC0xMjIwODg4NjEwZTEtMTIwOTQ5MjIwIiwiY3VzdG9tIjp7ImNvbnN1bWVyT3JnSWQiOiJtYXRlb2JvZGVubGxlX2dtYWlsLmNvbSIsIm1hcmtldHBsYWNlQ29kZSI6Im9wZW4tZGV2ZWxvcGVyLXBvcnRhbCIsIm1hcmtldHBsYWNlQXBwSWQiOiI5MjVhZmRlMy02NzE3LTQ0NWItOGFhZS00ZDM2MGZkNDk1ZTMifSwiaXNzIjoiaHR0cHM6Ly9hdXRoLmluZGl0ZXguY29tOjQ0My9vcGVuYW0vb2F1dGgyL2l0eGlkL2l0eGlkbXAiLCJ0b2tlbk5hbWUiOiJpZF90b2tlbiIsInVzZXJJZCI6Im9hdXRoLW1rcGxhY2Utb2F1dGhiZ3BjcGl5eXhqYmlnanlsc2Jwcm9wcm8iLCJhdWQiOiJvYXV0aC1ta3BsYWNlLW9hdXRoYmdwY3BpeXl4amJpZ2p5bHNicHJvcHJvIiwiaWRlbnRpdHlUeXBlIjoic2VydmljZSIsImF6cCI6Im9hdXRoLW1rcGxhY2Utb2F1dGhiZ3BjcGl5eXhqYmlnanlsc2Jwcm9wcm8iLCJhdXRoX3RpbWUiOjE3NDAyMDU2MjIsInNjb3BlIjoibWFya2V0IHRlY2hub2xvZ3kuY2F0YWxvZy5yZWFkIG9wZW5pZCIsInJlYWxtIjoiL2l0eGlkL2l0eGlkbXAiLCJ1c2VyVHlwZSI6ImV4dGVybmFsIiwiZXhwIjoxNzQwMjA5MjIyLCJ0b2tlblR5cGUiOiJKV1RUb2tlbiIsImlhdCI6MTc0MDIwNTYyMiwiYXV0aExldmVsIjoiMSJ9.C0RHgnrqa_Qnu-haMNdI8J3IKxKfQPLAElHn8wETGlSHIycTxM0qufjxN5TlcnbQr1B0brMQandnH-II4Cw1VMSSw9fqItrLgdnQxh9xqmwjIK3JNuSMlfe3dt8kbJ1sC2sns4f0ssgtx5AK1G03Oj0LAZQyXX3_MLSehX-AI0wKq-RH8Gv9F_gBsjO9cfM28bo3j8QGivQcB6pUXbBM5jCD98jlbil0B2dM2HCZnrx4m96-GYzb-N1w_2xBe2E0o5IxutlHFEbX3-hlV5wBDPem-mFJmAFDWvWY7NfT5Lik-EBQhloInmfu-iWgk_o9K5ne22rPFvUTQFc6jH0LIA"
    query = "wool sweater"
    resultado = search_products(token, query)
    print(resultado)