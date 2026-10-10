# Mini Soccer Aim — prototipo Android 0.1

Proyecto de Android Studio para una guía **manual** flotante sobre Mini Soccer Star.

## Funciones
- Menú flotante con cierre total.
- Dos puntos arrastrables: pelota y objetivo.
- Línea de dirección entre los puntos.
- Opción para ocultar la guía.

## Compilación
1. Abrir esta carpeta con Android Studio (requiere SDK Android 35).
2. Sincronizar Gradle y ejecutar `app` en un teléfono Android.
3. Autorizar «Mostrar sobre otras apps» y volver a pulsar «Iniciar guía flotante».
4. Mover las marcas manualmente sobre el juego.

## Limitaciones actuales
- **No es un APK compilado**: es el código fuente para compilar.
- No captura la pantalla, detecta automáticamente objetos ni calcula trayectorias físicas.
- En modo edición, la capa de guía intercepta toques. Pulsar «Dejar pasar toques» para jugar con la guía visible sin interceptar toques, y «Editar guía» para mover los puntos.
- No modifica el APK/XAPK original del juego.
