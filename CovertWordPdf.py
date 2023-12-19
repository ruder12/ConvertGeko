import os
import sys
import time

import comtypes.client


def procesar_archivos(archivo_entrada, archivo_salida, format_object_app):
    format_code = 17
    file_input = os.path.abspath(archivo_entrada)
    file_ouput = os.path.abspath(archivo_salida)

    time_init = time.time()
    # "Word.Application"
    word_app = comtypes.client.CreateObject(format_object_app)
    word_file = word_app.Documents.Open(file_input)
    word_file.SaveAs(file_ouput, FileFormat=format_code)
    word_file.Close()
    word_app.Quit()

    time_finist = time.time()

    print(f'Duracion de Conversion {time_finist - time_init}')


if __name__ == "__main__":
    # Verificar que se proporcionen los dos argumentos esperados
    if len(sys.argv) != 4:
        print("Uso: python script.py archivo_entrada archivo_salida format_object_app")
        sys.exit(1)

    # Obtener los argumentos de la línea de comandos
    archivo_entrada = sys.argv[1]
    archivo_salida = sys.argv[2]
    format_object_app = sys.argv[3]

    # Llamar a la función de procesamiento con los argumentos
    procesar_archivos(archivo_entrada, archivo_salida, format_object_app)
