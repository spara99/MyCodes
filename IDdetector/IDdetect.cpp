//LINKS:
//https://docs.opencv.org/3.4/da/d0c/tutorial_bounding_rects_circles.html
//https://cpp.hotexamples.com/es/examples/cv/RotatedRect/-/cpp-rotatedrect-class-examples.html
#include <stdio.h>
#include <string>
#include <iostream>
#include <sstream>
#include <opencv2\imgproc.hpp>
#include <opencv2\highgui.hpp>
#include <opencv2\video.hpp>
#include "opencv2/objdetect/objdetect.hpp"
//#include <tesseract/baseapi.h>
//#include <leptonica/allheaders.h>


using namespace cv;
using namespace std;

/// FUNCTION HEADERS
Mat dniDetection(Mat frame);
void faceDetection();
Mat reescalarDNI(Mat rotated_image);
Mat apellidoDetection();
Mat nombreDetection();
Mat numeroDetection();
Mat firmaDetection();
Mat nacimientoDetection();
Mat caducidadDetection();
Mat mrzDetection();

// GLOBAL VARIABLES
Mat dni_definitivo, definitivo_back;
Mat dni_capture;
Mat crop, cropback;
Mat croppedImg, firma;
Mat rotation_matix;
Point2f box_points[4];
int cont_frame = 0;
bool control_frame = false;
bool mensajeDNI, reinicia = false;
double angle = 0;
RotatedRect rect, rectaux;
double xmax, xmin, ymax, ymin;
Mat rotated_image;
double heirojo, widrojo;
Mat tamanho = Mat::zeros(Size(575, 365), CV_32FC3);
VideoCapture capture;
int main(int argc, char** argv)
{

	if (argc == 1) {
		//Captura de video:
		capture.open(0);
		if (!capture.isOpened()) return -1;
		
	}
	else {
		 capture.open(argv[1]);
		 if (!capture.isOpened()) return -1;

	}
	//double fps = capture.get(CAP_PROP_FPS);
	//cout << "Frame per seconds : " << fps << endl;
	//VideoCapture capture(0);
	
//%%%%%%%%   FRONT SIDE   %%%%%%%%   FRONT SIDE   %%%%%%%%   FRONT SIDE   %%%%%%%%   FRONT SIDE   %%%%%%%%   FRONT SIDE   %%%%%%%%   FRONT SIDE   %%%%%%%%   FRONT SIDE    %%%%%%%%
	Mat frame, frame_back;
	for (;;)
	{
		capture >> frame;
		capture >> crop;

		frame = dniDetection(frame);

		namedWindow("Normal Video");
		imshow("Normal Video", frame);

		if (control_frame == true) {

			cout << "Presione cualquier tecla para continuar...";
			waitKey();
			break;
		}

		else {
			if (reinicia == true) {
				cont_frame = 0;  //ta mal if mensaje VA MUY RÁPIDO Y SE REINICIA SIEMPRE :(
			}
			if (waitKey(30) >= 0)  break;
		}
	}

	//Usamos las posiciones de los vertices del DNI (rectangulo verde) para segmentarlo del resto del frame:
	xmax = box_points[0].x;
	xmin = box_points[0].x;
	ymin = box_points[0].y;
	ymax = box_points[0].y;

	for (int i = 0; i < 4; i++) {
		//DESCOMENTAR PARA VER RECTANGULO VERDE
		//line(crop, box_points[i], box_points[(i + 1) % 4], Scalar(0, 255, 0), 3);

		if (xmax <= box_points[i].x) {
			xmax = box_points[i].x;
		}
		if (ymax <= box_points[i].y) {
			ymax = box_points[i].y;
		}
		if (xmin >= box_points[i].x) {
			xmin = box_points[i].x;
		}
		if (ymin >= box_points[i].y) {
			ymin = box_points[i].y;
		}
	}

	//imshow("DNI capture", crop); //cuadro verde

	//Rotamos en una nueva imagen el DNI ya segmentado del resto de la imagen:
	crop(Rect(xmin, ymin, xmax - xmin, ymax - ymin)).copyTo(croppedImg);
	//imshow("IDdetect", croppedImg);

	Point2f center((croppedImg.cols - 1) / 2.0, (croppedImg.rows - 1) / 2.0); //Hallamos el centro del DNI

	//----------------------------Caso DNI girado hacia la derecha----------------------------------
	if (heirojo < widrojo && angle <= -60 && angle >= -90) {
		// using getRotationMatrix2D() to get the rotation matrix
		rotation_matix = getRotationMatrix2D(center, 90 + angle, 1.0);
	}

	//----------------------------Caso DNI girado hacia la izquierda----------------------------------
	if (heirojo < widrojo && angle <= 0 && angle >= -30) {
		// using getRotationMatrix2D() to get the rotation matrix
		rotation_matix = getRotationMatrix2D(center, angle, 1.0);
	}

	//Rotamos la imagen usando warpAffine
	warpAffine(croppedImg, rotated_image, rotation_matix, croppedImg.size());
	imshow("DNI rotado en uprigth position", rotated_image);

	//Eliminamos bordes externos no deseados del DNI rotado y reescalamos para manejarnos con un tamano de imagen conocido
	//Si el DNI esta en un angulo correcto desde el principio (no bordes basuras en la imagen) solo reescalamos, no hace falta cortar mas la imagen
	if ((angle <= 2 && angle >= 0) || (angle <= -88 && angle >= -90)) {
		dni_definitivo = rotated_image;
		resize(dni_definitivo, dni_definitivo, Size(575, 365));
	}
	//Si existen estos bordes "basura" llamamos a reescalarDNI() y recortamos los bordes excedentes
	else {
		dni_definitivo = reescalarDNI(rotated_image);
	}
	imshow("DNI", dni_definitivo);
	//Sacamos todos los datos de la parte frontal del DNI en diferentes imagenes
	faceDetection();
	imshow("Numero DNI", numeroDetection());
	imshow("Nombre", nombreDetection());
	imshow("Apellido", apellidoDetection());
	imshow("Firma", firmaDetection());
	imshow("Fecha de validez", caducidadDetection());
	imshow("Fecha de nacimiento", nacimientoDetection());
	waitKey(0);

	//%%%%%%%%  END FRONT SIDE   %%%%%%%% END FRONT SIDE   %%%%%%%%  END FRONT SIDE   %%%%%%%%  END FRONT SIDE   %%%%%%%%   END FRONT SIDE   %%%%%%%%  END FRONT SIDE   %%%%%%%% END FRONT SIDE    %%%%%%%%


	//%%%%%%%%   OTHER SIDE   %%%%%%%%   OTHER SIDE   %%%%%%%%   OTHER SIDE   %%%%%%%%   OTHER SIDE   %%%%%%%%   OTHER SIDE   %%%%%%%%   OTHER SIDE   %%%%%%%%   OTHER SIDE    %%%%%%%%

	control_frame = false;
	reinicia = false;
	mensajeDNI = false;
	cont_frame = 0;

	if (argc == 1) {
		//Captura de video:
		capture.open(0);
		if (!capture.isOpened()) return -1;

	}
	else {
		capture.open(argv[2]);
		if (!capture.isOpened()) return -1;

	}
	for (;;)
	{
		capture >> frame_back;
		capture >> crop;

		frame_back = dniDetection(frame_back);

		namedWindow("Normal Video");
		imshow("Normal Video", frame_back);

		if (control_frame == true) {

			cout << "Presione cualquier tecla para continuar...";
			waitKey();
			break;
		}

		else {
			if (reinicia == true) {
				cont_frame = 0;  //ta mal if mensaje VA MUY RÁPIDO Y SE REINICIA SIEMPRE :(
			}
			if (waitKey(30) >= 0)  break;
		}
	}

	//Usamos las posiciones de los vertices del DNI (rectangulo verde) para segmentarlo del resto del frame:
	xmax = box_points[0].x;
	xmin = box_points[0].x;
	ymin = box_points[0].y;
	ymax = box_points[0].y;

	for (int i = 0; i < 4; i++) {
		//DESCOMENTAR PARA VER RECTANGULO VERDE
		//line(crop, box_points[i], box_points[(i + 1) % 4], Scalar(0, 255, 0), 3);

		if (xmax <= box_points[i].x) {
			xmax = box_points[i].x;
		}
		if (ymax <= box_points[i].y) {
			ymax = box_points[i].y;
		}
		if (xmin >= box_points[i].x) {
			xmin = box_points[i].x;
		}
		if (ymin >= box_points[i].y) {
			ymin = box_points[i].y;
		}
	}

	//imshow("DNI capture", crop); //cuadro verde

	//Rotamos en una nueva imagen el DNI ya segmentado del resto de la imagen:
	crop(Rect(xmin, ymin, xmax - xmin, ymax - ymin)).copyTo(croppedImg);

	Point2f center_back((croppedImg.cols - 1) / 2.0, (croppedImg.rows - 1) / 2.0); //Hallamos el centro del DNI

	//----------------------------Caso DNI girado hacia la derecha----------------------------------
	if (heirojo < widrojo && angle <= -60 && angle >= -90) {
		// using getRotationMatrix2D() to get the rotation matrix
		rotation_matix = getRotationMatrix2D(center_back, 90 + angle, 1.0);
	}

	//----------------------------Caso DNI girado hacia la izquierda----------------------------------
	if (heirojo < widrojo && angle <= 0 && angle >= -30) {
		// using getRotationMatrix2D() to get the rotation matrix
		rotation_matix = getRotationMatrix2D(center_back, angle, 1.0);
	}

	//Rotamos la imagen usando warpAffine
	warpAffine(croppedImg, rotated_image, rotation_matix, croppedImg.size());
	imshow("DNI back rotado en uprigth position", rotated_image);

	//Eliminamos bordes externos no deseados del DNI rotado y reescalamos para manejarnos con un tamano de imagen conocido
	//Si el DNI esta en un angulo correcto desde el principio (no bordes basuras en la imagen) solo reescalamos, no hace falta cortar mas la imagen
	if ((angle <= 2 && angle >= 0) || (angle <= -88 && angle >= -90)) {
		definitivo_back = rotated_image;
		resize(definitivo_back, definitivo_back, Size(575, 365));
	}
	//Si existen estos bordes "basura" llamamos a reescalarDNI() y recortamos los bordes excedentes
	else {
		definitivo_back = reescalarDNI(rotated_image);
	}
	imshow("DNI BACK", definitivo_back);
	imshow("MRZ DNI", mrzDetection());
	waitKey();

	//%%%%%%%%   END   OTHER SIDE   %%%%%%%%   END   OTHER SIDE %%%%%%%%   END   OTHER SIDE %%%%%%%%   END   OTHER SIDE %%%%%%%%   END   OTHER SIDE %%%%%%%%   END   OTHER SIDE    %%%%


	return 0;

}
//------------------------------------------------FUNCIONES-----------------------------------------------

//-----------------------------Deteccion DNI superficies con contraste------------------------------------
Mat dniDetection(Mat frame) {
	//--------------------------------------------------------------//
	//	Pasamos por parametros el frame que queremos tratar		    //
	//  y devolvemos el frame con el rectangulo dibujado            // 
	//  ademas de modificar las variables globales:                 //
	//  (box_points, heirojo, widrojo)					            //
	//--------------------------------------------------------------//

	Mat edges;
	Mat frame_gray;
	//Canny detection:
	cvtColor(frame, frame_gray, COLOR_BGR2GRAY);
	blur(frame_gray, edges, Size(3, 3));
	Canny(edges, edges, 20, 275);
	//Ahora dibujamos contorno del borde del DNI:
	vector<vector<Point> > contours;
	vector<Vec4i> hierarchy;
	findContours(edges, contours, hierarchy, RETR_TREE, CHAIN_APPROX_SIMPLE);
	vector<vector<Point> > contours_poly(contours.size());
	vector<Rect> boundRect(contours.size());

	for (int i = 0; i < contours.size(); i++) {

		double area = contourArea(contours[i], false);
		if (area >= 10000) {
			reinicia = false;
			//cout << "Reinicia off\n";
			if (mensajeDNI == false) { cout << "DNI detectado, porfavor enfocar lo mejor posible.\n"; }
			mensajeDNI = true;

			approxPolyDP(contours[i], contours_poly[i], 3, true);
			boundRect[i] = boundingRect(contours_poly[i]);

			rectangle(frame, boundRect[i].tl(), boundRect[i].br(), Scalar(0, 0, 255), 2); //Dibuja rectangulo (rojo)

			cont_frame++;
			//cout << cont_frame << " a \n";

			if (cont_frame >= 30) { //1 segundos (30) detectando DNI para poder enfocar bien (30 frames por segundo) 
				control_frame = true;
				cout << boundRect[i];
				//Hallamos las coordenadas donde se ubica el DNI: 
				rect = minAreaRect(contours[i]);
				rect.points(box_points);
				angle = rect.angle; //angulo del DNI (rectangulo verde)
				heirojo = boundRect[i].height;
				widrojo = boundRect[i].width;

			}

		}
		else {
			reinicia = true;
			//cout << "Reinicia on\n";
			break;
		}
		break;
	}

	return frame;
}


//--------------------------------------------------Escalar DNI a tamano conocido-----------------------------------------
Mat reescalarDNI(Mat rotated_image) {

	//--------------------------------------------------------------//
	//	Pasamos por parametros la imagen con dni ya rotado (uprigth)//
	//  y devolvemos el dni perfectamente recortado en sus bordes   // 
	//--------------------------------------------------------------//

	Mat gray_rotated_image;
	Mat edgesaux;
	Mat definitivo_aux;
	double alto = 0, ancho = 0, equis = 0, yy = 0;

	cvtColor(rotated_image, gray_rotated_image, COLOR_BGR2GRAY);
	blur(gray_rotated_image, edgesaux, Size(3, 3));
	Canny(edgesaux, edgesaux, 20, 275);

	vector<vector<Point>> contours;
	vector<Vec4i> hierarchy;
	findContours(edgesaux, contours, hierarchy, RETR_TREE, CHAIN_APPROX_SIMPLE);
	vector<vector<Point> > contours_poly(contours.size());
	vector<Rect> boundRect(contours.size());

	for (int i = 0; i < contours.size(); i++) {

		double area = contourArea(contours[i], false);
		if (area >= 10000) {

			approxPolyDP(contours[i], contours_poly[i], 3, true);
			boundRect[i] = boundingRect(contours_poly[i]);

			equis = boundRect[i].x;
			yy = boundRect[i].y;
			cout << equis << "y el y: " << yy << "\n";

			alto = boundRect[i].height;
			ancho = boundRect[i].width;
			cout << alto << "y el ancho: " << ancho << "\n";

			break;
		}
	}

	rotated_image(Rect(equis, yy, ancho, alto)).copyTo(definitivo_aux);

	resize(definitivo_aux, definitivo_aux, Size(575, 365));

	return definitivo_aux;

}


//------------------------------------FUNCIONES EXTRACCION DE DATOS----------------------------------------

//------------------------------------Deteccion de cara----------------------------------------------------
void faceDetection() {

	CascadeClassifier detector;

	if (!detector.load("haarcascade_frontalface_alt.xml"))
		cout << "No se puede abrir clasificador." << endl;
	Mat dest, gray, facecrop;
	Mat imagen = dni_definitivo;

	cvtColor(imagen, gray, COLOR_BGR2GRAY);
	equalizeHist(gray, dest);

	vector<Rect> rect;
	detector.detectMultiScale(dest, rect);

	for (Rect rc : rect)
	{
		rectangle(imagen, Point(rc.x, rc.y), Point(rc.x + rc.width, rc.y + rc.height), CV_RGB(0, 0, 0), 0);
		imagen(Rect(rc.x, rc.y, rc.width, rc.height)).copyTo(facecrop);
	}

	imshow("Face ID", facecrop);
}

//------------------------------------Deteccion de apellido----------------------------------------------------
Mat apellidoDetection() {

	Mat apellido, apellido_gray;
	dni_definitivo(Rect(220, 90, 125, 60)).copyTo(apellido);
	//resize(apellido, apellido, Size(180, 87));
	cvtColor(apellido, apellido_gray, COLOR_BGR2GRAY);
	imwrite("apellido.jpg", apellido_gray);
	cout << "Lectura del apellido: \n";
	system("tesseract apellido.jpg stdout");
	return apellido;
}

//------------------------------------Deteccion de nombre----------------------------------------------------
Mat nombreDetection() {

	Mat nombre, nomrbe_gray;
	dni_definitivo(Rect(220, 148, 100, 35)).copyTo(nombre);
	//resize(nomrbe_gray, nomrbe_gray, Size(140, 49));
	cvtColor(nombre, nomrbe_gray, COLOR_BGR2GRAY);
	imwrite("nombre.jpg", nomrbe_gray);
	cout << "Lectura del nombre: \n";
	system("tesseract nombre.jpg stdout");
	return nombre;
}
//------------------------------------Deteccion de numero----------------------------------------------------
Mat numeroDetection() {

	Mat numeroDNI, numero_gray;
	dni_definitivo(Rect(47, 310, 170, 38)).copyTo(numeroDNI);
	cvtColor(numeroDNI, numero_gray, COLOR_BGR2GRAY);
	imwrite("numeroDNI.jpg", numero_gray);
	cout << "Lectura del número de DNI: \n";
	system("tesseract numeroDNI.jpg stdout");
	return numeroDNI;
}
//------------------------------------Deteccion de firma------------------------------------------------------
Mat firmaDetection() {

	Mat firma;
	dni_definitivo(Rect(225, 282, 185, 65)).copyTo(firma);
	return firma;
}
//------------------------------------Deteccion de caducidad--------------------------------------------------
Mat caducidadDetection() {

	Mat caducidad, caducidad_gray;
	dni_definitivo(Rect(320, 240, 175, 40)).copyTo(caducidad);
	//resize(caducidad, caducidad, Size(220, 51));
	cvtColor(caducidad, caducidad_gray, COLOR_BGR2GRAY);
	imwrite("caducidad.jpg", caducidad_gray);
	cout << "Lectura de la fecha de caducidad: \n";
	system("\ntesseract caducidad.jpg stdout");
	return caducidad;
}
//------------------------------------Deteccion de cumpleanhos------------------------------------------------
Mat nacimientoDetection() {

	Mat cumple, cumple_gray;
	dni_definitivo(Rect(220, 210, 200, 40)).copyTo(cumple);
	//resize(cumple, cumple, Size(250, 50));
	cvtColor(cumple, cumple_gray, COLOR_BGR2GRAY);
	imwrite("nacimiento.jpg", cumple_gray);
	cout << "Lectura de la fecha de nacimiento: \n";
	system("\ntesseract nacimiento.jpg stdout");
	return cumple;
}

//------------------------------------Deteccion de MRZ--------------------------------------------------------
Mat mrzDetection() {
	Mat mrz, mrz_gray;
	definitivo_back(Rect(2, 220, 560, 136)).copyTo(mrz);
	cvtColor(mrz, mrz_gray, COLOR_BGR2GRAY);
	imwrite("mrz.jpg", mrz_gray);
	cout << "Lectura del MRZ: \n";
	system("\ntesseract mrz.jpg stdout");
	return mrz;
}
//-------------------------------------------------------------------------------------------------------------
/*
void tesseractTexto() {

	string outText;

		// Create Tesseract object
		tesseract::TessBaseAPI * ocr = new tesseract::TessBaseAPI();
		// Initialize OCR engine to use English (eng) and The LSTM OCR engine.
		ocr->Init(NULL, "eng", tesseract::OEM_LSTM_ONLY);
		// Set Page segmentation mode to PSM_AUTO (3)
		ocr->SetPageSegMode(tesseract::PSM_AUTO);

		// Set image data
		ocr->SetImage(im.data, im.cols, im.rows, 3, im.step);
		// Run Tesseract OCR on image
		outText = string(ocr->GetUTF8Text());
		// print recognized text

		cout << outText << endl;
		// Destroy used object and release memory
		ocr->End();



}
*/
//-----------------------------------------------------------