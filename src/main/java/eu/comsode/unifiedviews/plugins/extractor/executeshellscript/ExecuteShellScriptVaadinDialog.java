package eu.comsode.unifiedviews.plugins.extractor.executeshellscript;

import eu.unifiedviews.dpu.config.DPUConfigException;
import eu.unifiedviews.helpers.dpu.vaadin.dialog.AbstractDialog;

/**
 * Vaadin configuration dialog .
 */
public class ExecuteShellScriptVaadinDialog extends AbstractDialog<ExecuteShellScriptConfig_V1> {

    public ExecuteShellScriptVaadinDialog() {
        super(ExecuteShellScript.class);
    }

    @Override
    public void setConfiguration(ExecuteShellScriptConfig_V1 c) throws DPUConfigException {

    }

    @Override
    public ExecuteShellScriptConfig_V1 getConfiguration() throws DPUConfigException {
        final ExecuteShellScriptConfig_V1 c = new ExecuteShellScriptConfig_V1();

        return c;
    }

    @Override
    public void buildDialogLayout() {
    }

}
