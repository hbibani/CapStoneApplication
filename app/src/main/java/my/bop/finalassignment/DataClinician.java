package my.bop.finalassignment;

public class DataClinician
{
    public String StaffNumber;
    public String Type;
    public String clinicianid;
    public String clinicianAdmissionid;
    public String patientmrn;
    public String admissionid;

    public String getStaffNumber()
    {
        return this.StaffNumber;
    }
    public String getclinicianid(){return this.clinicianid;}
    public String getclinicianadmissionid(){return this.clinicianAdmissionid;}
    public String getType()
    {
        return this.Type;
    }
    public String getMrn(){
        return this.patientmrn;
    }
    public String getAdmissionid(){
        return this.admissionid;
    }
}
