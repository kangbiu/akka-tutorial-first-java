Welcome to Strikingly Interview Test


# INTRODUCTION

This test is based on the famous Hanger Game. You will have to write a program to guess words sent from our system.

## REQUIREMENTS:
1. Write a prorgram according to the followings specifications
2. Submit your program to github and send us the link
3. Include intructions on how to run the program in README.MD file on your github
4. ANY programming langauage - You may use ANY computer programming language that you are familiar with. It could be JAVA, PHP, RUBY, JAVASCRIPT, PYTHON, C, C++, C#, bash...... ( you name it). 

## OUR EXPECTATIONS:
Through this programming test, you should be able to demostrate:
1. Good understanding on the programming language you are good at.
2. Good programming practices
3. Apply suitable algorithms to slove problems
4. Creativity!

## PROGRAMMING FLOW AND SEPECIFICATION
The overall workflow is in 5 stages, namely "Initiate Game", "Give Me A Word", "Make A Guess", "Get Test Results" & "Submit Test Results"

1. Initite Game
  - you send a request to the system to register yourself
	- The URL of the system could be found in your invitation email
	- get a secret key from the system and include that ket in the following communications
	- Request & Response
		Request:
			{
				"action" : "initiateGame"
				"userId" : "test@gmail.com"
			}

			Explanation: 
			1. "action" key should have the value for "initiateGame"
			2. "userId" key should be your email address, shown on your invitation email. please contact joyce@strikingly.com if you find your email address is invalid in this programming test.

		Response:
			{
				"message":"Welcome To Strikingly Interview Test!",
				"userId":"hello@strikingly.com",
				"secret":"2V2GUB00590H33DIEMH24V9V03PU8F",
				"status":200,
				"data":
				{
					"numberOflWordsToGuess":80,
					"numberOfGuessAllowedForEachWord":10
				}
			}

			Explanation: 
			1. "message" always tells you a human readable message. The message in this response is a welcome message.
			2. "userId" shows your email address
			3. "secret" gives you a secret for the follwing communication. This secret is different in your each trial
			4. "status" tells you the status of your request. It follows the standard HTTP Status Code. 200 - OK, 400 - Bad Request, 401 - Unauthenticated
			5. "data" contains some useful data for your reference. In the "initiateGame" response, 
				"numberOflWordsToGuess" - tells you how many words you will have to guess to finish the game.
				"numberOfGuessAllowedForEachWord" - tells you how many INCORRECT guess you may have for each word.

2. Give Me A Word
	- after gettingt the secret key, you can ask the system to give you a word
	- remember to include BOTH your "userId" and "secret", and put the correct "action" as "nextWord"
	- in the response you will have "word" in the JSON. the "*" indicates the characters that you have to guess in a word. The number "*" in the word key tells you the number of charaters in a word.
	- Request & Response
		Request:
			{
				"userId":"hello@strikingly.com",
				"action":"nextWord",
				"secret":"2V2GUB00590H33DIEMH24V9V03PU8F"
			}

			Explanation:
			1. "userId" should be your email address
			2. "action" should be "nextWord"
			3. "secret" should be the secret in the reponse from "initateGame"

		Response: 
			{		
				"word":"*****",
				"userId":"hello@strikingly.com",
				"secret":"2V2GUB00590H33DIEMH24V9V03PU8F",
				"status":200,
				"data":
					{
						"numberOfWordsTried":1,
						"numberOfGuessAllowedForThisWord":10
					}
			}
			
			Explanation:
			1. "word" contains the "*". A "*" sign indicate a missing character in a word
			2. "data" tells you the in  




3. Guss Word

4. Get Test Results
	Request: 

	Response:
	{
		"message":
		{
			"numberOfWordsTried":"4",
			"numberOfCorrectWord":"1",
			"numberOfWrongGuess":"8"
		},
		"userId":"hello@strikingly.com",
		"secret":"1ISR97UED6V6XBT9W7PZWWLG99MP9Z",
		"status":200
	}

## TECHNICAL HTTP REQUIREMTNTS:
- Request URL: <Please found it in your email address>

- HTTP Request
	HEADER
		Content-Type: application/json
		Method : post
	Request Body
		<should be in JSON format, see the following as an example>

# SUBMISSION GUIDELINES:
1. Please send your github userid to Joyce (joyce@strikingly.com)
2. You will be then granted access to a strikingly private repository
3. You commit your code to that specified repository
4. You MUST include a README.MD file which shows the instruction to run your program and the response json when you submit you're test results. (read "Submit Test Results" section in PROGRAMMING FLOW AND SEPECIFICATION) 
5. Feel free to commit and update your code base to github. We evaluate your program based on your latest commit after you notify us by email that you have done your program. 
6. Your access to the private repository will be REMOVED after you send us the email 

## TIPS:
use chrome and extension to simulate

## Q&A
1. Can I Skip A Word?
	yes! send another "Give Me A Word" request, i.e. "action":"nextWord"

2. Can you submit mulitple results?
	Yes and No! you submit your test results as many times as you want. BUT we ONLY store your LATEST submission.

3. Can I always inititate a new game?
	Yes! you can always initiate a new game and play the game again and again. BUT always remember to submit your results if you are happy with your performance on a certain game.


# QUESTIONS?
If you have any questions, please write to joyce@strikingly.com. 
In case you want to make queries on any unexpected system error, please send us your HTTP request body and response (if any).

HAVE FUN!

