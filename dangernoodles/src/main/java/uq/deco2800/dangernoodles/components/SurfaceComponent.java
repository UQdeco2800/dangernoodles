package uq.deco2800.dangernoodles.components;

import java.util.Random;

/**
 * Created by Justin Silver on 9/5/2016.
 *
 */
public class SurfaceComponent {
    private double[][] surfaceDiscrete;
    private double initialHeight;
    SurfaceComponent(double start, double end, int intervals,double initialHeight,double roughness) {
        this.initialHeight=initialHeight;
        calculateMidPoint(start, end, intervals);
        setInitialHeight();
        midPointDisplacement(0,getSurfaceDiscrete()[0].length-1,roughness);

    }

    private void calculateMidPoint(double start, double end, int intervels) {
        surfaceDiscrete=new double[2][intervels+1];
        surfaceDiscrete[0][0]=start;
        double increment=(end-start)/intervels;
        double next=start+increment;
        for(int i = 1;i<surfaceDiscrete[0].length;i++){
            surfaceDiscrete[0][i]=next;
            next=next+increment;
        }
    }

    private void setInitialHeight(){
        surfaceDiscrete[1][0]=initialHeight;
        surfaceDiscrete[1][surfaceDiscrete[0].length-1]=initialHeight;
    }
    private void midPointDisplacement(int start,int end,double roughness){
        if(start<end){
            int midPoint=start+(end-start)/2;
            double average;
            if(midPoint>start && midPoint<end){
                average=(surfaceDiscrete[1][start]+surfaceDiscrete[1][end])/2;
                surfaceDiscrete[1][midPoint]=displacement(roughness,average);
                midPointDisplacement(start,midPoint,roughness);
                midPointDisplacement(midPoint,end,roughness);
            }
        }
    }

    public double displacement(double roughness,double average){
       Random r = new Random();
        return  r.nextGaussian()*roughness+average;
    }

    public double[][] getSurfaceDiscrete() {
        return surfaceDiscrete;
    }

}

