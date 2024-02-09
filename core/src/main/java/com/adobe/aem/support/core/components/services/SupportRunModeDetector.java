package com.adobe.aem.support.core.components.services;

public interface SupportRunModeDetector {
    
    public String AUTHOR = "author";
    public String PUBLISH = "publish";
    public String PREVIEW = "preview";
    
    public String getRunMode();
}
