package org.opendatanode.plugins.extractor.executeshellscript;

import org.opendatanode.plugins.extractor.executeshellscript.ExecuteShellScriptConfig_V1;
import org.opendatanode.plugins.extractor.executeshellscript.ExecuteShellScript;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.SystemUtils;
import org.junit.Assert;
import org.junit.Test;

import cz.cuni.mff.xrg.odcs.dpu.test.TestEnvironment;
import eu.unifiedviews.dataunit.files.FilesDataUnit;
import eu.unifiedviews.dataunit.files.WritableFilesDataUnit;
import eu.unifiedviews.helpers.dataunit.files.FilesDataUnitUtils;
import eu.unifiedviews.helpers.dpu.test.config.ConfigurationBuilder;

public class ExecuteShellScriptTest {
    @Test
    public void test() throws Exception {
        ExecuteShellScriptConfig_V1 config = new ExecuteShellScriptConfig_V1();
        config.setConfiguration("Copy-of-");
        URL file = null;
        if (SystemUtils.IS_OS_WINDOWS) {
            file = Thread.currentThread().getContextClassLoader().getResource("pokus.bat");
        } else if (SystemUtils.IS_OS_UNIX) {
            file = Thread.currentThread().getContextClassLoader().getResource("pokus.sh");
        }
        File f = new File(file.toURI());
        config.setScriptName(f.getAbsolutePath());
        config.setOutputDir((new File(f.getParent(), "output")).getAbsolutePath());
        File outputDir = new File(config.getOutputDir());
        outputDir.mkdirs();

        // Prepare test environment.
        TestEnvironment environment = new TestEnvironment();
        WritableFilesDataUnit filesOutput = environment.createFilesOutput("filesOutput");
        // Prepare data unit.
        WritableFilesDataUnit filesInput = environment.createFilesInput("filesInput");
        filesInput.addExistingFile("t1", Thread.currentThread().getContextClassLoader().getResource("test1.txt").toString());
        filesInput.addExistingFile("t2", Thread.currentThread().getContextClassLoader().getResource("test2.txt").toString());
        filesInput.addExistingFile("t3", Thread.currentThread().getContextClassLoader().getResource("test3.txt").toString());

        // Prepare DPU.
        ExecuteShellScript dpu = new ExecuteShellScript();
        dpu.configure((new ConfigurationBuilder()).setDpuConfiguration(config).toString());

        try {
            // Run.
            environment.run(dpu);

            FilesDataUnit.Iteration iteration = filesInput.getIteration();
            while (iteration.hasNext()) {
                File inpF = FilesDataUnitUtils.asFile(iteration.next());
                File outF = new File(config.getOutputDir(), config.getConfiguration() + inpF.getName());
                Assert.assertTrue(outF.exists());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // Release resources.
            environment.release();
        }
    }

    @Test
    public void testNoInputFiles() throws Exception {
        ExecuteShellScriptConfig_V1 config = new ExecuteShellScriptConfig_V1();
        config.setConfiguration("adam eva had");
        URL file = null;
        if (SystemUtils.IS_OS_WINDOWS) {
            file = Thread.currentThread().getContextClassLoader().getResource("pokusNoInput.bat");
        } else if (SystemUtils.IS_OS_UNIX) {
            file = Thread.currentThread().getContextClassLoader().getResource("pokusNoInput.sh");
        }
        File f = new File(file.toURI());
        config.setScriptName(f.getAbsolutePath());
        config.setOutputDir((new File(f.getParent(), "output")).getAbsolutePath());
        File outputDir = new File(config.getOutputDir());
        outputDir.mkdirs();

        // Prepare test environment.
        TestEnvironment environment = new TestEnvironment();
        WritableFilesDataUnit filesOutput = environment.createFilesOutput("filesOutput");
        // Prepare DPU.
        ExecuteShellScript dpu = new ExecuteShellScript();
        dpu.configure((new ConfigurationBuilder()).setDpuConfiguration(config).toString());

        try {
            // Run.
            environment.run(dpu);

            String[] items = config.getConfiguration().split(" ");
            List<String> itemList = Arrays.asList(items);
            for (String newFile : itemList) {
                File outF = new File(config.getOutputDir(), newFile);
                Assert.assertTrue(outF.exists());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // Release resources.
            environment.release();
        }
    }
}
