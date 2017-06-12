package it.polimi.tracechecking.common.model;

import java.util.HashMap;
import java.util.Map;

public class Attribute extends DIAElement {

	private String type;

	private Boolean isKey = false;

	private Boolean isQuasiIdentifier = false;

	private Boolean isSensitive = false;

	private Map<String, Object> generalizationPolicy = new HashMap<String, Object>();

	public Attribute() {

	}

	public Map<String, Object> getGeneralizationPolicy() {
		return generalizationPolicy;
	}

	public void setGeneralizationPolicy(Map<String, Object> generalizationPolicy) {
		this.generalizationPolicy = generalizationPolicy;
	}

	public String getAttributeType() {
		if (isKey) {
			return "IDENTIFYING_ATTRIBUTE";
		} else if (isQuasiIdentifier) {
			return "QUASI_IDENTIFYING_ATTRIBUTE";
		} else if (isSensitive) {
			return "SENSITIVE_ATTRIBUTE";
		} else {
			return "INSENSITIVE_ATTRIBUTE";
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getIsQuasiIdentifier() {
		return isQuasiIdentifier;
	}

	public void setIsQuasiIdentifier(Boolean isQuasiIdentifier) {
		this.isQuasiIdentifier = isQuasiIdentifier;
	}

    public Boolean getIsSensitive() {
        return isSensitive;
    }

	public void setIsSensitive(Boolean isSensitive) {
		this.isSensitive = isSensitive;
	}

	public Boolean getIsKey() {
		return isKey;
	}

	public void setIsKey(Boolean isKey) {
		this.isKey = isKey;
	}

}
