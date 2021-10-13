package my.bop.finalassignment;

public class DataAdmission {

    public String patientmrn;
    public String bed;
    public String patientGender;
    public String admissionid;
    public String concerned;
    public String graphid;
    public String starttime;
    public String endtime;
    public String maxtimecounter;
    public String status;
    public String painregion;
    public String paintype;

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
    public String getGraphid(){return this.graphid;}
    public String getStartTime(){return this.starttime;}
    public String getEndTime(){return this.endtime;}
    public String getMaxTimeCounter(){return this.maxtimecounter;}
    public String getPainregion(){return this.painregion;}
    public String getPaintype(){return this.paintype;}
    public String getStatus(){return this.status;}
}
