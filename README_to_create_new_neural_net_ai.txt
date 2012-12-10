To create a new neural net AI:

1. extend AbstractNeuralNet, implement required functions
2. in GameOptionsPanel, add a new 'else if' clause for your new AI with the parameters to be passed to the create AI function [see TODO]
3. in Player, add your new AI name to the static array at the top of the class, plus add an else if clause in the create AI function to handle call from (2) [see 2 TODO(s)]
4. to make log statements, call logOut.write("string to log"); logOut.flush(); [logOut is a FileWriter defined in AbstractNeuralNet, net.getNetInfo() is a good NeuralNet logging utility]