package algorithms;

import Jama.Matrix;

public class KMedias {

    private static double SAMPLES[][];
    private double matrixPertenecias[][] = new double[2][100];
    private double[] centroSetosa = new double[4];
    private double[] centroVersicolor = new double[4];
    private double[] centroSetosaAnterior = new double[4];
    private double[] centroVersicolorAnterior = new double[4];
    private final int b = 2;
    private final double tolerancia = 0.01;

    public KMedias(double[][] samples) {

        SAMPLES = samples;

        inicializar();
        entrenamiento();


    }

    public void calcularPertencia(double[][] muestra) {
        double[] arrayMuestra = new double[4];
        arrayMuestra[0] = muestra[0][0];
        arrayMuestra[1] = muestra[0][1];
        arrayMuestra[2] = muestra[0][2];
        arrayMuestra[3] = muestra[0][3];

        
        double distanciaSetosa = distanciaEuclidea(arrayMuestra, centroSetosa);
        double distanciaVersicolor =  distanciaEuclidea(arrayMuestra, centroVersicolor);
        
         System.out.println("Pertenece a: ");
        if (distanciaSetosa < distanciaVersicolor) {
            System.out.println("Iris-setosa con distancia "+ String.format("%.2f", distanciaSetosa-0.005)+" < distancia versicolor:"+ String.format("%.2f", distanciaVersicolor-0.005) );
        } else {
            System.out.println("Iris-versicolor con distancia " + String.format("%.2f", distanciaVersicolor-0.005)+" < distancia setosa:"+ String.format("%.2f", distanciaSetosa-0.005) );
        }


    }

    private void entrenamiento() {
        reCalcularCentros();
        int muestrasProcesadas = 0;

        while (!criterioFinalizacion() && muestrasProcesadas < 100) {
            double[] arrayMuestra = new double[4];

            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 4; j++) {
                    arrayMuestra[j] = SAMPLES[i][j];
                }

                matrixPertenecias[0][i] = calcularProbabilidad(arrayMuestra, centroSetosa);
                matrixPertenecias[1][i] = calcularProbabilidad(arrayMuestra, centroVersicolor);
                //matrixPertenecias[1][i] = 1-matrixPertenecias[0][i];
            }

            reCalcularCentros();
            muestrasProcesadas++;
        }
    }

    private void inicializar() {
        centroSetosa[0] = 4.6;
        centroSetosa[1] = 3.0;
        centroSetosa[2] = 4.0;
        centroSetosa[3] = 0.0;
        centroVersicolor[0] = 6.8;
        centroVersicolor[1] = 3.4;
        centroVersicolor[2] = 4.6;
        centroVersicolor[3] = 0.7;

        centroSetosaAnterior[0] = 4.6;
        centroSetosaAnterior[1] = 3.0;
        centroSetosaAnterior[2] = 4.0;
        centroSetosaAnterior[3] = 0.0;
        centroVersicolorAnterior[0] = 6.8;
        centroVersicolorAnterior[1] = 3.4;
        centroVersicolorAnterior[2] = 4.6;
        centroVersicolorAnterior[3] = 0.7;
        double[] arrayMuestra = new double[4];

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 4; j++) {
                arrayMuestra[j] = SAMPLES[i][j];
            }

            matrixPertenecias[0][i] = calcularProbabilidad(arrayMuestra, centroVersicolor);
            matrixPertenecias[1][i] = calcularProbabilidad(arrayMuestra, centroSetosa);


        }

    }

    private double distanciaEuclidea(double[] vector1, double[] vector2) {
        return (Math.pow(vector1[0] - vector2[0], 2) + Math.pow(vector1[1] - vector2[1], 2) + Math.pow(vector1[2] - vector2[2], 2) + Math.pow(vector1[3] - vector2[3], 2));
    }

    private boolean criterioFinalizacion() {
        boolean finalizar = tolerancia > distanciaEuclidea(centroSetosaAnterior, centroSetosa) && tolerancia > distanciaEuclidea(centroVersicolorAnterior, centroVersicolor);

        centroSetosaAnterior[0] = centroSetosa[0];
        centroSetosaAnterior[1] = centroSetosa[1];
        centroSetosaAnterior[2] = centroSetosa[2];
        centroSetosaAnterior[3] = centroSetosa[3];
        centroVersicolorAnterior[0] = centroVersicolor[0];
        centroVersicolorAnterior[1] = centroVersicolor[1];
        centroVersicolorAnterior[2] = centroVersicolor[2];
        centroVersicolorAnterior[3] = centroVersicolor[3];

        return finalizar;

    }

    private void reCalcularCentros() {

        double[][] arrayNumeradorSetosa = {{0.0, 0.0, 0.0, 0.0}};
        double[][] arrayNumeradorVersicolor = {{0.0, 0.0, 0.0, 0.0}};
        Matrix numeradorSetosa = new Matrix(arrayNumeradorSetosa);
        Matrix numeradorVersicolor = new Matrix(arrayNumeradorVersicolor);
        double[][] centroSetosa2;
        double[][] centroVersicolor2;
        double[][] arrayMuestra = new double[1][4];
        Matrix matrixMuestra = null;
        double denominadorSetosa = 0;
        double denominadorVersicolor = 0;


        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 4; j++) {
                arrayMuestra[0][j] = SAMPLES[i][j];
            }

            matrixMuestra = new Matrix(arrayMuestra);
            numeradorSetosa = numeradorSetosa.plus(matrixMuestra.times(matrixPertenecias[0][i]));
            denominadorSetosa += matrixPertenecias[0][i];



            numeradorVersicolor = numeradorVersicolor.plus(matrixMuestra.times(matrixPertenecias[1][i]));
            denominadorVersicolor += matrixPertenecias[1][i];
        }



        centroSetosa2 = numeradorSetosa.times(1.0 / denominadorSetosa).getArray();

        centroVersicolor2 = numeradorVersicolor.times(1.0 / denominadorVersicolor).getArray();

        centroSetosa[0] = centroSetosa2[0][0];
        centroSetosa[1] = centroSetosa2[0][1];
        centroSetosa[2] = centroSetosa2[0][2];
        centroSetosa[3] = centroSetosa2[0][3];

        //numeradorSetosa.times(1.0/denominadorSetosa).print(NumberFormat.INTEGER_FIELD, 3);
        //  System.out.println(centroVersicolor2[0][1]);
        //  System.out.println(centroSetosa[1]);

        centroVersicolor[0] = centroVersicolor2[0][0];
        centroVersicolor[1] = centroVersicolor2[0][1];
        centroVersicolor[2] = centroVersicolor2[0][2];
        centroVersicolor[3] = centroVersicolor2[0][3];



    }

    private double calcularProbabilidad(double[] muestra, double[] centro) {
        double numerador = Math.pow((1.0 / distanciaEuclidea(muestra, centro)), (1 / b - 1));

        double denominador;

        denominador = Math.pow((1.0 / distanciaEuclidea(muestra, centroSetosa)), (1 / b - 1)) + Math.pow((1.0 / distanciaEuclidea(muestra, centroVersicolor)), (1 / b - 1));

        return numerador / denominador;


    }
}