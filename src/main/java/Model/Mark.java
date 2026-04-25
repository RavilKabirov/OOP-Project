import java.io.*;
import java.util.*;

public class Mark {

    private double firstAttestation;
    private double secondAttestation;
    private double finalExam;
    private Long enrollmentId;

    public Mark() {
    }
    public double calculateTotal(){
        return firstAttestation+secondAttestation+finalExam;
    }
    public double getFirstAttestation(){
        return firstAttestation;
    }
    public void setFirstAttestation(double value){
        this.firstAttestation=value;
    }
    public double getSecondAttestation(){
        return secondAttestation;
    }
    public void setSecondAttestation(double value){
        this.secondAttestation=value;
    }

    public double getFinal(){
        return finalExam;
    }
    public void setFinal(double value) {
        this.finalExam=value;
    }
    @Override
    public String toString() {
        return "Mark{"+"first="+firstAttestation+", second="+secondAttestation+
                ", final=" + finalExam +", total=" + calculateTotal() +'}';
    }
}
