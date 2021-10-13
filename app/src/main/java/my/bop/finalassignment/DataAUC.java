package my.bop.finalassignment;

public class DataAUC
{
    public String AUC;
    public String patientmrn;
    public String bed;
    public String patientGender;
    public String admissionid;
    public String concerned;




    public String getMrn(){
        return this.patientmrn;
    }

    public String getAdmissionid(){
        return this.admissionid;
    }
    public String getBedNumber(){
        return this.bed;
    }
    public String getAUC(){
        return this.AUC;
    }
}
