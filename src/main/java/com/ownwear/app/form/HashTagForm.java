package com.ownwear.app.form;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ownwear.app.model.PostHashTag;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
public class HashTagForm {

    private Long hashtag_id;

    private String hashtagname;

}
