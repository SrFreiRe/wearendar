from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time
import requests


# Configuración de Selenium
def configurar_driver():
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
def hacer_scroll(driver, scroll_step=1750, max_scroll=None, container=None):
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
def obtener_imagen_inditex(driver, tienda):
    img_element = None

    # Dependiendo de la tienda, se obtiene la imagen de una forma u otra (debido a la estructura de la página)
    if tienda == "zara":
        max_scroll = driver.execute_script("return document.body.scrollHeight")
        hacer_scroll(driver, 1750, max_scroll)
        # Esperar hasta que la imagen con alt="Image 5" o "Imagen 4" esté presente en el DOM, esta es la imagen limpia del producto
        wait = WebDriverWait(driver, 10)
        img_element = wait.until(EC.presence_of_element_located(
            (By.XPATH, '//img[contains(@alt, "Image 5") or contains(@alt, "Imagen 4")]')
        ))

    elif tienda == "massimodutti":
        wait = WebDriverWait(driver, 10)
        contenedor_imagenes = wait.until(EC.presence_of_element_located(
            (By.CLASS_NAME, 'cc-imagen-collection')
        ))
        max_scroll = driver.execute_script("return arguments[0].scrollHeight", contenedor_imagenes)
        hacer_scroll(driver, 1750, max_scroll, contenedor_imagenes)

        wait = WebDriverWait(driver, 10)
        img_element = wait.until(EC.presence_of_element_located(
            (By.XPATH, '//img[@imageindex="4"]')
        ))


    elif tienda == "bershka":
        max_scroll = driver.execute_script("return document.body.scrollHeight")
        hacer_scroll(driver, 1750, max_scroll)

        wait = WebDriverWait(driver, 10)
        img_element = wait.until(EC.presence_of_element_located(
            (By.XPATH, '//img[contains(@alt, "4")]')
        ))

    elif tienda == "stradivarius":
        time.sleep(0.5)  # Stradiarius carga las imágenes de forma más lenta
        max_scroll = driver.execute_script("return document.body.scrollHeight")
        hacer_scroll(driver, 600, max_scroll)
        # Esperar hasta que la imagen con alt="Image 5" o "Imagen 4" esté presente en el DOM, esta es la imagen limpia del producto
        wait = WebDriverWait(driver, 10)
        img_element = wait.until(EC.presence_of_element_located(
            (By.XPATH, '//img[@data-cy="horizontal-image-6"]')
        ))

    return img_element  # Devolver el elemento de la imagen


# Función principal que devuelve la URL de la imagen de un producto de Zara
def descargar_imagen_zara(url_producto, tienda):
    try:
        # Configurar Selenium
        driver = configurar_driver()
        # Abrir la página del producto
        driver.get(url_producto)

        # Llamamos a la función para obtener la imagen, la metodologia consistirá en hacer scroll en la página para cargar todas las imágenes, ya que estas se cargan de forma dinámica
        img_element = obtener_imagen_inditex(driver, tienda)

        # Obtener la URL de la imagen
        img_url = img_element.get_attribute('src')
        print("URL de la imagen encontrada:", img_url)

    except Exception as e:
        print("Ocurrió un error:", e)
        return None
    finally:
        driver.quit()


# Ejemplo de uso
url_producto = "https://www.massimodutti.com/es/pantalon-parachute-piel-ante-l05304914?pelement=47370412"
descargar_imagen_zara(url_producto, "massimodutti")
