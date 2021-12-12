# pets-gateway-layer

This app is a simple implementation of Spring Cloud Gateway to route calls from SPAs to microservices.

This is created/implemented to overcome CORS issue with a React based SPA. Microservices might also route their request via the gateway as required

The gateway also provides a layer of authentication by validating JWT tokens in the request.

To run the app, we need to supply the following environment variables:
* Active Profile
    * spring.profiles.active (development, docker, production)
* PETS Service Security Details:
    * BASIC_AUTH_USR_PETSSERVICE (auth username of pets-service)
    * BASIC_AUTH_PWD_PETSSERVICE (auth password of pets-service)
    * BASIC_AUTH_USR_PETSDATABASE (auth username of pets-database)
    * BASIC_AUTH_PWD_PETSDATABASE (auth password of pets-database)
* The final run command looks like this:
    * java -jar -D"spring.profiles.active=development" -DBASIC_AUTH_USR_PETSSERVICE=some_username -DBASIC_AUTH_PWD_PETSSERVICE=some_password -DBASIC_AUTH_USR_PETSDATABASE=another_username -DBASIC_AUTH_PWD_DATABASE=another_password JARFILE.jar

This app is one of the five apps that form the PETS (Personal Expenses Tracking System) application:
* https://github.com/bibekaryal86/pets-database-layer
* https://github.com/bibekaryal86/pets-service-layer
* https://github.com/bibekaryal86/pets-authenticate-layer
* https://github.com/bibekaryal86/pets-gateway-layer (this)
* https://github.com/bibekaryal86/pets-ui-layer

This app is deployed in Google Cloud Project. The GCP configurations are found in the `gcp` folder in the project root.
To deploy to GCP, we need to copy the jar file to that folder and use gcloud app deploy terminal command.
* App Test Link: https://pets-gateway.appspot.com/tests/ping
