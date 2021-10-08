package com.ownwear.app.form;

import com.ownwear.app.model.HashTag;
import lombok.Data;

import java.math.BigInteger;

public interface IIndexHashTag {
    int getCount();
    long getHashtag_id();
    String getHashtagName();
}