package com.radanlievristo.arnoldvoicechangerapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpeakRequestModel {

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("speaker")
    @Expose
    private String speaker;

    public SpeakRequestModel(String text){
        this.text = text;
        this.speaker = "arnold-schwarzenegger";
    }

}
