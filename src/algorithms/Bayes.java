package algorithms;

import Jama.Matrix;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ricardo
 */
public class Bayes {

    private static double SAMPLES[][];
    private double[][] mSetosa = new double[1][4];
    private double[][] mVersicolor = new double[1][4];
    private double[][] CSetosa = {{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};
    private double[][] CVersicolor = {{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};
    Matrix matrixMSetosa;
    Matrix matrixMVersicolor;
    Matrix matrixCSetosa = new Matrix(CSetosa);
    Matrix matrixCVersicolor = new Matrix(CVersicolor);

    public Bayes(double[][] samples) {
        SAMPLES = samples;
        calcularCs();


    }

    public void calcularPertenencia(double[][] test) {


        Matrix muestra = new Matrix(test);
        Matrix xMenosM = muestra.minus(matrixMSetosa);



        Matrix pertenenciaSetosa = xMenosM.times(matrixCSetosa.inverse()).times(xMenosM.transpose());



        xMenosM = muestra.minus(matrixMVersicolor);
        Matrix pertenenciaVersicolor = xMenosM.times(matrixCVersicolor.inverse()).times(xMenosM.transpose());

      
        
        
    
        double distanciaSetosa =   pertenenciaSetosa.get(0, 0);
        double distanciaVersicolor =  pertenenciaVersicolor.get(0, 0);
        
         System.out.println("Pertenece a: ");
        if (distanciaSetosa < distanciaVersicolor) {
            System.out.println("Iris-setosa con distancia "+ String.format("%.2f", distanciaSetosa-0.005)+" < distancia versicolor:"+ String.format("%.2f", distanciaVersicolor-0.005) );
        } else {
            System.out.println("Iris-versicolor con distancia " + String.format("%.2f", distanciaVersicolor-0.005)+" < distancia setosa:"+ String.format("%.2f", distanciaSetosa-0.005) );
        }


    }

    public Matrix calcularXMenosM(Matrix x, Matrix m) {
        return x.minus(m);

    }

    public void calcularCs() {
        calcularM();

        double[][] muestra = new double[1][4];

        for (int i = 0; i < 50; i++) {
            muestra[0][0] = SAMPLES[i][0];
            muestra[0][1] = SAMPLES[i][1];
            muestra[0][2] = SAMPLES[i][2];
            muestra[0][3] = SAMPLES[i][3];
            Matrix matrixMuestra = new Matrix(muestra);
            Matrix a = calcularXMenosM(matrixMuestra, matrixMSetosa);
            matrixCSetosa = matrixCSetosa.plus(a.transpose().times(a));
        }
        matrixCSetosa.timesEquals(0.02);


        for (int i = 50; i < 100; i++) {
            muestra[0][0] = SAMPLES[i][0];
            muestra[0][1] = SAMPLES[i][1];
            muestra[0][2] = SAMPLES[i][2];
            muestra[0][3] = SAMPLES[i][3];
            Matrix matrixMuestra = new Matrix(muestra);
            Matrix a = calcularXMenosM(matrixMuestra, matrixMVersicolor);
            matrixCVersicolor = matrixCVersicolor.plus(a.transpose().times(a));
        }
        matrixCVersicolor.timesEquals(0.02);

    }

    private void calcularM() {
        double x1 = 0;
        double x2 = 0;
        double x3 = 0;
        double x4 = 0;
        for (int i = 0; i < 50; i++) {
            x1 += SAMPLES[i][0];
            x2 += SAMPLES[i][1];
            x3 += SAMPLES[i][2];
            x4 += SAMPLES[i][3];
        }
        mSetosa[0][0] = x1 / 50;
        mSetosa[0][1] = x2 / 50;
        mSetosa[0][2] = x3 / 50;
        mSetosa[0][3] = x4 / 50;

        matrixMSetosa = new Matrix(mSetosa);
        x1 = 0;
        x2 = 0;
        x3 = 0;
        x4 = 0;
        for (int i = 50; i < 100; i++) {
            x1 += SAMPLES[i][0];
            x2 += SAMPLES[i][1];
            x3 += SAMPLES[i][2];
            x4 += SAMPLES[i][3];
        }
        mVersicolor[0][0] = x1 / 50;
        mVersicolor[0][1] = x2 / 50;
        mVersicolor[0][2] = x3 / 50;
        mVersicolor[0][3] = x4 / 50;

        matrixMVersicolor = new Matrix(mVersicolor);

    }
}
