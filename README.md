To compile, run:

   ant compile_orig

To run the test scripts on the original (hopefully non-faulty) implementation, use:

   ant test -Dprogram="original" -Dtest="Partitioning"

or

   ant test -Dprogram="original" -Dtest="Boundary"

To run a test script on the first mutant, use:

   ant test -Dprogram="mutant-1" -Dtest="Boundary"

To clean all class files, run:

   ant clean