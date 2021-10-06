package org.thshsh.spring;

import java.util.HashMap;
import java.util.Map;

public class DataSourceProperties {

    private Map<String, String> props = new HashMap<>();
    
    private String[] packages = new String[] {};

    private String name;
    
    public void setProps(Map<String, String> props) {
		this.props = props;
	}

	public void setPackages(String[] packages) {
		this.packages = packages;
	}

	public Map<String, String> getProps() {
        return props;
    }

	public String[] getPackages() {
		return packages;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
    

}