package com.adobe.aem.support.core.components.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;

import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.support.core.components.services.PdfViewerCaConfig;
import com.adobe.aem.support.core.components.services.SupportRunModeDetector;
import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.PdfViewer;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { SupportPDFViewer.class,
        ComponentExporter.class }, resourceType = { SupportPDFViewer.RESOURCE_TYPE })
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class SupportPDFViewer extends AbstractComponentImpl implements PdfViewer {

    protected static final String RESOURCE_TYPE = "core/wcm/components/pdfviewer/v1/pdfviewer";
    protected static final String FIELD_EMBED_MODE = "embedMode";

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    private String documentPath;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    private String type;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    private String defaultViewMode;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    private boolean borderless;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    private boolean showAnnotationTools;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    private boolean showFullScreen;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    private boolean showLeftHandPanel;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    private boolean showDownloadPdf;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    private boolean showPrintPdf;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    private boolean showPageControls;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    private boolean dockPageControls;

    @ScriptVariable
    private Resource resource;

    @Self
    private SlingHttpServletRequest slingHttpServletRequest;

    private PdfViewerCaConfig caConfig;

    @Inject
    private SupportRunModeDetector supportRunModeDetector;

    @PostConstruct
    protected void initModel() {
        ConfigurationBuilder cb = resource.adaptTo(ConfigurationBuilder.class);
        if (cb != null) {
            caConfig = cb.as(PdfViewerCaConfig.class);
        }
    }

    @Override
    public String getClientId() {
        String serverName = slingHttpServletRequest.getServerName();
        if (logger.isDebugEnabled()) {
            logger.debug("The server name is {}", serverName);
        }

        if (!serverName.contains(SupportRunModeDetector.PREVIEW.toLowerCase())
                && !serverName.contains(SupportRunModeDetector.PUBLISH.toLowerCase())
                && !serverName.contains(SupportRunModeDetector.AUTHOR.toLowerCase())) {
            logger.info("Defaulting to the default clientId for the PDF Viewer");
            return caConfig.clientId();
        } else {
            if (serverName.contains(SupportRunModeDetector.AUTHOR.toLowerCase())) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Using Author Client ID since in Author Mode");
                }
                return caConfig.authorClientId();
            }
            if (serverName.contains(SupportRunModeDetector.PUBLISH.toLowerCase())) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Using Publish Client ID since in Publish Mode");
                }
                return caConfig.publishClientId();
            }
            if (serverName.contains(SupportRunModeDetector.PREVIEW.toLowerCase())) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Using Publish Client ID since in Publish Mode");
                }
                return caConfig.previewClientId();
            }
        }

        logger.info("Defaulting to the default clientId for the PDF Viewer");
        return caConfig.clientId();
    }

    @Override
    public String getReportSuiteId() {
        return caConfig.reportSuiteId();
    }

    @Override
    @Nullable
    public String getDocumentPath() {
        return documentPath;
    }

    @Override
    public String getDocumentFileName() {
        if (this.documentPath != null) {
            return StringUtils.substringAfterLast(this.documentPath, "/");
        }
        return null;
    }

    @Override
    @Nullable
    public String getType() {
        return type;
    }

    @Override
    @Nullable
    public String getDefaultViewMode() {
        return defaultViewMode;
    }

    @Override
    public boolean isBorderless() {
        return borderless;
    }

    @Override
    public boolean isShowAnnotationTools() {
        return showAnnotationTools;
    }

    @Override
    public boolean isShowFullScreen() {
        return showFullScreen;
    }

    @Override
    public boolean isShowLeftHandPanel() {
        return showLeftHandPanel;
    }

    @Override
    public boolean isShowDownloadPdf() {
        return showDownloadPdf;
    }

    @Override
    public boolean isShowPrintPdf() {
        return showPrintPdf;
    }

    @Override
    public boolean isShowPageControls() {
        return showPageControls;
    }

    @Override
    public boolean isDockPageControls() {
        return dockPageControls;
    }

    @Override
    public String getViewerConfigJson() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add(FIELD_EMBED_MODE, type);

        if (StringUtils.equals(type, FULL_WINDOW)) {
            jsonObjectBuilder.add(PN_DEFAULT_VIEW_MODE, defaultViewMode);
            jsonObjectBuilder.add(PN_SHOW_ANNOTATION_TOOLS, showAnnotationTools);
            jsonObjectBuilder.add(PN_SHOW_LEFT_HAND_PANEL, showLeftHandPanel);
        }

        if (StringUtils.equals(type, SIZED_CONTAINER)) {
            jsonObjectBuilder.add(PN_SHOW_FULL_SCREEN, showFullScreen);
        }

        if (StringUtils.equals(type, FULL_WINDOW) || StringUtils.equals(type, SIZED_CONTAINER)) {
            jsonObjectBuilder.add(PN_SHOW_PAGE_CONTROLS, showPageControls);
            jsonObjectBuilder.add(PN_DOCK_PAGE_CONTROLS, dockPageControls);
        }

        jsonObjectBuilder.add(PN_SHOW_DOWNLOAD_PDF, showDownloadPdf);
        jsonObjectBuilder.add(PN_SHOW_PRINT_PDF, showPrintPdf);

        return jsonObjectBuilder.build().toString();
    }

    @Override
    public String getContainerClass() {
        if (!StringUtils.isEmpty(type)) {
            if (type.equals(FULL_WINDOW) && borderless) {
                return CSS_BORDERLESS;
            } else if (type.equals(SIZED_CONTAINER)) {
                return CSS_SIZED_CONTAINER;
            } else if (type.equals(IN_LINE)) {
                return CSS_IN_LINE;
            }
        }
        return CSS_FULL_WINDOW;
    }

    @NotNull
    @Override
    public String getExportedType() {
        return resource.getResourceType();
    }
}
