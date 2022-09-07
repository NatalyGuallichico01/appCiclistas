### Integrantes:
- Armas Alejandro
- Catota Luis
- Guala Paul
- Guallichico Nataly

### App Ciclistas - Manual técnico
### App Movi
<hr>

**0. Configuraciones**
> AndroidManifest.xml
!['directory project'](./public/images/12.JPG)

> build.gradle (general)
!['directory project'](./public/images/13.JPG)

> build.gradle (app)
!['directory project'](./public/images/14.JPG)
!['directory project'](./public/images/15.JPG)


<hr>

**1. Estructura del directorio**

Los principales archivos que componen la aplicación son dos: Login.kt y _Profile.kt_.

!['directory project'](./public/images/1.JPG)

<hr>
<br>  

**2. Login.kt**  
Implementa la funcionalidad de inicio de sesión y registro de usuario. La clase inicializa la configuración de su layout y, a continuación, ejecuta la función <em>setup()</em>.

!['directory project'](./public/images/5.JPG)

La función <em>setup()</em> configura las funcionalidades de los botones de _Inicio de sesión_ y de _Registro de usuario_. Al registrar un usuario, se almacenan sus credenciales en Firestore de Firebase, a la vez que se autentica al usuario en su servicio.

!['setup funtion'](./public/images/3.JPG)

Mientras la funcionalidad del botón de inicio se sesión se muestra en la siguiente imagen:

!['setup funtion'](./public/images/2.JPG)

Finalmente, la función <em>showProfile()</em> permite la navegación hacia la pantalla de perfil del usuario, enviando las credenciales del usuario mediante un contenedor de tipo _Bundle_, que será aceptado por la clase Profile.

!['setup funtion'](./public/images/4.JPG)

<br>

<hr>

**3. Profile.kt**  
Implementa la funcionalidad de localización mediante el servicio de Google Play.

En primer lugar, la clase inicializa la referencia a _Firestore_ para el alacenamiento de los datos, y _locationRequest_ para utilizar el servicio de Google Play.

!['setup funtion'](./public/images/6.JPG)

A continuación, se configura la función <em>create()</em> para el servicio de localización, se configura el tiempo de actualización del la posición del usuario, y se extraen los valores enviados por la interfaz anterior mendiante el contenedor _bundle_.

!['setup funtion'](./public/images/7.JPG)

Al iniciarse la interfaz de perfil del usuario, se le solicita que active la función de localización en su teléfono móvil para registrar su posición actual, mediante la ejecución de la función <em>getCurrentLocation()</em>.

!['setup funtion'](./public/images/8.JPG)

Esta función obtiene la ubicación actual del usuario. En primer lugar, verifica la correcta configuración de las versiones del SDK del servicio de Google. Además, configura los permisos para el acceso a los servicios de dispositivo

!['setup funtion'](./public/images/11.JPG)

Si la ubicación no se encuentra activa, ejecuta la función <em>turnOnGPS()</em> para la activación de la localización. Si la ubicación está activa, evalua el resultado obtenido de los datos de ubicación: si los datos son nulos (_null_) y el objeto de respuesta es vacío, ejecuta un bucle continuo hasta la obtención de datos. Una vez se obtiene una respuesta, setea los valores de latitud y longitud, los muestra en la interfaz y los actualiza en la base de datos de Firestore.

!['setup funtion'](./public/images/16.JPG)

La función <em>turnOnGPS()</em> permite la activación de la función de localización en el dipositivo, en caso de no estar activado. El valor _result_ ejecuta el servicio de solicitud de las configuraciones del dispositivo, en este caso, la configuración de la función de localización.

A continuación, evalua el resultado obtenido. Si la localización está activada, presentará un mensaje tipo _Toast_ informado de su estado. Caso contrario, mostrará en pantalla una tarjeta para la activación de este servicio.

!['setup funtion'](./public/images/9.JPG)

La función <em>isGPSEnabled()</em> verifica si el servicio de localización está activado, mediante la clase _LocationManager_.

!['setup funtion'](./public/images/10.JPG)


<hr>
### App Web

**0. Dependencias utilizadas**
> Package.json
![image](https://user-images.githubusercontent.com/66772757/188981105-61d1ad38-b88b-4f6d-adde-3acd0a3e78a2.png)
<hr>

**1. Estructura **
![image](https://user-images.githubusercontent.com/66772757/188981349-4999e1c0-1317-4de7-84e0-6b91b637dcc9.png)

<hr>
<br>  

**2. Componentes adicionales**  
Implementar componentes a travez del comando "ng generate component <nombre del componente>" para la funcionalidad del login completo:
  
> src/app/authentication/
  
![image](https://user-images.githubusercontent.com/66772757/188982434-fa87581d-3c2a-4f58-8018-46f5116730d9.png)

Implementar componentes a travez del comando "ng generate component <nombre del componente>" para la pagina principal denominada "dashboard":
  
> src/app/components/dashboard
  
![image](https://user-images.githubusercontent.com/66772757/188983040-d43d11ef-dd5b-4e5c-a073-5f9d688a4491.png)
<br>

<hr>

**3. Services**
Dentro del archivo "authservice.service.ts" se conficuran todos los metodos que tienen que ver con la geolocalizacion.
> src/app/services/authservice.service.ts
![image](https://user-images.githubusercontent.com/66772757/188984454-68a3fed0-5c2c-476c-b47a-16b93b429af0.png)

**4. Metodos de geolocalización**
>Método para crear la geololalización
  ![image](https://user-images.githubusercontent.com/66772757/188984732-2aba27ac-87d6-4cad-aa0c-f8f3a3301e51.png)
>Método para actualizar la geololalización
  ![image](https://user-images.githubusercontent.com/66772757/188984881-f437d174-21e8-48e0-a48a-814884728eee.png)
>Método para salir
  ![image](https://user-images.githubusercontent.com/66772757/188984997-a0e3d5b0-57cc-4395-a861-ce55c91d6476.png)
>Método para obtener los datos de geolocalización "latitud y longitud". 
  ![image](https://user-images.githubusercontent.com/66772757/188985413-d0b40def-ba61-4e62-9662-18f148311fd3.png)

### **Enlaces**

[Video Manual de Usuario](https://www.youtube.com/watch?v=xl8uGf5iNIQ)

[Video Explicación Técnica](https://youtu.be/TgirbT_Xez0)
