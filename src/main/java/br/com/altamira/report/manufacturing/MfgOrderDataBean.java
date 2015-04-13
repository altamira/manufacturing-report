package br.com.altamira.report.manufacturing;

import java.util.List;

public class MfgOrderDataBean {

    private String date;
    private String sector;
    private String issuedBy;
    private String code;
    private String description;
    private String operation;
    private List<MfgOrderComponent> components;
    private List<MfgOrderMaterial> materials;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public List<MfgOrderComponent> getComponents() {
        return components;
    }

    public void setComponents(List<MfgOrderComponent> components) {
        this.components = components;
    }

    public List<MfgOrderMaterial> getMaterials() {
        return materials;
    }

    public void setMaterials(List<MfgOrderMaterial> materials) {
        this.materials = materials;
    }

}
