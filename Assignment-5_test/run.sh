#!/bin/bash
ant;
ant make-jar;
# java -Xmx1g -jar jars/assembler.jar test_cases/3.asm a.out;
# diff test_cases/3.out a.out;
java -jar jars/simulator.jar src/configuration/config.xml stats.txt test_cases/descending.out;
