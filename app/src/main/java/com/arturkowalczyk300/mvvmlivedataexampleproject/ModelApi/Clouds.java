
package com.arturkowalczyk300.mvvmlivedataexampleproject.ModelApi;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused") //NON-NLS
public class Clouds {

    @Expose
    private Long all;

    public Long getAll() {
        return all;
    }

    public void setAll(Long all) {
        this.all = all;
    }

}
