- Para crear la apk es necesario cambiar la version de la misma modificando los siguientes:

	. En el archivo build.gradle del Modulo app, que este caso la version es la 1.5.62 y el codigo de version 578. Para aumentar a una nueva version hay que aumentar _patch en una unidad por cada version y tiene un tope de 99, y si llega a 99 volver a iniciar en 0 y el siguiente _minor aumentar en uno y lo mmisma relacion entre _minor y _major, una ves aumentado estos datos tambien aumentar en 1 _versionCode.
		def _versionCode = 578
	    def _major = 1
	    def _minor = 5
	    def _patch = 62

	. Para crear una apk es necesario tener una llave para la firma del apk final, en este cado los datos estan en build.gradle del Modulo app en la configuracion signingConfigs{config{...}} y se pedira un keystorepath, keystorepassword, alias y keypassword:
		Se les adjunta en el cd del codigo source el archivo con la llave que debe copiarse en [PATH_DIDRECTORIO_PROYECTOS_ANDROID_STUDIO]/unividapax.jks y tb debera estar la direccion en keystorepath antes de construir la apk y en el build.gradle del modulo app en "storeFile".

	. Por defecto android studio te lo creara una carpteta release al momento de creartelo la apk app-release.apk y un archivo output.json, entonces renombrar el archivo app-release.apk a UNIVIDAVX.X.XX.apk para la nueva version que en este caso sera _major par la primera x y _minor para el segundo x y finalmente _patch para las XX.


- Para obtener las apk de desarrollo y produccion realizar lo siguiente:
	. Desarrollo sin firma
	 	. Cambiar en la clase com.emizor.univida.util.actualizar.Autoupdater lo siguiente:
	 		//UNIVIDA
		    private static final String INFO_FILE = "https://s3.amazonaws.com/elasticbeanstalk-us-east-1-860067475977/cdn/apk/unividapaxa920/version.txt";//url de desarrrollo sin firma
		    //private static final String INFO_FILE = "https://s3.amazonaws.com/elasticbeanstalk-us-east-1-860067475977/cdn/apk/unividapaxa920/versionfirma.txt";// url de desarrollo con firma
		    //private static final String INFO_FILE = "https://s3.amazonaws.com/elasticbeanstalk-us-east-1-860067475977/cdn/apk/unividapaxa920/versionfirmaproduccion.txt";// url de produccion con firma.
		 esto lo necestiamos para que busque el archivo correcto dependiendo de si es de desrrollo(sin y con firma) y de produccion que es con firma.
		. Cambiar en la clase com.emizor.univida.rest.DatosConexion lo siguiente segun corresponda(desarrollo o produccion):
		 	//SERVIDOR PRUEBAS
		    public static final String SERVIDORUNIVIDA = "http://18.208.110.169:9000";
		    //SERVIDOR PRODUCCION
		    //public static final String SERVIDORUNIVIDA = "https://unividapos.univida.bo:6443";
	    . Una ves creado el apk renombrar el archivo generado a UNIVIDAVX.X.XX.apk para desarrollo sin firma.
	. Desarrollo con firma
		. Cambiar en la clase com.emizor.univida.util.actualizar.Autoupdater lo siguiente:
	 		//UNIVIDA
		    //private static final String INFO_FILE = "https://s3.amazonaws.com/elasticbeanstalk-us-east-1-860067475977/cdn/apk/unividapaxa920/version.txt";//url de desarrrollo sin firma
		    private static final String INFO_FILE = "https://s3.amazonaws.com/elasticbeanstalk-us-east-1-860067475977/cdn/apk/unividapaxa920/versionfirma.txt";// url de desarrollo con firma
		    //private static final String INFO_FILE = "https://s3.amazonaws.com/elasticbeanstalk-us-east-1-860067475977/cdn/apk/unividapaxa920/versionfirmaproduccion.txt";// url de produccion con firma.
		 esto lo necestiamos para que busque el archivo correcto dependiendo de si es de desrrollo(sin y con firma) y de produccion que es con firma.
		. Cambiar en la clase com.emizor.univida.rest.DatosConexion lo siguiente segun corresponda(desarrollo o produccion):
		 	//SERVIDOR PRUEBAS
		    public static final String SERVIDORUNIVIDA = "http://18.208.110.169:9000";
		    //SERVIDOR PRODUCCION
		    //public static final String SERVIDORUNIVIDA = "https://unividapos.univida.bo:6443";
	    . Una ves creado el apk renombrar el archivo generado a UNIVIDAVX.X.XXd.apk, d para desarrollo con firma(para los PAX A920)
	. Produccion 
		. Cambiar en la clase com.emizor.univida.util.actualizar.Autoupdater lo siguiente:
	 		//UNIVIDA
		    //private static final String INFO_FILE = "https://s3.amazonaws.com/elasticbeanstalk-us-east-1-860067475977/cdn/apk/unividapaxa920/version.txt";//url de desarrrollo sin firma
		    //private static final String INFO_FILE = "https://s3.amazonaws.com/elasticbeanstalk-us-east-1-860067475977/cdn/apk/unividapaxa920/versionfirma.txt";// url de desarrollo con firma
		    private static final String INFO_FILE = "https://s3.amazonaws.com/elasticbeanstalk-us-east-1-860067475977/cdn/apk/unividapaxa920/versionfirmaproduccion.txt";// url de produccion con firma.
		 esto lo necestiamos para que busque el archivo correcto dependiendo de si es de desrrollo(sin y con firma) y de produccion que es con firma.
		. Cambiar en la clase com.emizor.univida.rest.DatosConexion lo siguiente segun corresponda(desarrollo o produccion):
		 	//SERVIDOR PRUEBAS
		    //public static final String SERVIDORUNIVIDA = "http://18.208.110.169:9000";
		    //SERVIDOR PRODUCCION
		    public static final String SERVIDORUNIVIDA = "https://unividapos.univida.bo:6443";
		. Una ves creado el apk renombrar el archivo generado a UNIVIDAVX.X.XXp.apk, p para produccion con firma(para los PAX A920)	