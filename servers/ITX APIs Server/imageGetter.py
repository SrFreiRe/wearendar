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

        img_elements = driver.find_elements(By.XPATH, '//img[contains(@alt, "Image") or contains(@alt, "Imagen")]')
        img_urls = [img.get_attribute('src') for img in img_elements]
        print(f"Imágenes encontradas: {img_urls}")
        return img_urls

    elif store == "massimo_dutti":
        wait = WebDriverWait(driver, 10)
        container = wait.until(EC.presence_of_element_located(
            (By.CLASS_NAME, 'cc-imagen-collection')
        ))
        max_scroll = driver.execute_script("return arguments[0].scrollHeight", container)
        do_scroll(driver, 1750, max_scroll, container)

        num_range = range(1, 9)  # De 1 a 8
        xpath_query = f'//img[({" or ".join([f"@imageindex='{i}'" for i in num_range])})]'
        img_elements = driver.find_elements(By.XPATH, xpath_query)

        img_urls = [img.get_attribute('src') for img in img_elements]
        print(f"Imágenes encontradas: {img_urls}")
        return img_urls


    elif store == "lefties":
        wait = WebDriverWait(driver, 10)
        container = wait.until(EC.presence_of_element_located(
            (By.CLASS_NAME, 'lft-product-images')
        ))
        max_scroll = driver.execute_script("return arguments[0].scrollHeight", container)
        do_scroll(driver, 1750, max_scroll, container)

        img_elements = driver.find_elements(By.XPATH, '//img[@loading="lazy"]')
        img_urls = [img.get_attribute('src') for img in img_elements]
        print(f"Imágenes encontradas: {img_urls}")
        return img_urls

    elif store == "bershka":
        max_scroll = driver.execute_script("return document.body.scrollHeight")
        do_scroll(driver, 1750, max_scroll)

        img_elements = driver.find_elements(By.CLASS_NAME, 'image-item')

        # Filtra los elementos que tienen el atributo alt que contiene "5" o "6"
        filtered_elements = [img for img in img_elements if
                             '5' in img.get_attribute('alt') or '6' in img.get_attribute('alt')]
        img_urls = [img.get_attribute('src') for img in filtered_elements]
        print(f"Imágenes encontradas: {img_urls}")
        return img_urls


    elif store == "stradivarius":
        time.sleep(0.5)  # Stradiarius carga las imágenes de forma más lenta
        max_scroll = driver.execute_script("return document.body.scrollHeight")
        do_scroll(driver, 1000, max_scroll)

        img_elements = driver.find_elements(By.XPATH, '//img[contains(@data-cy, "horizontal-image")]')

        img_urls = [img.get_attribute('src') for img in img_elements]
        print(f"Imágenes encontradas: {img_urls}")
        return img_urls

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

        # Filtrar imágenes que no sean de modelos
        best_url = lowest_skin_percentage(img_element,tienda)
        print(f"Mejor URL encontrada: {best_url}")
        # Descargar la imagen desde la URL
        return best_url

    except Exception as e:
        print("Ocurrió un error:", e)
        return None

def lowest_skin_percentage(img_urls,tienda):
    result_url = None
    result_skin = 100
    for img_url in img_urls:
        skin_percentage = is_model_image(img_url,tienda)
        if skin_percentage + 2 < result_skin:
            result_skin = skin_percentage
            result_url = img_url
    return result_url

def is_model_image(img_url, tienda):
    try:
        headers = {
            "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
            "Referer": "https://www."+tienda+".com/",
            "Accept": "image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8"
        }
        # Descargar la imagen desde la URL
        response = requests.get(img_url, headers=headers)
        response.raise_for_status()
        img_array = np.array(Image.open(BytesIO(response.content)))

        # Convertir a espacio de color HSV
        hsv_img = cv2.cvtColor(img_array, cv2.COLOR_RGB2HSV)

        # Definir rango de colores de piel en HSV
        lower_skin = np.array([0, 40, 50], dtype=np.uint8)  # Rango bajo
        upper_skin = np.array([35, 255, 255], dtype=np.uint8)  # Rango alto
        skin_mask = cv2.inRange(hsv_img, lower_skin, upper_skin)

        # Calcular porcentaje de piel en la imagen
        skin_pixels = np.sum(skin_mask > 0)
        total_pixels = img_array.shape[0] * img_array.shape[1]
        skin_percentage = (skin_pixels / total_pixels) * 100

        # Determinar si es un modelo según el umbral de piel detectada
        return skin_percentage

    except Exception as e:
        print("Error al analizar la imagen:", e)
        return False


get_image_url("https://www.bershka.com/es/jersey-cropped-c0p172274332.html?colorId=829", "bershka")

