package it.polimi.cassandra.utils.common.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Dataset extends DIAElement {

    private List<Attribute> attributes;

    private Integer replicationFactor = 1;
    
    private String providedBy;
    
    public String getProvidedBy() {
        return providedBy;
    }

    public void setProvidedBy(String providedBy) {
        this.providedBy = providedBy;
    }

    public Dataset() {

    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public Integer getReplicationFactor() {
        return replicationFactor;
    }

    public void setReplicationFactor(Integer replicationFactor) {
        this.replicationFactor = replicationFactor;
    }

    public List<String> getAttributeNames() {
        Iterator<Attribute> iter = attributes.iterator();
        List<String> names = new ArrayList<String>();

        while (iter.hasNext()) {
            names.add(iter.next().getId());
        }

        return names;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }
}
