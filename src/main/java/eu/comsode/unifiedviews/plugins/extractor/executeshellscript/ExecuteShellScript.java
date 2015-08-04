package eu.comsode.unifiedviews.plugins.extractor.executeshellscript;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.unifiedviews.dataunit.DataUnit;
import eu.unifiedviews.dataunit.DataUnitException;
import eu.unifiedviews.dataunit.files.WritableFilesDataUnit;
import eu.unifiedviews.dpu.DPU;
import eu.unifiedviews.dpu.DPUException;
import eu.unifiedviews.helpers.dataunit.resource.Resource;
import eu.unifiedviews.helpers.dataunit.resource.ResourceHelpers;
import eu.unifiedviews.helpers.dataunit.virtualpath.VirtualPathHelpers;
import eu.unifiedviews.helpers.dpu.config.ConfigHistory;
import eu.unifiedviews.helpers.dpu.context.ContextUtils;
import eu.unifiedviews.helpers.dpu.exec.AbstractDpu;

/**
 * Main data processing unit class.
 */
@DPU.AsExtractor
public class ExecuteShellScript extends AbstractDpu<ExecuteShellScriptConfig_V1> {

    private static final Logger LOG = LoggerFactory.getLogger(ExecuteShellScript.class);

    @DataUnit.AsOutput(name = "filesInput")
    public WritableFilesDataUnit filesInput;

    @DataUnit.AsOutput(name = "filesOutput")
    public WritableFilesDataUnit filesOutput;

    public ExecuteShellScript() {
        super(ExecuteShellScriptVaadinDialog.class, ConfigHistory.noHistory(ExecuteShellScriptConfig_V1.class));
    }

    @Override
    protected void innerExecute() throws DPUException {
        try {
            String confFilePath = writeConfiguration().getAbsolutePath();
            List<String> scriptArgs;
            scriptArgs = java.util.Arrays.asList("sh", "-c", "./" + config.getScriptName() + " " + confFilePath + " " + config.getOutputDir());
            LOG.debug("Script to execute: " + scriptArgs);
            ProcessBuilder pbExecScript = new ProcessBuilder(scriptArgs);
            Process p = pbExecScript.start();

            int result = p.waitFor();

            if (result == 0) {
                File outputFolder = new File(config.getOutputDir());
                for (File fileEntry : outputFolder.listFiles()) {
                    filesOutput.addExistingFile(fileEntry.getName(), fileEntry.toURI().toASCIIString());
                    VirtualPathHelpers.setVirtualPath(filesOutput, fileEntry.getName(), fileEntry.getName());
                    Resource resource = ResourceHelpers.getResource(filesOutput, fileEntry.getName());
                    Date now = new Date();
                    resource.setCreated(now);
                    resource.setLast_modified(now);
                    resource.setSize(fileEntry.length());

                    ResourceHelpers.setResource(filesOutput, fileEntry.getName(), resource);

                }
            } else {
                LOG.error("Script execution error.");
            }
        } catch (DataUnitException | IOException | InterruptedException ex) {
            throw ContextUtils.dpuException(ctx, ex, "ExecuteShellScript.execute.exception");
        }

    }

    private File writeConfiguration() throws IOException, DataUnitException {
        File outputFile = File.createTempFile("____", ".conf", new File(URI.create(filesOutput.getBaseFileURIString())));
        FileWriter writer = new FileWriter(outputFile, true);
        writer.write(config.getConfiguration());
        writer.close();
        return outputFile;
    }
}
