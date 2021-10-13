package my.bop.finalassignment;

public class DataAdmissionAdmin
{
    public String patientmrn;
    public String bed;
    public String patientGender;
    public String admissionid;
    public String concerned;
    public String starttime;
    public String endtime;

    public String getMrn(){
        return this.patientmrn;
    }
    public String getConcerned(){return this.concerned;}
    public String getAdmissionid(){
        return this.admissionid;
    }
    public String getBedNumber(){
        return this.bed;
    }
    public String getPatientGender(){return this.patientGender;}
    public String getStartTime(){return this.starttime;}
    public String getEndTime(){return this.endtime;}
}
