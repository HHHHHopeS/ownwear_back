package com.ownwear.app.form;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

@Data
public class IndexBrand {

        private long brandid;

        private int tagCount;

        private String brandName;

}
