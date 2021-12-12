FROM adoptopenjdk/openjdk11:alpine
RUN addgroup -S springdocker && adduser -S springdocker -G springdocker
USER springdocker:springdocker
ARG JAR_FILE=app/build/libs/pets-gateway.jar
COPY ${JAR_FILE} pets-gateway.jar
ENTRYPOINT ["java","-jar", \
#"-Dspring.profiles.active=docker", \
#"-DBASIC_AUTH_USR_PETSSERVICE=some_username", \
#"-DBASIC_AUTH_PWD_PETSSERVICE=some_password", \
#"-DBASIC_AUTH_USR_PETSDATABASE=another_username", \
#"-DBASIC_AUTH_PWD_PETSDATABASE=another_password", \
"/pets-gateway.jar"]
# provide environment variables in docker-compose
