package wjy.morelove.bean;

import java.util.List;

import wjy.morelove.Jyson.JFName;


public class SubjectCardList {

    @JFName("data")
    List<SubjectCard> data;

    public List<SubjectCard> getData() {
        return data;
    }

    public void setData(List<SubjectCard> data) {
        this.data = data;
    }
}
