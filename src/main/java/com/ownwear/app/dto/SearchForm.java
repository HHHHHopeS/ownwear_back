package com.ownwear.app.dto;

import lombok.Data;

import java.util.List;

@Data
public class SearchForm {

    private List<UserForm> userForms;

    private List<BrandForm> brandForms;

    private List<HashTagForm> hashTagForms;
}
