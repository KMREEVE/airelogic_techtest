Kane Reeve | Aire logic tech test

For this project I chose to use Java as it is the language I have used the most in my recent university assignments, namely in creating programs with similar logic to what was requested in the brief.

For creation of the program I started with a process of reading, re-reading and placing the brief within comments in order to constantly reference. This helped me ensure that what I was doing was correct to the project brief.

The program works through two Java classes; 'Main' and 'Patient'. 'Main' is the driver of the program, while 'Patient' acts as the programs brain. 'Main' will initialize instances of 'Patient' based on the examples provided in the brief, and will call their functions.

Within the 'Patient' class, I broke down the individual tasks into their own functions each of which return integers (GetAirScore, GetConsciousnessScore, etc) which are then called by the classes primary 'CalculateMediScore' function, which gathers the integers returned by the 'Get' functions and adds them together, therefore calculating the final MediScore. Breaking the tasks down into functions like this allowed for far easier debugging, allowing me to easily modify individual elements without needing to make large changes to the code as a whole. 

Once I had completed the process of calculating Medi Scores, I moved on to the bonus tasks, firstly the alerts for trends in the Medi Score. I completed this through using linked lists that saves the mediscores of the last 24 hours using 'LocalDateTime'. To debug this I simulated a delay using a try loop in 'Main'. Then for the second bonus task I adapted what I had created for 'GetSpO2Score' and 'GetTempScore' with an additional enum check to determine whether the patient had eaten within the last 2 hours, or was fasting, and adapted the results accordingly. 
