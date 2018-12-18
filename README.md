

<h1>Test Mercado Libre</h1>


<p>Desarrollo de aplicación móvil, que consuma los recursos que provee Mercado Libre a través de sus APIs</p>

<h3>Objetivos</h3>
<ul>
  <li>Campo de búsqueda.</li>
  <li>Visualización de resultados de la búsqueda..</li>
  <li>Detalle de un producto.</li>
</ul>



<h3>Dependencias de terceros</h3>
<p><b>Retrofit 2:</b>Cliente REST que permite realizar peticiones “GET”, “POST”, “PUT”, “PATCH”, “DELETE” devolviendo automáticamente un POJO </p>

<p><b>GSON:</b> librería que permite la serialización y deserialización de JSON a objetos en java y viceversa 

<p><b>GLIDE:</b>librería que permite guardar en cache las imágenes que deseo mostrar desde una fuente externa (servidor web por ejemplo), para luego poder cargarlas dentro de alguna vista de nuestra aplicación. Tiene beneficios como los siguientes: puede utilizarse en actividades y fragmentos, tiempo de muestro de imagen reducido, imágenes mas comprimidas. </p>

<h3>Patrones utilizados</h3>
<p>ViewHolder: Permite cargar listas con mayor fluidez y de forma sencilla, permitiendo mostrar listas largas  </p>

<h3>Permisos utilizados</h3>
<p> <uses-permission android:name="android.permission.INTERNET" /> </p>
<p> <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /> </p>

<h3>Unit Test</h3>
<p> Unit Test para verificar un HTTPRequest </p>
