from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time
import requests
import cv2
import numpy as np
from io import BytesIO
from PIL import Image


# Configuración de Selenium
def configure_drivers():
    # Configurar Selenium con ChromeDriver
    chrome_options = Options()
    chrome_options.add_argument("--no-sandbox")  # Evitar errores de permisos
    chrome_options.add_argument("--disable-dev-shm-usage")  # Evitar errores de memoria
    chrome_options.add_argument("--disable-gpu")  # Deshabilitar la aceleración de hardware

    # Ruta de ChromeDriver
    service = Service('C:/Users/juanf/Downloads/chromedriver-win64/chromedriver.exe')
    driver = webdriver.Chrome(service=service, options=chrome_options)
    return driver


# Función para hacer scroll en la página
def do_scroll(driver, scroll_step=1750, max_scroll=None, container=None):
    total_scroll = 0
    # Dependiendo de si se pasa un contenedor o no, se hace scroll en la ventana o en el contenedor
    while total_scroll < max_scroll:
        if container:
            driver.execute_script("arguments[0].scrollTop = arguments[0].scrollTop + arguments[1];",
                                  container, scroll_step)
        else:
            driver.execute_script(f"window.scrollBy(0, {scroll_step});")  # Hacemos scroll de los pixeles indicados
        total_scroll += scroll_step  # Actualizamos el total de scroll
        time.sleep(0.2)  # Pausa para permitir la carga de imágenes

        # Actualizar el máximo de scroll por si la página sigue cargando contenido
        if container:
            new_max_scroll = driver.execute_script("return arguments[0].scrollHeight", container)
        else:
            new_max_scroll = driver.execute_script("return document.body.scrollHeight")
        if new_max_scroll > max_scroll:
            max_scroll = new_max_scroll
    return max_scroll


# Función para obtener la imagen de un producto de Inditex
def store_format(driver, store):
    img_element = None

    # Dependiendo de la tienda, se obtiene la imagen de una forma u otra (debido a la estructura de la página)
    if store == "zara":
        max_scroll = driver.execute_script("return document.body.scrollHeight")
        do_scroll(driver, 1750, max_scroll)
        # Esperar hasta que la imagen con alt="Image 5" o "Imagen 4" esté presente en el DOM, esta es la imagen limpia del producto
        wait = WebDriverWait(driver, 10)
        img_element = wait.until(EC.presence_of_element_located(
            (By.XPATH, '//img[contains(@alt, "Image 4") or contains(@alt, "Imagen 4")]')
        ))

    elif store == "massimodutti":
        wait = WebDriverWait(driver, 10)
        container = wait.until(EC.presence_of_element_located(
            (By.CLASS_NAME, 'cc-imagen-collection')
        ))
        max_scroll = driver.execute_script("return arguments[0].scrollHeight", container)
        do_scroll(driver, 1750, max_scroll, container)

        wait = WebDriverWait(driver, 10)
        img_element = wait.until(EC.presence_of_element_located(
            (By.XPATH, '//img[@imageindex="4"]')
        ))

    elif store == "lefties":
        wait = WebDriverWait(driver, 10)
        container = wait.until(EC.presence_of_element_located(
            (By.CLASS_NAME, 'lft-product-images')
        ))
        max_scroll = driver.execute_script("return arguments[0].scrollHeight", container)
        do_scroll(driver, 1750, max_scroll, container)

        wait = WebDriverWait(driver, 10)
        img_element = wait.until(EC.presence_of_element_located(
            (By.XPATH, '//img[@loading="lazy"]')
        ))

    elif store == "pullandbear":
        max_scroll = driver.execute_script("return document.body.scrollHeight")
        do_scroll(driver, 1750, max_scroll)

        wait = WebDriverWait(driver, 10)
        img_element = wait.until(EC.presence_of_element_located(
            (By.XPATH, '//img[contains(@alt, "4")]')
        ))


    elif store == "bershka":
        max_scroll = driver.execute_script("return document.body.scrollHeight")
        do_scroll(driver, 1750, max_scroll)

        wait = WebDriverWait(driver, 10)
        img_element = wait.until(EC.presence_of_element_located(
            (By.XPATH, '//img[contains(@alt, "4")]')
        ))

    elif store == "stradivarius":
        time.sleep(0.5)  # Stradiarius carga las imágenes de forma más lenta
        max_scroll = driver.execute_script("return document.body.scrollHeight")
        do_scroll(driver, 600, max_scroll)
        # Esperar hasta que la imagen con alt="Image 5" o "Imagen 4" esté presente en el DOM, esta es la imagen limpia del producto
        wait = WebDriverWait(driver, 10)
        img_element = wait.until(EC.presence_of_element_located(
            (By.XPATH, '//img[@data-cy="horizontal-image-6"]')
        ))


    return img_element  # Devolver el elemento de la imagen


# Función principal que devuelve la URL de la imagen de un producto de Zara
def get_image_url(url_producto, tienda):
    try:
        # Configurar Selenium
        driver = configure_drivers()
        # Abrir la página del producto
        driver.get(url_producto)

        # Llamamos a la función para obtener la imagen, la metodologia consistirá en hacer scroll en la página para cargar todas las imágenes, ya que estas se cargan de forma dinámica
        img_element = store_format(driver, tienda)

        # Obtener la URL de la imagen
        img_url = img_element.get_attribute('src')
        return img_url

    except Exception as e:
        print("Ocurrió un error:", e)
        return None
    finally:
        driver.quit()


def is_model_image(img_url, white_threshold=240, percentage_threshold=50, top_fraction=0.3):
    try:
        # Descargar la imagen desde la URL
        response = requests.get(img_url)
        response.raise_for_status()
        img_array = np.array(Image.open(BytesIO(response.content)))

        # Convertir a escala de grises
        gray_img = cv2.cvtColor(img_array, cv2.COLOR_RGB2GRAY)

        # Determinar la región superior a analizar
        height = gray_img.shape[0]
        top_height = int(height * top_fraction)
        top_region = gray_img[:top_height, :]

        # Contar píxeles "blancos" (o muy claros)
        white_pixels = np.sum(top_region > white_threshold)
        total_pixels = top_region.size
        white_percentage = (white_pixels / total_pixels) * 100

        print(f"Porcentaje de píxeles blancos en la parte superior: {white_percentage:.2f}%")

        return white_percentage > percentage_threshold

    except Exception as e:
        print(f"Error al procesar la imagen: {e}")
        return None

# Ejemplo de uso
url_producto = "https://www.lefties.com/es/mujer/novedades/jeans-culotte-el%C3%A1stico-c1030267503p659622175.html?colorId=428&parentId=659626182#fromrecommendation"
img_url= get_image_url(url_producto, 'lefties')
is_model = is_model_image(img_url)
print(f"URL de la imagen: {get_image_url(url_producto, 'lefties')}")
