package org.opendatanode.plugins.extractor.executeshellscript;

/**
 */
public class ExecuteShellScriptConfig_V1 {

    private String scriptName = "";

    private String configuration = "";

    private String outputDir = "";

    public ExecuteShellScriptConfig_V1() {

    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

}
