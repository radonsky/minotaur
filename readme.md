##Minotaur
In Greek mythology, the Minotaur was an angry creature that dwelt in a Labyrinth - an maze-like construction. See [Wikipedia Minotaur entry](http://en.wikipedia.org/wiki/Minotaur) for more.

Minotaur is also a simple service that demonstrates use of Twilio API to reply to SMS messages. This service is meant to be deployed online, so that it is accessible and available for requests from Twilio servers.

This service provides a simple text based game. By texting your name to the Twilio number which uses this service to respond to text messages, you get instructions how to use the game. The goal of the game is simple - you are in a maze and you need to find your way out. You can move around by texting W, E, S, N to move West, East, South and North respectively. At each point you'll receive a message telling you in which directions you can go. When you find the exit you'll be congratulated and you can start again.

###Building and running
In order to build this service you have to have Maven installed. Once it is installed, you can run

	mvn package

That should create an executable jar file, which could be started on your machine by the following command

	java -jar target/minotaur.jar server config.yml
	
This service uses Dropwizard so for more information about how to configure the service, take a look at the [Dropwizard documentation](https://dropwizard.github.io/dropwizard/manual/).

###Deployment and configuration
You have to deploy this service online for it to be available to Twilio servers. You have many options how to deploy this Java based service, but the easiest way would be using [Heroku](http://www.heroku.com/), because Heroku Procfile is has been alredy included and therefore you can just push this service as your application. You can use the following [guide](https://devcenter.heroku.com/articles/getting-started-with-java) to learn more about how to deploy Java services on Heroku.

Once you have the service up and running, you have to log-in to your [Twilio account](https://www.twilio.com/user/account/phone-numbers/incoming) and configure one of your phone numbers to use the deployed service as the Messaging Request URL. This service exposes a `/sms` endpoint, so say you deployed your service as `http://mygreatservice.heroku.com/`, you just put `http://mygreatservice.heroku.com/sms` in your Twilio number's Messaging configuration.

###Contact
If you have any questions, please send me an email to my name at gmail.com.

###License

Copyright 2015 Marek Radonsky

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0) Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.