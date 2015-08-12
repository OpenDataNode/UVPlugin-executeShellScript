package eu.comsode.unifiedviews.plugins.extractor.executeshellscript;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.unifiedviews.dataunit.DataUnit;
import eu.unifiedviews.dataunit.DataUnitException;
import eu.unifiedviews.dataunit.files.FilesDataUnit;
import eu.unifiedviews.dataunit.files.WritableFilesDataUnit;
import eu.unifiedviews.dpu.DPU;
import eu.unifiedviews.dpu.DPUException;
import eu.unifiedviews.helpers.dataunit.files.FilesDataUnitUtils;
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

    @DataUnit.AsInput(name = "filesInput", optional = true)
    public FilesDataUnit filesInput;

    @DataUnit.AsOutput(name = "filesOutput")
    public WritableFilesDataUnit filesOutput;

    private List<File> inputFilesToProcess;

//    private String inputFilesDir;

    public ExecuteShellScript() {
        super(ExecuteShellScriptVaadinDialog.class, ConfigHistory.noHistory(ExecuteShellScriptConfig_V1.class));
    }

    @Override
    protected void innerExecute() throws DPUException {
        try {
            File inpFilesList = null;
            if (filesInput != null) {
                inpFilesList = prepareInputFiles();
            }
            LOG.debug(String.format("Script name : %s", config.getScriptName()));
            LOG.debug(String.format("Configuration : %s", config.getConfiguration()));
            LOG.debug(String.format("Output directory : %s", config.getOutputDir()));
            String confFilePath = writeConfiguration().getAbsolutePath();

            if (!(new File(config.getScriptName())).exists()) {
                LOG.error(String.format("Script %s doesn't exist!", config.getScriptName()));
                throw ContextUtils.dpuException(ctx, "ExecuteShellScript.error.scriptDoesntExist");
            }
            File outputDir = new File(config.getOutputDir());
            if (!outputDir.exists()) {
                if (!outputDir.mkdirs()) {
                    LOG.error(String.format("Can't create output directory: %s", config.getOutputDir()));
                    throw ContextUtils.dpuException(ctx, "ExecuteShellScript.error.cantCreateOutDir");
                }
            }
            CommandLine cmdLine = new CommandLine(config.getScriptName());
            cmdLine.addArgument(confFilePath);
//            cmdLine.addArgument(inputFilesDir);
            if (inpFilesList != null) {
                cmdLine.addArgument(inpFilesList.getAbsolutePath());
            }
            cmdLine.addArgument(config.getOutputDir());

            String commandLine = cmdLine.toString().substring(1, cmdLine.toString().length() - 1);
            commandLine = commandLine.replace(",", "");
            LOG.debug(String.format("Executing script: %s", commandLine));

            DefaultExecutor executor = new DefaultExecutor();
            executor.setWorkingDirectory(new File(URI.create(filesOutput.getBaseFileURIString())));
            int result = executor.execute(cmdLine);
            if (result == 0) {
//                for (File f : inputFilesToProcess) {
//                    f.delete();
//                }
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
        } catch (DataUnitException | IOException ex) {
            throw ContextUtils.dpuException(ctx, ex, "ExecuteShellScript.execute.exception");
        }

    }

    private File writeConfiguration() throws IOException, DataUnitException {
        File outputFile = File.createTempFile("script_config", ".conf", new File(URI.create(filesOutput.getBaseFileURIString())));
        FileWriter writer = new FileWriter(outputFile, true);
        writer.write(config.getConfiguration());
        writer.close();
        return outputFile;
    }

//    private void prepareInputFiles() {
//        inputFilesToProcess = new ArrayList<File>();
//        try {
//            final List<FilesDataUnit.Entry> files = FaultToleranceUtils.getEntries(faultTolerance, filesInput, FilesDataUnit.Entry.class);
//            File inputDir = new File(URI.create(filesOutput.getBaseFileURIString() + "input"));
//            inputDir.mkdir();
//            inputFilesDir = inputDir.getAbsolutePath();
//            for (final FilesDataUnit.Entry entry : files) {
//                File fileToPrepare = FilesDataUnitUtils.asFile(entry);
//                File preparedFile = new File(inputDir, fileToPrepare.getName());
//                Files.copy(fileToPrepare, preparedFile);
//                inputFilesToProcess.add(preparedFile);
//            }
//        } catch (DataUnitException | DPUException | IOException ex) {
//            LOG.error("Error preparing input files.", ex);
//        }
//
//    }
    private File prepareInputFiles() {
        inputFilesToProcess = new ArrayList<File>();
        File filesToProcessList = null;
        FileWriter writer = null;
        try {
            filesToProcessList = new File(new File(URI.create(filesOutput.getBaseFileURIString())), "filesList.txt");
            writer = new FileWriter(filesToProcessList, true);
            FilesDataUnit.Iteration iteration = filesInput.getIteration();
            while (iteration.hasNext()) {
                File inpF = FilesDataUnitUtils.asFile(iteration.next());
                writer.write(inpF.getAbsolutePath() + "\n");
            }
        } catch (DataUnitException | IOException ex) {
            LOG.error("Error preparing input files.", ex);
        } finally {
            IOUtils.closeQuietly(writer);
        }
        return filesToProcessList;
    }
}
