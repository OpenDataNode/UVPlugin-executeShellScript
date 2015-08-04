# E-ExecuteShellScript #
----------

###General###

|                              |                                                                             |
|------------------------------|-----------------------------------------------------------------------------|
|**Name:**                     |E-ExecuteShellScript                                                               |
|**Description:**              |Executes shell script with provided configuration |
|**Status:**                   |       |
|                              |                                                                             |
|**DPU class name:**           |ExecuteShellScript                                                                | 
|**Configuration class name:** |ExecuteShellScriptConfig_V1                             |
|**Dialogue class name:**      |ExecuteShellScriptVaadinDialog                      |

***

###Configuration parameters###

|Parameter                                       |Description                                                              |                                                        
|------------------------------------------------|-------------------------------------------------------------------------|
|Script name                                     |The name of script to execute                                            |
|Configuration                                   |text which will be used as configuration for the script                  |

***

### Inputs and outputs ###

|Name         |Type           |DataUnit     |Description             |
|-------------|---------------|-------------|------------------------|
|filesInut    |i              |FilesDataUnit|Optional DataUnit with script input files. |
|filesOutput  |o              |FilesDataUnit|DataUnit which outputs all script output files. |

***

### Version history ###

|Version          |Release notes               |
|-----------------|----------------------------|
|1.0.0-SNAPSHOT            |Initial release.|                            


***

### Developer's notes ###

|Author           |Notes                           |
|-----------------|--------------------------------|
|N/A              |N/A                             | 
