#include <opencv2/core/core.hpp>
#include <opencv2/opencv.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include<opencv2/opencv.hpp>
#include <iostream>
#include <stdio.h>
#include <cstdlib>
#include <fstream>




#ifdef _MSC_VER
#define _CRT_SECURE_NO_WARNINGS
#endif

using namespace std;
using namespace cv;

//------------------------Funciones----------------------------------------
Mat leeImagen(String path);
Mat hsvHisto(Mat img);
string guardaPrimeraBase(Mat imagen, String cadena);
string guardaGrupo(Mat imagen, string cadena, string grupo);
void guardaHisto(string nombre, Mat histogram);
void srcCanny(Mat src1);
void imprimeBaseDatos();

//-------------------------Variables locales-------------------------------
Mat histogram; //Histograma de la imagen nueva
Mat histogramaux; //Variable para alamcenar histogramas auxliriares
Mat img1; //Imagen igresada por el usuario
Mat img1Canny; //Para almacenar la img1 pasada por un filtro canny
int numimagen = 0; //Contador de imagenes almacenadas en la database
int indicewhile = 0; //Indice para saber cuantas distancias guardar
double arraydist[20]; //Para almacener todas las distancias (max 14)
bool control = false; //boolean para controlar si hay coincidencia con correlacion o no

int main(int argc, char* argv[])
{
	//---------------------Si solo se pasa la debug flag se imprime la database------------------------------------
	if (argc == 2 && strcmp(argv[1], "-d") == 0) {
		imprimeBaseDatos();
		return 0;
	}
	//----------------------Si se pasa la imagen con la debug flag o solo la imagen vamospor aca--------------------
	if (argc == 3 || argc == 2) {

		String fichoriginal = argv[1]; //Nombre y direccion de la imagen.bmp a comparar. Ejemplo de nuestra maquina:(C:\Users\totti\Documents\Christian\AIMV\Tetrabricks\x64\Release\tetra\tcol1.bmp)

		//-------------------------------Variables de uso general---------------------------------------------------
		int isubstr, isubstr2, isubstr3 = 0; //inice que nos indica la posición para hacer substr
		String texto, baseoriginal, nuevonombre, grupoaux, subnombre = ""; //variables de control de ficheros
		//----------------------------------------------------------------------------------------------------------
		String archivoImagen = fichoriginal.substr(fichoriginal.find("tcol"), fichoriginal.length() - 1); //extraemos del path pasado por parametros, el nombre del archivo de imagen a comparar
		//cout << "Invocamos a: " << archivoImagen << "\n"; //Control para ver que imagen cargamos

		//--------------------------------Inicializacion imagen de trabajo-----------------------------------------
		img1 = leeImagen(fichoriginal);
		//Mostramos la imagen
		namedWindow("Source", 1);
		imshow("Source", img1);
		//Usamos filtro canny para poder hacer la comparacion de hitoramas mas eficiente
		img1Canny.create(img1.size(), img1.type());
		srcCanny(img1);

		//DESCOMENTAR SI SE QUIERE VER LA IMAGEN CON EL FILTRO CANNY APLICADO
		/*namedWindow("Source Canny", 1);
		imshow("Source Canny", img1Canny);*/
		waitKey(0);

		//Abrimos fichero de base de datos
		ifstream database;
		database.open("database.txt", ios::in);

		//-------------------------------Si existe un fichero database lo leemos----------------------------------------------------------------------
		if (database.is_open()) {

			while (!database.eof()) {
				isubstr, isubstr2, isubstr3 = 0;
				getline(database, texto);


				baseoriginal = texto.substr(0, texto.find(' '));

				//Verificamos si la imagen que se quiere cargar esta ya cargada:
				if (archivoImagen == baseoriginal) {
					for (isubstr = 0; isubstr <= baseoriginal.length(); isubstr++) {

					}

					grupoaux = texto.substr(isubstr, texto.find('\n'));
					nuevonombre = grupoaux.substr(0, grupoaux.find(' '));

					for (isubstr2 = 0; isubstr2 <= nuevonombre.length(); isubstr2++) {

					}

					string grupoaux2 = grupoaux.substr(isubstr2, texto.find('\n'));
					string nomegrupo = grupoaux2.substr(0, grupoaux2.find(' '));

					for (isubstr3 = 0; isubstr3 <= nomegrupo.length(); isubstr3++) {

					}
					string nomehist = "";
					nomehist = grupoaux2.substr(isubstr3, grupoaux2.find('\n'));
					cout << "La imagen que se quiere cargar ya existe en el data base y sus datos son:" << "\n";
					cout << "El nombre original era: " << baseoriginal << ", el nuevo es: " << nuevonombre << " y pertence al grupo: " << nomegrupo << ". Histograma almacenado en: " << nomehist << "\n";

					if (strcmp(argv[2], "-d") == 0)
						imprimeBaseDatos();
					return 0;

				} //Fin del if
			} //Fin del while
			database.close();



			//Existe un fichero y la imagen cargada no esta incluida en una anterior ejecucion, comparamos y guardamos
			histogram = hsvHisto(img1Canny);

			database.open("database.txt", ios::in);
			while (!database.eof()) {

				isubstr, isubstr2, isubstr3 = 0;
				getline(database, texto);
				baseoriginal = texto.substr(0, texto.find(' '));

				for (isubstr = 0; isubstr <= baseoriginal.length(); isubstr++) {

				}

				grupoaux = texto.substr(isubstr, texto.find('\n'));
				nuevonombre = grupoaux.substr(0, grupoaux.find(' '));

				for (isubstr2 = 0; isubstr2 <= nuevonombre.length(); isubstr2++) {

				}

				string grupoaux2 = grupoaux.substr(isubstr2, texto.find('\n'));
				string nomegrupo = grupoaux2.substr(0, grupoaux2.find(' '));

				for (isubstr3 = 0; isubstr3 <= nomegrupo.length(); isubstr3++) {

				}
				string nomehist = "";
				nomehist = grupoaux2.substr(isubstr3, grupoaux2.find('\n')); //nomehist: nombre del archivo histograma

				//Ahora cargamos el fichero.yml en nuestra variable aux para compararla con nuestro histograma deseado
				cv::FileStorage fr(nomehist, cv::FileStorage::READ);
				fr["hist_file"] >> histogramaux;
				fr.release();

				double correlation = compareHist(histogram, histogramaux, 0); //El 0 es que utilizaremos metodo de correlacion
				arraydist[indicewhile] = correlation;
				indicewhile++;


				if (correlation >= 0.52037 && control == false) {
					subnombre = guardaGrupo(img1, archivoImagen, nomegrupo);
					guardaHisto(subnombre, histogram);
					control = true;
				} //Fin del if



				if (database.peek() == EOF) {
					break;
				}
			} //Fin del while
			database.close();



			//Si control=false significa que nunca se entro en el if del bucle anterior por ende la imagen no pertenece a ningun grupo y lo creamos
			if (control == false) {
				subnombre = guardaPrimeraBase(img1, archivoImagen);
				guardaHisto(subnombre, histogram);
			}

			if (strcmp(argv[2], "-d") == 0) {
				cout << "-------------------------------------------------------------------------";
				cout << "\n" << "Las distancias obtenidas con respecto a la imagen cargada son:" << "\n";
				for (int i = 0; i <= indicewhile - 1; i++)
					cout << arraydist[i] << "\n";
				imprimeBaseDatos();
			}
			cout << "-------------------------------------------------------------------------";

		} //Fin del if


		//-------------------------Si no existe un fichero (primer ejecucion del programa) lo creamos----------------------------------------------
		else {

			histogram = hsvHisto(img1Canny);
			subnombre = guardaPrimeraBase(img1, archivoImagen);
			guardaHisto(subnombre, histogram);

			if (strcmp(argv[2], "-d") == 0) {
				imprimeBaseDatos();
			}
		}
		//----------------------------------------------------------------------------------------------------------------------------------------

		return 0;
	}


	else cout << "Formato solicitud erronea\n";


	return 0;
}
//-------------------------------------------------FUNCIONES-----------------------------------------------------------------------------------

//--------------------------------------Base de datos, si no se consigue match(imagenes,histogramas,nombres)-----------------------------------
string guardaPrimeraBase(Mat imagen, String cadena) {
	ofstream basewrite;
	string nuevonombre, grupo = "";
	basewrite.open("database.txt", ios::app);
	if (basewrite.is_open()) {
		fflush(stdin);
		cout << "Ingresa el nombre con el que deseas guardar la imagen: ";
		getline(cin, nuevonombre);
		cout << "A que grupo pertenece? ";
		getline(cin, grupo);
		basewrite << cadena << " " << nuevonombre << ".bmp " << grupo << " " << nuevonombre << "_hist" << ".yml" << "\n";
		imwrite(nuevonombre + ".bmp", imagen);
	}
	basewrite.close();
	nuevonombre = nuevonombre + "_hist";
	return nuevonombre;
}
//--------------------------------Base de datos, asignar imagen a grupo con que hizo match(imagenes,histogramas,nombres)---------------------------
string guardaGrupo(Mat imagen, string cadena, string grupo) {
	cout << "Match conseguido, este tetrabrick se asiganara al grupo: " << grupo << "\n";
	ofstream basewrite;
	string nuevonombre;
	basewrite.open("database.txt", ios::app);
	if (basewrite.is_open()) {
		fflush(stdin);
		cout << "Ingresa el nombre con el que deseas guardar la imagen: ";
		getline(cin, nuevonombre);
		basewrite << cadena << " " << nuevonombre << ".bmp " << grupo << " " << nuevonombre << "_hist" << ".yml" << "\n";
		imwrite(nuevonombre + ".bmp", imagen);
	}
	basewrite.close();
	nuevonombre = nuevonombre + "_hist";
	return nuevonombre;
}
//--------------------------------------Carga la imagen a comparar----------------------------------------------------
Mat leeImagen(String path) {
	Mat Imagen = imread(path);
	return Imagen;
}
//--------------------------------------Guarda los histogramas de las imagenes almacenadas----------------------------
void guardaHisto(string nombre, Mat histogram) {

	string nuevonombre, grupo = "";
	std::string resultado = nombre + ".yml";
	cv::FileStorage fs(resultado, cv::FileStorage::WRITE);
	fs << "hist_file" << histogram;
	fs.release();


}
//--------------------------------------Calcula los histogramas en formato HSV----------------------------------------
Mat hsvHisto(Mat src1) {

	// Pasamos la imagen de RGB a HSV
	Mat hsv_base;
	cvtColor(src1, hsv_base, COLOR_BGR2HSV);

	// Using 30 bins for hue and 32 for saturation
	int h_bins = 30; int s_bins = 32; int v_bins = 20;
	int histSize[] = { h_bins, s_bins , v_bins };


	// hue varies from 0 to 180, saturation from 0 to 255
	float h_ranges[] = { 0, 180 };
	float s_ranges[] = { 0, 255 };
	float v_ranges[] = { 50, 255 }; // Why am I using a range different from {0 , 255} for the value channel?

	const float* ranges[] = { h_ranges, s_ranges, v_ranges };

	// Use the 3 channels
	int channels[] = { 0, 1, 2 };


	/// Histograms
	MatND hist_base;


	/// Calculate the histograms for the HSV images
	calcHist(&hsv_base, 1, channels, Mat(), hist_base, 3, histSize, ranges, true, false);
	normalize(hist_base, hist_base, 1, 0, NORM_L1, -1, Mat());

	return hist_base;
}
//-------------------------------------------------Funcion para sacar canny a la imagen de entrada----------------------------------
void srcCanny(Mat src1) {
	Mat canny_edges;
	Mat canny_out;
	Mat src1_gray;
	cvtColor(src1, src1_gray, COLOR_BGR2GRAY);
	blur(src1_gray, canny_edges, cv::Size(3, 3));
	Canny(canny_edges, canny_out, 20, 255, 3);
	img1Canny = cv::Scalar::all(0);
	img1.copyTo(img1Canny, canny_out);

}
//----------------------------------------------------------------------------------------------------------------------------------

void imprimeBaseDatos() { //FALTA DISTANCIA

	ifstream database;
	database.open("database.txt", ios::in);

	//Si existe un fichero database lo leemos
	if (database.is_open()) {
		cout << "------------------------------------------------------------------------";
		cout << "\n" << "Tenemos los siguientes datos almacenados en la data base: \n";
		string aux = "";
		while (!database.eof()) {

			getline(database, aux);
			cout << aux << "\n";
			numimagen++;
		}
		database.close();
		cout << "La cantidad de tetrabricks almacenados en la data base es:" << numimagen - 1 << "\n";
		cout << "------------------------------------------------------------------------";
	}

	else {
		cout << "No hay data base creada.";
	}

}

//----------------------------------------------------------------------------------------------------------------------------------