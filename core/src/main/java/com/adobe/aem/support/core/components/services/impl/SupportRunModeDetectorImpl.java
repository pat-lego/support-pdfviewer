package com.adobe.aem.support.core.components.services.impl;

import java.util.Optional;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import com.adobe.aem.support.core.components.services.SupportRunModeDetector;

@Component(service = SupportRunModeDetector.class)
@Designate(ocd = SupportRunModeDetectorImpl.SupportRunModeDetectorConfig.class)
public class SupportRunModeDetectorImpl implements SupportRunModeDetector {

    @ObjectClassDefinition(name = "RunMode Detector for AEMaaCS")
    public @interface SupportRunModeDetectorConfig {

        @AttributeDefinition(name = "RunMode name")
        public String runMode() default "";
        
    }

    private SupportRunModeDetectorConfig supportRunModeDetectorConfig;

    @Activate
    public SupportRunModeDetectorImpl(SupportRunModeDetectorConfig supportRunModeDetectorConfig) {
        this.supportRunModeDetectorConfig = supportRunModeDetectorConfig;
    } 

    @Override
    public String getRunMode() {
        return Optional.ofNullable(this.supportRunModeDetectorConfig.runMode()).orElse(null);
    }
    
}
