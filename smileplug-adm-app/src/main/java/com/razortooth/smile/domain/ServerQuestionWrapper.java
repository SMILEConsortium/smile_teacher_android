package com.razortooth.smile.domain;

public class ServerQuestionWrapper {

    private String NAME;
    private String Q;
    private String O1;
    private String O2;
    private String O3;
    private String O4;
    private int A;
    private String PICURL;
    private String TYPE;
    private String IP;

    public ServerQuestionWrapper(Question question) {
        super();
        this.NAME = question.getOwner();
        this.IP = question.getIp();
        this.Q = question.getQuestion();
        this.O1 = question.getOption1();
        this.O2 = question.getOption2();
        this.O3 = question.getOption3();
        this.O4 = question.getOption4();
        this.A = question.getAnswer();
        if (question.hasImage()) {
            this.PICURL = question.getImageUrl();
            this.TYPE = "QUESTION_PIC";
        } else {
            this.TYPE = "QUESTION";
        }
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String nAME) {
        NAME = nAME;
    }

    public String getQ() {
        return Q;
    }

    public void setQ(String q) {
        Q = q;
    }

    public String getO1() {
        return O1;
    }

    public void setO1(String o1) {
        O1 = o1;
    }

    public String getO2() {
        return O2;
    }

    public void setO2(String o2) {
        O2 = o2;
    }

    public String getO3() {
        return O3;
    }

    public void setO3(String o3) {
        O3 = o3;
    }

    public String getO4() {
        return O4;
    }

    public void setO4(String o4) {
        O4 = o4;
    }

    public int getA() {
        return A;
    }

    public void setA(int a) {
        A = a;
    }

    public String getPICURL() {
        return PICURL;
    }

    public void setPICURL(String pIC) {
        PICURL = pIC;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String tYPE) {
        TYPE = tYPE;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String iP) {
        IP = iP;
    }
}
