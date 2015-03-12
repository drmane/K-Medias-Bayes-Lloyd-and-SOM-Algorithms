package algorithms;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ricardo
 */
public class SOM {

    private static double SAMPLES[][];
    private double[] centroSetosa = new double[4];
    private double[] centroVersicolor = new double[4];
    private double[] centroSetosaAnterior = new double[4];
    private double[] centroVersicolorAnterior = new double[4];
    private double razonAprendizaje = 0.1;
    private final double tolerancia = Math.pow(10, -6);
    private final int kMax = 1000;
    private double alfaInicial = 0.1;
    private double alfaFinal = 0.01;
    private double T = Math.pow(10, -5);
    private double kIteracion = 0;
    private double alfaDeK;

    public SOM(double[][] Samples) {
        SAMPLES = Samples;
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


        do {
            double[] muestra = {0, 0, 0, 0};
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 4; j++) {
                    muestra[j] = SAMPLES[i][j];
                }
                kIteracion++;
                anadirMuestra(muestra);
            }

        } while (kIteracion < kMax);
    }

    private void anadirMuestra(double[] muestra) {

        double regionVecindadSetosa;
        double regionVecindadVersicolor;
        alfaDeK = alfaInicial * Math.pow((alfaFinal / alfaInicial), (kIteracion / kMax));

        regionVecindadSetosa = calculoRegionVecindad(muestra, centroSetosa, alfaDeK);
        regionVecindadVersicolor = calculoRegionVecindad(muestra, centroVersicolor, alfaDeK);

        if (regionVecindadSetosa > T) {
            for (int i = 0; i < 4; i++) {
                centroSetosa[i] = centroSetosaAnterior[i] + (razonAprendizaje * (muestra[i] - centroSetosaAnterior[i]));
                centroSetosaAnterior[i] = centroSetosa[i];
            }
        }
        if (regionVecindadVersicolor > T) {
            for (int i = 0; i < 4; i++) {
                centroVersicolor[i] = centroVersicolorAnterior[i] + (razonAprendizaje * (muestra[i] - centroVersicolorAnterior[i]));
                centroVersicolorAnterior[i] = centroVersicolor[i];
            }
        }
    }


    private double calculoRegionVecindad(double[] vector1, double[] centro, double alfa) {

        return Math.exp(-(distanciaEuclidea(vector1, centro) / (2 * Math.pow(alfa, 2))));
    }

    private double distanciaEuclidea(double[] vector1, double[] vector2) {
        return (Math.pow(vector1[0] - vector2[0], 2) + Math.pow(vector1[1] - vector2[1], 2) + Math.pow(vector1[2] - vector2[2], 2) + Math.pow(vector1[3] - vector2[3], 2));
    }
}
