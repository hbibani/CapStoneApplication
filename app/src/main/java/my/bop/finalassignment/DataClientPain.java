package my.bop.finalassignment;

public class DataClientPain
{


    public String painscore;
    public String graphvalueid;
    public String graphid;
    public String time;

    public String patientmrn;
    public String admissionid;


    public String getpainScore()
    {
        return this.painscore;
    }
    public String getGraphID()
    {
        return this.graphid;
    }
    public String getGraphValueID()
    {
        return this.graphvalueid;
    }
    public String getTime(){return this.time;}
    public String getMrn(){
        return this.patientmrn;
    }
    public String getAdmissionid(){
        return this.admissionid;
    }

}
