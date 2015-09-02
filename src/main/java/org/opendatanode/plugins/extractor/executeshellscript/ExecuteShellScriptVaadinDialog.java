package org.opendatanode.plugins.extractor.executeshellscript;

import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import eu.unifiedviews.dpu.config.DPUConfigException;
import eu.unifiedviews.helpers.dpu.vaadin.dialog.AbstractDialog;

/**
 * Vaadin configuration dialog .
 */
public class ExecuteShellScriptVaadinDialog extends AbstractDialog<ExecuteShellScriptConfig_V1> {

    private TextField txtScriptName;

    private TextArea txtConfiguration;

    private TextField txtOutputDir;

    public ExecuteShellScriptVaadinDialog() {
        super(ExecuteShellScript.class);
    }

    @Override
    public void setConfiguration(ExecuteShellScriptConfig_V1 c) throws DPUConfigException {
        txtScriptName.setValue(c.getScriptName());
        txtConfiguration.setValue(c.getConfiguration());
        txtOutputDir.setValue(c.getOutputDir());
    }

    @Override
    public ExecuteShellScriptConfig_V1 getConfiguration() throws DPUConfigException {
        if (txtScriptName.getValue() == null || txtScriptName.getValue().trim() == "") {
            throw new DPUConfigException(ctx.tr("ExecuteShellScript.dialog.error.scriptNameEmpty"));
        }
        if (txtOutputDir.getValue() == null || txtOutputDir.getValue().trim() == "") {
            throw new DPUConfigException(ctx.tr("ExecuteShellScript.dialog.error.outputDirEmpty"));
        }
        final ExecuteShellScriptConfig_V1 c = new ExecuteShellScriptConfig_V1();

        c.setScriptName(txtScriptName.getValue());
        c.setConfiguration(txtConfiguration.getValue());
        c.setOutputDir(txtOutputDir.getValue());
        return c;
    }

    @Override
    public void buildDialogLayout() {
        final VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);

        txtScriptName = new TextField(ctx.tr("ExecuteShellScript.dialog.scriptName"));
        txtScriptName.setWidth("100%");
        txtScriptName.setRequired(true);
        mainLayout.addComponent(txtScriptName);
        mainLayout.setExpandRatio(txtScriptName, 0);

        txtConfiguration = new TextArea(ctx.tr("ExecuteShellScript.dialog.configuration"));
        txtConfiguration.setSizeFull();
        mainLayout.addComponent(txtConfiguration);
        mainLayout.setExpandRatio(txtConfiguration, 1.0f);

        txtOutputDir = new TextField(ctx.tr("ExecuteShellScript.dialog.outputDir"));
        txtOutputDir.setWidth("100%");
        txtOutputDir.setRequired(true);
        mainLayout.addComponent(txtOutputDir);
        mainLayout.setExpandRatio(txtOutputDir, 0);

        setCompositionRoot(mainLayout);
    }

}
