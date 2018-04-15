# i8085simulator

A simulator program for 8085 Microprocessor. 

# Motivation

This was a self initiated project developed by me during my engineering degree course to be able to practice assembly coding without the dependency on the hardware trainer kit. 

# Download

The executable jar can be downloaded [here](https://github.com/uniquetrij/i8085simulator/blob/master/target/i8085simulator-1.0.1-SNAPSHOT.jar)

# Dependencies
- Java 1.6+

# Run

```
java -jar i8085simulator-1.0.1-SNAPSHOT.jar
```

# Usage

1. Open the code editor by clicking on the `Assembly Editor` option on the top left of the simulator.
2. Write the 8085 assembly code into the editor. Note that the last line of the program should be `END` to mark the end of the code.
3. Click on `Assemble` option in the option bar of the editor to assemble the program. Check for any syntax errors that pops up below the editor. 
4. Once assembly is successful, click on the `Load` option to load the program into the memory. The respective memory addresses will be filled with the opcodes corresponding to the program.
5. If the program requires input from the registers, memory or i/o ports, double click on the respective register, memory address or i/o port. An input dialog will appear where the value can be set in hexadecimal, decimal, octal or binary.
6. To execute the program, click on the `Run` option in the editor option bar. Alternatively, you can click on `Step` option to step through each statement of the program and analyze the processing and output after each step. 