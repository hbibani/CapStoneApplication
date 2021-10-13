package my.bop.finalassignment;

public class DataMed
{
    public String medID;
    public String chemName;
    public String brandName;
    public String dosageName;
    public String doseForm;
    public String doseAmount;
    public String medicationStayId;
    public String time;
    public String patientmrn;
    public String bed;
    public String patientGender;
    public String admissionid;
    public String concerned;


    public String getmedID()
    {
        return this.medID;
    }
    public String getChemName()
    {
        return this.chemName;
    }
    public String getBrandName()
    {
        return this.brandName;
    }
    public String getDosageName() { return this.dosageName; }
    public String getDosageAmount() { return this.doseAmount; }
    public String getDoseForm()
    {
        return this.doseForm;
    }
    public String getMedicationStayId()
    {
        return this.medicationStayId;
    }
    public String getTime(){return this.time;}
    public String getMrn(){
        return this.patientmrn;
    }
    public String getAdmissionid(){
        return this.admissionid;
    }
}
