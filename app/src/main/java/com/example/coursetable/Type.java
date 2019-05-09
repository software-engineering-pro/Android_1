package com.example.coursetable;

public class Type {
    private int typeCode;
    private String typeName;

    public Type(int typeCode, String typeName) {
        this.typeCode = typeCode;
        this.typeName = typeName;
    }

    public int getTypeCode() {
        return typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeCode(int typeCode) {
        this.typeCode = typeCode;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    
}
