package com.adobe.aem.support.core.components.services;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(label = "Document Cloud Viewer - Configuration", description = "Document Cloud Viewer Configuration")
public @interface PdfViewerCaConfig {
    @Property(label = "Client ID", description = "Document Cloud Viewer API Client ID (Required)")
    String clientId() default "";

    @Property(label = "Author Client ID", description = "Document Cloud Viewer API Client ID (Required)")
    String authorClientId() default "";

    @Property(label = "Publish Client ID", description = "Document Cloud Viewer API Client ID (Required)")
    String publishClientId() default "";

    @Property(label = "Preview Client ID", description = "Document Cloud Viewer API Client ID (Required)")
    String previewClientId() default "";

    @Property(label = "Report Suite ID", description = "Adobe Analytics Report Suite ID (Optional)")
    String reportSuiteId() default "";

}
