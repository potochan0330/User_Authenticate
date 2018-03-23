# User_Authenticate
(Java + Security) User Registration and Login function in location side, the username/password need to be encrypted/decrypted by publicKey/privateKey


Execution instructions
Create_user:
1.Enter username
2.Enter password
3.Enter Y/y if more user needed to be created, the system will keep running
4.Enter N/n if no more user needed to be created, the system will be also terminated
5.Enter any key except from (y/n/Y/N), the system will be also terminated

Authenticate_user:
1.Enter username
2.Enter password
3.Only when both of them are matched and the check string is matched, the system is display “Welcome to the system username!”  
4.Either of username or password is not match will display “Authentication fail.”
5.If password and username are matched, but the check string is not matched, the system will display “The text file is modified.”

Assumption
1.Every time admin create users, the system is terminate by enter (N/n) after create one user.
2.The password should contain both numbers and characters.


 
System Design:
First, in the text file, there will be three set of data in each line, the first set of data is the username. The second set of data is a hashed passwords, because the text file can be accessed by attacker, therefore the passwords needed to be hashed in order to hide the original words. The third set of data is the check string for checking whether the text file is modified or not.
After that, in the system, I used sha256 to hash the passwords. Because I need to ensure the attacker cannot get the passwords easily, and I don’t need to use an encryption method, because I don’t need to know what the original words, therefore the passwords should use hash method in order to prevent algorithm to crack the passwords. If the attacker need to crack the passwords he needs to try all possible words. 
Then, I use Hmac-SHA256 to create a signature. The aims of create signature is to prevent the attacker modified the text file and the successful get login. The Hmac-SHA256 take 2 input which are the username and hashed passwords. Taking username as an input is because is also used to prevent attacker modified the username.
Also, the system required the length of passwords longer than 6 in order to increase the number of possible combination of passwords to increase time used to crack the password.
