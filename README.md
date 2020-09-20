# AppSeries
AppSeries es una aplicación móvil que desarrolle para poner en practica lo que he aprendido sobre Kotlin y Firebase. El objetivo de la aplicación es poder compartir nuestros viajes, momentos, series o cualquier foto que nos guste.

## Desarrollo
En la aplicación se utilizaron algunas de las librerías que ofrece Android Jetpack
* **LiveData:** Crea objetos de datos que al cambiar su contenido notifica a las vistas de este cambio.
* **ViewModel:** Almacena y administra los datos relacionados con la IU de manera optimizada para los ciclos de vida, lo que permite que los datos sobrevivan
a cambios de configuración, como las rotaciones de pantalla.
* **Fragment:** Segmenta la app en varias pantallas independientes alojadas en un objeto Activity.

<br>

Para mostrar las imágenes se utilizó **Picasso**

<br>

Para la persistencia de datos 
* **Cloud Firestore** para los datos 
* **Cloud Storage** para las fotos.

## Vistas


![image](https://user-images.githubusercontent.com/40668021/93691292-b8cb4380-faa8-11ea-8826-8dee6cd55832.png "Vista de login y registro")
  
Para el registro de usuarios se utilizó la autenticación mediante correo/contraseña que ofrece Firebase, por lo que al registrarse, se envía un correo al usuario 
para que confirme el registro.

![image](https://user-images.githubusercontent.com/40668021/93691332-81a96200-faa9-11ea-90fa-f307c33ed131.png "Correo de verificación")

En la vista de inicio se muestran las fotos que recientemente han subido los usuarios que sigamos. En esta vista también encontramos un floating button 
que permite subir nuevas fotos.

 ![image](https://user-images.githubusercontent.com/40668021/93691357-bf0def80-faa9-11ea-9652-13a55d61d248.png "Vista de inicio")

En esta vista es donde se realiza la búsqueda de las personas que queremos seguir. Escribimos un nombre, presionamos el icono de buscar en nuestro teclado
y se mostraran los usuarios encontrados con ese nombre. Al seleccionar un usuario de los encontrados se abre su perfil, donde podemos ver sus fotos, inspeccionar cada una de ellas o agregarlas a mis favoritos.

![image](https://user-images.githubusercontent.com/40668021/93691413-5410e880-faaa-11ea-9332-9dfb4b7fcf8b.png)

Acá se mostrarán todas las fotos que tengamos como favoritas. El estado de favorito se puede modificar haciendo tap en el icono de corazón de cada foto.

![image](https://user-images.githubusercontent.com/40668021/93691434-be298d80-faaa-11ea-9016-8d92290359e9.png)

Nuestro perfil, vista en la que estarán todas las fotos que hayamos subido hasta el momento. Al seleccionar una foto de estas, se muestran el icono para eliminar la foto (parte superior) y el de editar (derecha del título).

![image](https://user-images.githubusercontent.com/40668021/93691473-50319600-faab-11ea-8f24-9ac827eec226.png)

Cuando inspeccionamos una imagen, se muestra el usuario que la subió, el título, la descripción si posee y los comentarios que posee.

![image](https://user-images.githubusercontent.com/40668021/93691479-5f184880-faab-11ea-8cba-4ba6fd1d2db4.png)


## Requisitos
* Android 6 Marshmallow o versiones superiores.

