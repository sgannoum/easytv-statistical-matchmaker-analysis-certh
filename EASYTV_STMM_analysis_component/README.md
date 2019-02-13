# GPII/Cloud4all Statistical Matchmaker: Analysis Component

## Introduction

The analysis module is one of several components of the GPII/Cloud4all Statistical Matchmaker. 
The other components are the [runtime component](https://github.com/REMEXLabs/GPII-Statistical-Matchmaker) and 
the [training data](https://github.com/REMEXLabs/GPII-Statistical-Matchmaker-Data). 

How do these three components fit together? 
First, the analysis component analyses all the available preference sets (cf. the training data) and outputs
data structures that the runtime component can use in order to responde to matchmaking requests. 
Since the clustering algorithms used by the analysis component are computationally intensive, 
these algorithms would only run at certain regular intervals, e.g. once a day. 
Second, the runtime component uses the data generated by the analysis component to respond to matchmaking requests. 

The current training data will be phased out when the number of real GPII/Cloud4all becomes big enough to
use their preference sets for clustering. 

## Dependencies

* .NET Framework 4.5 (although the project also compiles when you set the target framework to .NET Framework 3.5)
* [Training data](https://github.com/REMEXLabs/GPII-Statistical-Matchmaker-Data)

## Getting the Analysis Component

Simply run the following command in a directory of your choice: 
```
git clone https://github.com/REMEXLabs/GPII-Statistical-Matchmaker-Analysis
```

## Compiling the Analysis Component

### Compiling on MS Windows
On Microsoft Windows, open the project in Microsoft Visual Studio Express or another IDE in the Visual Studio family
and press F7 ("Build Solution"). 

(You can probably also use [SharpDevelop](https://github.com/icsharpcode/SharpDevelop/wiki/Keyboard-Shortcuts#09) 
but we have not tested this.) 
(We have not found a way to compile the Analysis Component with [MonoDevelop](http://www.monodevelop.com/) 4.0/Xamarin,
which does not seem to support the .NET SDK 4.5.)

### Compiling on Linux
After installing the Mono Framework (see the [instructions for Fedora](http://www.mono-project.com/docs/getting-started/install/linux/#centos-fedora-and-derivatives)), you should *in theory* be able to compile Visual Basic code in a terminal using `vbnc`, Mono's Visual Basic compiler, or with [MonoDevelop](http://www.monodevelop.com/). However, this does not work yet.

## Running the Analysis Component

You need a collection of users preference sets in .ini format. See [training data repository](https://github.com/REMEXLabs/GPII-Statistical-Matchmaker-Data) for details. This description assumes that you used the official [training data repository](https://github.com/REMEXLabs/GPII-Statistical-Matchmaker-Data).
There are three important command line parameters you have to set for the analysis tool.
* `-p` should point to the folder storing the preferences. It will recursively take all .ini files as preferences.
* `-c` should point to the `config.ini` file.
* `-o` should point to the file (including its path) that represents the output of the analysis. 

For example:
```
StatisticalAnalysis.exe -p "C:\GPII-Statistical-Matchmaker-Data" -c "C:\GPII-Statistical-Matchmaker-Data\config.ini" -o "C:\GPII-Statistical-Matchmaker\lib\StatisticalMatchMakerData.js"
```

## Licence

The code of the GPII/Cloud4all Statistical Matchmaker's analysis component is available under the [BSD 3-Clause License](https://github.com/REMEXLabs/GPII-Statistical-Matchmaker-Analysis/blob/master/LICENSE.txt).

## Funding Acknowledgement

The research leading to these results has received funding from the European
Union's Seventh Framework Programme (FP7/2007-2013) under grant agreement No.289016
([Cloud4all](http://www.cloud4all.info/)).