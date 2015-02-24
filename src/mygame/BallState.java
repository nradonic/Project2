/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;

/**
 *
 * @author Nick Radonic
 */
public class BallState {

    private Vector3f ballLocation;
    private Vector3f ballVelocity;
    private Vector3f ballRotation;
    private int sampleNumber;
    private int ballNumber;

    BallState(int ballNumber, int sampleNumber, Vector3f ballLocation, Vector3f ballVelocity, Vector3f ballRotation) {
        this.ballLocation = ballLocation;
        this.ballVelocity = ballVelocity;
        this.ballRotation = ballRotation;
        this.sampleNumber = sampleNumber;
        this.ballNumber = ballNumber;
    }

    /**
     * @return the ballLocation
     */
    public Vector3f getBallLocation() {
        return ballLocation;
    }

    /**
     * @param ballLocation the ballLocation to set
     */
    public void setBallLocation(Vector3f ballLocation) {
        this.ballLocation = ballLocation;
    }

    /**
     * @return the ballVeocity
     */
    public Vector3f getBallVeocity() {
        return ballVelocity;
    }

    /**
     * @param ballVeocity the ballVeocity to set
     */
    public void setBallVeocity(Vector3f ballVeocity) {
        this.ballVelocity = ballVeocity;
    }

    /**
     * @return the ballRotation
     */
    public Vector3f getBallRotation() {
        return ballRotation;
    }

    /**
     * @param ballRotation the ballRotation to set
     */
    public void setBallRotation(Vector3f ballRotation) {
        this.ballRotation = ballRotation;
    }

    /**
     * @return the sampleNumber
     */
    public int getSampleNumber() {
        return sampleNumber;
    }

    /**
     * @param sampleNumber the sampleNumber to set
     */
    public void setSampleNumber(int sampleNumber) {
        this.sampleNumber = sampleNumber;
    }

    /**
     * @return the ballNumber
     */
    public int getBallNumber() {
        return ballNumber;
    }

    /**
     * @param ballNumber the ballNumber to set
     */
    public void setBallNumber(int ballNumber) {
        this.ballNumber = ballNumber;
    }
    
    public String toString(){
        String result = "";
        result += "# "+ballNumber+" Sample: "+sampleNumber+" POSN: "+ballLocation+"\nVEL: "+ballVelocity+" ROT: "+ballRotation+"\n";
        return result;
    }
}
