package com.develop.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Data
@Document
public class User {
    @Id
    private String id;
    private ArrayList<Long> dictionaryId;
}
