package com.ownwear.app.form;

import com.ownwear.app.model.HashTag;
import lombok.Data;

import java.util.List;

@Data
public class SearchForm {

    private List<UserForm> userForms;

    private List<BrandForm> brandForms;

    private List<HashTag> hashTagForms;
}
