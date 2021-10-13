package my.bop.finalassignment;

public class DataPatient
{
    public String patientmrn;
    public String patientGender;
    public String patientAge;
    public String patientWeight;
    public String patientID;
    public String userid;

    public String getMrn(){
        return this.patientmrn;
    }
    public String getuserid(){ return this.userid; }
    public String getPatientGender(){return this.patientGender;}
    public String getPatientAge(){return this.patientAge;}
    public String getPatientID(){return this.patientID;}
    public String getPatientWeight(){return this.patientWeight;}

}
