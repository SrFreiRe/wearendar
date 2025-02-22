from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time
import requests

# Función para descargar la imagen de un producto de Zara, dada la URL del producto para complementar la API ofrecidoa por Zara
def descargar_imagen_zara(url_producto):
    try:

        # Configurar Selenium con ChromeDriver
        chrome_options = Options()
        chrome_options.add_argument("--no-sandbox")  # Evitar errores de permisos
        chrome_options.add_argument("--disable-dev-shm-usage")  # Evitar errores de memoria
        chrome_options.add_argument("--disable-gpu")  # Deshabilitar la aceleración de hardware

        # Ruta de ChromeDriver
        service = Service('C:/Users/juanf/Downloads/chromedriver-win64/chromedriver.exe')
        driver = webdriver.Chrome(service=service, options=chrome_options)

        # Abrir la página del producto
        driver.get(url_producto)

        # Hacer scroll lentamente en pasos más pequeños para cargar todas las imágenes del js
        scroll_step = 1750  # Pixels por scroll
        total_scroll = 0
        max_scroll = driver.execute_script("return document.body.scrollHeight")

        # Recorrer la página hasta el final
        while total_scroll < max_scroll:
            driver.execute_script(f"window.scrollBy(0, {scroll_step});") # Hacemos scroll de los pixeles indicados
            total_scroll += scroll_step
            time.sleep(0.2)  # Pausa para permitir la carga de imágenes

            # Actualizar el máximo de scroll por si la página sigue cargando contenido
            new_max_scroll = driver.execute_script("return document.body.scrollHeight")
            if new_max_scroll > max_scroll:
                max_scroll = new_max_scroll

        # Esperar hasta que la imagen con alt="Image 5" o "Imagen 4" esté presente en el DOM, esta es la imagen limpia del producto
        wait = WebDriverWait(driver, 10)
        img_element = wait.until(EC.presence_of_element_located(
            (By.XPATH, '//img[contains(@alt, "Image 5") or contains(@alt, "Imagen 4")]')
        ))

        # Obtener la URL de la imagen
        img_url = img_element.get_attribute('src')
        print("URL de la imagen encontrada:", img_url)


    except Exception as e:
        print("Ocurrió un error:", e)
        return None
    finally:
        driver.quit()

# Ejemplo de uso
url_producto = "https://www.zara.com/es/es/sudadera-lavada-textos-p06224419.html?v1=419865302&v2=2436823"
descargar_imagen_zara(url_producto)
