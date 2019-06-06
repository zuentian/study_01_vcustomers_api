package com.example.zuer02.entity;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AddressNode {

    private String value;
    private String label;
    private List<AddressNode> children;
}
